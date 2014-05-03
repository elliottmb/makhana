package com.cogitareforma.hexrepublics.common.entities.traits;

import com.jme3.network.serializing.Serializable;

@Serializable
public class MountedTrait extends MoveableTrait
{
	public float getMovementMultiplier( )
	{
		return 0.75f;
	}
}
