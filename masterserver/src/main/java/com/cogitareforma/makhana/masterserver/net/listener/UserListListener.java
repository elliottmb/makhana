package com.cogitareforma.makhana.masterserver.net.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Account;
import com.cogitareforma.makhana.common.net.msg.UserListRequest;
import com.cogitareforma.makhana.masterserver.net.MasterServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Ryan Grier
 * 
 */
public class UserListListener implements MessageListener< HostedConnection >
{

	private final static Logger logger = Logger.getLogger( UserListListener.class.getName( ) );
	private MasterServerManager server;

	public UserListListener( MasterServerManager server )
	{
		this.server = server;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		Account loggedInUser = server.getSessionManager( ).getFromSession( source );
		if ( message instanceof UserListRequest )
		{
			if ( loggedInUser == null || loggedInUser.isServer( ) )
			{
				/*
				 * User is either null, is not the same as the logged in user,
				 * or is a game server
				 */
				return;
			}
			logger.log( Level.INFO, "Recieved a UserListRequest from " + source.getAddress( ) );
			server.broadcastUserList( );
		}
	}

}
