package com.cogitareforma.hexrepublics.common.entities.traits;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class HealthTrait implements EntityComponent
{

	private float health;

	public HealthTrait( )
	{

	}

	public HealthTrait( float health )
	{
		this.health = health;
	}

	public float getHealth( )
	{
		return health;
	}

	@Override
	public String toString( )
	{
		return "HealthTrait[" + health + "]";
	}
}
