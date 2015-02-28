package trendli.me.makhana.masterserver.net.listener;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import trendli.me.makhana.common.data.Session;
import trendli.me.makhana.common.net.DataManager;
import trendli.me.makhana.common.net.msg.ChatMessage;
import trendli.me.makhana.masterserver.net.MasterServerManager;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 * 
 */
public class ChatListener implements MessageListener< HostedConnection >
{
    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( ChatListener.class.getName( ) );

    private final static int chatInterval = 500;

    /**
	 * 
	 */
    private MasterServerManager server;

    /**
     * Previous message history, used for spam blocking
     */
    private Map< String, Pair< Date, Boolean > > chatTime;

    public ChatListener( MasterServerManager server )
    {
        this.server = server;
        this.chatTime = new LinkedHashMap< String, Pair< Date, Boolean > >( );
    }

    /**
     * Sends a NetworkChatMessage to all authenticated clients except for the
     * game servers. If
     * 
     * @param msg
     *            the NetworkChatMessage to send
     */
    private void broadcastMessage( ChatMessage msg )
    {
        DataManager< Session > sm = server.getSessionManager( );
        for ( HostedConnection conn : sm.getConnections( ) )
        {
            Session sess = sm.get( conn );
            /* If the account is null or is that of a game server, continue */
            if ( sess == null || sess.isInGame( ) )
            {
                continue;
            }
            conn.send( msg );
        }
    }

    @Override
    public void messageReceived( HostedConnection source, Message message )
    {

        if ( message instanceof ChatMessage )
        {
            ChatMessage msg = ( ChatMessage ) message;

            logger.log( Level.INFO, "We received a chat message" );
            Session loggedInUser = server.getSessionManager( ).get( source );
            if ( loggedInUser == null || !loggedInUser.equals( msg.getSession( ) ) )
            {
                /*
                 * Account is either null, is not the same as the logged in
                 * account, or is a game server
                 */
                return;
            }

            // Basic spam prevention.
            Pair< Date, Boolean > lastMessage = chatTime.get( loggedInUser.getDisplayName( ) );
            Date timeNow = new Date( );
            if ( lastMessage == null || lastMessage.getLeft( ).getTime( ) <= timeNow.getTime( ) - chatInterval )
            {
                logger.log( Level.INFO, "Chat -> " + msg.getSession( ).getDisplayName( ) + ": " + msg.getMessage( ) );
                chatTime.put( loggedInUser.getDisplayName( ), Pair.of( timeNow, false ) );
                broadcastMessage( msg );
            }
            else
            {
                if ( lastMessage.getRight( ) == false )
                {
                    logger.log( Level.INFO, "Chat -> " + msg.getSession( ).getDisplayName( ) + " is spamming the chat." );
                    sendNotice( source, String.format( "Server Warning: You are spamming the chat, please wait %.1f seconds.",
                            ( chatInterval ) / 1000.0 ) );
                    chatTime.put( loggedInUser.getDisplayName( ), Pair.of( timeNow, true ) );
                }
            }
        }
    }

    /**
     * Sends a notice to the specified connection.
     * 
     * @param conn
     * @param notice
     */
    private void sendNotice( HostedConnection conn, String notice )
    {
        conn.send( new ChatMessage( null, notice ) );
    }

}
