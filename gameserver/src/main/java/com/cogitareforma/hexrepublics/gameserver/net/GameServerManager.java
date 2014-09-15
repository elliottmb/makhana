package com.cogitareforma.hexrepublics.gameserver.net;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomUtils;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.data.ServerStatus;
import com.cogitareforma.hexrepublics.common.entities.traits.ActionTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.CapitalTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.WorldTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventManager;
import com.cogitareforma.hexrepublics.common.net.SerializerRegistrar;
import com.cogitareforma.hexrepublics.common.net.ServerManager;
import com.cogitareforma.hexrepublics.common.net.msg.ServerStatusResponse;
import com.cogitareforma.hexrepublics.common.util.PackageUtils;
import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.cogitareforma.hexrepublics.gameserver.GameServer;
import com.cogitareforma.hexrepublics.gameserver.eventsystem.events.ActionCompletedEntityEvent;
import com.cogitareforma.hexrepublics.gameserver.eventsystem.handlers.ActionCompletedEventHandler;
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
	private EntityEventManager entityEventManager;

	private EntitySet actingEntities;
	private EntitySet playerEntities;
	private boolean advanceTurn;
	private EntityId theWorld;

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

				switch ( getSessionManager( ).getAllSessions( ).size( ) )
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
				getEntityData( ).setComponent( tileId, new CapitalTrait( ) );
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
			entityEventManager = new EntityEventManager( );

			entityEventManager.addEventHandler( ActionCompletedEntityEvent.class, new ActionCompletedEventHandler( ) );

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
				if ( playerEntities != null )
				{
					if ( playerEntities.applyChanges( ) )
					{
						logger.log( Level.INFO, "There were changes to the players!" );
						for ( Entity e : playerEntities.getChangedEntities( ) )
						{
							logger.log( Level.INFO, "Change: " + e.getId( ).toString( ) + ", " + e.get( PlayerTrait.class ) );
						}
						int readyCount = 0;
						for ( Entity e : playerEntities )
						{
							if ( e.get( PlayerTrait.class ).isReady( ) )
							{
								readyCount++;
							}
						}
						if ( readyCount == playerEntities.size( ) )
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

							for ( Entity e : playerEntities )
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
					playerEntities = getEntityData( ).getEntities( PlayerTrait.class );
				}

				if ( actingEntities != null )
				{

					actingEntities.applyChanges( );
					if ( advanceTurn )
					{
						WorldTrait wt = getEntityData( ).getComponent( getTheWorld( ), WorldTrait.class );
						for ( Entity e : actingEntities )
						{
							ActionTrait at = e.get( ActionTrait.class );
							if ( at != null )
							{

								int turnsRemaining = ( at.getStartTurn( ) + at.getDuration( ) ) - wt.getCurrentTurn( );
								System.out.println( at.getStartTurn( ) + " + " + at.getDuration( ) + " - " + wt.getCurrentTurn( ) + " = "
										+ turnsRemaining );
								if ( turnsRemaining <= 1 )
								{

									entityEventManager.triggerEvent( new ActionCompletedEntityEvent( entityData, e.getId( ), at ) );

								}
							}
						}

						if ( wt != null )
						{
							WorldTrait newWt = new WorldTrait( wt.getCurrentTurn( ) + 1, wt.getSeed( ), true );
							getEntityData( ).setComponent( getTheWorld( ), newWt );
						}
						advanceTurn = false;
					}
				}
				else
				{
					actingEntities = getEntityData( ).getEntities( ActionTrait.class );
				}

			}
			getEntityDataHostService( ).sendUpdates( );
		}
	}
}
