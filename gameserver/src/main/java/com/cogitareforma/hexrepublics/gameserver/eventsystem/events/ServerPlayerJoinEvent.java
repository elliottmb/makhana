package com.cogitareforma.hexrepublics.gameserver.eventsystem.events;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.eventsystem.events.PlayerJoinEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class ServerPlayerJoinEvent extends PlayerJoinEvent
{
	private Account account;
	private int playerCount;
	private boolean alreadyExisting;

	public ServerPlayerJoinEvent( EntityData entityData, EntityId source, Account account, int playerCount, boolean alreadyExisting )
	{
		super( entityData, source );
		this.account = account;
		this.playerCount = playerCount;
		this.alreadyExisting = alreadyExisting;
	}

	public Account getAccount( )
	{
		return account;
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
