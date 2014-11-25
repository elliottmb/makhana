package com.cogitareforma.makhana.gameserver.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomUtils;

import com.cogitareforma.makhana.common.data.ServerStatus;
import com.cogitareforma.makhana.common.data.Session;
import com.cogitareforma.makhana.common.entities.components.ActionTrait;
import com.cogitareforma.makhana.common.entities.components.Player;
import com.cogitareforma.makhana.common.entities.components.Position;
import com.cogitareforma.makhana.common.entities.components.TileTrait;
import com.cogitareforma.makhana.common.entities.components.WorldTrait;
import com.cogitareforma.makhana.common.eventsystem.Event;
import com.cogitareforma.makhana.common.eventsystem.EventHandler;
import com.cogitareforma.makhana.common.eventsystem.EventManager;
import com.cogitareforma.makhana.common.eventsystem.ThreadSafeEventManager;
import com.cogitareforma.makhana.common.eventsystem.events.TileCapturedEvent;
import com.cogitareforma.makhana.common.eventsystem.events.TileClaimedEvent;
import com.cogitareforma.makhana.common.eventsystem.events.TileFreedEvent;
import com.cogitareforma.makhana.common.net.SerializerRegistrar;
import com.cogitareforma.makhana.common.net.ServerManager;
import com.cogitareforma.makhana.common.net.msg.ServerStatusResponse;
import com.cogitareforma.makhana.common.util.MakhanaConfig;
import com.cogitareforma.makhana.common.util.PackageUtils;
import com.cogitareforma.makhana.gameserver.GameServer;
import com.cogitareforma.makhana.gameserver.eventsystem.events.ActionCompletedEvent;
import com.cogitareforma.makhana.gameserver.eventsystem.events.ServerPlayerJoinEvent;
import com.cogitareforma.makhana.gameserver.eventsystem.handlers.ActionCompletedEventHandler;
import com.cogitareforma.makhana.gameserver.eventsystem.handlers.ServerPlayerJoinEventHandler;
import com.cogitareforma.makhana.gameserver.eventsystem.handlers.TileOwnerChangedEventHandler;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.BulletAppState.ThreadingType;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
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

	private EntitySet actingEntitySet;
	private boolean advanceTurn;
	private EntityData entityData;

	private EntityDataHostService entityDataHostService;
	private EventManager< Event, EventHandler > eventManager;
	private EntitySet ownedTileEntitySet;
	private EntitySet playerEntitySet;
	/**
	 * The server's status, used for informing the master server and clients.
	 */
	private ServerStatus status;

	private EntityId theWorld;

	private Map< EntityId, EntityId > tileToOwnerMap;

	private BulletAppState bulletAppState;

	private Map< EntityId, PhysicsRigidBody > physicsObjects;

	/**
	 * @return the bulletAppState
	 */
	public BulletAppState getBulletAppState( )
	{
		return bulletAppState;
	}

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

		bulletAppState = new BulletAppState( );
		bulletAppState.setThreadingType( ThreadingType.PARALLEL );
		app.getStateManager( ).attach( bulletAppState );

		physicsObjects = new ConcurrentHashMap<>( );

		PhysicsRigidBody floor = new PhysicsRigidBody( new PlaneCollisionShape( new Plane( new Vector3f( 0, 1, 0 ), 0 ) ), 0 );
		floor.setPhysicsLocation( new Vector3f( 0f, -6, 0f ) );

		bulletAppState.getPhysicsSpace( ).add( floor );

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

	public void createPlayerEntity( Session session )
	{
		if ( getEntityData( ) != null )
		{
			if ( session != null )
			{

				// Check if already exists
				for ( Entity e : entityData.getEntities( Player.class ) )
				{
					Player playerTrait = e.get( Player.class );
					if ( session.equals( playerTrait.getSession( ) ) )
					{
						logger.log( Level.WARNING, "Could not create an Entity, Player already exists" );
						eventManager.triggerEvent( new ServerPlayerJoinEvent( entityData, e.getId( ), playerTrait, getSessionManager( )
								.getConnections( ).size( ), true ) );
						return;
					}
				}

				EntityId playerId = entityData.createEntity( );
				logger.log( Level.INFO, "Creating an Entity for player: " + session.getDisplayName( ) + ", " + playerId );
				Player playerTrait = new Player( session );
				entityData.setComponent( playerId, playerTrait );
				eventManager.triggerEvent( new ServerPlayerJoinEvent( entityData, playerId, playerTrait, getSessionManager( )
						.getConnections( ).size( ), false ) );
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

	public EntityId getPlayerEntityId( Session session )
	{
		if ( getEntityData( ) != null )
		{
			if ( session != null )
			{
				ComponentFilter< Player > sessionFilter = FieldFilter.create( Player.class, "session", session );
				return getEntityData( ).findEntity( sessionFilter, Player.class );
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

	public void removePlayerEntity( Session session )
	{
		if ( getEntityData( ) != null )
		{
			EntityId id = getPlayerEntityId( session );

			if ( id != null )
			{
				logger.log( Level.INFO, "Removing Entity for Account: " + session.getDisplayName( ) );
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
			eventManager = null;
			status = null;
		}

		try
		{

			setServer( Network.createServer( "makhana", 2, port, port ) );
			getServer( ).addChannel( port + 1 );
			logger.log( Level.INFO, String.format( "We will bind to TCP: %d, UDP: %d", port, port ) );

			/* register all message types with the serializer */
			logger.log( Level.FINE, "Registering all serializable classes." );
			SerializerRegistrar.registerAllSerializable( );

			/* add listeners */
			logger.log( Level.FINE, "Registering ConnListener with server." );
			getServer( ).addConnectionListener( new ConnListener( this ) );

			logger.log( Level.FINE, "Registering message listeners with server." );
			List< Object > messageListeners = PackageUtils.createAllInPackage( "com.cogitareforma.makhana.gameserver.net.listener.game",
					this );
			for ( Object messageListener : messageListeners )
			{
				getServer( ).addMessageListener( ( MessageListener ) messageListener );
			}

			logger.log( Level.FINE, "Starting the server." );
			getServer( ).start( );

			entityData = new DefaultEntityData( );
			entityDataHostService = new EntityDataHostService( getServer( ), 0, entityData );
			eventManager = new ThreadSafeEventManager( getApp( ) );

			eventManager.addEventHandler( new ActionCompletedEventHandler( ), ActionCompletedEvent.class );
			eventManager.addEventHandler( new ServerPlayerJoinEventHandler( ), ServerPlayerJoinEvent.class );
			eventManager.addEventHandler( new TileOwnerChangedEventHandler( ), TileClaimedEvent.class, TileCapturedEvent.class,
					TileFreedEvent.class );

			MakhanaConfig config = getApp( ).getConfiguration( );
			String name = ( String ) config.get( "gameserver.name" );
			if ( name == null )
			{
				name = "Default server";
				config.put( "gameserver.name", name );
			}

			status = new ServerStatus( name, 4, 0, port );

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
			if ( getServerStatus( ).getCurrentPlayers( ) != getSessionManager( ).getConnections( ).size( ) )
			{
				logger.log( Level.INFO, "Server Status Current Player Count does not match Authed Connection Count, fixing." );
				getServerStatus( ).setCurrentPlayers( getSessionManager( ).getConnections( ).size( ) );
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

			status = new ServerStatus( ( String ) getApp( ).getConfiguration( ).get( "gameserver.name" ), 4, 0, this.getApp( ).getPort( ) );
			getServerStatus( ).setChanged( true );
		}

		if ( getEntityDataHostService( ) != null )
		{
			if ( getEntityData( ) != null )
			{

				for ( Entry< EntityId, PhysicsRigidBody > e : physicsObjects.entrySet( ) )
				{

					Vector3f location = e.getValue( ).getPhysicsLocation( );
					Quaternion rotation = e.getValue( ).getPhysicsRotation( );

					Position currentPosition = getEntityData( ).getComponent( e.getKey( ), Position.class );

					// Threshold for updates.. may be removed
					if ( currentPosition.getLocation( ).distance( location ) > 0.05 )
					{
						// System.out.println( e.getKey( ) + ", " + location +
						// ", " + rotation + ", " + e.getValue(
						// ).getLinearVelocity( ) );
						getEntityData( ).setComponent( e.getKey( ), new Position( location, rotation ) );
					}

				}

				if ( playerEntitySet != null )
				{
					if ( playerEntitySet.applyChanges( ) )
					{
						logger.log( Level.INFO, "There were changes to the players!" );
						int readyCount = 0;
						for ( Entity e : playerEntitySet )
						{
							if ( e.get( Player.class ).isAlive( ) )
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
								Player pt = e.get( Player.class );
								Player newPt = new Player( pt.getSession( ), pt.getKills( ), pt.getDeaths( ), false );
								e.set( newPt );
							}

						}
					}
				}
				else
				{
					playerEntitySet = getEntityData( ).getEntities( Player.class );
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

										eventManager.triggerEvent( new ActionCompletedEvent( entityData, e.getId( ), at ) );

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

								eventManager
										.triggerEvent( new TileClaimedEvent( entityData, tileId, tileTrait, createdBy.getCreatorId( ) ) );

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

								eventManager.triggerEvent( new TileCapturedEvent( entityData, tileId, tileTrait, oldOwnerId, createdBy
										.getCreatorId( ) ) );

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

								eventManager.triggerEvent( new TileFreedEvent( entityData, tileId, tileTrait, oldOwnerId ) );

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

	/**
	 * @return the physicsObjects
	 */
	public Map< EntityId, PhysicsRigidBody > getPhysicsObjects( )
	{
		return physicsObjects;
	}

}
