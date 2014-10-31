package com.cogitareforma.makhana.client.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.views.GeneralController;
import com.cogitareforma.hexrepublics.client.views.HudViewController;
import com.cogitareforma.hexrepublics.client.views.LobbyViewController;
import com.cogitareforma.makhana.client.util.NiftyFactory;
import com.cogitareforma.makhana.common.net.msg.ClientStatusMessage;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ErrorListener;
import com.simsilica.es.client.RemoteEntityData;

public class GameConnectionListener implements ClientStateListener, ErrorListener< Client >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( GameConnectionListener.class.getName( ) );

	/**
	 * The client's manager.
	 */
	private GameConnectionManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param manager
	 *            the client's manager
	 */
	public GameConnectionListener( GameConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void clientConnected( Client client )
	{
		logger.log( Level.INFO, "Connected to the game server successfuly." );

		logger.log( Level.INFO, "Attaching RemoteEntityData." );
		manager.setRemoteEntityData( new RemoteEntityData( manager.getClient( ), 0 ) );

		// throw us into the game lobby!
		GeneralController gController = ( GeneralController ) manager.getApp( ).getNifty( ).getCurrentScreen( ).getScreenController( );
		NiftyFactory.createGameLobby( manager.getApp( ).getNifty( ) );
		gController.gotoScreen( "lobby", false, true, false, null, null );

		logger.log( Level.INFO, "Informing Master Server of in game status" );
		MasterConnectionManager masterConnManager = manager.getApp( ).getMasterConnectionManager( );
		if ( masterConnManager.isConnected( ) )
		{
			manager.getApp( ).getMasterConnectionManager( ).send( new ClientStatusMessage( true ) );
		}
	}

	@Override
	public void clientDisconnected( Client client, DisconnectInfo info )
	{
		logger.log( Level.INFO, "Disconnected from the game server." );
		// TODO Auto-generated method stub
		HudViewController hvc = manager.getApp( ).getStateManager( ).getState( HudViewController.class );
		if ( hvc != null )
		{
			hvc.exitToNetwork( );
		}
		else
		{
			LobbyViewController lvc = manager.getApp( ).getStateManager( ).getState( LobbyViewController.class );
			if ( lvc != null )
			{
				lvc.back( );
			}
		}

		manager.setRemoteEntityData( null );

		logger.log( Level.INFO, "Informing Master Server of out of game status" );
		MasterConnectionManager masterConnManager = manager.getApp( ).getMasterConnectionManager( );
		if ( masterConnManager.isConnected( ) )
		{
			manager.getApp( ).getMasterConnectionManager( ).send( new ClientStatusMessage( false ) );
		}
	}

	@Override
	public void handleError( Client client, Throwable exception )
	{
		logger.log( Level.SEVERE, "A Game Server Connection error has occured. ", exception );
		manager.getApp( ).enqueue( ( ) ->
		{
			manager.close( );
			return null;
		} );
	}
}
