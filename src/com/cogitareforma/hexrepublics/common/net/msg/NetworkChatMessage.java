package com.cogitareforma.hexrepublics.common.net.msg;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 * 
 */
@Serializable
public class NetworkChatMessage extends AbstractMessage
{

	/**
	 * The message's account
	 */
	private Account account;
	/**
	 * The message's actual content
	 */
	private String message;

	/**
	 * Used by the serializer.
	 */
	public NetworkChatMessage( )
	{
	}

	/**
	 * Constructor for NetworkChatMessage that sets its account and its message.
	 * 
	 * @param account
	 *            The message's account.
	 * @param message
	 *            The actual message.
	 */
	public NetworkChatMessage( Account account, String message )
	{
		this.account = account;
		this.message = message;
	}

	/**
	 * Returns this NetworkChatMessage's account.
	 * 
	 * @return The account of the NetworkChatMessage.
	 */
	public Account getAccount( )
	{
		return account;
	}

	/**
	 * Returns this NetworkChatMessage's message.
	 * 
	 * @return the message of this NetworkChatMessage.
	 */
	public String getMessage( )
	{
		return message;
	}

	/**
	 * Sets this NetworkChatMessage's account.
	 * 
	 * @param account
	 *            the account to set this NetworkChatMessage's account to.
	 */
	public void setAccount( Account account )
	{
		this.account = account;
	}

	/**
	 * Sets this NetworksChatMessage's message.
	 * 
	 * @param message
	 *            the message to set this NetworkChatMessage's message to.
	 */
	public void setMessage( String message )
	{
		this.message = message;
	}

}
