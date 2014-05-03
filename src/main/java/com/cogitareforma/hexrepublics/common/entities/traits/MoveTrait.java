package com.cogitareforma.hexrepublics.common.entities.traits;

import java.util.Date;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityId;

@Serializable
public class MoveTrait extends ActionTrait
{

	EntityId newTile;

	/**
	 * Used by Serializer
	 */
	public MoveTrait( )
	{

	}

	public MoveTrait( Date start, Integer duration, EntityId newTile )
	{
		super( start, duration );
		this.newTile = newTile;
	}

	public EntityId getNewTile( )
	{
		return newTile;
	}

	@Override
	public String toString( )
	{
		return "MoveTrait[" + getStartTime( ) + ", " + getDuration( ) + ", " + newTile + "]";
	}

}
