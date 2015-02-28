package trendli.me.makhana.client.net;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.ClientMain;
import trendli.me.makhana.common.data.LoginCredentials;
import trendli.me.makhana.common.data.ServerStatus;
import trendli.me.makhana.common.data.Session;
import trendli.me.makhana.common.net.ConnectionManager;
import trendli.me.makhana.common.net.SerializerRegistrar;
import trendli.me.makhana.common.net.msg.ChatMessage;
import trendli.me.makhana.common.net.msg.LogoutRequest;
import trendli.me.makhana.common.net.msg.SecureLoginRequest;
import trendli.me.makhana.common.net.msg.ServerListResponse;
import trendli.me.makhana.common.net.msg.UserListResponse;
import trendli.me.makhana.common.util.PackageUtils;

import com.jme3.network.MessageListener;
import com.jme3.network.Network;

import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ListBox;

/**
 * 
 * @author Elliott Butler
 * @author Ryan Grier
 * 
 */
public class MasterConnectionManager extends ConnectionManager< ClientMain >
{

    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( MasterConnectionManager.class.getName( ) );

    private Key publicKey;

    /**
     * List of authenticated servers.
     */
    private ArrayList< ServerStatus > servers;

    /**
     * The account of this connection
     */
    private Session session;

    @SuppressWarnings(
    {
            "rawtypes", "unchecked"
    } )
    public MasterConnectionManager( ClientMain app )
    {
        super( app );
        try
        {

            String tempHost = ( String ) app.getConfiguration( ).get( "networkserver.host" );
            Integer tempPort = ( Integer ) app.getConfiguration( ).get( "networkserver.port" );
            if ( tempHost == null || tempPort == null )
            {
                if ( tempHost == null )
                {
                    tempHost = "localhost";
                    app.getConfiguration( ).put( "networkserver.host", tempHost );
                }
                if ( tempPort == null )
                {
                    tempPort = new Integer( 1337 );
                    app.getConfiguration( ).put( "networkserver.port", tempPort );

                }
                app.getConfiguration( ).save( );
            }
            final String host = tempHost;
            final Integer port = tempPort;

            setClient( Network.connectToServer( "makhana", 2, host, port, port + 1 ) );
            logger.log( Level.INFO, String.format( "We will connect to master server %s with ports TCP:%d, UDP:%d.", host, port, port + 1 ) );

            /* register all message types with the serializer */
            logger.log( Level.FINE, "Registering all serializable classes." );
            SerializerRegistrar.registerAllSerializable( );

            /* add listeners */
            logger.log( Level.FINE, "Registering connection listener with client." );
            getClient( ).addClientStateListener( new MasterConnectionListener( this ) );

            logger.log( Level.FINE, "Registering message listeners with client." );
            List< Object > messageListeners = PackageUtils.createAllInPackage( "trendli.me.makhana.client.net.masterlistener", this );
            for ( Object messageListener : messageListeners )
            {
                getClient( ).addMessageListener( ( MessageListener ) messageListener );
            }

            /* start client */
            logger.log( Level.FINE, "Starting the master client connection." );
            getClient( ).start( );
        }
        catch ( Exception e )
        {
            logger.log( Level.SEVERE, "Couldn't connect NetworkClient to server!", e );
        }
    }

    /**
     * @return the publicKey
     */
    public Key getPublicKey( )
    {
        return publicKey;
    }

    /**
     * Returns the list of current authenticated servers.
     * 
     * @return List of authenticated servers.
     */
    public ArrayList< ServerStatus > getServers( )
    {
        return servers;
    }

    /**
     * Returns the account of this connection
     * 
     * @return the account of this connection
     */
    public Session getSession( )
    {
        return session;
    }

    /**
     * Returns true if we are logged in, false otherwise.
     * 
     * @return true if we are logged in, false otherwise
     */
    public boolean isLoggedIn( )
    {
        return getSession( ) != null;
    }

    /**
     * Receives a NetworkChatMessage and posts it to the networks chat.
     * 
     * @param message
     */
    public void receiveMessage( ChatMessage message )
    {
        if ( getApp( ).getNifty( ).getScreen( "network" ) != null )
        {
            Chat chat = getApp( ).getNifty( ).getScreen( "network" ).findNiftyControl( "networkChat", Chat.class );
            /*
             * If the session exists, use it's name. Otherwise it's probably a
             * server notice.
             */
            logger.log( Level.INFO, "Received a chat message" );
            if ( message.getSession( ) != null )
            {
                String chatMessage = message.getMessage( );
                String chatDisplayName = message.getSession( ).getDisplayName( );
                if ( chatMessage != null )
                {
                    if ( chatMessage.length( ) > 0 )
                    {
                        if ( chatMessage.charAt( 0 ) == '/' )
                        {
                            if ( chatMessage.substring( 0, 3 ).equals( "/me" ) )
                            {
                                chat.receivedChatLine( "* " + chatDisplayName + chatMessage.substring( 3 ), null );
                            }
                        }
                        else
                        {
                            chat.receivedChatLine( chatDisplayName + ": " + chatMessage, null );
                        }
                    }
                    else
                    {
                        logger.log( Level.WARNING, "Received a blank message by " + chatDisplayName );
                    }
                }
                else
                {
                    logger.log( Level.WARNING, "Received a null message by " + chatDisplayName );
                }
            }
            else
            {
                chat.receivedChatLine( message.getMessage( ), null );
            }
        }
    }

    /**
     * Receives a Server list and shows the authenticated servers on the network
     * screen.
     * 
     * @param message
     */
    public void receiveServerList( ServerListResponse message )
    {
        if ( getApp( ).getNifty( ).getScreen( "network" ) != null )
        {
            this.servers = message.getServerList( );
            @SuppressWarnings( "unchecked" )
            ListBox< ServerStatus > networks = getApp( ).getNifty( ).getScreen( "network" )
                    .findNiftyControl( "networkScroll", ListBox.class );
            networks.clear( );
            if ( !this.servers.isEmpty( ) )
            {
                networks.addAllItems( this.servers );
            }
            else
            {
            }
        }
    }

    /**
     * Receives a list of current users in the network lobby and adds them to
     * the chat.
     * 
     * @param message
     */
    public void receiveUserList( UserListResponse message )
    {
        if ( getApp( ).getNifty( ).getScreen( "network" ) != null )
        {
            Chat chat = getApp( ).getNifty( ).getScreen( "network" ).findNiftyControl( "networkChat", Chat.class );
            ArrayList< String > users = message.getAccountNames( );
            if ( users != null )
            {
                ListBox< ? > playerList = chat.getElement( ).findNiftyControl( "networkChat#playerList", ListBox.class );
                if ( playerList != null )
                {
                    playerList.clear( );
                }
                for ( String user : users )
                {
                    chat.addPlayer( user, null );
                }
            }
        }
    }

    /**
     * Sends a login to the MasterServer.
     * 
     * @param accountName
     *            the accountname to auth as
     * @param password
     *            the password to auth as
     * 
     */
    public void sendLogin( String accountName, String password )
    {
        logger.log( Level.FINE, "Sending account and pass to the connection." );
        send( new SecureLoginRequest( new LoginCredentials( accountName, password, null ), getPublicKey( ), false ) );
    }

    /**
     * Sends a logout to the MasterServer.
     * 
     */
    public void sendLogout( )
    {
        logger.log( Level.FINE, "Sending logout request to the connection." );
        send( new LogoutRequest( ) );
        setSession( null );
    }

    /**
     * Sends the chat messages to all current clients.
     * 
     * @param message
     */
    public void sendMessage( String message )
    {
        send( new ChatMessage( getSession( ), message ) );
    }

    /**
     * @param publicKey
     *            the publicKey to set
     */
    public void setPublicKey( Key publicKey )
    {
        this.publicKey = publicKey;
    }

    public void setServers( ArrayList< ServerStatus > servers )
    {
        this.servers = servers;
    }

    /**
     * Sets the account of this connection
     * 
     * @param account
     *            the account of this connection
     */
    public void setSession( Session session )
    {
        logger.log( Level.INFO, "Setting session: " + session );
        this.session = session;
    }

}
