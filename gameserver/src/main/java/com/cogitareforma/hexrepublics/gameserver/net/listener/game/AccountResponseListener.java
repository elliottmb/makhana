package com.cogitareforma.hexrepublics.gameserver.net.listener.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.net.msg.AccountResponse;
import com.cogitareforma.hexrepublics.common.net.msg.AccountVerificationRequest;
import com.cogitareforma.hexrepublics.gameserver.net.GameServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 */
public class AccountResponseListener implements MessageListener< HostedConnection >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( AccountResponseListener.class.getName( ) );

	/**
	 * The server manager
	 */
	private GameServerManager manager;

	/**
	 * The default constructor accepts the server's manager.
	 * 
	 * @param manager
	 *            the server manager
	 */
	public AccountResponseListener( GameServerManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		if ( message instanceof AccountResponse )
		{
			logger.log( Level.INFO, "Received an AccountResponse from " + source.getAddress( ) );
			AccountResponse msg = ( AccountResponse ) message;
			if ( msg.getAccount( ) == null )
			{
				logger.log( Level.INFO, "Account from " + source.getAddress( ) + " is null, dropping connection." );
				source.close( "Could not authenticate a null account" );
				return;
			}
			logger.log( Level.INFO, "Account from " + source.getAddress( ) + " has name: " + msg.getAccount( ).getAccountName( ) );

			// Add the session temporarily
			manager.getSessionManager( ).put( source, msg.getAccount( ) );
			// Send off a verification request
			manager.getApp( ).getMasterConnManager( ).send( new AccountVerificationRequest( msg.getAccount( ) ) );
		}
	}
}
