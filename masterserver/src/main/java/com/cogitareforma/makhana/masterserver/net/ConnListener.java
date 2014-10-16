package com.cogitareforma.makhana.masterserver.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Account;
import com.cogitareforma.makhana.common.net.SessionManager;
import com.cogitareforma.makhana.common.net.msg.NetworkChatMessage;
import com.cogitareforma.makhana.common.net.msg.WelcomeMessage;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

/**
 * ConnListener serves as a connection listener for removing user sessions from
 * the SessionManager when clients disconnect.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 * 
 */
public class ConnListener implements ConnectionListener
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ConnListener.class.getName( ) );

	/**
	 * The MasterServerManager that this will respond to
	 */
	private MasterServerManager serverManager;

	/**
	 * Constructs a new ConnListener for the given MasterServerManager
	 * 
	 * @param serverManager
	 *            given MasterServerManager
	 */
	public ConnListener( MasterServerManager serverManager )
	{
		this.serverManager = serverManager;
	}

	@Override
	public void connectionAdded( Server server, HostedConnection source )
	{
		logger.log( Level.INFO, "New incoming connection=" + source.toString( ) );
		source.send( new WelcomeMessage( serverManager.getKeyPair( ).getPublic( ), "No notice yet! Maybe one day!" ) );
	}

	@Override
	public void connectionRemoved( Server server, HostedConnection source )
	{
		logger.log( Level.INFO, "Removing the sessions for connection=" + source.toString( ) );
		Account account = serverManager.getSessionManager( ).getFromSession( source );

		SessionManager sessionManager = serverManager.getSessionManager( );
		sessionManager.removeSession( source );
		sessionManager.removeSessions( account );
		serverManager.getServerStatusManager( ).remove( source );

		if ( account != null && !account.isServer( ) )
		{
			for ( HostedConnection hc : sessionManager.getAllSessions( ) )
			{
				hc.send( new NetworkChatMessage( null, String.format( "Server Notice: %s is now offline.", account.getAccountName( ) ) ) );
			}
			serverManager.broadcastUserList( );
		}
	}

}
