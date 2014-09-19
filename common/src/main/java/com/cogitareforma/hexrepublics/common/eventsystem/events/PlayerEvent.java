package com.cogitareforma.hexrepublics.common.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public abstract class PlayerEvent extends EntityEvent
{
	/**
	 * Player trait when the event was triggered
	 */
	private PlayerTrait playerTrait;

	/**
	 * An event involving a Player entity
	 * 
	 * @param entityData
	 *            the entity data containing the source
	 * @param source
	 *            the source entity
	 * @param playerTrait
	 *            player trait when the event was triggered
	 */
	public PlayerEvent( EntityData entityData, EntityId source, PlayerTrait playerTrait )
	{
		super( entityData, source );
		// TODO Auto-generated constructor stub
		this.playerTrait = playerTrait;
	}

	/**
	 * Returns the player trait associated to the entity when the event was
	 * triggered
	 * 
	 * @return player trait when the event was triggered
	 */
	public PlayerTrait getPlayerTrait( )
	{
		return playerTrait;
	}
}
