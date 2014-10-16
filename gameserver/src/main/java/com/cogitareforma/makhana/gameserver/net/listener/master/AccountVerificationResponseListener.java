package com.cogitareforma.makhana.gameserver.net.listener.master;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.net.SessionManager;
import com.cogitareforma.makhana.common.net.msg.AccountVerificationResponse;
import com.cogitareforma.makhana.gameserver.net.GameMasterConnManager;
import com.jme3.network.Client;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 */
public class AccountVerificationResponseListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( AccountVerificationResponseListener.class.getName( ) );

	/**
	 * The client's controller.
	 */
	private GameMasterConnManager manager;

	/**
	 * The default constructor accepts the server's controller.
	 * 
	 * @param manager
	 *            the client's controller
	 */
	public AccountVerificationResponseListener( GameMasterConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof AccountVerificationResponse )
		{
			logger.log( Level.INFO, "Received an AccountVerificationResponse." );
			AccountVerificationResponse msg = ( AccountVerificationResponse ) message;

			if ( msg.isVerified( ) )
			{
				logger.log( Level.INFO, "Account for " + msg.getAccount( ).getAccountName( ) + " successfully verified." );
				manager.getApp( ).getGameServerManager( ).createPlayerEntity( msg.getAccount( ) );
			}
			else
			{
				logger.log( Level.INFO, "Account for " + msg.getAccount( ).getAccountName( ) + " could not be verified." );
				SessionManager sm = manager.getApp( ).getGameServerManager( ).getSessionManager( );
				for ( HostedConnection hc : sm.getAllSessions( ) )
				{
					if ( sm.getFromSession( hc ).equals( msg.getAccount( ) ) )
					{
						hc.close( "Account could not be verified" );
					}
				}
			}
		}
	}
}
