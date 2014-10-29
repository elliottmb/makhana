package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.TileTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
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
