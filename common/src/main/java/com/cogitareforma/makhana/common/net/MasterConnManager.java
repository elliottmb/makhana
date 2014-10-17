package com.cogitareforma.makhana.common.net;

import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Account;
import com.cogitareforma.makhana.common.data.LoginCredentials;
import com.cogitareforma.makhana.common.net.msg.LogoutRequest;
import com.cogitareforma.makhana.common.net.msg.SecureLoginRequest;
import com.jme3.app.Application;

/**
 * MasterConnManager serves as the manager of the Client's connection to the
 * Master Server.
 * 
 * @author Elliott Butler
 * @param <A>
 *            Subclass of Application
 * 
 */
public class MasterConnManager< A extends Application > extends ConnectionManager< A >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( MasterConnManager.class.getName( ) );

	/**
	 * The account of this connection
	 */
	private Account account;

	private Key publicKey;

	/**
	 * Creates an instance of the MasterConnManager with the supplied owning
	 * Application
	 * 
	 * @param app
	 *            the owning application of this manager
	 */
	protected MasterConnManager( A app )
	{
		super( app );
	}

	/**
	 * Returns the account of this connection
	 * 
	 * @return the account of this connection
	 */
	public Account getAccount( )
	{
		return account;
	}

	/**
	 * Returns true if we are logged in, false otherwise.
	 * 
	 * @return true if we are logged in, false otherwise
	 */
	public boolean isLoggedIn( )
	{
		return getAccount( ) != null;
	}

	/**
	 * Sends a login to the MasterServer.
	 * 
	 * @param accountName
	 *            the accountname to auth as
	 * @param password
	 *            the password to auth as
	 * 
	 */
	public void sendLogin( String accountName, String password )
	{
		logger.log( Level.FINE, "Sending account and pass to the connection." );
		send( new SecureLoginRequest( new LoginCredentials( accountName, password, null ), getPublicKey( ), false ) );
	}

	/**
	 * Sends a logout to the MasterServer.
	 * 
	 */
	public void sendLogout( )
	{
		logger.log( Level.FINE, "Sending logout request to the connection." );
		send( new LogoutRequest( ) );
		setAccount( null );
	}

	/**
	 * Sets the account of this connection
	 * 
	 * @param account
	 *            the account of this connection
	 */
	public void setAccount( Account account )
	{
		logger.log( Level.INFO, "Setting user account: " + account );
		this.account = account;
	}

	/**
	 * @return the publicKey
	 */
	public Key getPublicKey( )
	{
		return publicKey;
	}

	/**
	 * @param publicKey
	 *            the publicKey to set
	 */
	public void setPublicKey( Key publicKey )
	{
		this.publicKey = publicKey;
	}

}
