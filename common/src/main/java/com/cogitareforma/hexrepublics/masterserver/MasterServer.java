package com.cogitareforma.hexrepublics.masterserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.cogitareforma.hexrepublics.common.data.ServerStatus;
import com.cogitareforma.hexrepublics.common.net.msg.ServerStatusRequest;
import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.cogitareforma.hexrepublics.masterserver.net.MasterServerManager;
import com.jme3.app.SimpleApplication;
import com.jme3.network.HostedConnection;
import com.jme3.system.JmeContext;

/**
 * MasterServer contains the main method for a server that talks with
 * NetworkClients and validates/handles their authentication and broadcasts
 * their chat messages.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
public class MasterServer extends SimpleApplication
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( MasterServer.class.getName( ) );

	public static void main( String[ ] args )
	{
		MasterServer masterServer = new MasterServer( );
		masterServer.start( JmeContext.Type.Headless );
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
		String line = null;
		try
		{
			while ( ( line = in.readLine( ) ) != null )
			{
				if ( "exit".equals( line ) )
				{
					break;
				}
				if ( "status".equals( line ) )
				{
					List< HostedConnection > connections = masterServer.getMasterServerManager( ).getSessionManager( )
							.getAllAuthedConnections( );
					if ( connections.size( ) > 0 )
					{
						StringBuilder sb = new StringBuilder( );
						sb.append( connections.size( ) + " Authenticated sessions: \n" );
						for ( HostedConnection hc : connections )
						{
							sb.append( String.format( "%s: %s - %s \n", hc.getId( ), hc.getAddress( ), masterServer
									.getMasterServerManager( ).getSessionManager( ).getAccountFromSession( hc ) ) );
						}
						logger.log( Level.INFO, sb.toString( ) );
					}
					else
					{
						logger.log( Level.INFO, "No authenticated sessions." );
					}
				}
				if ( "srvstatus".equals( line ) )
				{
					List< HostedConnection > connections = masterServer.getMasterServerManager( ).getSessionManager( )
							.getAllAuthedConnections( );
					if ( connections.size( ) > 0 )
					{
						for ( HostedConnection hc : connections )
						{
							Account act = masterServer.getMasterServerManager( ).getSessionManager( ).getAccountFromSession( hc );
							if ( act.isServer( ) )
							{
								hc.send( new ServerStatusRequest( ) );
							}
						}
						for ( ServerStatus ss : masterServer.getMasterServerManager( ).getServerStatusManager( ).getAllServerStatuses( ) )
						{
							System.out.println( String.format( "%s - %d / %d - %s", ss.getServerName( ), ss.getCurrentPlayers( ),
									ss.getMaxPlayers( ), ss.getAddress( ) ) );
						}
					}
					else
					{
						logger.log( Level.INFO, "No authenticated sessions." );
					}
				}
			}
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Error handling console input", e );
		}
		masterServer.stop( );
	}

	/**
	 * Last time that servers were polled for their statuses
	 */
	private Date lastUpdate;

	/**
	 * The Master Server's ServerManager
	 */
	private MasterServerManager serverManager;

	/**
	 * Returns the ServerManager associated with this server.
	 * 
	 * @return the ServerManager associated with this server
	 */
	public MasterServerManager getMasterServerManager( )
	{
		return serverManager;
	}

	@Override
	public void simpleInitApp( )
	{
		serverManager = new MasterServerManager( this );

		Integer port = ( Integer ) YamlConfig.DEFAULT.get( "networkserver.port" );
		if ( port == null )
		{
			port = new Integer( 1337 ); /* default */
			YamlConfig.DEFAULT.put( "networkserver.port", port );
			YamlConfig.DEFAULT.save( );
		}

		serverManager.run( port );
		lastUpdate = new Date( );
	}

	@Override
	public void simpleUpdate( float tpf )
	{
		super.simpleUpdate( tpf );

		Date currentTime = new Date( );
		if ( lastUpdate.getTime( ) <= currentTime.getTime( ) - 300000 )
		{
			lastUpdate = currentTime;
			logger.log( Level.INFO, "Pinging all active servers for their current server status" );
			serverManager.requestCurrentServerStatuses( );
		}

	}

}
