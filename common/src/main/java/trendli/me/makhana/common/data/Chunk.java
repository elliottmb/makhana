package trendli.me.makhana.common.data;

import com.jme3.network.serializing.Serializable;

@Serializable
public class Chunk
{
	private int x;
	private int y;

	/**
	 * Used by the serializer.
	 */
	public Chunk( )
	{
	}

	public Chunk( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	public int getX( )
	{
		return x;
	}

	public int getY( )
	{
		return y;
	}

	public boolean isInRange( Chunk other, int radius )
	{
		return Math.pow( other.getX( ) - x, 2 ) + Math.pow( other.getY( ) - y, 2 ) < Math.pow( radius, 2 );
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