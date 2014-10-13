package com.cogitareforma.makhana.gameserver.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.ActionTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public class ActionCompletedEvent extends EntityEvent
{

	private ActionTrait action;

	public ActionCompletedEvent( EntityEventManager entityEventManager, EntityId source, ActionTrait action )
	{
		super( entityEventManager, source );
		this.action = action;
	}

	public ActionTrait getAction( )
	{
		return action;
	}

}
