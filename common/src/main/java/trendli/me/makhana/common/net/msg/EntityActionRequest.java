package trendli.me.makhana.common.net.msg;

import trendli.me.makhana.common.entities.components.ActionTrait;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityId;

@Serializable
public class EntityActionRequest extends EntityRequest
{

	private ActionTrait action;
	private EntityId entityId;

	/**
	 * Used by Serializer
	 */
	public EntityActionRequest( )
	{

	}

	public EntityActionRequest( EntityId id, ActionTrait action )
	{
		this.entityId = id;
		this.action = action;
	}

	/**
	 * @return the action
	 */
	public ActionTrait getAction( )
	{
		return action;
	}

	/**
	 * @return the id
	 */
	public EntityId getEntityId( )
	{
		return entityId;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction( ActionTrait action )
	{
		this.action = action;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setEntityId( EntityId id )
	{
		this.entityId = id;
	}

}
