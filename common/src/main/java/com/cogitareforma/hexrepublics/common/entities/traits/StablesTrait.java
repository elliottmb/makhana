package com.cogitareforma.hexrepublics.common.entities.traits;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;

@Serializable
public class StablesTrait extends StaticTrait
{
	// TODO
	private static Material material;

	/**
	 * Used by Serializer
	 */
	public StablesTrait( )
	{

	}

	@Override
	public Spatial getSpatial( AssetManager assetManager )
	{
		return assetManager.loadModel( "Materials/stablePrototype.obj" );
	}

	@Override
	public Material getMaterial( AssetManager assetManager )
	{
		// TODO
		return null;
	}
}
