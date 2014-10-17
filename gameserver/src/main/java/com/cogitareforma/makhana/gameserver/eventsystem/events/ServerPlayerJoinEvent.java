package com.cogitareforma.makhana.gameserver.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.Player;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.cogitareforma.makhana.common.eventsystem.events.PlayerJoinEvent;
import com.simsilica.es.EntityId;

public class ServerPlayerJoinEvent extends PlayerJoinEvent
{
	private int playerCount;
	private boolean alreadyExisting;

	public ServerPlayerJoinEvent( EntityEventManager entityEventManager, EntityId source, Player playerTrait, int playerCount,
			boolean alreadyExisting )
	{
		super( entityEventManager, source, playerTrait );
		this.playerCount = playerCount;
		this.alreadyExisting = alreadyExisting;
	}

	public int getPlayerCount( )
	{
		return playerCount;
	}

	/**
	 * @return the alreadyExisting
	 */
	public boolean isAlreadyExisting( )
	{
		return alreadyExisting;
	}

}