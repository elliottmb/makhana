package com.cogitareforma.hexrepublics.common.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public class PlayerLeaveEvent extends PlayerEvent
{

	public PlayerLeaveEvent( EntityEventManager entityEventManager, EntityId source, PlayerTrait playerTrait )
	{
		super( entityEventManager, source, playerTrait );
	}

}
