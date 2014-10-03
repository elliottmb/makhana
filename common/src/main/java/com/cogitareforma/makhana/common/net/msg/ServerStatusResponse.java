package com.cogitareforma.makhana.common.net.msg;

import com.cogitareforma.makhana.common.data.ServerStatus;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class ServerStatusResponse extends AbstractMessage
{

	/**
	 * The state's actual content
	 */
	private ServerStatus state;

	/**
	 * Used by the serializer.
	 */
	public ServerStatusResponse( )
	{
	}

	/**
	 * Constructs a ServerStatusResponse with given account and ServerStatus.
	 * 
	 * @param account
	 *            the account for the ServerStatusResponse.
	 * @param state
	 *            the ServerStatus for the ServerStatusResponse.
	 */
	public ServerStatusResponse( ServerStatus state )
	{
		this.state = state;
	}

	/**
	 * Returns the ServerStatus of the ServerStatusResponse.
	 * 
	 * @return the ServerStatus.
	 */
	public ServerStatus getServerStatus( )
	{
		return state;
	}

	/**
	 * Sets the ServerStatusResponse's ServerStatus.
	 * 
	 * @param state
	 *            the ServerStatus for the ServerStatusResponse to set.
	 */
	public void setServerStatus( ServerStatus state )
	{
		this.state = state;
	}

}
