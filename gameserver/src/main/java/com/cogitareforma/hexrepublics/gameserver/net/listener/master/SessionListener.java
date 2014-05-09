package com.cogitareforma.hexrepublics.gameserver.net.listener.master;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.net.msg.LoginResponse;
import com.cogitareforma.hexrepublics.gameserver.net.GameMasterConnManager;
import com.cogitareforma.hexrepublics.gameserver.net.GameServerManager;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * The SessionListener class serves as a handler for receiving authentication
 * responses.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
public class SessionListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SessionListener.class.getName( ) );

	/**
	 * The client's controller.
	 */
	private GameMasterConnManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param manager
	 *            the client's manage
	 */
	public SessionListener( GameMasterConnManager manager )
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
			manager.setAccount( response.getAccount( ) );
			if ( manager.isLoggedIn( ) )
			{
				GameServerManager gsm = manager.getApp( ).getGameServerManager( );
				gsm.getServerStatus( ).setAddress( response.getAccount( ).getAddress( ) );
			}
		}
	}

}