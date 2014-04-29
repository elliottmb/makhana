package com.cogitareforma.hexrepublics.client.net.masterlistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.net.ClientMasterConnManager;
import com.cogitareforma.hexrepublics.common.net.msg.NetworkChatMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 * 
 */
public class NetworkChatListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( NetworkChatListener.class.getName( ) );

	/**
	 * The client's manager.
	 */
	private ClientMasterConnManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param manager
	 *            the client's manager
	 */
	public NetworkChatListener( ClientMasterConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof NetworkChatMessage )
		{
			logger.log( Level.INFO, "Received a chat message." );
			NetworkChatMessage msg = ( NetworkChatMessage ) message;
			logger.log( Level.INFO, "Sending message to the controller." );
			manager.receiveMessage( msg );
		}
	}

}
