package com.cogitareforma.makhana.common.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.Application;
import com.jme3.network.Client;
import com.jme3.network.Message;

/**
 * 
 * @author Elliott Butler
 *
 * @param <A>
 */
public class ConnectionManager< A extends Application >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ConnectionManager.class.getName( ) );

	/**
	 * The owning Application of this ConnectionManager
	 */
	private A app;

	/**
	 * The jMonkeyEngine client to be managed
	 */
	private Client client;

	/**
	 * Creates an instance of the ConnectionManager with the supplied owning
	 * Application
	 * 
	 * @param app
	 *            the owning application of this manager
	 */
	protected ConnectionManager( A app )
	{
		this.app = app;
	}

	/**
	 * Closes the client connection
	 */
	public void close( )
	{
		if ( isConnected( ) )
		{
			getClient( ).close( );
		}
	}

	/**
	 * Returns the owning application of this manager
	 * 
	 * @return the owning application of this manager
	 */
	public A getApp( )
	{
		return app;
	}

	/**
	 * Returns the jMonkeyEngine client that is managed by this connection
	 * manager
	 * 
	 * @return the jMonkeyEngine client that is managed by this connection
	 *         manager
	 */
	public Client getClient( )
	{
		return client;
	}

	/**
	 * Returns true if the manager's client is connected
	 * 
	 * @return true if the manager's client is connected
	 */
	public boolean isConnected( )
	{
		if ( getClient( ) == null )
		{
			return false;
		}
		return getClient( ).isConnected( );
	}

	/**
	 * Sends a Message to the Master Server
	 * 
	 */
	public void send( Message message )
	{
		logger.log( Level.FINER, "Sending a " + message.getClass( ).getSimpleName( ) + " message the connection." );
		if ( getClient( ) != null )
		{
			getClient( ).send( message );
		}
	}

	/**
	 * Sets the jMonkeyEngine client that is to be managed by this connection
	 * manager
	 * 
	 * @param client
	 *            the jMonkeyEngine client that is to be managed by this
	 *            connection manager
	 */
	public void setClient( Client client )
	{
		this.client = client;
	}

}
