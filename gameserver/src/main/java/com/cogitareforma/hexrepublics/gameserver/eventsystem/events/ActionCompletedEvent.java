package com.cogitareforma.hexrepublics.gameserver.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.ActionTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
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
