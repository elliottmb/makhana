package trendli.me.makhana.client.net.masterlistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.net.MasterConnectionManager;
import trendli.me.makhana.common.net.msg.LoginResponse;

import com.cogitareforma.hexrepublics.client.views.StartViewController;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * The SessionListener class serves as a handler for receiving authentication
 * responses.
 * 
 * @author Justin Kaufman
 */
public class SessionListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SessionListener.class.getName( ) );

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
	public SessionListener( MasterConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof LoginResponse )
		{
			logger.log( Level.INFO, "Received an authentication response." );
			LoginResponse response = ( LoginResponse ) message;
			logger.log( Level.INFO, "Sending authentication response to the controller." );
			manager.setSession( response.getSession( ) );
			if ( response.getNotice( ) != null )
			{
				logger.log( Level.INFO, response.getNotice( ) );
			}
			if ( manager.getApp( ).getNifty( ).getCurrentScreen( ).getScreenController( ) instanceof StartViewController )
			{
				StartViewController svc = ( StartViewController ) manager.getApp( ).getNifty( ).getCurrentScreen( ).getScreenController( );
				svc.setFailText( response.getNotice( ) );
			}
		}

	}
}
