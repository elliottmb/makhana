package trendli.me.makhana.masterserver.net.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.data.Session;
import trendli.me.makhana.common.net.msg.UserListRequest;
import trendli.me.makhana.masterserver.net.MasterServerManager;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Ryan Grier
 * 
 */
public class UserListListener implements MessageListener< HostedConnection >
{

    private final static Logger logger = Logger.getLogger( UserListListener.class.getName( ) );
    private MasterServerManager server;

    public UserListListener( MasterServerManager server )
    {
        this.server = server;
    }

    @Override
    public void messageReceived( HostedConnection source, Message message )
    {
        Session loggedInUser = server.getSessionManager( ).get( source );
        if ( message instanceof UserListRequest )
        {
            if ( loggedInUser == null )
            {
                /*
                 * User is either null, is not the same as the logged in user,
                 * or is a game server
                 */
                return;
            }
            logger.log( Level.INFO, "Recieved a UserListRequest from " + source.getAddress( ) );
            server.broadcastUserList( );
        }
    }

}
