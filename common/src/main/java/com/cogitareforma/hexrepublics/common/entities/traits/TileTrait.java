package com.cogitareforma.hexrepublics.common.entities.traits;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 * @author Ryan Grier
 */
@Serializable
public class TileTrait implements EntityComponent
{

	private int x;
	private int y;

	public TileTrait( )
	{
	}

	public TileTrait( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	public void setX( int x )
	{
		this.x = x;
	}

	public void setY( int y )
	{
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

	@Override
	public String toString( )
	{
		return "TileTrait[" + x + ", " + y + "]";
	}
}
