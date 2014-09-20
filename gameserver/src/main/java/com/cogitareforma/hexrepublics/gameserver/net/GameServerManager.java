package com.cogitareforma.hexrepublics.gameserver.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomUtils;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.data.ServerStatus;
import com.cogitareforma.hexrepublics.common.entities.traits.ActionTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.WorldTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventManager;
import com.cogitareforma.hexrepublics.common.eventsystem.events.TileCapturedEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.events.TileClaimedEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.events.TileFreedEvent;
import com.cogitareforma.hexrepublics.common.net.SerializerRegistrar;
import com.cogitareforma.hexrepublics.common.net.ServerManager;
import com.cogitareforma.hexrepublics.common.net.msg.ServerStatusResponse;
import com.cogitareforma.hexrepublics.common.util.PackageUtils;
import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.cogitareforma.hexrepublics.gameserver.GameServer;
import com.cogitareforma.hexrepublics.gameserver.eventsystem.events.ActionCompletedEvent;
import com.cogitareforma.hexrepublics.gameserver.eventsystem.events.ServerPlayerJoinEvent;
import com.cogitareforma.hexrepublics.gameserver.eventsystem.handlers.ActionCompletedEventHandler;
import com.cogitareforma.hexrepublics.gameserver.eventsystem.handlers.ServerPlayerJoinEventHandler;
import com.cogitareforma.hexrepublics.gameserver.eventsystem.handlers.TileOwnerChangedEventHandler;
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
	private EntityEventManager entityEventManager;

	private EntitySet actingEntitySet;
	private EntitySet playerEntitySet;
	private EntitySet ownedTileEntitySet;
	private boolean advanceTurn;
	private EntityId theWorld;

	private Map< EntityId, EntityId > tileToOwnerMap;

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

	private void buildTiles( int terrainSize, float hexSize )
	{
		logger.log( Level.INFO, "Generating Tile Entities" );
		for ( int i = 0; i < FastMath.floor( terrainSize / ( hexSize * 6 / 4 ) ) - 1; i++ )
		{
			for ( int j = 0; j < FastMath.ceil( terrainSize / ( FastMath.sqrt( 3f ) * hexSize ) ) - 1; j++ )
			{
				EntityId tile = getEntityData( ).createEntity( );
				getEntityData( ).setComponent( tile, new TileTrait( i, j ) );
			}
		}
	}

	private void constructWorld( )
	{
		// TODO: Clean up existing entities, no accidental duplicates here!
		buildTiles( 257, 10.0f );

		logger.log( Level.INFO, "Constructing initial world state" );
		setTheWorld( getEntityData( ).createEntity( ) );
		byte[ ] randomBytes = RandomUtils.nextBytes( 3 );
		getEntityData( ).setComponent( getTheWorld( ), new WorldTrait( 0, randomBytes[ RandomUtils.nextInt( 0, 3 ) ], false ) );
		advanceTurn = false;
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
					PlayerTrait playerTrait = e.get( PlayerTrait.class );
					if ( account.equals( playerTrait.getAccount( ) ) )
					{
						logger.log( Level.WARNING, "Could not create an Entity, Player already exists" );
						entityEventManager.triggerEvent( new ServerPlayerJoinEvent( entityEventManager, e.getId( ), playerTrait,
								getSessionManager( ).getAllSessions( ).size( ), true ) );
						return;
					}
				}

				EntityId playerId = entityData.createEntity( );
				logger.log( Level.INFO, "Creating an Entity for player: " + account.getAccountName( ) + ", " + playerId );
				PlayerTrait playerTrait = new PlayerTrait( account );
				entityData.setComponent( playerId, playerTrait );
				entityEventManager.triggerEvent( new ServerPlayerJoinEvent( entityEventManager, playerId, playerTrait, getSessionManager( )
						.getAllSessions( ).size( ), false ) );
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

	public EntityId getPlayerEntityId( Account account )
	{
		if ( getEntityData( ) != null )
		{
			if ( account != null )
			{
				ComponentFilter< PlayerTrait > accountFilter = FieldFilter.create( PlayerTrait.class, "account", account );
				return getEntityData( ).findEntity( accountFilter, PlayerTrait.class );
			}
			else
			{
				logger.log( Level.WARNING, "Could not find Entity for a null Account" );
			}
		}
		else
		{
			logger.log( Level.WARNING, "Could not find Entity for Account, Entity Data is null" );
		}
		return null;
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

	/**
	 * @return the theWorld
	 */
	public EntityId getTheWorld( )
	{
		return theWorld;
	}

	public void removePlayerEntity( Account account )
	{
		if ( getEntityData( ) != null )
		{
			EntityId id = getPlayerEntityId( account );

			if ( id != null )
			{
				logger.log( Level.INFO, "Removing Entity for Account: " + account.getAccountName( ) );
				getEntityData( ).removeEntity( id );
			}
			else
			{
				logger.log( Level.WARNING, "Could not remove Entity for given Account, Entity may not exist" );
			}

		}
		else
		{
			logger.log( Level.WARNING, "Could not remove Entity for Account, Entity Data is null" );
		}
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
			entityEventManager = null;
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
			entityEventManager = new EntityEventManager( entityData );

			entityEventManager.addEventHandler( new ActionCompletedEventHandler( ), ActionCompletedEvent.class );
			entityEventManager.addEventHandler( new ServerPlayerJoinEventHandler( ), ServerPlayerJoinEvent.class );
			entityEventManager.addEventHandler( new TileOwnerChangedEventHandler( ), TileClaimedEvent.class, TileCapturedEvent.class,
					TileFreedEvent.class );

			String name = ( String ) YamlConfig.DEFAULT.get( "gameserver.name" );
			if ( name == null )
			{
				name = "Default server";
				YamlConfig.DEFAULT.put( "gameserver.name", name );
			}

			status = new ServerStatus( name, 4, 0, 0, port );

			// TODO: Move to when match started
			constructWorld( );

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

	/**
	 * @param theWorld
	 *            the theWorld to set
	 */
	private void setTheWorld( EntityId theWorld )
	{
		this.theWorld = theWorld;
	}

	@Override
	public void update( float tpf )
	{
		super.update( tpf );

		if ( getServerStatus( ) != null )
		{
			if ( getServerStatus( ).getCurrentPlayers( ) != getSessionManager( ).getAllSessions( ).size( ) )
			{
				logger.log( Level.INFO, "Server Status Current Player Count does not match Authed Connection Count, fixing." );
				getServerStatus( ).setCurrentPlayers( getSessionManager( ).getAllSessions( ).size( ) );
			}

			if ( getServerStatus( ).isChanged( ) )
			{
				logger.log( Level.INFO, "Server Status has changed since last update, sending to Master Server" );
				getApp( ).getMasterConnManager( ).send( new ServerStatusResponse( getServerStatus( ) ) );
				getServerStatus( ).setChanged( false );
			}
		}
		else
		{
			logger.log( Level.INFO, "Server Status was null." );
			status = new ServerStatus( ( String ) YamlConfig.DEFAULT.get( "gameserver.name" ), 4, 0, 0, this.getApp( ).getPort( ) );
			getServerStatus( ).setChanged( false );
		}

		if ( getEntityDataHostService( ) != null )
		{
			if ( getEntityData( ) != null )
			{
				if ( playerEntitySet != null )
				{
					if ( playerEntitySet.applyChanges( ) )
					{
						logger.log( Level.INFO, "There were changes to the players!" );
						int readyCount = 0;
						for ( Entity e : playerEntitySet )
						{
							if ( e.get( PlayerTrait.class ).isReady( ) )
							{
								readyCount++;
							}
						}

						if ( readyCount == playerEntitySet.size( ) )
						{
							WorldTrait wt = getEntityData( ).getComponent( getTheWorld( ), WorldTrait.class );
							if ( wt != null )
							{
								if ( !wt.isPlaying( ) )
								{
									WorldTrait newWt = new WorldTrait( wt.getCurrentTurn( ), wt.getSeed( ), true );
									getEntityData( ).setComponent( getTheWorld( ), newWt );
								}
								else
								{
									advanceTurn = true;
								}
							}

							for ( Entity e : playerEntitySet )
							{
								PlayerTrait pt = e.get( PlayerTrait.class );
								PlayerTrait newPt = new PlayerTrait( pt.getAccount( ), pt.getWins( ), pt.getLosses( ), false );
								e.set( newPt );
							}

						}
					}
				}
				else
				{
					playerEntitySet = getEntityData( ).getEntities( PlayerTrait.class );
				}

				if ( actingEntitySet != null )
				{

					actingEntitySet.applyChanges( );

					if ( advanceTurn )
					{
						WorldTrait wt = getEntityData( ).getComponent( getTheWorld( ), WorldTrait.class );
						if ( wt != null )
						{
							for ( Entity e : actingEntitySet )
							{
								ActionTrait at = e.get( ActionTrait.class );
								if ( at != null )
								{
									// If enough turns have passed
									if ( ( at.getStartTurn( ) + at.getDuration( ) ) - wt.getCurrentTurn( ) <= 1 )
									{

										entityEventManager.triggerEvent( new ActionCompletedEvent( entityEventManager, e.getId( ), at ) );

									}
								}
							}

							getEntityData( ).setComponent( getTheWorld( ), new WorldTrait( wt.getCurrentTurn( ) + 1, wt.getSeed( ), true ) );
							advanceTurn = false;
						}
					}

				}
				else
				{
					actingEntitySet = getEntityData( ).getEntities( ActionTrait.class );
				}

				if ( ownedTileEntitySet != null )
				{
					if ( tileToOwnerMap != null )
					{
						if ( ownedTileEntitySet.applyChanges( ) )
						{
							for ( Entity tileEntity : ownedTileEntitySet.getAddedEntities( ) )
							{
								EntityId tileId = tileEntity.getId( );
								TileTrait tileTrait = tileEntity.get( TileTrait.class );
								CreatedBy createdBy = tileEntity.get( CreatedBy.class );

								entityEventManager.triggerEvent( new TileClaimedEvent( entityEventManager, tileId, tileTrait, createdBy
										.getCreatorId( ) ) );

								tileToOwnerMap.put( tileId, createdBy.getCreatorId( ) );
							}

							for ( Entity tileEntity : ownedTileEntitySet.getChangedEntities( ) )
							{
								EntityId tileId = tileEntity.getId( );
								TileTrait tileTrait = tileEntity.get( TileTrait.class );
								CreatedBy createdBy = tileEntity.get( CreatedBy.class );
								EntityId oldOwnerId = null;

								if ( tileToOwnerMap.containsKey( tileId ) )
								{
									oldOwnerId = tileToOwnerMap.get( tileId );
								}

								entityEventManager.triggerEvent( new TileCapturedEvent( entityEventManager, tileId, tileTrait, oldOwnerId,
										createdBy.getCreatorId( ) ) );

								tileToOwnerMap.put( tileId, createdBy.getCreatorId( ) );
							}

							for ( Entity tileEntity : ownedTileEntitySet.getRemovedEntities( ) )
							{
								EntityId tileId = tileEntity.getId( );
								TileTrait tileTrait = tileEntity.get( TileTrait.class );
								EntityId oldOwnerId = null;

								if ( tileToOwnerMap.containsKey( tileId ) )
								{
									oldOwnerId = tileToOwnerMap.get( tileId );
								}

								entityEventManager.triggerEvent( new TileFreedEvent( entityEventManager, tileId, tileTrait, oldOwnerId ) );

								tileToOwnerMap.remove( tileId );
							}
						}
					}
					else
					{
						tileToOwnerMap = new HashMap<>( );
					}
				}
				else
				{
					ownedTileEntitySet = getEntityData( ).getEntities( TileTrait.class, CreatedBy.class );
				}
			}

			getEntityDataHostService( ).sendUpdates( );
		}
	}
}
