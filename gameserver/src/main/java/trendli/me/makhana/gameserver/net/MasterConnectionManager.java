package trendli.me.makhana.gameserver.net;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.net.ConnectionManager;
import trendli.me.makhana.common.net.SerializerRegistrar;
import trendli.me.makhana.common.util.PackageUtils;
import trendli.me.makhana.gameserver.GameServer;

import com.jme3.network.MessageListener;
import com.jme3.network.Network;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class MasterConnectionManager extends ConnectionManager< GameServer >
{
    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( MasterConnectionManager.class.getName( ) );

    @SuppressWarnings(
    {
            "unchecked", "rawtypes"
    } )
    public MasterConnectionManager( GameServer app )
    {
        super( app );
        try
        {

            final String host = ( String ) getApp( ).getConfiguration( ).get( "networkserver.host" );
            final Integer port = ( Integer ) getApp( ).getConfiguration( ).get( "networkserver.port" );

            setClient( Network.connectToServer( "makhana", 2, host, port, port + 1 ) );
            logger.log( Level.INFO, String.format( "We will connect to master server %s with ports TCP:%d, UDP:%d.", host, port, port + 1 ) );

            /* register all message types with the serializer */
            logger.log( Level.FINE, "Registering all serializable classes for network connection." );
            SerializerRegistrar.registerAllSerializable( );

            /* add listeners */
            logger.log( Level.FINE, "Registering message listeners with for network connection." );
            List< Object > messageListeners = PackageUtils.createAllInPackage( "trendli.me.makhana.gameserver.net.listener.master", this );
            for ( Object messageListener : messageListeners )
            {
                getClient( ).addMessageListener( ( MessageListener ) messageListener );
            }

            /* start client */
            logger.log( Level.FINE, "Starting the network connectionclient." );
            getClient( ).start( );
        }
        catch ( Exception e )
        {
            logger.log( Level.SEVERE, "Couldn't connect NetworkClient to server!", e );
            System.exit( 1 );
        }
    }
}
