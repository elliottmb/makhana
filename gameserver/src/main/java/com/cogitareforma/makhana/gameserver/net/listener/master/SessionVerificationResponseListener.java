package com.cogitareforma.makhana.gameserver.net.listener.master;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Session;
import com.cogitareforma.makhana.common.net.DataManager;
import com.cogitareforma.makhana.common.net.msg.SessionVerificationResponse;
import com.cogitareforma.makhana.gameserver.net.MasterConnectionManager;
import com.jme3.network.Client;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 */
public class SessionVerificationResponseListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SessionVerificationResponseListener.class.getName( ) );

	/**
	 * The client's controller.
	 */
	private MasterConnectionManager manager;

	/**
	 * The default constructor accepts the server's controller.
	 * 
	 * @param manager
	 *            the client's controller
	 */
	public SessionVerificationResponseListener( MasterConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof SessionVerificationResponse )
		{
			logger.log( Level.INFO, "Received an AccountVerificationResponse." );
			SessionVerificationResponse msg = ( SessionVerificationResponse ) message;

			if ( msg.isVerified( ) )
			{
				logger.log( Level.INFO, "Account for " + msg.getSession( ).getDisplayName( ) + " successfully verified." );
				manager.getApp( ).getGameServerManager( ).createPlayerEntity( msg.getSession( ) );
			}
			else
			{
				logger.log( Level.INFO, "Account for " + msg.getSession( ).getDisplayName( ) + " could not be verified." );
				DataManager< Session > sm = manager.getApp( ).getGameServerManager( ).getSessionManager( );
				for ( HostedConnection hc : sm.getConnections( ) )
				{
					if ( sm.get( hc ).equals( msg.getSession( ) ) )
					{
						hc.close( "Account could not be verified" );
					}
				}
			}
		}
	}
}
