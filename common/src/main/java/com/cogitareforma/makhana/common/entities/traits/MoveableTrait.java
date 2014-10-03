package com.cogitareforma.makhana.common.entities.traits;

import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public abstract class MoveableTrait extends TypeTrait
{

	public float getMovementMultiplier( )
	{
		return 1.0f;
	}

}
