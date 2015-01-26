package trendli.me.makhana.masterserver.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.data.Session;
import trendli.me.makhana.common.net.DataManager;
import trendli.me.makhana.common.net.msg.ChatMessage;
import trendli.me.makhana.common.net.msg.WelcomeMessage;

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
		Session session = serverManager.getSessionManager( ).get( source );

		DataManager< Session > sessionManager = serverManager.getSessionManager( );
		sessionManager.remove( source );
		sessionManager.removeConnections( session );
		serverManager.getServerStatusManager( ).remove( source );

		if ( session != null )
		{
			for ( HostedConnection hc : sessionManager.getConnections( ) )
			{
				hc.send( new ChatMessage( null, String.format( "Server Notice: %s is now offline.", session.getDisplayName( ) ) ) );
			}
			serverManager.broadcastUserList( );
		}
	}

}
