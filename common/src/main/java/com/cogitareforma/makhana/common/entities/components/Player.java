package com.cogitareforma.makhana.common.entities.components;

import com.cogitareforma.makhana.common.data.Account;
import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

@Serializable
public class Player implements EntityComponent
{

	private Account account;
	private int deaths;
	private int kills;
	private boolean alive;

	/**
	 * Used by Serializer
	 */
	public Player( )
	{
		this( null, 0, 0, false );
	}

	public Player( Account account )
	{
		this( account, 0, 0, false );
	}

	public Player( Account account, int kills, int deaths, boolean isAlive )
	{
		this.account = account;
		this.kills = kills;
		this.deaths = deaths;
		this.alive = isAlive;
	}

	public Account getAccount( )
	{
		return account;
	}

	/**
	 * @return the deaths
	 */
	public int getDeaths( )
	{
		return deaths;
	}

	/**
	 * @return the kills
	 */
	public int getKills( )
	{
		return kills;
	}

	/**
	 * @return is alive
	 */
	public boolean isAlive( )
	{
		return alive;
	}

	@Override
	public String toString( )
	{
		return "PlayerTrait[" + account + ", kills: " + kills + ", deaths: " + deaths + ", isAlive: " + alive + "]";
	}
}
