package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.TileTrait;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class TileClaimedEvent extends TileEvent
{
	private EntityId newOwner;

	public TileClaimedEvent( EntityData entityData, EntityId source, TileTrait tileTrait, EntityId newOwner )
	{
		super( entityData, source, tileTrait );
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
