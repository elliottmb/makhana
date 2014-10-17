package com.cogitareforma.makhana.masterserver.net.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.net.msg.SessionVerificationRequest;
import com.cogitareforma.makhana.common.net.msg.SessionVerificationResponse;
import com.cogitareforma.makhana.masterserver.net.MasterServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class SessionVerificationRequestListener implements MessageListener< HostedConnection >
{

	private final static Logger logger = Logger.getLogger( SessionVerificationRequestListener.class.getName( ) );
	private MasterServerManager server;

	public SessionVerificationRequestListener( MasterServerManager server )
	{
		this.server = server;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		if ( message instanceof SessionVerificationRequest )
		{
			logger.log( Level.INFO, "Recieved a AccountVerificationRequest from " + source.getAddress( ) );

			SessionVerificationRequest msg = ( SessionVerificationRequest ) message;

			// Sending response back to connection
			logger.log( Level.INFO, "Sending a AccountVerificationResponse to " + source.getAddress( ) );
			source.send( new SessionVerificationResponse( msg.getSession( ), server.getSessionManager( ).contains( msg.getSession( ) ) ) );
		}
	}
}
