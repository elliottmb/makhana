package com.cogitareforma.makhana.common.eventsystem;

import com.simsilica.es.EntityId;

public class EntityEvent
{
	private EntityId source;
	private EntityEventManager entityEventManager;

	public EntityEvent( EntityEventManager entityEventManager, EntityId source )
	{
		this.source = source;
		this.entityEventManager = entityEventManager;
	}

	public EntityEventManager getEntityEventManager( )
	{
		return entityEventManager;
	}

	public EntityId getSource( )
	{
		return source;
	}

}
