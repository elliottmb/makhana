package com.cogitareforma.makhana.masterserver.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.ServerStatus;
import com.jme3.network.HostedConnection;

/**
 * 
 * @author Elliott Butler
 */
public class ServerStatusManager
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ServerStatusManager.class.getName( ) );

	/**
	 * Maps a client connection to a ServerStatus.
	 */
	private HashMap< HostedConnection, ServerStatus > db;

	/**
	 * Default constructor initializes the hash bi-map and associates to a
	 * server.
	 * 
	 */
	protected ServerStatusManager( )
	{
		db = new HashMap< HostedConnection, ServerStatus >( );
	}

	/**
	 * Returns all current game server connections.
	 * 
	 * @return all current game server connections
	 */
	public List< HostedConnection > getAllGameServerConnections( )
	{
		logger.log( Level.FINE, "Retrieving all currently active game server connections." );
		ArrayList< HostedConnection > list = new ArrayList< HostedConnection >( );
		Iterator< HostedConnection > it = db.keySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			list.add( it.next( ) );
		}
		return list;
	}

	/**
	 * Returns all current server statuses.
	 * 
	 * @return all current server statuses
	 */
	public ArrayList< ServerStatus > getAllServerStatuses( )
	{
		logger.log( Level.FINE, "Retrieving all currently active server statuses." );
		ArrayList< ServerStatus > list = new ArrayList< ServerStatus >( );
		Iterator< ServerStatus > it = db.values( ).iterator( );
		while ( it.hasNext( ) )
		{
			list.add( it.next( ) );
		}
		return list;
	}

	/**
	 * Gets an associated UserAccount from the client's connection.
	 * 
	 * @param conn
	 *            the client connection to get the UserAccount for
	 * @return the UserAccount associated with the client connection
	 */
	public ServerStatus getFromConnection( HostedConnection conn )
	{
		logger.log( Level.FINE, "Retrieving user session from database." );
		if ( db.containsKey( conn ) )
		{
			return db.get( conn );
		}
		return null;
	}

	/**
	 * Adds a session to the hashmap given a connection and a user serverStatus.
	 * 
	 * @param conn
	 *            the client connection that has successfully authenticated
	 * @param serverStatus
	 *            the serverStatus associated with the connection
	 */
	public void put( HostedConnection conn, ServerStatus serverStatus )
	{
		logger.log( Level.FINE, "Adding user session to database." );
		db.put( conn, serverStatus );
	}

	/**
	 * Removes the session associated with the specified connection from the
	 * hash bi-map.
	 * 
	 * @param conn
	 *            the connection to remove the session for
	 */
	public void remove( HostedConnection conn )
	{
		logger.log( Level.FINE, "Removing user session from database." );
		db.remove( conn );
	}

	/**
	 * Removes all sessions associated with the specified ServerStatus from the
	 * hashmap.
	 * 
	 * @param serverStatus
	 *            the ServerStatus to remove the sessions for
	 */
	public void removeByServerStatus( ServerStatus serverStatus )
	{
		logger.log( Level.FINE, "Removing all user sessions associated with an account from database." );
		Iterator< HostedConnection > it = db.keySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			HostedConnection conn = it.next( );
			if ( db.get( conn ).equals( serverStatus ) )
			{
				db.remove( conn );
			}
		}
	}

}
