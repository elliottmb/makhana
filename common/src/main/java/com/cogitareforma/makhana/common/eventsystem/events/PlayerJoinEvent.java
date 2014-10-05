package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.Player;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public class PlayerJoinEvent extends PlayerEvent
{

	public PlayerJoinEvent( EntityEventManager entityEventManager, EntityId source, Player playerTrait )
	{
		super( entityEventManager, source, playerTrait );
	}

}
