package com.cogitareforma.makhana.common.entities.components;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class DefenseTrait implements EntityComponent
{

	private float defense;

	public DefenseTrait( )
	{

	}

	public DefenseTrait( float defense )
	{
		this.defense = defense;
	}

	public float getDefense( )
	{
		return defense;
	}

	@Override
	public String toString( )
	{
		return "DefenseTrait[" + defense + "]";
	}
}
