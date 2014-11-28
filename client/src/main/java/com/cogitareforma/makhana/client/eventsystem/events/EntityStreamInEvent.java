package com.cogitareforma.makhana.client.eventsystem.events;

import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class EntityStreamInEvent extends EntityEvent
{

	public EntityStreamInEvent( EntityData entityData, EntityId source )
	{
		super( entityData, source );
	}

}
