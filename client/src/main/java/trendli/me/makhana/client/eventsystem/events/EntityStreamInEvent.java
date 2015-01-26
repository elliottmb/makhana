package trendli.me.makhana.client.eventsystem.events;

import trendli.me.makhana.common.eventsystem.EntityEvent;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class EntityStreamInEvent extends EntityEvent
{

	public EntityStreamInEvent( EntityData entityData, EntityId source )
	{
		super( entityData, source );
	}

}
