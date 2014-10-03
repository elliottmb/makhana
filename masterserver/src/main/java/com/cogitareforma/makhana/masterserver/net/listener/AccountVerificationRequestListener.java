package com.cogitareforma.makhana.masterserver.net.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Account;
import com.cogitareforma.makhana.common.net.msg.AccountVerificationRequest;
import com.cogitareforma.makhana.common.net.msg.AccountVerificationResponse;
import com.cogitareforma.makhana.masterserver.net.MasterServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class AccountVerificationRequestListener implements MessageListener< HostedConnection >
{

	private final static Logger logger = Logger.getLogger( AccountVerificationRequestListener.class.getName( ) );
	private MasterServerManager server;

	public AccountVerificationRequestListener( MasterServerManager server )
	{
		this.server = server;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		Account loggedInUser = server.getSessionManager( ).getFromSession( source );
		if ( message instanceof AccountVerificationRequest )
		{
			logger.log( Level.INFO, "Recieved a AccountVerificationRequest from " + source.getAddress( ) );
			if ( loggedInUser == null || !loggedInUser.isServer( ) )
			{
				/*
				 * User is either null, is not the same as the logged in user,
				 * or is a client
				 */
				logger.log( Level.INFO, source.getAddress( ) + " does not have permission to AccountVerificationRequest" );
				return;
			}

			AccountVerificationRequest msg = ( AccountVerificationRequest ) message;

			// Sending response back to connection
			logger.log( Level.INFO, "Sending a AccountVerificationResponse to " + source.getAddress( ) );
			source.send( new AccountVerificationResponse( msg.getAccount( ), server.getSessionManager( )
					.containsAccount( msg.getAccount( ) ) ) );

		}
	}

}
