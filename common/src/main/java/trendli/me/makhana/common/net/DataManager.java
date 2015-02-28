package trendli.me.makhana.common.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.HostedConnection;

/**
 * DataManager contains a hash map of client connections and the user data
 * objects that they have been associated with. Data is stored in memory to
 * lower database overhead.
 * 
 * @author Elliott Butler
 */
public class DataManager< T >
{

    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( DataManager.class.getName( ) );

    /**
     * Maps a client connection to T.
     */
    private HashMap< HostedConnection, T > db;

    /**
     * Default constructor initializes the hash map and associates to a server.
     * 
     */
    public DataManager( )
    {
        db = new HashMap<>( );
    }

    /**
     * Checks if the supplied object is currently in the database
     * 
     * @param t
     *            the object to be checked
     * @return true if the object is currently in the database
     */
    public boolean contains( T t )
    {
        logger.log( Level.FINE, "Checking if object is in the session database." );
        return db.containsValue( t );
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
     * Gets an associated Session from the client's connection.
     * 
     * @param conn
     *            the client connection to get the UserAccount for
     * @return the Session associated with the client connection
     */
    public T get( HostedConnection conn )
    {
        logger.log( Level.FINE, "Retrieving user session from session database." );
        if ( db.containsKey( conn ) )
        {
            return db.get( conn );
        }
        return null;
    }

    /**
     * Returns all currently active objects.
     * 
     * @return all currently active objects
     */
    public ArrayList< T > getAll( )
    {
        logger.log( Level.FINE, "Retrieving all currently active sessions." );
        ArrayList< T > list = new ArrayList<>( );
        Iterator< T > it = db.values( ).iterator( );
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
    public ArrayList< HostedConnection > getConnections( )
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
     * Adds a session to the hash map given a client connection and a session.
     * 
     * @param conn
     *            the client connection that has successfully authenticated
     * @param session
     *            the session associated with the client
     */
    public void put( HostedConnection conn, T session )
    {
        logger.log( Level.FINE, "Adding user session to session database." );
        db.put( conn, session );
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
        logger.log( Level.FINE, "Removing user session from session database." );
        db.remove( conn );
    }

    /**
     * Removes all sessions associated with the specified user account from the
     * hash map.
     * 
     * @param session
     *            the session to remove the connections for
     */
    public void removeConnections( T session )
    {
        logger.log( Level.FINE, "Removing all user sessions associated with an account from session database." );
        Iterator< HostedConnection > it = db.keySet( ).iterator( );
        while ( it.hasNext( ) )
        {
            HostedConnection conn = it.next( );
            if ( db.get( conn ).equals( session ) )
            {
                db.remove( conn );
            }
        }
    }

}
