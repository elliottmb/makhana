package com.cogitareforma.makhana.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityId;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class EntityResponse extends AbstractMessage
{
	private String requestType;
	private EntityId entityId;
	private boolean successful;

	/**
	 * Used by Serializer
	 */
	public EntityResponse( )
	{

	}

	public EntityResponse( EntityId entityId, String requestType, boolean successful )
	{
		this.entityId = entityId;
		this.requestType = requestType;
		this.successful = successful;
	}

	/**
	 * @return the entityId
	 */
	public EntityId getEntityId( )
	{
		return entityId;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType( )
	{
		return requestType;
	}

	/**
	 * @return the successful
	 */
	public boolean isSuccessful( )
	{
		return successful;
	}

	/**
	 * @param entityId
	 *            the entityId to set
	 */
	public void setEntityId( EntityId entityId )
	{
		this.entityId = entityId;
	}

	/**
	 * @param requestType
	 *            the requestType to set
	 */
	public void setRequestType( String requestType )
	{
		this.requestType = requestType;
	}

	/**
	 * @param successful
	 *            the successful to set
	 */
	public void setSuccessful( boolean successful )
	{
		this.successful = successful;
	}

}
