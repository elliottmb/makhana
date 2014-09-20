package com.cogitareforma.hexrepublics.common.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class PlayerJoinEvent extends PlayerEvent
{

	public PlayerJoinEvent( EntityData entityData, EntityId source, PlayerTrait playerTrait )
	{
		super( entityData, source, playerTrait );
	}

}
