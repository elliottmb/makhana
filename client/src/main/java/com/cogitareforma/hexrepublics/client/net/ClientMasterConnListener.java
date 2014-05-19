package com.cogitareforma.hexrepublics.client.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ErrorListener;

public class ClientMasterConnListener implements ClientStateListener, ErrorListener< Client >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ClientMasterConnListener.class.getName( ) );

	/**
	 * The client's manager.
	 */
	private ClientMasterConnManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param controller
	 *            the client's manager
	 */
	public ClientMasterConnListener( ClientMasterConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void clientConnected( Client client )
	{
		logger.log( Level.INFO, "Connected to the master server successfuly." );
		// TODO Auto-generated method stub

	}

	@Override
	public void clientDisconnected( Client client, DisconnectInfo info )
	{
		logger.log( Level.INFO, "Disconnected from the master server." );
		// TODO Auto-generated method stub

	}

	@Override
	public void handleError( Client client, Throwable exception )
	{
		logger.log( Level.SEVERE, "A Master Connection error has occured. ", exception );
		manager.getApp( ).enqueue( ( ) ->
		{
			manager.close( );
			return null;
		} );
	}

}
