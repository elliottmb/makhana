package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.traits.TileTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public class TileClaimedEvent extends TileEvent
{
	private EntityId newOwner;

	public TileClaimedEvent( EntityEventManager entityEventManager, EntityId source, TileTrait tileTrait, EntityId newOwner )
	{
		super( entityEventManager, source, tileTrait );
		this.newOwner = newOwner;
	}

	/**
	 * @return the new owner, may be null
	 */
	public EntityId getNewOwner( )
	{
		return newOwner;
	}

}
