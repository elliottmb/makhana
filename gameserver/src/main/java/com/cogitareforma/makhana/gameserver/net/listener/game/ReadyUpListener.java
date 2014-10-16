package com.cogitareforma.makhana.gameserver.net.listener.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Account;
import com.cogitareforma.makhana.common.entities.components.Player;
import com.cogitareforma.makhana.common.net.msg.ReadyUpRequest;
import com.cogitareforma.makhana.gameserver.net.GameServerManager;
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

			Account account = manager.getSessionManager( ).getFromSession( source );
			if ( account != null )
			{
				EntityId id = manager.getPlayerEntityId( account );
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
								Player newPt = new Player( pt.getAccount( ), pt.getKills( ), pt.getDeaths( ), readyUp.isReady( ) );
								entityData.setComponent( id, newPt );
							}
						}
					}
				}
			}
		}
	}
}
