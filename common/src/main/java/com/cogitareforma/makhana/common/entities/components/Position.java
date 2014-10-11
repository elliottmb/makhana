package com.cogitareforma.makhana.common.entities.components;

import com.cogitareforma.makhana.common.data.Chunk;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

@Serializable
public class Position implements EntityComponent
{
	private Chunk chunk;
	private Vector3f location;
	private Quaternion facing;

	/**
	 * Used by the serializer.
	 */
	public Position( )
	{
	}

	public Position( Chunk chunk, Vector3f location, Quaternion facing )
	{
		this.chunk = chunk;
		this.location = location;
		this.facing = facing;
	}

	public Chunk getChunk( )
	{
		return chunk;
	}

	public Vector3f getLocation( )
	{
		return location;
	}

	public Quaternion getFacing( )
	{
		return facing;
	}

	public int getQuadrant( )
	{
		if ( location.x != 0 || location.y != 0 )
		{
			if ( location.x >= 0 )
			{
				return location.y >= 0 ? 1 : 4;
			}
			else
			{
				return location.y >= 0 ? 2 : 3;
			}
		}
		return 0;
	}

	@Override
	public String toString( )
	{
		return "Position[" + chunk + ", " + location + ", " + facing + "]";
	}
}