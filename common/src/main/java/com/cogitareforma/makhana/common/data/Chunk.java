package com.cogitareforma.makhana.common.data;

import com.jme3.network.serializing.Serializable;

@Serializable
public class Chunk
{
	private short x;
	private short y;

	public Chunk( short x, short y )
	{
		this.x = x;
		this.y = y;
	}

	public short getX( )
	{
		return x;
	}

	public short getY( )
	{
		return y;
	}

	public boolean isNeighbor( Chunk other )
	{
		if ( other.getX( ) == x )
		{
			if ( other.getY( ) - 1 == y || other.getY( ) + 1 == y )
			{
				return true;
			}
		}
		else if ( other.getY( ) == y )
		{
			if ( other.getX( ) - 1 == x || other.getX( ) + 1 == x )
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString( )
	{
		return "Chunk[" + x + ", " + y + "]";
	}

}