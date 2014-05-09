package com.cogitareforma.hexrepublics.gameserver.net;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.data.ServerStatus;
import com.cogitareforma.hexrepublics.common.entities.Traits;
import com.cogitareforma.hexrepublics.common.entities.traits.DefenseTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.FabricatingTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.HealthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.MoveTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.StrengthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.net.SerializerRegistrar;
import com.cogitareforma.hexrepublics.common.net.ServerManager;
import com.cogitareforma.hexrepublics.common.net.msg.ServerStatusResponse;
import com.cogitareforma.hexrepublics.common.util.PackageUtils;
import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.cogitareforma.hexrepublics.gameserver.GameServer;
import com.jme3.math.FastMath;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.base.DefaultEntityData;
import com.simsilica.es.filter.AndFilter;
import com.simsilica.es.filter.FieldFilter;
import com.simsilica.es.server.EntityDataHostService;

public class GameServerManager extends ServerManager< GameServer >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( GameServerManager.class.getName( ) );

	private EntityData entityData;
	private EntityDataHostService entityDataHostService;

	private EntitySet fabricating;
	private EntitySet moving;

	/**
	 * The server's status, used for informing the master server and clients.
	 */
	private ServerStatus status;

	/**
	 * Creates an instance of the GameServerManager with the supplied owning
	 * GameServer Application
	 * 
	 * @param app
	 *            the owning GameServer Application
	 */
	public GameServerManager( GameServer app )
	{
		super( app );
	}

	/**
	 * Returns the managed EntityData instance
	 * 
	 * @return the managed EntityData instance
	 */
	public EntityData getEntityData( )
	{
		return entityData;
	}

	/**
	 * Returns the managed EntityDataHostService instance
	 * 
	 * @return the managed EntityDataHostService instance
	 */
	public EntityDataHostService getEntityDataHostService( )
	{
		return entityDataHostService;
	}

	/**
	 * Returns the current ServerStatus
	 * 
	 * @return the current ServerStatus
	 */
	public ServerStatus getServerStatus( )
	{
		return status;
	}

	@SuppressWarnings(
	{
			"unchecked", "rawtypes"
	} )
	@Override
	public boolean run( final Integer port )
	{
		// Close existing connection
		if ( isRunning( ) )
		{
			close( );
			entityDataHostService = null;
			entityData = null;
			status = null;
		}

		try
		{

			setServer( Network.createServer( "Hex Republics", 2, port, port ) );
			getServer( ).addChannel( port + 1 );
			logger.log( Level.INFO, String.format( "We will bind to TCP: %d, UDP: %d", port, port ) );

			/* register all message types with the serializer */
			logger.log( Level.FINE, "Registering all serializable classes." );
			SerializerRegistrar.registerAllSerializable( );

			/* add listeners */
			logger.log( Level.FINE, "Registering ConnListener with server." );
			getServer( ).addConnectionListener( new ConnListener( this ) );

			logger.log( Level.FINE, "Registering message listeners with server." );
			List< Object > messageListeners = PackageUtils.createAllInPackage(
					"com.cogitareforma.hexrepublics.gameserver.net.listener.game", this );
			for ( Object messageListener : messageListeners )
			{
				getServer( ).addMessageListener( ( MessageListener ) messageListener );
			}

			logger.log( Level.FINE, "Starting the server." );
			getServer( ).start( );

			entityData = new DefaultEntityData( );
			entityDataHostService = new EntityDataHostService( getServer( ), 0, entityData );

			String name = ( String ) YamlConfig.DEFAULT.get( "gameserver.name" );
			if ( name == null )
			{
				name = "Default server";
			}

			status = new ServerStatus( name, 4, 0, 0, "unknown", port );

			// Move to when match started
			buildTiles( 257, 10.0f );

			return true;
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Couldn't bind to port " + port, e );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Encountered unexpected error!", e );
		}

		return false;
	}

	private void buildTiles( int terrainSize, float hexSize )
	{
		for ( int i = 0; i < FastMath.floor( terrainSize / ( hexSize * 6 / 4 ) ) - 1; i++ )
		{
			for ( int j = 0; j < FastMath.ceil( terrainSize / ( FastMath.sqrt( 3f ) * hexSize ) ) - 1; j++ )
			{
				EntityId tile = getEntityData( ).createEntity( );
				getEntityData( ).setComponent( tile, new TileTrait( i, j ) );
			}
		}

		EntitySet es = getEntityData( ).getEntities( TileTrait.class );

		for ( Entity e : es )
		{
			System.out.println( e.get( TileTrait.class ) );
		}
	}

	public void createPlayerEntity( Account account )
	{
		if ( getEntityData( ) != null )
		{
			if ( account != null )
			{
				// Check if already exists
				for ( Entity e : entityData.getEntities( PlayerTrait.class ) )
				{
					if ( e.get( PlayerTrait.class ).getAccount( ).equals( account ) )
					{
						return;
					}
				}
				EntityId playerEntity = entityData.createEntity( );
				logger.log( Level.INFO, "Creating an Entity for player: " + account.getAccountName( ) + ", " + playerEntity );
				getEntityData( ).setComponent( playerEntity, new PlayerTrait( account ) );

				int x = 0;
				int y = 0;

				switch ( getSessionManager( ).getAllAuthedConnections( ).size( ) )
				{
					case 1:
					{
						x = 3;
						y = 3;
						break;
					}
					case 2:
					{
						x = 12;
						y = 10;
						break;
					}
					case 3:
					{
						x = 3;
						y = 10;
						break;
					}
					default:
					{
						x = 12;
						y = 3;
						break;
					}
				}
				ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", x );
				ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", y );
				@SuppressWarnings( "unchecked" )
				ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );

				EntityId tileId = getEntityData( ).findEntity( completeFilter, TileTrait.class );

				getEntityData( ).removeComponent( tileId, CreatedBy.class );
				getEntityData( ).setComponent( tileId, new CreatedBy( playerEntity ) );
			}
			else
			{
				logger.log( Level.WARNING, "Could not create an Entity for a null player" );
			}
		}
		else
		{
			logger.log( Level.WARNING, "Could not create an Entity for a player, Entity Data is null" );
		}
	}

	public void removePlayerEntity( Account account )
	{
		if ( getEntityData( ) != null )
		{
			if ( account != null )
			{

				ComponentFilter< PlayerTrait > accountFilter = FieldFilter.create( PlayerTrait.class, "account", account );
				EntityId tileId = getEntityData( ).findEntity( accountFilter, PlayerTrait.class );
				if ( tileId != null )
				{
					logger.log( Level.INFO, "Removing Entity for Account: " + account.getAccountName( ) );
					getEntityData( ).removeEntity( tileId );
				}
				else
				{
					logger.log( Level.WARNING, "Could not remove Entity for given Account, Entity may not exist" );
				}
			}
			else
			{
				logger.log( Level.WARNING, "Could not remove Entity for a null Account" );
			}
		}
		else
		{
			logger.log( Level.WARNING, "Could not remove Entity for Account, Entity Data is null" );
		}
	}

	@Override
	public void update( float tpf )
	{
		super.update( tpf );

		if ( getServerStatus( ) != null )
		{
			if ( getServerStatus( ).getCurrentPlayers( ) != getSessionManager( ).getAllAuthedConnections( ).size( ) )
			{
				logger.log( Level.INFO, "Server Status Current Player Count does not match Authed Connection Count, fixing." );
				getServerStatus( ).setCurrentPlayers( getSessionManager( ).getAllAuthedConnections( ).size( ) );
			}

			if ( getServerStatus( ).isChanged( ) )
			{
				logger.log( Level.INFO, "Server Status has changed since last update, sending to Master Server" );
				Account act = getApp( ).getMasterConnManager( ).getAccount( );
				getApp( ).getMasterConnManager( ).send( new ServerStatusResponse( act, getServerStatus( ) ) );
				getServerStatus( ).setChanged( false );
			}
		}

		if ( getEntityDataHostService( ) != null )
		{
			if ( getEntityData( ) != null )
			{
				if ( fabricating != null )
				{
					fabricating.applyChanges( );
					for ( Entity e : fabricating )
					{
						FabricatingTrait ft = e.get( FabricatingTrait.class );
						if ( ft != null )
						{
							double time = ( ft.getStartTime( ).getTime( ) + ft.getDuration( ) - new Date( ).getTime( ) ) / 60000.0;
							if ( time <= 0 )
							{
								logger.log( Level.INFO, "Removing FabricatingTrait from " + e.toString( ) );
								getEntityData( ).removeComponent( e.getId( ), FabricatingTrait.class );
							}
						}
					}
				}
				else
				{
					fabricating = getEntityData( ).getEntities( FabricatingTrait.class );
				}

				if ( moving != null )
				{
					moving.applyChanges( );
					for ( Entity e : moving )
					{
						MoveTrait mt = e.get( MoveTrait.class );
						if ( mt != null )
						{
							double time = ( mt.getStartTime( ).getTime( ) + mt.getDuration( ) - new Date( ).getTime( ) ) / 60000.0;
							if ( time <= 0 )
							{
								LocationTrait location = getEntityData( ).getComponent( e.getId( ), LocationTrait.class );
								if ( location != null )
								{
									if ( Traits.areNeighbors( getEntityData( ), location.getTile( ), mt.getNewTile( ) ) )
									{
										// Get the entities are the target tile
										ComponentFilter< LocationTrait > locFilter = FieldFilter.create( LocationTrait.class, "tile",
												mt.getNewTile( ) );
										Set< EntityId > targetIdSet = getEntityData( ).findEntities( locFilter, LocationTrait.class );

										ComponentFilter< LocationTrait > sourceLocFilter = FieldFilter.create( LocationTrait.class, "tile",
												location.getTile( ) );
										Set< EntityId > sourceIdSet = getEntityData( ).findEntities( sourceLocFilter, LocationTrait.class );

										// Units at the target tile
										int unitCount = Traits.countUnits( getEntityData( ), targetIdSet );
										// Buildings at the target tile
										int buildingCount = Traits.countBuildings( getEntityData( ), targetIdSet );

										// Get info on the target tile
										Entity tile = getEntityData( ).getEntity( mt.getNewTile( ), CreatedBy.class, TileTrait.class );

										Entity source = getEntityData( ).getEntity( location.getTile( ), CreatedBy.class, TileTrait.class );

										if ( tile.get( CreatedBy.class ) != null )
										{
											// Owned, check if owned by same
											// player
											// or enemy
											if ( source.get( CreatedBy.class ).getCreatorId( )
													.compareTo( tile.get( CreatedBy.class ).getCreatorId( ) ) == 0 )
											{
												if ( unitCount < 6 )
												{
													getEntityData( ).setComponent( e.getId( ),
															new LocationTrait( mt.getNewTile( ), ( byte ) ( unitCount + 1 ) ) );
												}
											}
											else
											{
												// Owned by enemy
												float targetStr = 0.0f;
												float sourceStr = 0.0f;
												if ( getEntityData( ).getComponent( e.getId( ), StrengthTrait.class ) != null )
												{
													sourceStr = getEntityData( ).getComponent( e.getId( ), StrengthTrait.class )
															.getStrength( );
												}
												System.out.println( "Source Strength: " + sourceStr );
												// divide att by amount of
												// defending units
												// apply to all def units health
												// if health <= 0 delete unit
												// if all units dead, move to
												// tile and take ownership
												// butt if units remain, apply
												// str back to attacking
												float newStr = sourceStr / Traits.countUnits( getEntityData( ), targetIdSet );
												for ( EntityId id : targetIdSet )
												{
													if ( Traits.isUnit( getEntityData( ), id ) )
													{
														Entity unit = getEntityData( )
																.getEntity( id, HealthTrait.class, DefenseTrait.class );
														if ( unit.get( HealthTrait.class ) != null )
														{
															float health = unit.get( HealthTrait.class ).getHealth( ) - newStr;
															if ( health <= 0 )
															{
																getEntityData( ).removeEntity( id );
															}
															else
															{
																System.out.println( "New Target Health: " + health );
																getEntityData( ).setComponent( id, new HealthTrait( health ) );
															}

														}
													}
												}

												targetIdSet = getEntityData( ).findEntities( locFilter, LocationTrait.class );
												if ( Traits.countUnits( getEntityData( ), targetIdSet ) == 0 )
												{
													getEntityData( ).setComponent(
															mt.getNewTile( ),
															new CreatedBy( getEntityData( ).getComponent( location.getTile( ),
																	CreatedBy.class ).getCreatorId( ) ) );
													getEntityData( ).setComponent( e.getId( ),
															new LocationTrait( mt.getNewTile( ), ( byte ) ( 1 ) ) );
												}
												else
												{
													for ( EntityId id : targetIdSet )
													{
														if ( Traits.isUnit( getEntityData( ), id ) )
														{
															Entity unit = getEntityData( ).getEntity( id, StrengthTrait.class );
															if ( unit.get( StrengthTrait.class ) != null )
															{
																targetStr += unit.get( StrengthTrait.class ).getStrength( );
															}
														}
													}
													// Entity sourceUnit =
													// getEntityData(
													// ).getEntity( e.getId( ),
													// HealthTrait.class );
													HealthTrait sourceHealth = getEntityData( )
															.getComponent( e.getId( ), HealthTrait.class );
													if ( sourceHealth != null )
													{
														float health = sourceHealth.getHealth( ) - targetStr;
														if ( health <= 0 )
														{
															getEntityData( ).removeEntity( e.getId( ) );
														}
														else
														{
															System.out.println( "New Source Health: " + health );
															getEntityData( ).setComponent( e.getId( ), new HealthTrait( health ) );
														}

													}
												}
											}
										}
										else
										{
											// Unowned, just take it
											getEntityData( ).setComponent(
													mt.getNewTile( ),
													new CreatedBy( getEntityData( ).getComponent( location.getTile( ), CreatedBy.class )
															.getCreatorId( ) ) );
											getEntityData( ).setComponent( e.getId( ),
													new LocationTrait( mt.getNewTile( ), ( byte ) ( unitCount + 1 ) ) );
										}
									}

								}
								logger.log( Level.INFO, "Removing MoveTrait from " + e.toString( ) );
								getEntityData( ).removeComponent( e.getId( ), MoveTrait.class );
							}

						}
					}

				}
				else
				{

					moving = getEntityData( ).getEntities( MoveTrait.class );
				}
			}
			getEntityDataHostService( ).sendUpdates( );
		}
	}
}