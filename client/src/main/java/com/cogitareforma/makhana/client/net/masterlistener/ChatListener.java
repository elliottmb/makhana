package com.cogitareforma.makhana.client.net.masterlistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.client.net.MasterConnectionManager;
import com.cogitareforma.makhana.common.net.msg.ChatMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 * 
 */
public class ChatListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ChatListener.class.getName( ) );

	/**
	 * The client's manager.
	 */
	private MasterConnectionManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param manager
	 *            the client's manager
	 */
	public ChatListener( MasterConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof ChatMessage )
		{
			logger.log( Level.INFO, "Received a chat message." );
			ChatMessage msg = ( ChatMessage ) message;
			logger.log( Level.INFO, "Sending message to the controller." );
			manager.receiveMessage( msg );
		}
	}

}
