package com.cogitareforma.makhana.common.entities.components;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class Health implements EntityComponent
{
	private float health;

	public Health( float health )
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
