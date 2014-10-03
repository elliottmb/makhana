package com.cogitareforma.hexrepublics.client.net.gamelistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.net.ClientGameConnManager;
import com.cogitareforma.makhana.common.net.msg.AccountRequest;
import com.cogitareforma.makhana.common.net.msg.AccountResponse;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class AccountRequestListener implements MessageListener< Client >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( AccountRequestListener.class.getName( ) );

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
	public AccountRequestListener( ClientGameConnManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof AccountRequest )
		{
			logger.log( Level.INFO, "Received an Account request, sending account details to game server." );
			manager.send( new AccountResponse( manager.getApp( ).getMasterConnManager( ).getAccount( ) ) );
		}
	}

}
