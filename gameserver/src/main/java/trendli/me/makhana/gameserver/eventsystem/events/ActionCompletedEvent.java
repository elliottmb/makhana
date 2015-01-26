package trendli.me.makhana.gameserver.eventsystem.events;

import trendli.me.makhana.common.entities.components.ActionTrait;
import trendli.me.makhana.common.eventsystem.EntityEvent;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class ActionCompletedEvent extends EntityEvent
{

	private ActionTrait action;

	public ActionCompletedEvent( EntityData entityData, EntityId source, ActionTrait action )
	{
		super( entityData, source );
		this.action = action;
	}

	public ActionTrait getAction( )
	{
		return action;
	}

}
