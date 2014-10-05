package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.TileTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public class TileCapturedEvent extends TileEvent
{
	private EntityId oldOwner;
	private EntityId newOwner;

	public TileCapturedEvent( EntityEventManager entityEventManager, EntityId source, TileTrait tileTrait, EntityId oldOwner,
			EntityId newOwner )
	{
		super( entityEventManager, source, tileTrait );
		this.newOwner = newOwner;
		this.oldOwner = oldOwner;
	}

	/**
	 * @return the old owner, may be null
	 */
	public EntityId getOldOwner( )
	{
		return oldOwner;
	}

	/**
	 * @return the new owner, may be null
	 */
	public EntityId getNewOwner( )
	{
		return newOwner;
	}

}
