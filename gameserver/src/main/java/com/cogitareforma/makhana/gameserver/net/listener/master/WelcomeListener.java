package com.cogitareforma.makhana.gameserver.net.listener.master;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.ServerStatus;
import com.cogitareforma.makhana.common.net.msg.WelcomeMessage;
import com.cogitareforma.makhana.gameserver.net.GameMasterConnManager;
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
	private GameMasterConnManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param manager
	 *            the client's manage
	 */
	public WelcomeListener( GameMasterConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof WelcomeMessage )
		{
			logger.log( Level.INFO, "Recieved a welcome message from master" );
			WelcomeMessage wm = ( WelcomeMessage ) message;
			manager.setPublicKey( wm.getPublicKey( ) );

			ServerStatus ss = manager.getApp( ).getGameServerManager( ).getServerStatus( );
			if ( ss != null )
			{
				ss.setChanged( true );
			}
		}
	}
}
