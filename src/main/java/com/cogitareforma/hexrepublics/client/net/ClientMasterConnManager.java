package com.cogitareforma.hexrepublics.client.net;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.common.data.ServerStatus;
import com.cogitareforma.hexrepublics.common.net.MasterConnManager;
import com.cogitareforma.hexrepublics.common.net.SerializerRegistrar;
import com.cogitareforma.hexrepublics.common.net.msg.NetworkChatMessage;
import com.cogitareforma.hexrepublics.common.net.msg.ServerListResponse;
import com.cogitareforma.hexrepublics.common.net.msg.UserListResponse;
import com.cogitareforma.hexrepublics.common.util.PackageUtils;
import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;

import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ListBox;

/**
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 * @author Ryan Grier
 * 
 */
public class ClientMasterConnManager extends MasterConnManager< ClientMain >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ClientMasterConnManager.class.getName( ) );
	/**
	 * List of authenticated servers.
	 */
	private ArrayList< ServerStatus > servers;

	@SuppressWarnings(
	{
			"rawtypes", "unchecked"
	} )
	public ClientMasterConnManager( ClientMain app )
	{
		super( app );
		try
		{

			String tempHost = ( String ) YamlConfig.DEFAULT.get( "networkserver.host" );
			Integer tempPort = ( Integer ) YamlConfig.DEFAULT.get( "networkserver.port" );
			if ( tempHost == null || tempPort == null )
			{
				if ( tempHost == null )
				{
					tempHost = "localhost";
					YamlConfig.DEFAULT.put( "networkserver.host", tempHost );
				}
				if ( tempPort == null )
				{
					tempPort = new Integer( 1337 );
					YamlConfig.DEFAULT.put( "networkserver.port", tempPort );

				}
				YamlConfig.DEFAULT.save( );
			}
			final String host = tempHost;
			final Integer port = tempPort;

			setClient( Network.connectToServer( "Hex Republics", 2, host, port, port + 1 ) );
			logger.log( Level.INFO, String.format( "We will connect to master server %s with ports TCP:%d, UDP:%d.", host, port, port + 1 ) );

			/* register all message types with the serializer */
			logger.log( Level.FINE, "Registering all serializable classes." );
			SerializerRegistrar.registerAllSerializable( );

			/* add listeners */
			logger.log( Level.FINE, "Registering connection listener with client." );
			getClient( ).addClientStateListener( new ClientMasterConnListener( this ) );

			logger.log( Level.FINE, "Registering message listeners with client." );
			List< Object > messageListeners = PackageUtils.createAllInPackage( "com.cogitareforma.hexrepublics.client.net.masterlistener",
					this );
			for ( Object messageListener : messageListeners )
			{
				getClient( ).addMessageListener( ( MessageListener ) messageListener );
			}

			/* start client */
			logger.log( Level.FINE, "Starting the master client connection." );
			getClient( ).start( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Couldn't connect NetworkClient to server!", e );
		}
	}

	/**
	 * Returns the list of current authenticated servers.
	 * 
	 * @return List of authenticated servers.
	 */
	public ArrayList< ServerStatus > getServers( )
	{
		return servers;
	}

	/**
	 * Receives a NetworkChatMessage and posts it to the networks chat.
	 * 
	 * @param message
	 */
	public void receiveMessage( NetworkChatMessage message )
	{
		if ( getApp( ).getNifty( ).getScreen( "network" ) != null )
		{
			Chat chat = getApp( ).getNifty( ).getScreen( "network" ).findNiftyControl( "networkChat", Chat.class );
			/*
			 * If the account exists, use it's name. Otherwise it's probably a
			 * server notice.
			 */
			if ( message.getAccount( ) != null )
			{
				if ( message.getMessage( ).charAt( 0 ) == '/' )
				{
					if ( message.getMessage( ).substring( 0, 3 ).equals( "/me" ) )
					{
						chat.receivedChatLine( "* " + message.getAccount( ).getAccountName( ) + message.getMessage( ).substring( 3 ), null );
					}
				}
				else
				{
					chat.receivedChatLine( message.getAccount( ).getAccountName( ) + ": " + message.getMessage( ), null );
				}
			}
			else
			{
				chat.receivedChatLine( message.getMessage( ), null );
			}
		}
	}

	/**
	 * Receives a Server list and shows the authenticated servers on the network
	 * screen.
	 * 
	 * @param message
	 */
	public void receiveServerList( ServerListResponse message )
	{
		if ( getApp( ).getNifty( ).getScreen( "network" ) != null )
		{
			this.servers = message.getServerList( );
			@SuppressWarnings( "unchecked" )
			ListBox< ServerStatus > networks = getApp( ).getNifty( ).getScreen( "network" )
					.findNiftyControl( "networkScroll", ListBox.class );
			networks.clear( );
			if ( !this.servers.isEmpty( ) )
			{
				networks.addAllItems( this.servers );
			}
			else
			{
			}
		}
	}

	/**
	 * Receives a list of current users in the network lobby and adds them to
	 * the chat.
	 * 
	 * @param message
	 */
	public void receiveUserList( UserListResponse message )
	{
		if ( getApp( ).getNifty( ).getScreen( "network" ) != null )
		{
			Chat chat = getApp( ).getNifty( ).getScreen( "network" ).findNiftyControl( "networkChat", Chat.class );
			ArrayList< String > users = message.getAccountNames( );
			if ( users != null )
			{
				ListBox< ? > playerList = chat.getElement( ).findNiftyControl( "networkChat#playerList", ListBox.class );
				if ( playerList != null )
				{
					playerList.clear( );
				}
				for ( String user : users )
				{
					chat.addPlayer( user, null );
				}
			}
		}
	}

	/**
	 * Sends the chat messages to all current clients.
	 * 
	 * @param message
	 */
	public void sendMessage( String message )
	{
		send( new NetworkChatMessage( getAccount( ), message ) );
	}

	public void setServers( ArrayList< ServerStatus > servers )
	{
		this.servers = servers;
	}

}
