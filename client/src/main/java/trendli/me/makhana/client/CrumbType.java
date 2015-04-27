package trendli.me.makhana.client;

import java.nio.FloatBuffer;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;

public class CrumbType
{
    // Water
    public static CrumbType Water = new CrumbType( "Models/ground.obj", 0, 0, false );
    // Bark
    public static CrumbType Chocolate = new CrumbType( "Models/ground.obj", 0, 0, false );
    // Dirt
    public static CrumbType Dirt = new CrumbType( "Models/ground.obj", 1, 0, false );
    // Road
    public static CrumbType Licorce = new CrumbType( "Models/ground.obj", 0, 0, false );
    // Grass
    public static CrumbType Grass = new CrumbType( "Models/ground.obj", 2, 0, false );
    // Grass
    public static CrumbType Beach_Concave_Grass = new CrumbType( "Models/beach-concave.obj", 0, 0, false );
    // Sand
    public static CrumbType Lemon = new CrumbType( "Models/ground.obj", 0, 0, false );
    // Leaves
    public static CrumbType Mint = new CrumbType( "Models/ground.obj", 0, 0, false );

    private final String modelPath;
    private Spatial spatial;
    private final boolean isPartial;

    private final int offsetX;
    private final int offsetY;
    private static Texture palette;
    private static Material material;

    private CrumbType( String modelPath, int offsetX, int offsetY, boolean isPartial )
    {
        this.offsetX = offsetX;
        this.offsetY = offsetY;

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
        }
        if ( spatial == null )
        {
            spatial = assetManager.loadModel( modelPath ).deepClone( );

            spatial.setMaterial( material );

            Geometry geo = ( Geometry ) spatial;

            FloatBuffer textureBuffer = geo.getMesh( ).getFloatBuffer( Type.TexCoord );

            for ( int i = 0; i < textureBuffer.limit( ) / 2; i++ )
            {

                float x = textureBuffer.get( i * 2 + 0 );
                float y = textureBuffer.get( i * 2 + 1 );

                x += offsetX * 0.0625f;
                y -= offsetY * 0.0625f;

                textureBuffer.put( i * 2 + 0, x );
                textureBuffer.put( i * 2 + 1, y );
            }
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
