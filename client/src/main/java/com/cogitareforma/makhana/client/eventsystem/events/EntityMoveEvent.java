package com.cogitareforma.makhana.client.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.Position;
import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class EntityMoveEvent extends EntityEvent
{
	/**
	 * Player trait when the event was triggered
	 */
	private Position position;

	/**
	 * An event involving a position change
	 * 
	 * @param entityData
	 *            the entityData for the entity
	 * @param source
	 *            the source entity
	 * @param position
	 *            position when the event was triggered
	 */
	public EntityMoveEvent( EntityData entityData, EntityId source, Position position )
	{
		super( entityData, source );
		this.position = position;
	}

	/**
	 * Returns the position associated to the entity when the event was
	 * triggered
	 * 
	 * @return position when the event was triggered
	 */
	public Position getPosition( )
	{
		return position;
	}
}
