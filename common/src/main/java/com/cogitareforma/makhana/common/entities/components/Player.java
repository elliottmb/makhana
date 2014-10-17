package com.cogitareforma.makhana.common.entities.components;

import com.cogitareforma.makhana.common.data.Session;
import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

@Serializable
public class Player implements EntityComponent
{

	private Session session;
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

	public Player( Session session )
	{
		this( session, 0, 0, false );
	}

	public Player( Session session, int kills, int deaths, boolean isAlive )
	{
		this.session = session;
		this.kills = kills;
		this.deaths = deaths;
		this.alive = isAlive;
	}

	public Session getSession( )
	{
		return session;
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
		return "PlayerTrait[" + session + ", kills: " + kills + ", deaths: " + deaths + ", isAlive: " + alive + "]";
	}
}
