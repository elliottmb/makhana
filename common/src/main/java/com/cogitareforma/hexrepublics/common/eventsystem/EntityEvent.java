package com.cogitareforma.hexrepublics.common.eventsystem;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class EntityEvent
{
	private EntityData entityData;
	private EntityId source;

	public EntityEvent( EntityData entityData, EntityId source )
	{
		this.entityData = entityData;
		this.source = source;
	}

	public EntityData getEntityData( )
	{
		return entityData;
	}

	public EntityId getSource( )
	{
		return source;
	}

}
