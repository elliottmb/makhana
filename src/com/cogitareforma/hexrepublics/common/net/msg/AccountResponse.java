package com.cogitareforma.hexrepublics.common.net.msg;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * 
 * @author Elliott Butler
 */
@Serializable
public class AccountResponse extends AbstractMessage
{

	/**
	 * The user account that the server has associated us with.
	 */
	private Account account;

	/**
	 * Used by the serializer.
	 */
	public AccountResponse( )
	{
	}

	public AccountResponse( Account account )
	{
		this.account = account;
	}

	public Account getAccount( )
	{
		return account;
	}

	public void setAccount( Account account )
	{
		this.account = account;
	}

	/**
	 * Returns a string representation of this DAO.
	 */
	public String toString( )
	{
		return "account=" + ( ( account == null ) ? "null" : account.toString( ) );
	}

}
