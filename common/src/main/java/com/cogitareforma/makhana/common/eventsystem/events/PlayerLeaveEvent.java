package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.Player;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class PlayerLeaveEvent extends PlayerEvent
{

	public PlayerLeaveEvent( EntityData entityData, EntityId source, Player playerTrait )
	{
		super( entityData, source, playerTrait );
	}

}
