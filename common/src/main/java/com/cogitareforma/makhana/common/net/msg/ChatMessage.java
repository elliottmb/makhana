package com.cogitareforma.makhana.common.net.msg;

import com.cogitareforma.makhana.common.data.Session;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class ChatMessage extends AbstractMessage
{

	/**
	 * The message's session
	 */
	private Session session;
	/**
	 * The message's actual content
	 */
	private String message;

	/**
	 * Used by the serializer.
	 */
	public ChatMessage( )
	{
	}

	/**
	 * Constructor for NetworkChatMessage that sets its session and its message.
	 * 
	 * @param session
	 *            The message's session.
	 * @param message
	 *            The actual message.
	 */
	public ChatMessage( Session session, String message )
	{
		this.session = session;
		this.message = message;
	}

	/**
	 * Returns this NetworkChatMessage's session.
	 * 
	 * @return The session of the NetworkChatMessage.
	 */
	public Session getSession( )
	{
		return session;
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
	public void setSession( Session session )
	{
		this.session = session;
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
