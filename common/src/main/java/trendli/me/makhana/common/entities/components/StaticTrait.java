package trendli.me.makhana.common.entities.components;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public abstract class StaticTrait extends TypeTrait
{
	public Material getMaterial( AssetManager assetManager )
	{
		return null;
	}

	public Spatial getSpatial( AssetManager assetManager )
	{
		return new Geometry( "Box", new Box( 8, 8, 8 ) );
	}
}
