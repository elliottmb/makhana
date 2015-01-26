package trendli.me.makhana.common.net.msg;

import trendli.me.makhana.common.entities.components.LocationTrait;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class EntityCreationRequest extends EntityRequest
{
	private LocationTrait location;
	private EntityComponent[ ] components;

	/**
	 * Used by Serializer
	 */
	public EntityCreationRequest( )
	{

	}

	public EntityCreationRequest( LocationTrait location, EntityComponent... components )
	{
		this.location = location;
		this.components = components;
	}

	/**
	 * @return the components
	 */
	public EntityComponent[ ] getComponents( )
	{
		return components;
	}

	/**
	 * @return the location
	 */
	public LocationTrait getLocation( )
	{
		return location;
	}

	/**
	 * @param components
	 *            the components to set
	 */
	public void setComponents( EntityComponent[ ] components )
	{
		this.components = components;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation( LocationTrait location )
	{
		this.location = location;
	}
}
