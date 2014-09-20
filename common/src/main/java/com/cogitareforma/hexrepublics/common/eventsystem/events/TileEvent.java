package com.cogitareforma.hexrepublics.common.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class TileEvent extends EntityEvent
{
	private TileTrait tileTrait;

	public TileEvent( EntityData entityData, EntityId source, TileTrait tileTrait )
	{
		super( entityData, source );

		this.tileTrait = tileTrait;
	}

	public TileTrait getTileTrait( )
	{
		return tileTrait;
	}
}
