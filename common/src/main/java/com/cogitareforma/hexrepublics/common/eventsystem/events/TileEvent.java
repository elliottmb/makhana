package com.cogitareforma.hexrepublics.common.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventManager;
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
