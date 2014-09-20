package com.cogitareforma.hexrepublics.gameserver.eventsystem.handlers;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.entities.ActionType;
import com.cogitareforma.hexrepublics.common.entities.Traits;
import com.cogitareforma.hexrepublics.common.entities.traits.ActionTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.DefenseTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.HealthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.StrengthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventHandler;
import com.cogitareforma.hexrepublics.gameserver.eventsystem.events.ActionCompletedEvent;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.filter.FieldFilter;

public class ActionCompletedEventHandler implements EntityEventHandler
{
	private final static Logger logger = Logger.getLogger( ActionCompletedEventHandler.class.getName( ) );

	@Override
	public byte getPriority( )
	{
		return 0;
	};

	@Override
	public boolean handle( EntityEvent event )
	{
		if ( event instanceof ActionCompletedEvent )
		{
			ActionCompletedEvent actionCompleted = ( ActionCompletedEvent ) event;

			EntityData entityData = actionCompleted.getEntityData( );
			EntityId id = actionCompleted.getSource( );
			ActionTrait at = actionCompleted.getAction( );

			if ( at.getType( ).equals( ActionType.MOVE ) )
			{
				Map< String, Object > data = at.getData( );

				EntityId newTile = ( EntityId ) data.get( "newTile" );
				if ( newTile != null )
				{
					LocationTrait location = entityData.getComponent( id, LocationTrait.class );
					if ( location != null )
					{
						if ( Traits.areNeighbors( entityData, location.getTile( ), newTile ) )
						{
							// Get the entities are the target tile
							ComponentFilter< LocationTrait > locFilter = FieldFilter.create( LocationTrait.class, "tile", newTile );
							Set< EntityId > targetIdSet = entityData.findEntities( locFilter, LocationTrait.class );

							ComponentFilter< LocationTrait > sourceLocFilter = FieldFilter.create( LocationTrait.class, "tile",
									location.getTile( ) );
							Set< EntityId > sourceIdSet = entityData.findEntities( sourceLocFilter, LocationTrait.class );

							// Units at the target tile
							int unitCount = Traits.countUnits( entityData, targetIdSet );
							// Buildings at the target tile
							int buildingCount = Traits.countBuildings( entityData, targetIdSet );

							// Get info on the target tile
							Entity tile = entityData.getEntity( newTile, CreatedBy.class, TileTrait.class );

							Entity source = entityData.getEntity( location.getTile( ), CreatedBy.class, TileTrait.class );

							if ( tile.get( CreatedBy.class ) != null )
							{
								/*
								 * Owned, check if owned by same player or enemy
								 */
								if ( source.get( CreatedBy.class ).getCreatorId( ).compareTo( tile.get( CreatedBy.class ).getCreatorId( ) ) == 0 )
								{
									// Make sure there is room
									if ( unitCount < 6 )
									{
										entityData.setComponent( id,
												new LocationTrait( newTile, Traits.getOpenUnitPosition( entityData, targetIdSet ) ) );
									}
								}
								else
								{
									// Owned by enemy
									float targetStr = 0.0f;
									float sourceStr = 0.0f;
									if ( entityData.getComponent( id, StrengthTrait.class ) != null )
									{
										sourceStr = entityData.getComponent( id, StrengthTrait.class ).getStrength( );
									}
									System.out.println( "Source Strength: " + sourceStr );
									/*
									 * divide att by amount of defending units
									 * apply to all def units health if health
									 * <= 0 delete unit if all units dead, move
									 * to tile and take ownership butt if units
									 * remain, apply str back to attacking
									 */
									float newStr = sourceStr / Traits.countUnits( entityData, targetIdSet );
									for ( EntityId targetId : targetIdSet )
									{
										if ( Traits.isUnit( entityData, targetId ) )
										{
											Entity unit = entityData.getEntity( targetId, HealthTrait.class, DefenseTrait.class );
											if ( unit.get( HealthTrait.class ) != null )
											{
												float health = unit.get( HealthTrait.class ).getHealth( ) - newStr;
												if ( health <= 0 )
												{
													entityData.removeEntity( targetId );
												}
												else
												{
													System.out.println( "New Target Health: " + health );
													entityData.setComponent( targetId, new HealthTrait( health ) );
												}

											}
										}
									}

									targetIdSet = entityData.findEntities( locFilter, LocationTrait.class );
									if ( Traits.countUnits( entityData, targetIdSet ) == 0 )
									{
										entityData.setComponent( newTile,
												new CreatedBy( entityData.getComponent( location.getTile( ), CreatedBy.class )
														.getCreatorId( ) ) );
										entityData.setComponent( id, new LocationTrait( newTile, ( byte ) 0 ) );
									}
									else
									{
										for ( EntityId targetId : targetIdSet )
										{
											if ( Traits.isUnit( entityData, targetId ) )
											{
												Entity unit = entityData.getEntity( targetId, StrengthTrait.class );
												if ( unit.get( StrengthTrait.class ) != null )
												{
													targetStr += unit.get( StrengthTrait.class ).getStrength( );
												}
											}
										}
										// Entity sourceUnit = getEntityData(
										// ).getEntity( e.getId( ),
										// HealthTrait.class );
										HealthTrait sourceHealth = entityData.getComponent( id, HealthTrait.class );
										if ( sourceHealth != null )
										{
											float health = sourceHealth.getHealth( ) - targetStr;
											if ( health <= 0 )
											{
												entityData.removeEntity( id );
											}
											else
											{
												System.out.println( "New Source Health: " + health );
												entityData.setComponent( id, new HealthTrait( health ) );
											}

										}
									}
								}
							}
							else
							{
								// Unowned, just take it
								entityData.setComponent( newTile,
										new CreatedBy( entityData.getComponent( location.getTile( ), CreatedBy.class ).getCreatorId( ) ) );
								entityData.setComponent( id, new LocationTrait( newTile, ( byte ) ( unitCount ) ) );
							}
						}

					}
				}
			}

			logger.log( Level.INFO, "Removing ActionTrait from " + id.toString( ) );
			entityData.removeComponent( id, ActionTrait.class );
		}

		return false;
	}

}