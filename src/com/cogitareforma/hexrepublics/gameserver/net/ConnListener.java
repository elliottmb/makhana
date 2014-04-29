package com.cogitareforma.hexrepublics.gameserver.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.net.msg.AccountRequest;
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
	 * The server manager
	 */
	private GameServerManager manager;

	public ConnListener( GameServerManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void connectionAdded( Server server, HostedConnection conn )
	{
		logger.log( Level.INFO, "Adding the sessions for connection=" + conn.toString( ) );
		if ( manager.getServerStatus( ).getCurrentPlayers( ) >= manager.getServerStatus( ).getMaxPlayers( ) )
		{
			conn.close( "Server is full" );
		}
		else
		{
			conn.send( new AccountRequest( ) );
		}
	}

	@Override
	public void connectionRemoved( Server server, HostedConnection conn )
	{
		logger.log( Level.INFO, "Removing the sessions for connection=" + conn.toString( ) );
		// TODO: Handle disconnects better
		/*
		 * if ( manager.getSessionManager( ).getAccountFromSession( conn ) !=
		 * null ) { manager.removePlayerEntity( manager.getSessionManager(
		 * ).getAccountFromSession( conn ) ); }
		 */
		if ( manager.getSessionManager( ).containsConnection( conn ) )
		{
			manager.getSessionManager( ).removeSession( conn );
		}
	}

}
