package trendli.me.makhana.masterserver.net;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.data.ServerStatus;
import trendli.me.makhana.common.data.Session;
import trendli.me.makhana.common.net.DataManager;
import trendli.me.makhana.common.net.SerializerRegistrar;
import trendli.me.makhana.common.net.ServerManager;
import trendli.me.makhana.common.net.msg.ServerStatusRequest;
import trendli.me.makhana.common.net.msg.UserListResponse;
import trendli.me.makhana.common.util.PackageUtils;
import trendli.me.makhana.masterserver.MasterServer;

import com.jme3.network.HostedConnection;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;

public class MasterServerManager extends ServerManager< MasterServer >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( MasterServerManager.class.getName( ) );

	private KeyPair keyPair;

	/**
	 * This MasterServerManager's server status manager
	 */
	private DataManager< ServerStatus > serverStatusManager;

	/**
	 * Creates an instance of the MasterServerManager with the supplied owning
	 * Application
	 * 
	 * @param app
	 *            the owning application of this manager
	 */
	public MasterServerManager( MasterServer app )
	{
		super( app );
		this.serverStatusManager = new DataManager< ServerStatus >( );
		try
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance( "RSA" );
			kpg.initialize( 1024 );

			this.keyPair = kpg.genKeyPair( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Error creating keypair " + e.getMessage( ) );
		}
	}

	/**
	 * Broadcasts the current client list to the clients
	 */
	public void broadcastUserList( )
	{
		logger.log( Level.INFO, "Sending updated UserList to all active Game Clients" );

		List< HostedConnection > connections = getSessionManager( ).getConnections( );
		if ( connections.size( ) > 0 )
		{
			ArrayList< String > users = new ArrayList< String >( );
			for ( HostedConnection hc : connections )
			{
				Session act = getSessionManager( ).get( hc );
				if ( !act.isInGame( ) )
				{
					users.add( act.getDisplayName( ) );
				}
			}
			for ( HostedConnection hc : connections )
			{
				Session act = getSessionManager( ).get( hc );
				if ( !act.isInGame( ) )
				{
					hc.send( new UserListResponse( users ) );
				}
			}
		}
	}

	public KeyPair getKeyPair( )
	{
		return keyPair;
	}

	/**
	 * Returns the ServerStatusManager associated with this server.
	 * 
	 * @return the ServerStatusManager associated with this server
	 */
	public DataManager< ServerStatus > getServerStatusManager( )
	{
		return serverStatusManager;
	}

	/**
	 * Sends a request to all current game servers for their status
	 */
	public void requestCurrentServerStatuses( )
	{
		for ( HostedConnection hc : getServerStatusManager( ).getConnections( ) )
		{
			hc.send( new ServerStatusRequest( ) );
		}
	}

	@SuppressWarnings(
	{
			"unchecked", "rawtypes"
	} )
	@Override
	public boolean run( final Integer port )
	{
		// Close existing connection
		if ( isRunning( ) )
		{
			close( );
		}

		try
		{

			setServer( Network.createServer( "makhana", 2, port, port + 1 ) );
			logger.log( Level.INFO, String.format( "We will bind to TCP: %d, UDP: %d", port, port + 1 ) );

			/* register all message types with the serializer */
			logger.log( Level.FINE, "Registering all serializable classes." );
			SerializerRegistrar.registerAllSerializable( );

			/* add listeners */
			logger.log( Level.FINE, "Registering ConnListener with server." );
			getServer( ).addConnectionListener( new ConnListener( this ) );

			logger.log( Level.FINE, "Registering message listeners with server." );
			List< Object > messageListeners = PackageUtils.createAllInPackage( "trendli.me.makhana.masterserver.net.listener", this );
			for ( Object messageListener : messageListeners )
			{
				getServer( ).addMessageListener( ( MessageListener ) messageListener );
			}

			logger.log( Level.FINE, "Starting the server." );
			getServer( ).start( );
			return true;
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Couldn't bind to port " + port, e );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Encountered unexpected error!", e );
		}

		return false;
	}
}
