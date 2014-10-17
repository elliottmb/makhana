package com.cogitareforma.makhana.masterserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cogitareforma.makhana.common.data.ServerStatus;
import com.cogitareforma.makhana.common.net.msg.ServerStatusRequest;
import com.cogitareforma.makhana.common.util.YamlConfig;
import com.cogitareforma.makhana.masterserver.db.AccountRepository;
import com.cogitareforma.makhana.masterserver.db.DatabaseConfig;
import com.cogitareforma.makhana.masterserver.net.MasterServerManager;
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
				if ( "exit".equals( line ) || "quit".equals( line ) || "stop".equals( line ) )
				{
					break;
				}
				if ( "status".equals( line ) )
				{
					List< HostedConnection > connections = masterServer.getMasterServerManager( ).getSessionManager( ).getConnections( );
					if ( connections.size( ) > 0 )
					{
						StringBuilder sb = new StringBuilder( );
						sb.append( connections.size( ) + " Authenticated sessions: \n" );
						for ( HostedConnection hc : connections )
						{
							sb.append( String.format( "%s: %s - %s \n", hc.getId( ), hc.getAddress( ), masterServer
									.getMasterServerManager( ).getSessionManager( ).get( hc ) ) );
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

					List< HostedConnection > connections = masterServer.getMasterServerManager( ).getServerStatusManager( )
							.getConnections( );
					if ( connections.size( ) > 0 )
					{
						for ( HostedConnection hc : connections )
						{
							ServerStatus ss = masterServer.getMasterServerManager( ).getServerStatusManager( ).get( hc );
							System.out.println( String.format( "%s - %d / %d - %s", ss.getServerName( ), ss.getCurrentPlayers( ),
									ss.getMaxPlayers( ), ss.getAddress( ) ) );
							hc.send( new ServerStatusRequest( ) );

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
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( MasterServer.class.getName( ) );

	private AnnotationConfigApplicationContext configContext;

	/**
	 * Last time that servers were polled for their statuses
	 */
	private Date lastUpdate;

	/**
	 * The Master Server's ServerManager
	 */
	private MasterServerManager serverManager;

	public AccountRepository getAccountRepository( )
	{
		if ( configContext != null )
		{
			return configContext.getBean( AccountRepository.class );
		}
		return null;
	}

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

		configContext = new AnnotationConfigApplicationContext( );
		configContext.register( DatabaseConfig.class );
		configContext.refresh( );

		DataSource dataSource = configContext.getBean( DataSource.class );

		try
		{
			System.out.println( dataSource.getConnection( ).toString( ) );
			AccountRepository accountRepository = configContext.getBean( AccountRepository.class );

			System.out.println( accountRepository.getAccount( "elliottb" ) );

		}
		catch ( SQLException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		YamlConfig config = YamlConfig.DEFAULT;
		Integer port = ( Integer ) config.get( "networkserver.port" );
		if ( port == null )
		{
			port = new Integer( 1337 ); /* default */
			config.put( "networkserver.port", port );
			config.save( );
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

	@Override
	public void stop( )
	{
		getMasterServerManager( ).close( );
		super.stop( );
	}

}
