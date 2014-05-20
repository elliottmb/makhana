package com.cogitareforma.hexrepublics.masterserver.net.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.net.SessionManager;
import com.cogitareforma.hexrepublics.common.net.msg.ClientStatusMessage;
import com.cogitareforma.hexrepublics.common.net.msg.NetworkChatMessage;
import com.cogitareforma.hexrepublics.masterserver.net.MasterServerManager;
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
		SessionManager sm = serverManager.getSessionManager( );
		if ( message instanceof ClientStatusMessage )
		{
			Account account = sm.getFromSession( source );
			if ( account != null )
			{
				logger.log( Level.INFO, "Received a ClientStatus message." );
				ClientStatusMessage clientStatus = ( ClientStatusMessage ) message;

				if ( clientStatus.isInGame( ) != account.isInGame( ) )
				{
					account.setInGame( clientStatus.isInGame( ) );

					for ( HostedConnection hc : sm.getAllSessions( ) )
					{
						Account act = sm.getFromSession( hc );
						if ( !act.isServer( ) && !act.isInGame( ) )
						{
							hc.send( new NetworkChatMessage( null, String.format( "Server Notice: %s has %s.", account.getAccountName( ),
									( account.isInGame( ) ? "joined a server" : "left a server" ) ) ) );
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
