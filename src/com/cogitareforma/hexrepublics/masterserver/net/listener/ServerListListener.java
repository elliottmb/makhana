package com.cogitareforma.hexrepublics.masterserver.net.listener;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.data.ServerStatus;
import com.cogitareforma.hexrepublics.common.net.msg.ServerListRequest;
import com.cogitareforma.hexrepublics.common.net.msg.ServerListResponse;
import com.cogitareforma.hexrepublics.masterserver.net.MasterServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ServerListListener implements MessageListener< HostedConnection >
{
	private final static Logger logger = Logger.getLogger( UserListListener.class.getName( ) );
	private MasterServerManager server;

	public ServerListListener( MasterServerManager server )
	{
		this.server = server;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{

		if ( message instanceof ServerListRequest )
		{
			logger.log( Level.INFO, "Recieved a ServerListRequest from " + source.getAddress( ) );

			Account loggedInUser = server.getSessionManager( ).getAccountFromSession( source );
			if ( loggedInUser == null || loggedInUser.isServer( ) )
			{
				/*
				 * Account is either null, or is a game server
				 */
				return;
			}

			broadcastServerList( source );
		}
	}

	public void broadcastServerList( HostedConnection conn )
	{
		logger.log( Level.INFO, "Sending updated ServerList to requesting client " + conn.getAddress( ) );

		ArrayList< ServerStatus > servers = server.getServerStatusManager( ).getAllServerStatuses( );
		conn.send( new ServerListResponse( servers ) );

	}
}
