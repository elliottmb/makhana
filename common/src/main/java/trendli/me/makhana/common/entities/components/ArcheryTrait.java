package trendli.me.makhana.common.entities.components;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;

@Serializable
public class ArcheryTrait extends StaticTrait
{
    // TODO
    // private static Material material;

    /**
     * Used by Serializer
     */
    public ArcheryTrait( )
    {

    }

    @Override
    public Material getMaterial( AssetManager assetManager )
    {
        // TODO
        return null;
    }

    @Override
    public Spatial getSpatial( AssetManager assetManager )
    {
        return assetManager.loadModel( "Materials/archery.obj" );
    }

}
