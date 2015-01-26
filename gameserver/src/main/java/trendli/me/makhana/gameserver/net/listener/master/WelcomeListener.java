package trendli.me.makhana.gameserver.net.listener.master;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.data.ServerStatus;
import trendli.me.makhana.common.net.msg.WelcomeMessage;
import trendli.me.makhana.gameserver.net.MasterConnectionManager;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 */
public class WelcomeListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( WelcomeListener.class.getName( ) );

	/**
	 * The client's controller.
	 */
	private MasterConnectionManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param manager
	 *            the client's manage
	 */
	public WelcomeListener( MasterConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof WelcomeMessage )
		{
			logger.log( Level.INFO, "Recieved a welcome message from master" );

			ServerStatus ss = manager.getApp( ).getGameServerManager( ).getServerStatus( );
			if ( ss != null )
			{
				ss.setChanged( true );
			}
		}
	}
}
