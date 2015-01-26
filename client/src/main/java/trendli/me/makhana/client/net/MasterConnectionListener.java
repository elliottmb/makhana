package trendli.me.makhana.client.net;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.eventsystem.ClientNetworkEvent.ConnectionType;
import trendli.me.makhana.common.eventsystem.events.ClientConnectedEvent;
import trendli.me.makhana.common.eventsystem.events.ClientConnectionErrorEvent;
import trendli.me.makhana.common.eventsystem.events.ClientDisconnectedEvent;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.ErrorListener;

public class MasterConnectionListener implements ClientStateListener, ErrorListener< Client >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( MasterConnectionListener.class.getName( ) );

	/**
	 * The client's manager.
	 */
	private MasterConnectionManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param controller
	 *            the client's manager
	 */
	public MasterConnectionListener( MasterConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void clientConnected( Client client )
	{
		logger.log( Level.INFO, "Connected to the master server successfuly." );
		// TODO Auto-generated method stub
		manager.getApp( ).getEventManager( ).triggerEvent( new ClientConnectedEvent( client, ConnectionType.MASTER ) );

	}

	@Override
	public void clientDisconnected( Client client, DisconnectInfo info )
	{
		logger.log( Level.INFO, "Disconnected from the master server." );
		// TODO Auto-generated method stub
		manager.getApp( ).getEventManager( ).triggerEvent( new ClientDisconnectedEvent( client, ConnectionType.MASTER, info ) );
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

		// TODO: Move the above function into a handler for the following event
		manager.getApp( ).getEventManager( ).triggerEvent( new ClientConnectionErrorEvent( client, ConnectionType.MASTER, exception ) );
	}

}
