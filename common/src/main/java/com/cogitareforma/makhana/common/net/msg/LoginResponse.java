package com.cogitareforma.makhana.common.net.msg;

import com.cogitareforma.makhana.common.data.Session;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * AuthResponse is a DAO class that represents the server's response to an
 * authentication request.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
@Serializable
public class LoginResponse extends AbstractMessage
{

	/**
	 * The user account that the server has associated us with.
	 */
	private Session session;

	/**
	 * A notice, or message, which outlines the state of the response
	 */
	private String notice;

	/**
	 * Used by the serializer.
	 */
	public LoginResponse( )
	{
	}

	/**
	 * Constructor sets the specified UserAccount as the user account that a
	 * client has successfully authenticated as.
	 */
	public LoginResponse( Session session )
	{
		this.session = session;
	}

	/**
	 * Constructor sets the specified notice and leaves the account null
	 */
	public LoginResponse( String notice )
	{
		this.notice = notice;
	}

	/**
	 * Returns this associated UserAccount with the server response.
	 * 
	 * @return the associated UserAccount
	 */
	public Session getSession( )
	{
		return session;
	}

	/**
	 * @return the notice
	 */
	public String getNotice( )
	{
		return notice;
	}

	/**
	 * Sets the UserAccount for this DAO.
	 * 
	 * @param session
	 *            the UserAccount to set
	 */
	public void setAccount( Session session )
	{
		this.session = session;
	}

	/**
	 * @param notice
	 *            the notice to set
	 */
	public void setNotice( String notice )
	{
		this.notice = notice;
	}

	/**
	 * Returns a string representation of this DAO.
	 */
	public String toString( )
	{
		return "session=" + ( ( session == null ) ? "null" : session.toString( ) );
	}

}
