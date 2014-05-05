package com.cogitareforma.hexrepublics.gameserver.net.listener.master;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.net.msg.ServerStatusRequest;
import com.cogitareforma.hexrepublics.common.net.msg.ServerStatusResponse;
import com.cogitareforma.hexrepublics.gameserver.net.GameMasterConnManager;
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
	private GameMasterConnManager manager;

	/**
	 * Constructor for ServerStatusListener. Sets its master connection manager.
	 * 
	 * @param manager
	 *            the master connection manager to set.
	 */
	public ServerStatusListener( GameMasterConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof ServerStatusRequest )
		{
			logger.log( Level.INFO, "Received a ServerStatusRequest." );
			source.send( new ServerStatusResponse( manager.getAccount( ), manager.getApp( ).getGameServerManager( ).getServerStatus( ) ) );
		}
	}
}
