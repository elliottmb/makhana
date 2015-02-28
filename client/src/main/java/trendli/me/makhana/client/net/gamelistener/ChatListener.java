package trendli.me.makhana.client.net.gamelistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.net.GameConnectionManager;
import trendli.me.makhana.common.net.msg.ChatMessage;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ChatListener implements MessageListener< Client >
{
    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( ChatListener.class.getName( ) );

    /**
     * The client's manager.
     */
    private GameConnectionManager manager;

    /**
     * The default constructor accepts the client's manager.
     * 
     * @param controller
     *            the client's manager
     */
    public ChatListener( GameConnectionManager manager )
    {
        this.manager = manager;
    }

    @Override
    public void messageReceived( Client source, Message message )
    {
        if ( message instanceof ChatMessage )
        {
            logger.log( Level.INFO, "Received a chat message." );
            ChatMessage msg = ( ChatMessage ) message;
            logger.log( Level.INFO, "Sending message to the controller." );
            manager.receiveMessage( msg );
        }
    }
}
