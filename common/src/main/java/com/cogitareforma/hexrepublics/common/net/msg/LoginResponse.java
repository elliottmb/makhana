package com.cogitareforma.hexrepublics.common.net.msg;

import com.cogitareforma.hexrepublics.common.data.Account;
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
	private Account account;

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
	public LoginResponse( Account account )
	{
		this.account = account;
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
	public Account getAccount( )
	{
		return account;
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
	 * @param account
	 *            the UserAccount to set
	 */
	public void setAccount( Account account )
	{
		this.account = account;
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
		return "account=" + ( ( account == null ) ? "null" : account.toString( ) );
	}

}
