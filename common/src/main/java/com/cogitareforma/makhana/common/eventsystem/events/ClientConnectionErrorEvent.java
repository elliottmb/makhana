package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.eventsystem.ClientNetworkEvent;
import com.jme3.network.Client;

public class ClientConnectionErrorEvent extends ClientNetworkEvent
{
	private final Throwable exception;

	public ClientConnectionErrorEvent( Client client, ConnectionType type, Throwable exception )
	{
		super( client, type );
		this.exception = exception;
	}

	/**
	 * @return the exception
	 */
	public Throwable getException( )
	{
		return exception;
	}

}
