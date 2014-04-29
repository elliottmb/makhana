package com.cogitareforma.hexrepublics.common.entities.traits;

import java.util.Date;

import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class FabricatingTrait extends ActionTrait
{
	/**
	 * Used by Serializer
	 */
	public FabricatingTrait( )
	{

	}

	public FabricatingTrait( Date start, long duration )
	{
		super( start, duration );
	}

	@Override
	public String toString( )
	{
		return "FabricatingTrait[" + super.toString( ) + "]";
	}

}
