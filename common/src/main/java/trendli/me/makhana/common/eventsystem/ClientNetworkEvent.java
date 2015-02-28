package trendli.me.makhana.common.eventsystem;

import com.jme3.network.Client;

public abstract class ClientNetworkEvent extends Event
{
    public enum ConnectionType
    {
        MASTER, GAME
    }

    private final Client client;
    private final ConnectionType connectionType;

    public ClientNetworkEvent( Client client, ConnectionType type )
    {
        this.client = client;
        this.connectionType = type;
    }

    /**
     * @return the client
     */
    public Client getClient( )
    {
        return client;
    }

    /**
     * @return the type
     */
    public ConnectionType getConnectionType( )
    {
        return connectionType;
    }

}
