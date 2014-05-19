package com.cogitareforma.hexrepublics.common.entities.traits;

import com.jme3.network.serializing.Serializable;

@Serializable
public class CatapultTrait extends MoveableTrait
{
	@Override
	public float getInitialDefense( )
	{
		return 5.0f;
	}

	// TODO Change values.
	@Override
	public float getInitialHealth( )
	{
		return 10.0f;
	}

	@Override
	public float getInitialStrength( )
	{
		return 10.0f;
	}

	@Override
	public float getMovementMultiplier( )
	{
		return 0.95f;
	}

}
