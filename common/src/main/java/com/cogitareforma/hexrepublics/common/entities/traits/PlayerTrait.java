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

	/**
	 * Used by Serializer
	 */
	public PlayerTrait( )
	{
		this( null, 0, 0 );
	}

	public PlayerTrait( Account account )
	{
		this( account, 0, 0 );
	}

	public PlayerTrait( Account account, int wins, int losses )
	{
		this.account = account;
		this.wins = wins;
		this.losses = losses;
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
		return "PlayerTrait[" + account + ", wins: " + wins + ", losses: " + losses + "]";
	}
}
