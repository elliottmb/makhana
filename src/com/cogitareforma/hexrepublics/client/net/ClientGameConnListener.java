package com.cogitareforma.hexrepublics.client.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.views.HudViewController;
import com.cogitareforma.hexrepublics.client.views.LobbyViewController;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.simsilica.es.client.RemoteEntityData;

public class ClientGameConnListener implements ClientStateListener
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ClientGameConnListener.class.getName( ) );

	/**
	 * The client's manager.
	 */
	private ClientGameConnManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param manager
	 *            the client's manager
	 */
	public ClientGameConnListener( ClientGameConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void clientConnected( Client client )
	{
		logger.log( Level.INFO, "Connected to the game server successfuly." );

		logger.log( Level.INFO, "Attaching RemoteEntityData." );
		manager.setRemoteEntityData( new RemoteEntityData( manager.getClient( ), 0 ) );
		// TODO Auto-generated method stub

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

	}
}
