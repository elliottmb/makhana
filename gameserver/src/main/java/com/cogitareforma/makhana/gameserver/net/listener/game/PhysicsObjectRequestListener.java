package com.cogitareforma.makhana.gameserver.net.listener.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.net.msg.PhysicsObjectRequest;
import com.cogitareforma.makhana.gameserver.net.GameServerManager;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
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
			PhysicsObjectRequest physicsMessage = ( PhysicsObjectRequest ) message;
			EntityId newEntity = entityData.createEntity( );

			logger.log( Level.INFO, "Creating entity at: " + physicsMessage.getPosition( ) );

			entityData.setComponent( newEntity, physicsMessage.getPosition( ) );

			PhysicsRigidBody prb = new PhysicsRigidBody( new SphereCollisionShape( 4f ), 1.5f );
			prb.setPhysicsLocation( physicsMessage.getPosition( ).getLocation( ) );
			prb.setLinearVelocity( physicsMessage.getPosition( ).getFacing( ).getRotationColumn( 2 ).mult( 3 ) );

			manager.getBulletAppState( ).getPhysicsSpace( ).add( prb );
			manager.getPhysicsObjects( ).put( newEntity, prb );
			prb.activate( );
		}

	}
}
