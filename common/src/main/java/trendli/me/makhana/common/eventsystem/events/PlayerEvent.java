package trendli.me.makhana.common.eventsystem.events;

import trendli.me.makhana.common.entities.components.Player;
import trendli.me.makhana.common.eventsystem.EntityEvent;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public abstract class PlayerEvent extends EntityEvent
{
	/**
	 * Player trait when the event was triggered
	 */
	private Player playerTrait;

	/**
	 * An event involving a Player entity
	 * 
	 * @param entityData
	 *            the entityData for the entity
	 * @param source
	 *            the source entity
	 * @param playerTrait
	 *            player trait when the event was triggered
	 */
	public PlayerEvent( EntityData entityData, EntityId source, Player playerTrait )
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
	public Player getPlayerTrait( )
	{
		return playerTrait;
	}
}
