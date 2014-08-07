package com.cogitareforma.hexrepublics.common.entities.traits;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class WorldTrait implements EntityComponent
{
	private int currentTurn;
	private boolean playing;
	private byte seed;

	public WorldTrait( )
	{
		this( 0, ( byte ) ( System.currentTimeMillis( ) % 255 - 127 ), false );
	}

	public WorldTrait( int currentTurn, byte seed, boolean isPlayer )
	{
		this.currentTurn = currentTurn;
		this.seed = seed;
		this.playing = isPlayer;
	}

	/**
	 * @return the currentTurn
	 */
	public int getCurrentTurn( )
	{
		return currentTurn;
	}

	/**
	 * @return the seed
	 */
	public byte getSeed( )
	{
		return seed;
	}

	@Override
	public String toString( )
	{
		return "WorldTrait[" + currentTurn + "]";
	}

	/**
	 * @return the playing
	 */
	public boolean isPlaying( )
	{
		return playing;
	}

}
