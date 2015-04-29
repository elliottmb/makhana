package trendli.me.makhana.client;

import java.nio.FloatBuffer;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;

public class Crumb
{
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

    public static Crumb Water = new Crumb( "Models/ground.obj", Flavor.BLUE_RASPBERRY, false );

    public static Crumb Dirt = new Crumb( "Models/ground.obj", Flavor.CHOCOLATE, false );

    public static Crumb Dirt_Slope = new Crumb( "Models/ground_slope.obj", Flavor.CHOCOLATE, false );

    public static Crumb Road = new Crumb( "Models/road.obj", Flavor.BLUE_RASPBERRY, false );

    public static Crumb Road_Slope = new Crumb( "Models/road_slope.obj", Flavor.BLUE_RASPBERRY, false );

    public static Crumb Grass = new Crumb( "Models/ground.obj", Flavor.LIME, false );

    public static Crumb Grass_Slope = new Crumb( "Models/ground_slope.obj", Flavor.LIME, false );

    public static Crumb Beach_Concave_Dirt = new Crumb( "Models/beach-concave.obj", Flavor.LEMON_CHOCOLATE, false );

    public static Crumb Beach_Concave_Grass = new Crumb( "Models/beach-concave.obj", Flavor.LEMON_LIME, false );

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
