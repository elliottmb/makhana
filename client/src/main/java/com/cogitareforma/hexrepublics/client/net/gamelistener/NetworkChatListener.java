package com.cogitareforma.hexrepublics.client.net.gamelistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.net.ClientGameConnManager;
import com.cogitareforma.makhana.common.net.msg.NetworkChatMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class NetworkChatListener implements MessageListener< Client >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( NetworkChatListener.class.getName( ) );

	/**
	 * The client's manager.
	 */
	private ClientGameConnManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param controller
	 *            the client's manager
	 */
	public NetworkChatListener( ClientGameConnManager manager )
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
