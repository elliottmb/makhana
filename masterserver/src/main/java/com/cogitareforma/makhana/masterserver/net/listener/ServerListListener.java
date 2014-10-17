package com.cogitareforma.makhana.masterserver.net.listener;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.ServerStatus;
import com.cogitareforma.makhana.common.data.Session;
import com.cogitareforma.makhana.common.net.msg.ServerListRequest;
import com.cogitareforma.makhana.common.net.msg.ServerListResponse;
import com.cogitareforma.makhana.masterserver.net.MasterServerManager;
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

	public void broadcastServerList( HostedConnection conn )
	{
		logger.log( Level.INFO, "Sending updated ServerList to requesting client " + conn.getAddress( ) );

		ArrayList< ServerStatus > servers = server.getServerStatusManager( ).getAll( );
		conn.send( new ServerListResponse( servers ) );

	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{

		if ( message instanceof ServerListRequest )
		{
			logger.log( Level.INFO, "Recieved a ServerListRequest from " + source.getAddress( ) );

			Session loggedInUser = server.getSessionManager( ).get( source );
			if ( loggedInUser == null )
			{
				/*
				 * Account is either null, or is a game server
				 */
				return;
			}

			broadcastServerList( source );
		}
	}
}
