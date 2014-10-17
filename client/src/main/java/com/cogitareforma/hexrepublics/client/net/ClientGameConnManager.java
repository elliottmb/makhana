package com.cogitareforma.hexrepublics.client.net;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.client.ClientMain;
import com.cogitareforma.makhana.common.net.ConnectionManager;
import com.cogitareforma.makhana.common.net.SerializerRegistrar;
import com.cogitareforma.makhana.common.net.msg.NetworkChatMessage;
import com.cogitareforma.makhana.common.util.PackageUtils;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.simsilica.es.client.RemoteEntityData;

import de.lessvoid.nifty.controls.Chat;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class ClientGameConnManager extends ConnectionManager< ClientMain >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ClientGameConnManager.class.getName( ) );

	private RemoteEntityData remoteEntityData;

	public ClientGameConnManager( ClientMain app )
	{
		super( app );
	}

	/**
	 * Connects client to a given host and port.
	 * 
	 * @param host
	 *            host to connect to.
	 * @param port
	 *            port to use.
	 * @return true if connection is successful, otherwise false
	 */
	@SuppressWarnings(
	{
			"rawtypes", "unchecked"
	} )
	public boolean connect( final String host, final Integer port )
	{
		// Close existing connection
		if ( isConnected( ) )
		{
			close( );
		}

		// Attempt to establish a new connection
		try
		{
			setClient( Network.connectToServer( "makhana", 2, host, port, port ) );
			logger.log( Level.INFO, "Attempting to connect to game server at: " + host + ":" + port );

			/* register all message types with the serializer */
			logger.log( Level.FINE, "Registering all serializable classes." );
			SerializerRegistrar.registerAllSerializable( );

			/* add listeners */
			logger.log( Level.FINE, "Registering connection listener with client." );
			ClientGameConnListener connListener = new ClientGameConnListener( this );
			getClient( ).addClientStateListener( connListener );
			getClient( ).addErrorListener( connListener );

			logger.log( Level.FINE, "Registering message listeners with client." );
			List< Object > messageListeners = PackageUtils.createAllInPackage( "com.cogitareforma.hexrepublics.client.net.gamelistener",
					this );
			for ( Object messageListener : messageListeners )
			{
				getClient( ).addMessageListener( ( MessageListener ) messageListener );
			}

			/* start client */
			logger.log( Level.FINE, "Starting the game client connection." );
			getClient( ).start( );

			return true;
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Couldn't connect client to game server!", e );
		}
		return false;
	}

	/**
	 * Returns the ClientGameConnManager's remote entity data.
	 * 
	 * @return the ClientGameConnManager's remote entity data.
	 */
	public RemoteEntityData getRemoteEntityData( )
	{
		return remoteEntityData;
	}

	/**
	 * Receives a NetworkChatMessage and posts it to either the game's lobby
	 * chat or the in game chat.
	 * 
	 * @param message
	 */
	public void receiveMessage( NetworkChatMessage message )
	{
		if ( getApp( ).getNifty( ).getScreen( "lobby" ) != null )
		{
			Chat chat = getApp( ).getNifty( ).getScreen( "lobby" ).findNiftyControl( "lobbyChat", Chat.class );
			if ( message.getAccount( ) != null )
			{
				chat.receivedChatLine( message.getAccount( ).getAccountName( ) + ": " + message.getMessage( ), null );
			}
			else
			{
				chat.receivedChatLine( message.getMessage( ), null );
			}
		}
		else
		{
			if ( getApp( ).getNifty( ).getScreen( "hud" ) != null )
			{
				Chat chat = getApp( ).getNifty( ).findPopupByName( "ingameChat" ).findNiftyControl( "gameChat", Chat.class );
				if ( message.getAccount( ) != null )
				{
					chat.receivedChatLine( message.getAccount( ).getAccountName( ) + ": " + message.getMessage( ), null );
				}
				else
				{
					chat.receivedChatLine( message.getMessage( ), null );
				}
			}
		}
	}

	/**
	 * Takes message from the game's lobby chat or in game chat and sends them
	 * to all connected clients.
	 * 
	 * @param message
	 */
	public void sendMessage( String message )
	{
		send( new NetworkChatMessage( getApp( ).getMasterConnManager( ).getAccount( ), message ) );
	}

	/**
	 * @param remoteEntityData
	 *            the remoteEntityData to set
	 */
	public void setRemoteEntityData( RemoteEntityData remoteEntityData )
	{
		this.remoteEntityData = remoteEntityData;
	}
}
