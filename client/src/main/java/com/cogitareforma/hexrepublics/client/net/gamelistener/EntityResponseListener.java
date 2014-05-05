package com.cogitareforma.hexrepublics.client.net.gamelistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.net.ClientGameConnManager;
import com.cogitareforma.hexrepublics.common.net.msg.EntityResponse;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class EntityResponseListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( EntityResponseListener.class.getName( ) );

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
	public EntityResponseListener( ClientGameConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof EntityResponse )
		{
			logger.log( Level.INFO, "Received an EntityResponse from the game server." );
			EntityResponse response = ( EntityResponse ) message;
			logger.log( Level.INFO, "Received an: " + response.getRequestType( ) + ", it was "
					+ ( response.isSuccessful( ) ? "successful" : "not successful" ) );
			// TODO:
		}
	}

}
