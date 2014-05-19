package com.cogitareforma.hexrepublics.gameserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.cogitareforma.hexrepublics.gameserver.net.GameMasterConnManager;
import com.cogitareforma.hexrepublics.gameserver.net.GameServerManager;
import com.jme3.app.SimpleApplication;
import com.jme3.network.HostedConnection;
import com.jme3.system.JmeContext;

/**
 * The GameServer is the core jMonkeyEngine application that represents a Game
 * Server. The actual server behavior is handled by Managers that are controlled
 * by this application.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 * 
 */
public class GameServer extends SimpleApplication
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( GameServer.class.getName( ) );

	public static void main( String[ ] args )
	{
		GameServer gameServer = new GameServer( );

		Options options = new Options( );
		options.addOption( "A", "account", true, "the account name registered with the master server" );
		options.addOption( "P", "password", true, "the password registered with the master server" );
		options.addOption( "p", "port", true, "the port this server will bind to (optional)" );

		CommandLineParser parser = new BasicParser( );

		try
		{
			gameServer.setArguments( parser.parse( options, args ) );

			if ( !gameServer.getArguments( ).hasOption( "A" ) )
			{
				logger.log( Level.SEVERE, "Expected account name argument, no argument found." );
				System.exit( 1 );
			}

			if ( !gameServer.getArguments( ).hasOption( "P" ) )
			{
				logger.log( Level.SEVERE, "Expected password argument, no argument found." );
				System.exit( 1 );
			}
		}
		catch ( ParseException exp )
		{
			logger.log( Level.SEVERE, "Issue handling command line arguments", exp );
		}

		gameServer.start( JmeContext.Type.Headless );

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
					List< HostedConnection > connections = gameServer.getGameServerManager( ).getSessionManager( )
							.getAllSessions( );
					if ( connections.size( ) > 0 )
					{
						StringBuilder sb = new StringBuilder( );
						sb.append( connections.size( ) + " Authenticated sessions: \n" );
						for ( HostedConnection hc : connections )
						{
							sb.append( String.format( "%s: %s - %s \n", hc.getId( ), hc.getAddress( ), gameServer.getGameServerManager( )
									.getSessionManager( ).getFromSession( hc ) ) );
						}
						logger.log( Level.INFO, sb.toString( ) );
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
		logger.log( Level.INFO, "Shutting down" );
		gameServer.stop( );
	}

	/**
	 * The supplied command line arguments parsed into a more usable form
	 */
	private CommandLine arguments;

	/**
	 * The server connection manager to the clients
	 */
	private GameServerManager gameServerManager;

	/**
	 * The client connection manager to the Master Server
	 */
	private GameMasterConnManager masterConnManager;

	/**
	 * The port the server will be hosted on
	 */
	private int port;

	/**
	 * Returns if the MasterServer connection manager is logged in
	 * 
	 * @return logged in status
	 */
	public boolean authenticated( )
	{
		if ( masterConnManager != null )
		{
			return masterConnManager.isLoggedIn( );
		}
		return false;
	}

	/**
	 * Returns the parsed command line arguments
	 * 
	 * @return parsed command line arguments
	 */
	public CommandLine getArguments( )
	{
		return arguments;
	}

	/**
	 * Returns the current GameServerManager instance
	 * 
	 * @return current GameServerManager instance
	 */
	public GameServerManager getGameServerManager( )
	{
		return gameServerManager;
	}

	/**
	 * Returns the current GameMasterConnManager instance
	 * 
	 * @return current GameMasterConnManager instance
	 */
	public GameMasterConnManager getMasterConnManager( )
	{
		return masterConnManager;
	}

	/**
	 * Sets the parsed command line arguments
	 * 
	 * @param arguments
	 *            parsed command line arguments
	 */
	public void setArguments( CommandLine arguments )
	{
		this.arguments = arguments;
	}

	@Override
	public void simpleInitApp( )
	{
		gameServerManager = new GameServerManager( this );

		port = 7331; /* default */

		YamlConfig config = YamlConfig.DEFAULT;

		if ( config.containsKey( "gameserver.port" ) )
		{
			if ( config.get( "gameserver.port" ) instanceof Integer )
			{
				port = ( Integer ) YamlConfig.DEFAULT.get( "gameserver.port" );
			}
			else
			{
				logger.log( Level.WARNING, "Configuration port value could not be parsed, defaulting to 7331" );
			}
		}
		else
		{
			logger.log( Level.WARNING, "Configuration port value not set, defaulting to 7331" );
		}
		config.put( "gameserver.port", port );
		config.save( );

		if ( getArguments( ).hasOption( "p" ) )
		{
			String value = getArguments( ).getOptionValue( "p" );
			try
			{
				int intValue = Integer.parseInt( value );
				if ( intValue < 65535 && intValue > 0 )
				{
					port = intValue;
					logger.log( Level.INFO, "Using command line argument port value: " + port );
				}
				else
				{
					logger.log( Level.WARNING, "Command line argument port value is out of range, defaulting to configuration" );
				}
			}
			catch ( NumberFormatException e )
			{
				logger.log( Level.WARNING, "Command line argument port value could not be parsed, defaulting to configuration" );
			}
		}

		gameServerManager.run( port );

		masterConnManager = new GameMasterConnManager( this );
	}

	/**
	 * @return the port
	 */
	public int getPort( )
	{
		return port;
	}

	@Override
	public void simpleUpdate( float tpf )
	{
		gameServerManager.update( tpf );

		super.simpleUpdate( tpf );
	}

	@Override
	public void stop( )
	{
		getGameServerManager( ).close( );
		getMasterConnManager( ).close( );
		super.stop( );
	}

}
