package com.cogitareforma.makhana.gameserver.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.Player;
import com.cogitareforma.makhana.common.eventsystem.events.PlayerJoinEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class ServerPlayerJoinEvent extends PlayerJoinEvent
{
	private int playerCount;
	private boolean alreadyExisting;

	public ServerPlayerJoinEvent( EntityData entityData, EntityId source, Player playerTrait, int playerCount, boolean alreadyExisting )
	{
		super( entityData, source, playerTrait );
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
