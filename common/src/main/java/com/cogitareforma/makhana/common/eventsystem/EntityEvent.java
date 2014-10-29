package com.cogitareforma.makhana.common.eventsystem;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class EntityEvent extends Event
{
	private EntityId source;
	private EntityData entityData;

	public EntityEvent( EntityData entityData, EntityId source )
	{
		super( );
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
