package com.cogitareforma.hexrepublics.gameserver.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.ActionTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class ActionCompletedEntityEvent extends EntityEvent
{

	private ActionTrait action;

	public ActionCompletedEntityEvent( EntityData entityData, EntityId source, ActionTrait action )
	{
		super( entityData, source );
		// TODO Auto-generated constructor stub
		this.action = action;
	}

	public ActionTrait getAction( )
	{
		return action;
	}

}
