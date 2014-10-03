package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.traits.PlayerTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
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
	 * @param entityEventManager
	 *            the entityEventManager which triggered this event
	 * @param source
	 *            the source entity
	 * @param playerTrait
	 *            player trait when the event was triggered
	 */
	public PlayerEvent( EntityEventManager entityEventManager, EntityId source, PlayerTrait playerTrait )
	{
		super( entityEventManager, source );
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
