package com.cogitareforma.hexrepublics.gameserver.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.ActionTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventManager;
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
