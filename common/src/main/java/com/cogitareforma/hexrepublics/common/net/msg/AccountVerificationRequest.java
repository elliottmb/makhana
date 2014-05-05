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
public class AccountVerificationRequest extends AbstractMessage
{
	/**
	 * The user account that the server has associated us with.
	 */
	private Account account;

	/**
	 * Used by serializer.
	 */
	public AccountVerificationRequest( )
	{
	}

	/**
	 * Constructs a new AccountVerificationRequest with the given Account to be
	 * verified
	 * 
	 * @param account
	 *            given Account to be verified
	 */
	public AccountVerificationRequest( Account account )
	{
		this.account = account;
	}

	/**
	 * Returns the Account that needs to be verified
	 * 
	 * @return Account to be verified
	 */
	public Account getAccount( )
	{
		return account;
	}

	/**
	 * Sets the Account that needs to be verified
	 * 
	 * @param account
	 *            Account to be verified
	 */
	public void setAccount( Account account )
	{
		this.account = account;
	}

}
