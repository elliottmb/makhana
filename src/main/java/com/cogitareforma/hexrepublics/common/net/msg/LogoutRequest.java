package com.cogitareforma.hexrepublics.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * LogoutRequest is a specific message type that indicates the client's
 * intention to log out.
 * 
 * @author Justin Kaufman
 */
@Serializable
public class LogoutRequest extends AbstractMessage
{

	/**
	 * Used by serializer.
	 */
	public LogoutRequest( )
	{
	}

}
