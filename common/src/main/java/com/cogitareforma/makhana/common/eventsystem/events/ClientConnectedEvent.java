package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.eventsystem.ClientNetworkEvent;
import com.jme3.network.Client;

public class ClientConnectedEvent extends ClientNetworkEvent
{

	public ClientConnectedEvent( Client client, ConnectionType type )
	{
		super( client, type );
	}

}
