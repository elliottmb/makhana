package com.cogitareforma.makhana.common.entities.traits;

import com.cogitareforma.makhana.common.data.Chunk;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

public class Position implements EntityComponent
{
	private Chunk chunk;
	private Vector3f location;
	private Quaternion facing;

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

	@Override
	public String toString( )
	{
		return "Position[" + chunk + ", " + location + ", " + facing + "]";
	}
}