package com.cogitareforma.hexrepublics.common.entities.traits;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

@Serializable
public class PlayerTrait implements EntityComponent
{

	private Account account;
	private int losses;
	private int wins;
	private boolean ready;

	/**
	 * Used by Serializer
	 */
	public PlayerTrait( )
	{
		this( null, 0, 0, false );
	}

	public PlayerTrait( Account account )
	{
		this( account, 0, 0, false );
	}

	public PlayerTrait( Account account, int wins, int losses, boolean isReady )
	{
		this.account = account;
		this.wins = wins;
		this.losses = losses;
		this.ready = isReady;
	}

	public Account getAccount( )
	{
		return account;
	}

	/**
	 * @return the losses
	 */
	public int getLosses( )
	{
		return losses;
	}

	/**
	 * @return the wins
	 */
	public int getWins( )
	{
		return wins;
	}

	@Override
	public String toString( )
	{
		return "PlayerTrait[" + account + ", wins: " + wins + ", losses: " + losses + ", ready: " + ready + "]";
	}

	/**
	 * @return the ready
	 */
	public boolean isReady( )
	{
		return ready;
	}
}
