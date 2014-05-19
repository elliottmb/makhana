package com.cogitareforma.hexrepublics.common.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.jme3.network.HostedConnection;

/**
 * SessionManager contains a hash map of client connections that have
 * successfully authenticated and the user accounts that they have authenticated
 * to. Sessions are stored in memory to lower database overhead.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
public class SessionManager
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SessionManager.class.getName( ) );

	/**
	 * Maps a client connection to a UserAccount.
	 */
	private HashMap< HostedConnection, Account > db;

	/**
	 * Default constructor initializes the hash bi-map and associates to a
	 * server.
	 * 
	 */
	public SessionManager( )
	{
		db = new HashMap< HostedConnection, Account >( );
	}

	/**
	 * Checks if the supplied Account is currently in the database
	 * 
	 * @param account
	 *            the account to be checked
	 * @return true if the account is currently in the database
	 */
	public boolean containsAccount( Account account )
	{
		logger.log( Level.FINE, "Checking if account is in the session database." );
		return db.containsValue( account );
	}

	/**
	 * Checks if the supplied HostedConnection is currently in the database
	 * 
	 * @param conn
	 *            the HostedConnection to be checked
	 * @return true if the conn is currently in the database
	 */
	public boolean containsConnection( HostedConnection conn )
	{
		logger.log( Level.FINE, "Checking if connection is in the session database." );
		return db.containsKey( conn );
	}

	/**
	 * Returns all currently active accounts.
	 * 
	 * @return all currently active accounts
	 */
	public List< Account > getAllActiveAccounts( )
	{
		logger.log( Level.FINE, "Retrieving all currently active accounts." );
		ArrayList< Account > list = new ArrayList<>( );
		Iterator< Account > it = db.values( ).iterator( );
		while ( it.hasNext( ) )
		{
			list.add( it.next( ) );
		}
		return list;
	}

	/**
	 * Returns all currently authenticated sessions.
	 * 
	 * @return all currently authenticated sessions
	 */
	public List< HostedConnection > getAllSessions( )
	{
		logger.log( Level.FINE, "Retrieving all currently active connections." );
		ArrayList< HostedConnection > list = new ArrayList<>( );
		Iterator< HostedConnection > it = db.keySet( ).iterator( );
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
	public Account getFromSession( HostedConnection conn )
	{
		logger.log( Level.FINE, "Retrieving user session from session database." );
		if ( db.containsKey( conn ) )
		{
			return db.get( conn );
		}
		return null;
	}

	/**
	 * Adds a session to the hash map given a client connection and a user
	 * account.
	 * 
	 * @param conn
	 *            the client connection that has successfully authenticated
	 * @param account
	 *            the account associated with the client
	 */
	public void put( HostedConnection conn, Account account )
	{
		logger.log( Level.FINE, "Adding user session to session database." );
		db.put( conn, account );
	}

	/**
	 * Removes the session associated with the specified connection from the
	 * hash bi-map.
	 * 
	 * @param conn
	 *            the connection to remove the session for
	 */
	public void removeSession( HostedConnection conn )
	{
		logger.log( Level.FINE, "Removing user session from session database." );
		db.remove( conn );
	}

	/**
	 * Removes all sessions associated with the specified user account from the
	 * hash bi-map.
	 * 
	 * @param account
	 *            the account to remove the sessions for
	 */
	public void removeSessions( Account account )
	{
		logger.log( Level.FINE, "Removing all user sessions associated with an account from session database." );
		Iterator< HostedConnection > it = db.keySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			HostedConnection conn = it.next( );
			if ( db.get( conn ).equals( account ) )
			{
				db.remove( conn );
			}
		}
	}

}
