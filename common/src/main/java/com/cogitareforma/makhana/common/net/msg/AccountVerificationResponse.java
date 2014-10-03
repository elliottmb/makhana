package com.cogitareforma.makhana.common.net.msg;

import com.cogitareforma.makhana.common.data.Account;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * 
 * @author Elliott Butler
 */
@Serializable
public class AccountVerificationResponse extends AbstractMessage
{
	/**
	 * The state of the Account's verification
	 */
	private boolean isVerified;

	/**
	 * The Account that was verified or not
	 */
	private Account account;

	/**
	 * Used by the serializer.
	 */
	public AccountVerificationResponse( )
	{
	}

	/**
	 * Constructs a new AccountVerificationResponse with the given Account and
	 * it's verification status
	 * 
	 * @param account
	 *            Account that was verified
	 * @param isVerified
	 *            Verification status: true if the Account was verified
	 *            successfully, otherwise false
	 */
	public AccountVerificationResponse( Account account, Boolean isVerified )
	{
		this.account = account;
		this.isVerified = isVerified;
	}

	/**
	 * Returns the Account which was verified
	 * 
	 * @return the account
	 */
	public Account getAccount( )
	{
		return account;
	}

	/**
	 * Returns the state of the Account's verification
	 * 
	 * @return true if the Account was verified successfully, otherwise false
	 */
	public boolean isVerified( )
	{
		return isVerified;
	}

	/**
	 * Sets the Account which was verified
	 * 
	 * @param account
	 *            the account to set
	 */
	public void setAccount( Account account )
	{
		this.account = account;
	}

	/**
	 * Sets the state of the Account's verification
	 * 
	 * @param isVerified
	 *            true if the Account was verified successfully, otherwise false
	 */
	public void setVerified( boolean isVerified )
	{
		this.isVerified = isVerified;
	}

}
