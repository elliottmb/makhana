package com.cogitareforma.makhana.client.net.masterlistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.client.net.MasterConnectionManager;
import com.cogitareforma.makhana.common.net.msg.WelcomeMessage;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * The SessionListener class serves as a handler for receiving authentication
 * responses.
 * 
 * @author Justin Kaufman
 */
public class WelcomeListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( WelcomeListener.class.getName( ) );

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
	public WelcomeListener( MasterConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof WelcomeMessage )
		{
			logger.log( Level.INFO, "Received a welcoem message." );
			WelcomeMessage welcomeMessage = ( WelcomeMessage ) message;
			logger.log( Level.INFO, welcomeMessage.getNotice( ) );
			logger.log( Level.INFO, String.format( "Setting Manager's public key: %s", welcomeMessage.getPublicKey( ) ) );
			manager.setPublicKey( welcomeMessage.getPublicKey( ) );
		}
	}
}
