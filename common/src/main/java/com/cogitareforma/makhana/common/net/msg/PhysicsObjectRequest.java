package com.cogitareforma.makhana.common.net.msg;

import com.cogitareforma.makhana.common.entities.components.Position;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class PhysicsObjectRequest extends AbstractMessage
{
	private Position position;

	/**
	 * Used by Serializer
	 */
	public PhysicsObjectRequest( )
	{
	}

	public PhysicsObjectRequest( Position position )
	{
		this.position = position;
	}

	public Position getPosition( )
	{
		return position;
	}
}
