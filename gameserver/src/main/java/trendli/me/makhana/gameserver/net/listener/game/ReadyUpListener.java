package trendli.me.makhana.gameserver.net.listener.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.data.Session;
import trendli.me.makhana.common.entities.components.Player;
import trendli.me.makhana.common.net.msg.ReadyUpRequest;
import trendli.me.makhana.gameserver.net.GameServerManager;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 * 
 * @author Elliott Butler
 */
public class ReadyUpListener implements MessageListener< HostedConnection >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ReadyUpListener.class.getName( ) );

	/**
	 * The server manager
	 */
	private GameServerManager manager;

	/**
	 * The default constructor accepts the server's manager.
	 * 
	 * @param manager
	 *            the server manager
	 */
	public ReadyUpListener( GameServerManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		if ( message instanceof ReadyUpRequest )
		{
			logger.log( Level.INFO, "Received an ReadyUpRequest from " + source.getAddress( ) );
			ReadyUpRequest readyUp = ( ReadyUpRequest ) message;

			Session session = manager.getSessionManager( ).get( source );
			if ( session != null )
			{
				EntityId id = manager.getPlayerEntityId( session );
				if ( id != null )
				{
					EntityData entityData = manager.getEntityData( );
					if ( entityData != null )
					{
						Player pt = entityData.getComponent( id, Player.class );
						if ( pt != null )
						{
							if ( pt.isAlive( ) != readyUp.isReady( ) )
							{
								Player newPt = new Player( pt.getSession( ), pt.getKills( ), pt.getDeaths( ), readyUp.isReady( ) );
								entityData.setComponent( id, newPt );
							}
						}
					}
				}
			}
		}
	}
}
