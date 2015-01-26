package trendli.me.makhana.common.eventsystem.events;

import trendli.me.makhana.common.eventsystem.ClientNetworkEvent;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener.DisconnectInfo;

public class ClientDisconnectedEvent extends ClientNetworkEvent
{
	private final DisconnectInfo disconnectionInfo;

	public ClientDisconnectedEvent( Client client, ConnectionType type, DisconnectInfo info )
	{
		super( client, type );
		this.disconnectionInfo = info;
	}

	/**
	 * @return the disconnectionInfo
	 */
	public DisconnectInfo getDisconnectionInfo( )
	{
		return disconnectionInfo;
	}

}
