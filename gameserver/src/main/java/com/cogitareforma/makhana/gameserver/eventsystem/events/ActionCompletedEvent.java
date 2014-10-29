package com.cogitareforma.makhana.gameserver.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.ActionTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
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
