package com.cogitareforma.hexrepublics.masterserver.net.listener;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.net.SessionManager;
import com.cogitareforma.hexrepublics.common.net.msg.NetworkChatMessage;
import com.cogitareforma.hexrepublics.masterserver.net.MasterServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 * 
 */
public class NetworkChatListener implements MessageListener< HostedConnection >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( NetworkChatListener.class.getName( ) );

	private final static int chatInterval = 500;

	/**
	 * 
	 */
	private MasterServerManager server;

	/**
	 * Previous message history, used for spam blocking
	 */
	private Map< String, Pair< Date, Boolean > > chatTime;

	public NetworkChatListener( MasterServerManager server )
	{
		this.server = server;
		this.chatTime = new LinkedHashMap< String, Pair< Date, Boolean > >( );
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{

		if ( message instanceof NetworkChatMessage )
		{
			NetworkChatMessage msg = ( NetworkChatMessage ) message;

			Account loggedInUser = server.getSessionManager( ).getAccountFromSession( source );
			if ( loggedInUser == null || !loggedInUser.equals( msg.getAccount( ) ) || loggedInUser.isServer( ) )
			{
				/*
				 * Account is either null, is not the same as the logged in
				 * account, or is a game server
				 */
				return;
			}

			// Basic spam prevention.
			Pair< Date, Boolean > lastMessage = chatTime.get( loggedInUser.getAccountName( ) );
			Date timeNow = new Date( );
			if ( lastMessage == null || lastMessage.getLeft( ).getTime( ) <= timeNow.getTime( ) - chatInterval )
			{
				logger.log( Level.INFO, "Chat -> " + msg.getAccount( ).getAccountName( ) + ": " + msg.getMessage( ) );
				chatTime.put( loggedInUser.getAccountName( ), Pair.of( timeNow, false ) );
				broadcastMessage( msg );
			}
			else
			{
				if ( lastMessage.getRight( ) == false )
				{
					logger.log( Level.INFO, "Chat -> " + msg.getAccount( ).getAccountName( ) + " is spamming the chat." );
					sendNotice( source, String.format( "Server Warning: You are spamming the chat, please wait %.1f seconds.",
							( chatInterval ) / 1000.0 ) );
					chatTime.put( loggedInUser.getAccountName( ), Pair.of( timeNow, true ) );
				}
			}
		}
	}

	/**
	 * Sends a NetworkChatMessage to all authenticated clients except for the
	 * game servers. If
	 * 
	 * @param msg
	 *            the NetworkChatMessage to send
	 */
	private void broadcastMessage( NetworkChatMessage msg )
	{
		SessionManager sm = server.getSessionManager( );
		for ( HostedConnection conn : sm.getAllAuthedConnections( ) )
		{
			Account act = sm.getAccountFromSession( conn );
			/* If the account is null or is that of a game server, continue */
			if ( act == null || act.isServer( ) )
			{
				continue;
			}
			conn.send( msg );
		}
	}

	/**
	 * Sends a notice to the specified connection.
	 * 
	 * @param conn
	 * @param notice
	 */
	private void sendNotice( HostedConnection conn, String notice )
	{
		conn.send( new NetworkChatMessage( null, notice ) );
	}

}
