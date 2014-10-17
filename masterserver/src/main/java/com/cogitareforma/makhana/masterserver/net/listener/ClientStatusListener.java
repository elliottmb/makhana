package com.cogitareforma.makhana.masterserver.net.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Session;
import com.cogitareforma.makhana.common.net.DataManager;
import com.cogitareforma.makhana.common.net.msg.ChatMessage;
import com.cogitareforma.makhana.common.net.msg.ClientStatusMessage;
import com.cogitareforma.makhana.masterserver.net.MasterServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class ClientStatusListener implements MessageListener< HostedConnection >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ClientStatusListener.class.getName( ) );

	/**
	 * The master server manager for the SessionListener.
	 */
	private MasterServerManager serverManager;

	/**
	 * Constructor for the SessionListener. Sets its master server manager.
	 * 
	 * @param serverManager
	 *            the master server manager to set.
	 */
	public ClientStatusListener( MasterServerManager serverManager )
	{
		this.serverManager = serverManager;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		DataManager< Session > sm = serverManager.getSessionManager( );
		if ( message instanceof ClientStatusMessage )
		{
			Session session = sm.get( source );
			if ( session != null )
			{
				logger.log( Level.INFO, "Received a ClientStatus message." );
				ClientStatusMessage clientStatus = ( ClientStatusMessage ) message;

				if ( clientStatus.isInGame( ) != session.isInGame( ) )
				{
					session.setInGame( clientStatus.isInGame( ) );

					for ( HostedConnection hc : sm.getConnections( ) )
					{
						Session sess = sm.get( hc );
						if ( !sess.isInGame( ) )
						{
							hc.send( new ChatMessage( null, String.format( "Server Notice: %s has %s.", session.getDisplayName( ),
									( session.isInGame( ) ? "joined a server" : "left a server" ) ) ) );
						}
					}
					serverManager.broadcastUserList( );
				}
			}
			else
			{
				logger.log( Level.WARNING, "Recieved a ClientStatus message from an unauthenticated session: " + source.getAddress( ) );
			}
		}
	}

}
