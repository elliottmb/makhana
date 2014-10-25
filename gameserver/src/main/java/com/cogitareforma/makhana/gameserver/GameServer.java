package com.cogitareforma.makhana.gameserver;

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

import com.cogitareforma.makhana.common.util.MakhanaConfig;
import com.cogitareforma.makhana.gameserver.net.GameServerManager;
import com.cogitareforma.makhana.gameserver.net.MasterConnectionManager;
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
	public static void main( String[ ] args )
	{
		GameServer gameServer = new GameServer( );

		Options options = new Options( );
		options.addOption( "p", "port", true, "the port this server will bind to (optional)" );
		options.addOption( "o", "offline", true, "toggle whether account checking should be run (optional)" );

		CommandLineParser parser = new BasicParser( );

		try
		{
			gameServer.setArguments( parser.parse( options, args ) );
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
					List< HostedConnection > connections = gameServer.getGameServerManager( ).getSessionManager( ).getConnections( );
					if ( connections.size( ) > 0 )
					{
						StringBuilder sb = new StringBuilder( );
						sb.append( connections.size( ) + " Authenticated sessions: \n" );
						for ( HostedConnection hc : connections )
						{
							sb.append( String.format( "%s: %s - %s \n", hc.getId( ), hc.getAddress( ), gameServer.getGameServerManager( )
									.getSessionManager( ).get( hc ) ) );
						}
						logger.log( Level.INFO, sb.toString( ) );
					}
					else
					{
						logger.log( Level.INFO, "No authenticated sessions." );
					}
				}
				if ( "restart".equals( line ) )
				{
					gameServer.restart( );
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

	private MakhanaConfig configuration;

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( GameServer.class.getName( ) );

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
	private MasterConnectionManager masterConnManager;

	/**
	 * The port the server will be hosted on
	 */
	private int port;

	private boolean onlineMode;

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
	public MasterConnectionManager getMasterConnManager( )
	{
		return masterConnManager;
	}

	/**
	 * @return the port
	 */
	public int getPort( )
	{
		return port;
	}

	/**
	 * @return the onlineMode
	 */
	public boolean isOnlineMode( )
	{
		return onlineMode;
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

	/**
	 * @param onlineMode
	 *            the onlineMode to set
	 */
	public void setOnlineMode( boolean onlineMode )
	{
		this.onlineMode = onlineMode;
	}

	@Override
	public void simpleInitApp( )
	{
		gameServerManager = new GameServerManager( this );

		port = 7331; /* default */

		this.configuration = new MakhanaConfig( );

		onlineMode = true;

		if ( configuration.containsKey( "gameserver.port" ) )
		{
			Object portValue = configuration.get( "gameserver.port" );
			if ( portValue instanceof Integer )
			{
				port = ( Integer ) portValue;
				logger.log( Level.INFO, "Configuration port value was: " + portValue );
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
		configuration.put( "gameserver.port", port );

		if ( configuration.containsKey( "gameserver.online" ) )
		{
			Object onlineValue = configuration.get( "gameserver.port" );
			if ( onlineValue instanceof Boolean )
			{
				onlineMode = ( Boolean ) onlineValue;
				logger.log( Level.INFO, "Configuration onlineMode value was: " + onlineValue );
			}
			else
			{
				logger.log( Level.WARNING, "Configuration online mode value could not be parsed, defaulting to online" );
			}
		}
		else
		{
			logger.log( Level.WARNING, "Configuration online mode value not set, defaulting to online" );
		}
		configuration.put( "gameserver.online", onlineMode );

		configuration.save( );

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

		if ( getArguments( ).hasOption( "o" ) )
		{
			String value = getArguments( ).getOptionValue( "o" );
			if ( value != null )
			{
				onlineMode = Boolean.parseBoolean( value );
			}
			else
			{
				logger.log( Level.WARNING, "Command line argument online mode value is undefined, defaulting to configuration" );
			}
		}

		gameServerManager.run( port );

		masterConnManager = new MasterConnectionManager( this );
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

	/**
	 * @return the configuration
	 */
	public MakhanaConfig getConfiguration( )
	{
		return configuration;
	}

	/**
	 * @param configuration
	 *            the configuration to set
	 */
	public void setConfiguration( MakhanaConfig configuration )
	{
		this.configuration = configuration;
	}

}
