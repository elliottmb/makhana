package com.cogitareforma.makhana.common.entities.traits;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class StrengthTrait implements EntityComponent
{

	private float strength;

	public StrengthTrait( )
	{

	}

	public StrengthTrait( float strength )
	{
		this.strength = strength;
	}

	public float getStrength( )
	{
		return strength;
	}

	@Override
	public String toString( )
	{
		return "StrengthTrait[" + strength + "]";
	}
}
