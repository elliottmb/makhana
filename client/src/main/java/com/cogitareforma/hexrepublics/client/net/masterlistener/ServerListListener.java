package com.cogitareforma.hexrepublics.client.net.masterlistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.net.ClientMasterConnManager;
import com.cogitareforma.makhana.common.net.msg.ServerListResponse;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author elliottb
 * 
 */
public class ServerListListener implements MessageListener< Client >
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( UserListListener.class.getName( ) );

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
	public ServerListListener( ClientMasterConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof ServerListResponse )
		{
			logger.log( Level.INFO, "Received a ServerList response." );
			ServerListResponse msg = ( ServerListResponse ) message;
			manager.receiveServerList( msg );
		}
	}

}
