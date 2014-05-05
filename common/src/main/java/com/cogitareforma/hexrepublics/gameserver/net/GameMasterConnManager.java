package com.cogitareforma.hexrepublics.gameserver.net;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.net.MasterConnManager;
import com.cogitareforma.hexrepublics.common.net.SerializerRegistrar;
import com.cogitareforma.hexrepublics.common.util.PackageUtils;
import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.cogitareforma.hexrepublics.gameserver.GameServer;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class GameMasterConnManager extends MasterConnManager< GameServer >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( GameMasterConnManager.class.getName( ) );

	@SuppressWarnings(
	{
			"unchecked", "rawtypes"
	} )
	public GameMasterConnManager( GameServer app )
	{
		super( app );
		try
		{

			final String host = ( String ) YamlConfig.DEFAULT.get( "networkserver.host" );
			final Integer port = ( Integer ) YamlConfig.DEFAULT.get( "networkserver.port" );

			setClient( Network.connectToServer( "Hex Republics", 2, host, port, port + 1 ) );
			logger.log( Level.INFO, String.format( "We will connect to master server %s with ports TCP:%d, UDP:%d.", host, port, port + 1 ) );

			/* register all message types with the serializer */
			logger.log( Level.FINE, "Registering all serializable classes for network connection." );
			SerializerRegistrar.registerAllSerializable( );

			/* add listeners */
			logger.log( Level.FINE, "Registering message listeners with for network connection." );
			List< Object > messageListeners = PackageUtils.createAllInPackage(
					"com.cogitareforma.hexrepublics.gameserver.net.listener.master", this );
			for ( Object messageListener : messageListeners )
			{
				getClient( ).addMessageListener( ( MessageListener ) messageListener );
			}

			/* start client */
			logger.log( Level.FINE, "Starting the network connectionclient." );
			getClient( ).start( );
			logger.log( Level.FINE, "Sending login request." );
			sendLogin( app.getArguments( ).getOptionValue( "A" ), app.getArguments( ).getOptionValue( "P" ), true );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Couldn't connect NetworkClient to server!", e );
			System.exit( 1 );
		}
	}
}
