package com.cogitareforma.hexrepublics.gameserver.net.listener.game;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.entities.Traits;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.MoveableTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.StaticTrait;
import com.cogitareforma.hexrepublics.common.net.msg.EntityActionRequest;
import com.cogitareforma.hexrepublics.common.net.msg.EntityCreationRequest;
import com.cogitareforma.hexrepublics.common.net.msg.EntityDeletionRequest;
import com.cogitareforma.hexrepublics.common.net.msg.EntityResponse;
import com.cogitareforma.hexrepublics.gameserver.net.GameServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;
import com.simsilica.es.filter.FieldFilter;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class EntityRequestListener implements MessageListener< HostedConnection >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( EntityRequestListener.class.getName( ) );

	/**
	 * The server manager
	 */
	private GameServerManager manager;

	/**
	 * The default constructor accepts the server's manager.
	 * 
	 * @param manager
	 *            the server manager
	 */
	public EntityRequestListener( GameServerManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		if ( message instanceof EntityCreationRequest )
		{
			EntityCreationRequest entityMsg = ( EntityCreationRequest ) message;
			logger.log(
					Level.INFO,
					"Received an EntityCreationRequest from " + source.getAddress( ) + ", "
							+ ( entityMsg.getLocation( ) != null ? entityMsg.getLocation( ) : "null" ) + ", "
							+ Arrays.toString( entityMsg.getComponents( ) ) );
			if ( entityMsg.getLocation( ) != null )
			{
				LocationTrait location = entityMsg.getLocation( );
				if ( location.getTile( ) != null )
				{
					EntityId ownerId = Traits.getOwner( manager.getEntityData( ), location.getTile( ) );
					if ( ownerId != null )
					{
						Entity owner = manager.getEntityData( ).getEntity( ownerId, PlayerTrait.class );
						if ( owner != null && owner.get( PlayerTrait.class ) != null )
						{
							Account act = manager.getSessionManager( ).getFromSession( source );
							if ( owner.get( PlayerTrait.class ).getAccount( ).equals( act ) )
							{
								/*
								 * TODO: More validation, need to check that all
								 * EntityComponents passed are valid
								 */

								boolean isBuilding = false;
								boolean isUnit = false;
								for ( EntityComponent e : entityMsg.getComponents( ) )
								{
									if ( e instanceof MoveableTrait )
									{
										isUnit = true;
									}
									if ( e instanceof StaticTrait )
									{
										isBuilding = true;
									}
								}

								// As of now, clients can only build
								// buildings or units
								if ( isBuilding || isUnit )
								{
									ComponentFilter< LocationTrait > locFilter = FieldFilter.create( LocationTrait.class, "tile",
											location.getTile( ) );
									Set< EntityId > idSet = manager.getEntityData( ).findEntities( locFilter, LocationTrait.class );

									int unitCount = Traits.countUnits( manager.getEntityData( ), idSet );
									int buildingCount = Traits.countBuildings( manager.getEntityData( ), idSet );

									System.out.println( String.format( "Unit( %s ): %d, Building( %s ): %d", isUnit, unitCount, isBuilding,
											buildingCount ) );

									if ( !isBuilding )
									{
										if ( unitCount < 6 )
										{
											// TODO: Need to check that we
											// have the required buildings
											// and they are not currently
											// being built
											EntityId newEntityId = manager.getEntityData( ).createEntity( );
											manager.getEntityData( ).setComponent( newEntityId,
													new LocationTrait( location.getTile( ), ( byte ) ( unitCount + 1 ) ) );
											manager.getEntityData( ).setComponents( newEntityId, entityMsg.getComponents( ) );

											Traits.entityParturition( manager.getEntityData( ), newEntityId, entityMsg.getComponents( ) );

											source.send( new EntityResponse( newEntityId, EntityCreationRequest.class.getSimpleName( )
													+ manager.getEntityData( ).getEntities( LocationTrait.class ).size( ), true ) );
											return;
										}
									}
									else
									{
										if ( buildingCount < 6 )
										{
											EntityId newEntityId = manager.getEntityData( ).createEntity( );
											manager.getEntityData( ).setComponent( newEntityId,
													new LocationTrait( location.getTile( ), ( byte ) ( buildingCount + 1 ) ) );
											manager.getEntityData( ).setComponents( newEntityId, entityMsg.getComponents( ) );

											Traits.entityParturition( manager.getEntityData( ), newEntityId, entityMsg.getComponents( ) );

											source.send( new EntityResponse( newEntityId, EntityCreationRequest.class.getSimpleName( )
													+ manager.getEntityData( ).getEntities( LocationTrait.class ).size( ), true ) );
											return;
										}
									}
								}
							}
						}
					}
				}
			}
			source.send( new EntityResponse( null, EntityCreationRequest.class.getSimpleName( ) + " Unfinished", false ) );
			return;

		}
		else if ( message instanceof EntityDeletionRequest )
		{
			EntityDeletionRequest entityMsg = ( EntityDeletionRequest ) message;
			logger.log( Level.INFO, "Received an EntityDeletionRequest from " + source.getAddress( ) + ", " + entityMsg.getEntityId( ) );
			if ( entityMsg.getEntityId( ) != null )
			{
				Entity theEntity = manager.getEntityData( ).getEntity( entityMsg.getEntityId( ), CreatedBy.class, LocationTrait.class );
				if ( theEntity != null )
				{
					// TODO:
					EntityId ownerId = null;

					if ( ownerId == null && theEntity.get( CreatedBy.class ) != null )
					{
						ownerId = theEntity.get( CreatedBy.class ).getCreatorId( );
					}

					if ( ownerId == null && theEntity.get( LocationTrait.class ) != null )
					{
						LocationTrait location = theEntity.get( LocationTrait.class );
						if ( location.getTile( ) != null )
						{
							Entity tile = manager.getEntityData( ).getEntity( location.getTile( ), CreatedBy.class );
							if ( tile != null && tile.get( CreatedBy.class ) != null )
							{
								ownerId = tile.get( CreatedBy.class ).getCreatorId( );
							}
						}

					}

					if ( ownerId != null )
					{
						Entity owner = manager.getEntityData( ).getEntity( ownerId, PlayerTrait.class );

						if ( owner != null && owner.get( PlayerTrait.class ) != null )
						{
							Account act = manager.getSessionManager( ).getFromSession( source );
							if ( owner.get( PlayerTrait.class ).getAccount( ).equals( act ) )
							{
								manager.getEntityData( ).removeEntity( entityMsg.getEntityId( ) );
								source.send( new EntityResponse( entityMsg.getEntityId( ), EntityDeletionRequest.class.getSimpleName( ),
										true ) );
								return;
							}
						}
					}
				}
			}
			source.send( new EntityResponse( entityMsg.getEntityId( ), EntityDeletionRequest.class.getSimpleName( ), false ) );
		}
		else if ( message instanceof EntityActionRequest )
		{
			EntityActionRequest entityMsg = ( EntityActionRequest ) message;
			logger.log( Level.INFO, "Received an EntityActionRequest from " + source.getAddress( ) );
			if ( entityMsg.getEntityId( ) != null )
			{
				System.out.println( "EntityActionRequest: " + !Traits.inAction( manager.getEntityData( ), entityMsg.getEntityId( ) ) + ", "
						+ Traits.isUnit( manager.getEntityData( ), entityMsg.getEntityId( ) ) );
				if ( !Traits.inAction( manager.getEntityData( ), entityMsg.getEntityId( ) ) )
				{
					if ( Traits.isUnit( manager.getEntityData( ), entityMsg.getEntityId( ) ) )
					{
						EntityId ownerId = Traits.getOwner( manager.getEntityData( ), entityMsg.getEntityId( ) );
						System.out.println( "EntityActionRequest: owner: " + ownerId );
						if ( ownerId != null )
						{
							// TODO: Validate movement since the user made it
							// Check movetrait's date isn't more than 2 seconds
							// ago
							// Check movetrait's duration is valid
							// Check that the next tile is actually a neighbor
							manager.getEntityData( ).setComponent( entityMsg.getEntityId( ), entityMsg.getAction( ) );
							source.send( new EntityResponse( null, EntityActionRequest.class.getSimpleName( ) + " Unfinished", true ) );
							return;
						}
					}
				}
			}
			source.send( new EntityResponse( null, EntityActionRequest.class.getSimpleName( ) + " Unfinished", false ) );
			return;
			// TODO:
		}

	}
}
