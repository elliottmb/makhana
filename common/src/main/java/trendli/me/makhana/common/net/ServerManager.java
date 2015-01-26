package trendli.me.makhana.common.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.data.Session;

import com.jme3.app.Application;
import com.jme3.network.Message;
import com.jme3.network.Server;

/**
 * ServerManager is a class that handles the core jMonkeyEngine server component
 * and all connected sessions for a given Application.
 * 
 * @author Elliott Butler
 * 
 * @param <A>
 *            The owning Application
 */
public abstract class ServerManager< A extends Application >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ServerManager.class.getName( ) );

	/**
	 * The owning Application of this ServerManager
	 */
	private A app;

	/**
	 * The jMonkeyEngine server to be managed
	 */
	private Server server;

	/**
	 * The SessionManager to associate with this MasterServer.
	 */
	private DataManager< Session > sessionManager;

	/**
	 * Creates an instance of the ServerManager with the supplied owning
	 * Application
	 * 
	 * @param app
	 *            the owning application of this manager
	 */
	public ServerManager( A app )
	{
		this.app = app;
		sessionManager = new DataManager< Session >( );
	}

	/**
	 * Closes the server connection
	 */
	public void close( )
	{
		if ( isRunning( ) )
		{
			getServer( ).close( );
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
	 * Returns the jMonkeyEngine Server that is managed by this connection
	 * manager
	 * 
	 * @return the jMonkeyEngine Server that is managed by this connection
	 *         manager
	 */
	public Server getServer( )
	{
		return server;
	}

	/**
	 * Returns the SessionManager associated with this server.
	 * 
	 * @return the SessionManager associated with this server
	 */
	public DataManager< Session > getSessionManager( )
	{
		return sessionManager;
	}

	/**
	 * Returns true if the manager's server is running
	 * 
	 * @return true if the manager's server is running
	 */
	public boolean isRunning( )
	{
		if ( getServer( ) == null )
		{
			return false;
		}
		return getServer( ).isRunning( );
	}

	/**
	 * Starts the ServerManager's Server instance on the given port. If the
	 * Server is already running, the implementation is free to close it and
	 * start it with the given port. Returns true if the server was successfully
	 * started, otherwise false.
	 * 
	 * @param port
	 *            port to bind the server on
	 * @return true if the server was successfully started, otherwise false.
	 */
	public abstract boolean run( final Integer port );

	/**
	 * Sends a given Message to the connected Clients.
	 * 
	 * @param message
	 *            the given Message
	 */
	public void send( Message message )
	{
		logger.log( Level.FINER, "Sending a " + message.getClass( ).getSimpleName( ) + " message to all connected clients." );
		getServer( ).broadcast( message );
	}

	/**
	 * Sets the jMonkeyEngine server that is to be managed by this server
	 * manager
	 * 
	 * @param server
	 *            the jMonkeyEngine server that is to be managed by this server
	 *            manager
	 */
	public void setServer( Server server )
	{
		this.server = server;
	}

	/**
	 * Function that contains any update logic that should be called by the main
	 * application's update loop. This function may be left unchanged if no
	 * logic changes need to occur every frame.
	 * 
	 * @param tpf
	 *            time per frame
	 */
	public void update( float tpf )
	{

	}

}
