package com.cogitareforma.makhana.common.net.msg;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityId;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class EntityDeletionRequest extends EntityRequest
{
	private EntityId entityId;

	/**
	 * Used by Serializer
	 */
	public EntityDeletionRequest( )
	{

	}

	public EntityDeletionRequest( EntityId entityId )
	{
		this.entityId = entityId;
	}

	/**
	 * @return the entityId
	 */
	public EntityId getEntityId( )
	{
		return entityId;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId( EntityId entityId )
	{
		this.entityId = entityId;
	}

}
