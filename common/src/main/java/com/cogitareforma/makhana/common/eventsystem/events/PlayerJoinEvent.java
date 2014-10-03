package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.traits.PlayerTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public class PlayerJoinEvent extends PlayerEvent
{

	public PlayerJoinEvent( EntityEventManager entityEventManager, EntityId source, PlayerTrait playerTrait )
	{
		super( entityEventManager, source, playerTrait );
	}

}
