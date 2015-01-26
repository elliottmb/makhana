package trendli.me.makhana.common.net.msg;

import java.util.HashMap;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 * Unused for now, marked as Deprecated
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
@Deprecated
public class EntityUpdateRequest extends EntityRequest
{

	/**
	 * Used by Serializer
	 */
	public EntityUpdateRequest( )
	{
	}

	public EntityUpdateRequest( EntityId entityId, HashMap< EntityComponent, Boolean > changes )
	{
		/*
		 * TODO: Not fully set on using a HashMap. It may just be the best
		 * possible solution, as it allows us to send a message where we can
		 * both remove and add/update components. If a Component is paired with
		 * a true, it'll be added or replace an existing component. If it is
		 * paired with a false, it'll be removed.
		 */
	}
}
