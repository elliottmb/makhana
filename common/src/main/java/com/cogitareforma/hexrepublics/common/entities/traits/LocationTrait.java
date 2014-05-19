package com.cogitareforma.hexrepublics.common.entities.traits;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class LocationTrait implements EntityComponent
{
	private EntityId tile;
	private byte position;

	/**
	 * Used by the serializer
	 */
	public LocationTrait( )
	{

	}

	public LocationTrait( EntityId tile, byte position )
	{
		this.tile = tile;
		this.position = position;
	}

	public byte getPosition( )
	{
		return position;
	}

	public EntityId getTile( )
	{
		return tile;
	}

	@Override
	public String toString( )
	{
		return "LocationTrait[" + tile + "," + position + "]";
	}
}
