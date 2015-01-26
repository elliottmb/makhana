package trendli.me.makhana.client.net.gamelistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.net.GameConnectionManager;
import trendli.me.makhana.common.net.msg.SessionRequest;
import trendli.me.makhana.common.net.msg.SessionResponse;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class SessionRequestListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SessionRequestListener.class.getName( ) );

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
	public SessionRequestListener( GameConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof SessionRequest )
		{
			logger.log( Level.INFO, "Received an Account request, sending account details to game server." );
			manager.send( new SessionResponse( manager.getApp( ).getMasterConnectionManager( ).getSession( ) ) );
		}
	}

}
