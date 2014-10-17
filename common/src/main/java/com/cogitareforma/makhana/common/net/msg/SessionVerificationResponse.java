package com.cogitareforma.makhana.common.net.msg;

import com.cogitareforma.makhana.common.data.Session;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * 
 * @author Elliott Butler
 */
@Serializable
public class SessionVerificationResponse extends AbstractMessage
{
	/**
	 * The state of the Session verification
	 */
	private boolean isVerified;

	/**
	 * The Session that was verified or not
	 */
	private Session session;

	/**
	 * Used by the serializer.
	 */
	public SessionVerificationResponse( )
	{
	}

	/**
	 * Constructs a new AccountVerificationResponse with the given Account and
	 * it's verification status
	 * 
	 * @param session
	 *            Session that was verified
	 * @param isVerified
	 *            Verification status: true if the Account was verified
	 *            successfully, otherwise false
	 */
	public SessionVerificationResponse( Session session, Boolean isVerified )
	{
		this.session = session;
		this.isVerified = isVerified;
	}

	/**
	 * Returns the Session which was verified
	 * 
	 * @return the Session
	 */
	public Session getSession( )
	{
		return session;
	}

	/**
	 * Returns the state of the Session's verification
	 * 
	 * @return true if the Session was verified successfully, otherwise false
	 */
	public boolean isVerified( )
	{
		return isVerified;
	}

	/**
	 * Sets the Session which was verified
	 * 
	 * @param account
	 *            the Session to set
	 */
	public void setSession( Session session )
	{
		this.session = session;
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
