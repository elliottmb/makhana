package trendli.me.makhana.gameserver.net.listener.master;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.net.msg.ServerStatusRequest;
import trendli.me.makhana.common.net.msg.ServerStatusResponse;
import trendli.me.makhana.gameserver.net.MasterConnectionManager;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class ServerStatusListener implements MessageListener< Client >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ServerStatusListener.class.getName( ) );

	/**
	 * The servers master connection manager.
	 */
	private MasterConnectionManager manager;

	/**
	 * Constructor for ServerStatusListener. Sets its master connection manager.
	 * 
	 * @param manager
	 *            the master connection manager to set.
	 */
	public ServerStatusListener( MasterConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof ServerStatusRequest )
		{
			logger.log( Level.INFO, "Received a ServerStatusRequest." );
			source.send( new ServerStatusResponse( manager.getApp( ).getGameServerManager( ).getServerStatus( ) ) );
		}
	}
}
