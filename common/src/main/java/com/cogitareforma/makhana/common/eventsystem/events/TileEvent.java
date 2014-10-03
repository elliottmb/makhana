package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.traits.TileTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public class TileEvent extends EntityEvent
{
	private TileTrait tileTrait;

	public TileEvent( EntityEventManager entityEventManager, EntityId source, TileTrait tileTrait )
	{
		super( entityEventManager, source );

		this.tileTrait = tileTrait;
	}

	public TileTrait getTileTrait( )
	{
		return tileTrait;
	}
}
