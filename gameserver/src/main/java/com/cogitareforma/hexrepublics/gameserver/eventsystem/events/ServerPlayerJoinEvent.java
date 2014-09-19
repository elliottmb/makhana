package com.cogitareforma.hexrepublics.gameserver.eventsystem.events;

import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.eventsystem.events.PlayerJoinEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class ServerPlayerJoinEvent extends PlayerJoinEvent
{
	private int playerCount;
	private boolean alreadyExisting;

	public ServerPlayerJoinEvent( EntityData entityData, EntityId source, PlayerTrait playerTrait, int playerCount, boolean alreadyExisting )
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
