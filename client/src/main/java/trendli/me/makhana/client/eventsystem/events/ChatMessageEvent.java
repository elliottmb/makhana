package trendli.me.makhana.client.eventsystem.events;

import trendli.me.makhana.common.eventsystem.ClientNetworkEvent;

import com.jme3.network.Client;

public class ChatMessageEvent extends ClientNetworkEvent
{
    private final String message;

    public ChatMessageEvent( Client client, ConnectionType type, String message )
    {
        super( client, type );
        this.message = message;
    }

    /**
     * @return the message
     */
    public String getMessage( )
    {
        return message;
    }

}
