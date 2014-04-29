package com.cogitareforma.hexrepublics.common.net.msg;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.data.ServerStatus;
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
	 * The state's account
	 */
	private Account account;
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
	public ServerStatusResponse( Account account, ServerStatus state )
	{
		this.account = account;
		this.state = state;
	}

	/**
	 * Returns the account of the ServerStatusResponse.
	 * 
	 * @return the account of the ServerStatusResponse.
	 */
	public Account getAccount( )
	{
		return account;
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
	 * Sets the ServerStatusResponse's account.
	 * 
	 * @param account
	 *            the account for the ServerStatusResponse to set.
	 */
	public void setAccount( Account account )
	{
		this.account = account;
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
