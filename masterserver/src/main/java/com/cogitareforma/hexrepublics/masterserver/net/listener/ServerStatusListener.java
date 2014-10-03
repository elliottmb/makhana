package com.cogitareforma.hexrepublics.masterserver.net.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.masterserver.net.MasterServerManager;
import com.cogitareforma.makhana.common.net.msg.ServerStatusResponse;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class ServerStatusListener implements MessageListener< HostedConnection >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ServerStatusListener.class.getName( ) );

	/**
	 * 
	 */
	private MasterServerManager server;

	public ServerStatusListener( MasterServerManager server )
	{
		this.server = server;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		if ( message instanceof ServerStatusResponse )
		{
			ServerStatusResponse msg = ( ServerStatusResponse ) message;

			if ( server.getSessionManager( ).containsConnection( source ) )
			{
				logger.log( Level.INFO, "Recieved a Server State message: " + msg.getServerStatus( ).toString( ) );
				msg.getServerStatus( ).setAddress( source.getAddress( ).substring( 1 ).split( "\\:" )[ 0 ] );
				server.getServerStatusManager( ).put( source, msg.getServerStatus( ) );
			}
			else
			{
				logger.log( Level.WARNING, "Recieved a Server State message from an unauthenticated session: " + source.getAddress( ) );
			}
		}
	}
}
