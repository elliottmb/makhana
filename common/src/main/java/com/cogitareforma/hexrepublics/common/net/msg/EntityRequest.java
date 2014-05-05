package com.cogitareforma.hexrepublics.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public abstract class EntityRequest extends AbstractMessage
{

	/**
	 * Used by Serializer
	 */
	public EntityRequest( )
	{

	}
}
