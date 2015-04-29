package trendli.me.makhana.client;

import java.nio.FloatBuffer;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;

public enum Crumb
{

    WATER( "Models/ground.obj", Flavor.BLUE_RASPBERRY, false ),

    DIRT( "Models/ground.obj", Flavor.CHOCOLATE, false ),

    DIRT_SLOPE( "Models/ground_slope.obj", Flavor.CHOCOLATE, false ),

    ROAD( "Models/road.obj", Flavor.BLUE_RASPBERRY, false ),

    ROAD_SLOPE( "Models/road_slope.obj", Flavor.BLUE_RASPBERRY, false ),

    GRASS( "Models/ground.obj", Flavor.LIME, false ),

    GRASS_SLOPE( "Models/ground_slope.obj", Flavor.LIME, false ),

    GRASS_EDGE( "Models/ground_edge.obj", Flavor.LIME, false ),

    BEACH_CONCAVE_DIRT( "Models/beach-concave.obj", Flavor.LEMON_CHOCOLATE, false ),

    BEACH_CONCAVE_GRASS( "Models/beach-concave.obj", Flavor.LEMON_LIME, false ),

    TREE_PINE_LD( "Models/tree_pine.obj", Flavor.WINTERGREEN_DARK_ROAST, false ),

    TREE_PINE_LL( "Models/tree_pine.obj", Flavor.WINTERGREEN_LIGHT_ROAST, false ),

    TREE_PINE_DD( "Models/tree_pine.obj", Flavor.MINT_DARK_ROAST, false ),

    TREE_PINE_DL( "Models/tree_pine.obj", Flavor.MINT_LIGHT_ROAST, false ),

    TREE_PINE_ROUND_LD( "Models/tree_pine_round.obj", Flavor.WINTERGREEN_DARK_ROAST, false ),

    TREE_PINE_ROUND_LL( "Models/tree_pine_round.obj", Flavor.WINTERGREEN_LIGHT_ROAST, false ),

    TREE_PINE_ROUND_DD( "Models/tree_pine_round.obj", Flavor.MINT_DARK_ROAST, false ),

    TREE_PINE_ROUND_DL( "Models/tree_pine_round.obj", Flavor.MINT_LIGHT_ROAST, false ),

    TREE_PINE_ROUND_SMALL_LD( "Models/tree_pine_round_small.obj", Flavor.WINTERGREEN_DARK_ROAST, false ),

    TREE_PINE_ROUND_SMALL_LL( "Models/tree_pine_round_small.obj", Flavor.WINTERGREEN_LIGHT_ROAST, false ),

    TREE_PINE_ROUND_SMALL_DD( "Models/tree_pine_round_small.obj", Flavor.MINT_DARK_ROAST, false ),

    TREE_PINE_ROUND_SMALL_DL( "Models/tree_pine_round_small.obj", Flavor.MINT_LIGHT_ROAST, false ),

    TREE_SQUARE_LD( "Models/tree_square.obj", Flavor.WINTERGREEN_DARK_ROAST, false ),

    TREE_SQUARE_LL( "Models/tree_square.obj", Flavor.WINTERGREEN_LIGHT_ROAST, false ),

    TREE_SQUARE_DD( "Models/tree_square.obj", Flavor.MINT_DARK_ROAST, false ),

    TREE_SQUARE_DL( "Models/tree_square.obj", Flavor.MINT_LIGHT_ROAST, false );

    public static void updateTextureCoordinates( Geometry geo, Flavor flavor )
    {
        FloatBuffer textureBuffer = geo.getMesh( ).getFloatBuffer( Type.TexCoord );

        for ( int i = 0; i < textureBuffer.limit( ) / 2; i++ )
        {

            float x = textureBuffer.get( i * 2 + 0 );
            float y = textureBuffer.get( i * 2 + 1 );

            x += flavor.getOffsetX( ) * 0.0625f;
            y -= flavor.getOffsetY( ) * 0.0625f;

            textureBuffer.put( i * 2 + 0, x );
            textureBuffer.put( i * 2 + 1, y );
        }
    }

    private final String modelPath;
    private Spatial spatial;

    private final boolean isPartial;
    private final Flavor flavor;
    private static Texture palette;

    private static Material material;

    private Crumb( Crumb other, Flavor newFlavor )
    {
        this.flavor = newFlavor;
        this.modelPath = other.modelPath;
        this.isPartial = other.isPartial;
        if ( other.spatial != null )
        {
            spatial = other.spatial.deepClone( );
        }
    }

    private Crumb( String modelPath, Flavor flavor, boolean isPartial )
    {
        this.flavor = flavor;

        this.modelPath = modelPath;
        this.isPartial = isPartial;
    }

    public Spatial getSpatial( AssetManager assetManager )
    {
        if ( palette == null )
        {
            palette = assetManager.loadTexture( "Textures/palette.png" );
        }
        if ( material == null )
        {
            material = new Material( assetManager, "Common/MatDefs/Light/Lighting.j3md" );

            material.setTexture( "DiffuseMap", palette );

            // material.setBoolean( "UseMaterialColors", true );
            // material.setColor( "Ambient", ColorRGBA.White.mult( 0.3f ) );
            // material.setColor( "Diffuse", ColorRGBA.White );
            // material.setColor( "Specular", ColorRGBA.White );
            // material.setFloat( "Shininess", 64f ); // [0,128]

        }
        if ( spatial == null )
        {
            spatial = assetManager.loadModel( modelPath ).deepClone( );

            spatial.setMaterial( material );

            updateTextureCoordinates( ( Geometry ) spatial, flavor );

            spatial.setShadowMode( ShadowMode.CastAndReceive );
        }

        return spatial;
    }

    /**
     * @return the isPartial
     */
    public boolean isPartial( )
    {
        return isPartial;
    }

}
