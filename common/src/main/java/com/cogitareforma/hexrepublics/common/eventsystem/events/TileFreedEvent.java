package com.cogitareforma.hexrepublics.common.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class TileFreedEvent extends TileEvent
{
	private EntityId oldOwner;

	public TileFreedEvent( EntityData entityData, EntityId source, TileTrait tileTrait, EntityId oldOwner )
	{
		super( entityData, source, tileTrait );

		this.oldOwner = oldOwner;
	}

	/**
	 * @return the old owner, may be null
	 */
	public EntityId getOldOwner( )
	{
		return oldOwner;
	}

}
