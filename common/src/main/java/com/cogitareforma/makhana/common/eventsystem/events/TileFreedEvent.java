package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.TileTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public class TileFreedEvent extends TileEvent
{
	private EntityId oldOwner;

	public TileFreedEvent( EntityEventManager entityEventManager, EntityId source, TileTrait tileTrait, EntityId oldOwner )
	{
		super( entityEventManager, source, tileTrait );

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
