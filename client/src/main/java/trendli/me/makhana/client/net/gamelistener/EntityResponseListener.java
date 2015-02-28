package trendli.me.makhana.client.net.gamelistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.net.GameConnectionManager;
import trendli.me.makhana.common.net.msg.EntityResponse;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class EntityResponseListener implements MessageListener< Client >
{

    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( EntityResponseListener.class.getName( ) );

    /**
     * The client's manager.
     */
    private GameConnectionManager manager;

    /**
     * The default constructor accepts the client's manager.
     * 
     * @param manager
     *            the client's manager
     */
    public EntityResponseListener( GameConnectionManager manager )
    {
        this.manager = manager;
    }

    @Override
    public void messageReceived( Client source, Message message )
    {
        if ( message instanceof EntityResponse )
        {
            logger.log( Level.INFO, "Received an EntityResponse from the game server." );
            EntityResponse response = ( EntityResponse ) message;
            logger.log( Level.INFO, "Received an: " + response.getRequestType( ) + ", it was "
                    + ( response.isSuccessful( ) ? "successful" : "not successful" ) );
            // TODO:
        }
    }

}
