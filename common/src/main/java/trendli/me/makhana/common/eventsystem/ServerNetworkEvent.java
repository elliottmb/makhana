package trendli.me.makhana.common.eventsystem;

import com.jme3.network.Server;

public abstract class ServerNetworkEvent extends Event
{

	private Server server;

	public ServerNetworkEvent( Server server )
	{
		this.server = server;

	}

	/**
	 * @return the server
	 */
	public Server getServer( )
	{
		return server;
	}

}
