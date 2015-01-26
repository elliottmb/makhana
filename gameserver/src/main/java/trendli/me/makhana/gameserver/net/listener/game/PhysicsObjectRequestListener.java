package trendli.me.makhana.gameserver.net.listener.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.net.msg.PhysicsObjectRequest;
import trendli.me.makhana.gameserver.net.GameServerManager;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class PhysicsObjectRequestListener implements MessageListener< HostedConnection >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( PhysicsObjectRequestListener.class.getName( ) );

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
	public PhysicsObjectRequestListener( GameServerManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		EntityData entityData = manager.getEntityData( );
		if ( message instanceof PhysicsObjectRequest )
		{
			manager.getApp( ).enqueue(
					( ) ->
					{
						PhysicsObjectRequest physicsMessage = ( PhysicsObjectRequest ) message;
						EntityId newEntity = entityData.createEntity( );

						logger.log( Level.INFO, "Creating entity at: " + physicsMessage.getPosition( ) );

						entityData.setComponent( newEntity, physicsMessage.getPosition( ) );

						Vector3f messageLocation = physicsMessage.getPosition( ).getLocation( );
						messageLocation = messageLocation.add( 0,
								manager.getTerrain( ).getHeight( new Vector2f( messageLocation.x, messageLocation.z ) ), 0 );
						PhysicsRigidBody prb = new PhysicsRigidBody( new SphereCollisionShape( 4f ), 15f );
						prb.setPhysicsLocation( messageLocation );
						prb.setLinearVelocity( physicsMessage.getPosition( ).getFacing( ).getRotationColumn( 2 ).mult( 20 ) );
						prb.setFriction( 1.6f );

						manager.getBulletAppState( ).getPhysicsSpace( ).add( prb );
						manager.getPhysicsObjects( ).put( newEntity, prb );
						prb.activate( );
						return null;
					} );

		}

	}
}
