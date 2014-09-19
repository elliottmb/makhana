package com.cogitareforma.hexrepublics.common.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class TileCapturedEvent extends TileEvent
{
	private EntityId oldOwner;
	private EntityId newOwner;

	public TileCapturedEvent( EntityData entityData, EntityId source, TileTrait tileTrait, EntityId oldOwner, EntityId newOwner )
	{
		super( entityData, source, tileTrait );
		// TODO Auto-generated constructor stub
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
