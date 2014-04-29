package com.cogitareforma.hexrepublics.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class LoadWorldMessage extends AbstractMessage
{

	/**
	 * The game's world seed
	 */
	private byte seed;

	/**
	 * Used by the serializer.
	 */
	public LoadWorldMessage( )
	{
	}

	/**
	 * Constructor for LoadWorldMessage with the given seed
	 * 
	 * @param seed
	 *            The world seed
	 */
	public LoadWorldMessage( byte seed )
	{
		this.seed = seed;
	}

	/**
	 * Returns this LoadWorldMessage's world seed.
	 * 
	 * @return world seed
	 */
	public byte getSeed( )
	{
		return seed;
	}

	/**
	 * Sets this LoadWorldMessage's world seed.
	 * 
	 * @param seed
	 *            world seed
	 */
	public void setSeed( byte seed )
	{
		this.seed = seed;
	}

}
