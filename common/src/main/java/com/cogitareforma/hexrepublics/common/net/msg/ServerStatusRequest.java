package com.cogitareforma.hexrepublics.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class ServerStatusRequest extends AbstractMessage
{

	/**
	 * Used by serializer.
	 */
	public ServerStatusRequest( )
	{
	}

}
