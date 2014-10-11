package com.cogitareforma.makhana.masterserver.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Account;
import com.cogitareforma.makhana.common.net.SerializerRegistrar;
import com.cogitareforma.makhana.common.net.ServerManager;
import com.cogitareforma.makhana.common.net.msg.ServerStatusRequest;
import com.cogitareforma.makhana.common.net.msg.UserListResponse;
import com.cogitareforma.makhana.common.util.PackageUtils;
import com.cogitareforma.makhana.masterserver.MasterServer;
import com.jme3.network.HostedConnection;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;

public class MasterServerManager extends ServerManager< MasterServer >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( MasterServerManager.class.getName( ) );

	/**
	 * This MasterServerManager's server status manager
	 */
	private ServerStatusManager serverStatusManager;

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
		serverStatusManager = new ServerStatusManager( );
	}

	/**
	 * Broadcasts the current client list to the clients
	 */
	public void broadcastUserList( )
	{
		logger.log( Level.INFO, "Sending updated UserList to all active Game Clients" );

		List< HostedConnection > connections = getSessionManager( ).getAllSessions( );
		if ( connections.size( ) > 0 )
		{
			ArrayList< String > users = new ArrayList< String >( );
			for ( HostedConnection hc : connections )
			{
				Account act = getSessionManager( ).getFromSession( hc );
				if ( !act.isServer( ) && !act.isInGame( ) )
				{
					users.add( act.getAccountName( ) );
				}
			}
			for ( HostedConnection hc : connections )
			{
				Account act = getSessionManager( ).getFromSession( hc );
				if ( !act.isServer( ) && !act.isInGame( ) )
				{
					hc.send( new UserListResponse( users ) );
				}
			}
		}
	}

	/**
	 * Returns the ServerStatusManager associated with this server.
	 * 
	 * @return the ServerStatusManager associated with this server
	 */
	public ServerStatusManager getServerStatusManager( )
	{
		return serverStatusManager;
	}

	/**
	 * Sends a request to all current game servers for their status
	 */
	public void requestCurrentServerStatuses( )
	{
		for ( HostedConnection hc : getServerStatusManager( ).getAllGameServerConnections( ) )
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

			setServer( Network.createServer( "Hex Republics", 2, port, port + 1 ) );
			logger.log( Level.INFO, String.format( "We will bind to TCP: %d, UDP: %d", port, port + 1 ) );

			/* register all message types with the serializer */
			logger.log( Level.FINE, "Registering all serializable classes." );
			SerializerRegistrar.registerAllSerializable( );

			/* add listeners */
			logger.log( Level.FINE, "Registering ConnListener with server." );
			getServer( ).addConnectionListener( new ConnListener( this ) );

			logger.log( Level.FINE, "Registering message listeners with server." );
			List< Object > messageListeners = PackageUtils.createAllInPackage( "com.cogitareforma.makhana.masterserver.net.listener",
					this );
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
