package com.cogitareforma.hexrepublics.common.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HillHeightMap;
import com.jme3.terrain.util.HeightBasedAlphaMapGenerator;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.util.BufferUtils;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class WorldFactory
{
	private final static Logger logger = Logger.getLogger( WorldFactory.class.getName( ) );

	public static void attachHexagonGridToNode( Node root, AssetManager am )
	{
		if ( root != null )
		{
			Node hexagonRoot = new Node( "hexagonRoot" );
			TerrainQuad terrain = ( TerrainQuad ) root.getChild( "terrain" );
			if ( terrain != null )
			{
				// TODO: Eventually parameterize
				int terrainSize = terrain.getTerrainSize( );
				// TODO: Eventually parameterize
				float size = ( terrainSize - 1 ) / 256 * 10f;

				Material hexMat = buildHexagonMaterial( am );

				for ( int i = 1; i < FastMath.floor( terrainSize / ( size * 6 / 4 ) ); i++ )
				{
					for ( int j = 1; j < FastMath.ceil( terrainSize / ( FastMath.sqrt( 3f ) * size ) ); j++ )
					{
						Geometry lineGeo = new Geometry( String.format( "hexagon[%d, %d]", i, j ), createHexagon(
								createCenterPoint( terrainSize, size, i, j ), size, terrain, ( terrainSize - 1 ) / 256 * 0.5f ) );
						lineGeo.setMaterial( hexMat );
						lineGeo.setQueueBucket( RenderQueue.Bucket.Transparent );

						hexagonRoot.attachChild( lineGeo );
					}
				}
				root.attachChild( hexagonRoot );
			}
		}
	}

	public static void attachLighting( Node root, ViewPort vp, AssetManager am )
	{
		AmbientLight al = new AmbientLight( );
		al.setColor( ColorRGBA.White.mult( 0.23f ) );
		root.addLight( al );

		DirectionalLight dl = new DirectionalLight( );
		dl.setColor( ColorRGBA.White.mult( 0.75f ) );
		dl.setDirection( new Vector3f( 2, -4, 4 ) );
		root.addLight( dl );

		final int SHADOWMAP_SIZE = 1024;
		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer( am, SHADOWMAP_SIZE, 3 );
		dlsr.setLight( dl );
		dlsr.setLambda( 0.75f );
		dlsr.setShadowIntensity( 0.6f );
		dlsr.setEdgeFilteringMode( EdgeFilteringMode.PCFPOISSON );
		dlsr.setShadowCompareMode( CompareMode.Hardware );
		dlsr.setEdgesThickness( 4 );
		dlsr.setEnabledStabilization( true );
		vp.addProcessor( dlsr );

		DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter( am, SHADOWMAP_SIZE, 3 );
		dlsf.setLight( dl );
		dlsf.setEnabled( false );
		dlsf.setLambda( 0.75f );
		dlsf.setShadowIntensity( 0.6f );
		dlsf.setEdgeFilteringMode( EdgeFilteringMode.PCFPOISSON );
		dlsf.setShadowCompareMode( CompareMode.Hardware );
		dlsf.setEdgesThickness( 4 );
		dlsf.setEnabledStabilization( true );
		FilterPostProcessor fpp = new FilterPostProcessor( am );
		fpp.addFilter( dlsf );
		vp.addProcessor( fpp );
	}

	public static void attachTerrainToNode( Camera lodCamera, AssetManager am, Node node, byte seed )
	{
		AbstractHeightMap heightMap = WorldFactory.buildHeightMap( seed );
		TerrainQuad terrain = WorldFactory.buildTerrain( heightMap, lodCamera );
		Material terrainMaterial = WorldFactory.buildTerrainMaterial( am );
		terrainMaterial.setTexture( "AlphaMap", WorldFactory.getHeightAlphaMap( heightMap ) );
		terrain.setMaterial( terrainMaterial );
		node.attachChild( terrain );
	}

	private static AbstractHeightMap buildHeightMap( byte seed )
	{
		AbstractHeightMap heightMap = null;
		logger.log( Level.INFO, "Attempting to generate heightmap with seed: " + seed );

		try
		{
			heightMap = new HillHeightMap( 1025, 4096, 16, 64, seed );
			heightMap.erodeTerrain( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Failed to generate heightmap with seed: " + seed, e );
		}

		return heightMap;
	}

	private static Material buildHexagonMaterial( AssetManager am )
	{
		Material hexagonMaterial = new Material( am, "Common/MatDefs/Misc/Unshaded.j3md" );
		hexagonMaterial.setColor( "Color", new ColorRGBA( 0f, 0f, 0f, 0.3f ) );
		hexagonMaterial.getAdditionalRenderState( ).setBlendMode( RenderState.BlendMode.Alpha );
		return hexagonMaterial;
	}

	private static TerrainQuad buildTerrain( AbstractHeightMap heightMap, Camera lodCamera )
	{

		logger.log( Level.INFO, "Constructing the terrain mesh" );
		TerrainQuad terrain = null;
		int patchSize = 65;
		terrain = new TerrainQuad( "terrain", patchSize, 1025, heightMap.getHeightMap( ) );

		terrain.setLocalTranslation( 0, -3, 0 );
		terrain.setLocalScale( 1f, 0.125f, 1f );
		terrain.setShadowMode( RenderQueue.ShadowMode.Receive );

		logger.log( Level.INFO, "Constructing the terrain LOD" );
		TerrainLodControl control = new TerrainLodControl( terrain, lodCamera );
		terrain.addControl( control );

		return terrain;
	}

	private static Material buildTerrainMaterial( AssetManager am )
	{
		Texture grass = am.loadTexture( "Textures/grass.jpg" );
		grass.setWrap( Texture.WrapMode.Repeat );
		Texture grassNormal = am.loadTexture( "Textures/grass_normal.jpg" );
		grassNormal.setWrap( Texture.WrapMode.Repeat );
		Texture dirt = am.loadTexture( "Textures/dirt.png" );
		dirt.setWrap( Texture.WrapMode.Repeat );
		Texture snow = am.loadTexture( "Textures/snow.jpg" );
		snow.setWrap( Texture.WrapMode.Repeat );

		Material matTerrain = new Material( am, "Common/MatDefs/Terrain/TerrainLighting.j3md" );
		matTerrain.setBoolean( "useTriPlanarMapping", false );
		matTerrain.setBoolean( "WardIso", true );
		matTerrain.setFloat( "Shininess", 0 );

		// GRASS texture
		matTerrain.setTexture( "DiffuseMap", grass );
		matTerrain.setFloat( "DiffuseMap_0_scale", 64 );

		// DIRT texture
		matTerrain.setTexture( "DiffuseMap_1", dirt );
		matTerrain.setFloat( "DiffuseMap_1_scale", 16 );

		// ROCK texture
		matTerrain.setTexture( "DiffuseMap_2", snow );
		matTerrain.setFloat( "DiffuseMap_2_scale", 128 );

		return matTerrain;
	}

	public static Vector3f createCenterPoint( int worldSize, float hexSize, int i, int j )
	{
		float x = worldSize / 2;
		float y = 0;
		float z = worldSize / 2;
		if ( i % 2 == 0 )
		{
			x = x - ( i * hexSize * 3 / 2 );
		}
		else
		{
			x = x - ( i * hexSize * 6 / 4 );
			z = z - ( FastMath.sqrt( 3f ) / 2 * hexSize );
		}
		z += ( FastMath.sqrt( 3f ) / 2 * hexSize );
		z = z - ( j * hexSize * FastMath.sqrt( 3f ) );

		return new Vector3f( x, y, z );
	}

	private static Mesh createHexagon( Vector3f centerPoint, float size, TerrainQuad terrain, float heightAdjustment )
	{
		Vector3f[ ] vertices = new Vector3f[ 6 ];
		for ( int i = 0; i < 6; i++ )
		{
			float angle = 2 * FastMath.PI / 6 * i;
			float x = centerPoint.x + size * FastMath.cos( angle );
			float z = centerPoint.z + size * FastMath.sin( angle );
			float y = 3;
			if ( !Float.isNaN( terrain.getHeight( new Vector2f( x, z ) ) ) )
			{
				y = terrain.getHeight( new Vector2f( x, z ) ) + heightAdjustment;
			}

			vertices[ i ] = new Vector3f( x, y, z );
		}
		Mesh lineMesh = new Mesh( );
		lineMesh.setMode( Mesh.Mode.Lines );
		lineMesh.setBuffer( VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer( vertices ) );
		lineMesh.setBuffer( VertexBuffer.Type.Index, 2, new short[ ]
		{
				0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 0
		} );
		lineMesh.setLineWidth( 3.0f );
		lineMesh.updateBound( );
		lineMesh.updateCounts( );
		return lineMesh;
	}

	private static Texture2D getHeightAlphaMap( AbstractHeightMap heightMap )
	{
		HeightBasedAlphaMapGenerator hbamg = new HeightBasedAlphaMapGenerator( heightMap );

		return new Texture2D( hbamg.renderAlphaMap( ) );
	}

}
