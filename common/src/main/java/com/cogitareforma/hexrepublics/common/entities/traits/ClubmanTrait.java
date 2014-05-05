package com.cogitareforma.hexrepublics.common.entities.traits;

import com.jme3.network.serializing.Serializable;

//TODO fix initial everything.
@Serializable
public class ClubmanTrait extends MoveableTrait
{
	@Override
	public float getInitialHealth( )
	{
		return 10.0f;
	}

	@Override
	public float getInitialDefense( )
	{
		return 5.0f;
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
