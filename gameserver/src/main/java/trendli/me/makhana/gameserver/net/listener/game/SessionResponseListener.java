package trendli.me.makhana.gameserver.net.listener.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.net.msg.SessionResponse;
import trendli.me.makhana.common.net.msg.SessionVerificationRequest;
import trendli.me.makhana.gameserver.net.GameServerManager;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 */
public class SessionResponseListener implements MessageListener< HostedConnection >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SessionResponseListener.class.getName( ) );

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
	public SessionResponseListener( GameServerManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		if ( message instanceof SessionResponse )
		{
			logger.log( Level.INFO, "Received an AccountResponse from " + source.getAddress( ) );
			SessionResponse msg = ( SessionResponse ) message;
			if ( msg.getSession( ) == null )
			{
				logger.log( Level.INFO, "Account from " + source.getAddress( ) + " is null, dropping connection." );
				source.close( "Could not authenticate a null account" );
				return;
			}
			logger.log( Level.INFO, "Account from " + source.getAddress( ) + " has name: " + msg.getSession( ).getDisplayName( ) );

			// Add the session temporarily
			manager.getSessionManager( ).put( source, msg.getSession( ) );

			if ( manager.getApp( ).isOnlineMode( ) )
			{
				// Send off a verification request
				manager.getApp( ).getMasterConnManager( ).send( new SessionVerificationRequest( msg.getSession( ) ) );
			}
		}
	}
}
