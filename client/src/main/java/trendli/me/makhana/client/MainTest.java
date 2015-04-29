package trendli.me.makhana.client;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

/**
 * 
 * @author Ryan Grier
 *
 */
public class MainTest extends SimpleApplication implements ActionListener
{
    public static void main( String[ ] args )
    {
        MainTest app = new MainTest( );
        app.setShowSettings( false );
        AppSettings settings = new AppSettings( true );
        settings.setResolution( 1280, 720 );
        settings.setFrameRate( 60 );
        app.setSettings( settings );
        app.start( );

    }

    private BetterCharacterControl player;
    private BulletAppState bulletAppState;
    private Vector3f walkDirection = new Vector3f( );
    private boolean left = false, right = false, up = false, down = false;
    private boolean testPhysics = true;
    private boolean firstPerson = false;
    private boolean testNewFullMap = true;
    private CameraNode camNode;

    private Node playerNode;

    private void createMap( )
    {
        Node landNode = new Node( );
        setUpKeys( );
        // flyCam.setEnabled( true );
        flyCam.setMoveSpeed( 40f );

        Spatial dirt = ( Spatial ) Crumb.Dirt.getSpatial( assetManager );

        Spatial grass = ( Spatial ) Crumb.Grass.getSpatial( assetManager );

        Spatial road = ( Spatial ) Crumb.Road.getSpatial( assetManager );

        Spatial beach = ( Spatial ) Crumb.Beach_Concave_Grass.getSpatial( assetManager );

        Spatial water = ( Spatial ) Crumb.Water.getSpatial( assetManager );

        Spatial roadRamp = ( Spatial ) Crumb.Road_Slope.getSpatial( assetManager );

        Spatial grassRamp = ( Spatial ) Crumb.Grass_Slope.getSpatial( assetManager );

        Spatial grassWater = ( Spatial ) Crumb.Grass.getSpatial( assetManager ); // assetManager.loadModel(
                                                                                 // "Models/single-layer-grass-edge.obj"
                                                                                 // );
        // System.out.println(grassWater.getClass( ));
        Spatial office = ( Spatial ) Crumb.Grass.getSpatial( assetManager ); // assetManager.loadModel(
                                                                             // "Models/single-slice-office.obj"
                                                                             // );
        // System.out.println(office.getClass( ));
        Spatial bigTree = ( Spatial ) Crumb.Grass.getSpatial( assetManager ); // assetManager.loadModel(
                                                                              // "Models/large-tree.obj"
                                                                              // );
        // System.out.println(bigTree.getClass( ));
        Spatial smallTree = ( Spatial ) Crumb.Grass.getSpatial( assetManager ); // assetManager.loadModel(
                                                                                // "Models/small-tree.obj"
                                                                                // );
        // System.out.println(smallTree.getClass( ));
        Spatial temp;
        /* First layer */
        for ( int x = 0; x < 10; x++ )
        {
            for ( int y = 0; y < 10; y++ )
            {
                if ( x == 2 || x == 3 )
                {
                    if ( y == 8 || y == 9 )
                    {
                        temp = water.clone( );
                        temp.setLocalTranslation( x * 8, 0, y * 8 );
                        landNode.attachChild( temp );
                    }
                    else
                    {
                        temp = dirt.clone( );
                        temp.setLocalTranslation( x * 8, 0, y * 8 );
                        landNode.attachChild( temp );
                    }
                }
                else if ( x == 0 )
                {
                    temp = water.clone( );
                    temp.setLocalTranslation( x * 8, 0, y * 8 );
                    landNode.attachChild( temp );
                }
                else
                {
                    temp = dirt.clone( );
                    temp.setLocalTranslation( x * 8, 0, y * 8 );
                    landNode.attachChild( temp );
                }
            }
        }
        /* Second Layer */
        for ( int x = 0; x < 10; x++ )
        {
            for ( int y = 0; y < 10; y++ )
            {
                if ( x == 0 )
                {
                    temp = grassWater.clone( );
                    temp.setLocalTranslation( x * 8, 1, y * 8 );
                    temp.rotate( 0f, -1.57079633f, 0f );
                    landNode.attachChild( temp );
                }
                else if ( x == 2 || x == 3 )
                {
                    if ( y == 8 || y == 9 )
                    {
                        temp = beach.clone( );
                        temp.setLocalTranslation( x * 8, 1, y * 8 );
                        if ( x == 2 && y == 8 )
                        {
                            temp.rotate( 0f, 1.57079633f, 0f );
                        }
                        if ( x == 2 && y == 9 )
                        {
                            temp.rotate( 0f, 3.14159265f, 0f );
                        }
                        if ( x == 3 && y == 9 )
                        {
                            temp.rotate( 0f, 4.71238898f, 0f );
                        }
                        landNode.attachChild( temp );
                    }
                    else
                    {
                        temp = grass.clone( );
                        temp.setLocalTranslation( x * 8, 1, y * 8 );
                        landNode.attachChild( temp );
                    }
                }
                else
                {
                    temp = grass.clone( );
                    temp.setLocalTranslation( x * 8, 1, y * 8 );
                    landNode.attachChild( temp );
                }
            }
        }
        /* Thrid Layer */
        for ( int x = 0; x < 10; x++ )
        {
            for ( int y = 0; y < 10; y++ )
            {
                if ( y == 1 && x != 0 && x != 1 && x != 9 )
                {
                    temp = road.clone( );
                    temp.setLocalTranslation( x * 8, 2, y * 8 );
                    landNode.attachChild( temp );
                }
                else if ( x == 9 )
                {
                    temp = grassRamp.clone( );
                    temp.setLocalTranslation( x * 8, 2, y * 8 );
                    landNode.attachChild( temp );
                }
                else
                {
                    /*
                     * Spatial temp = air.clone( ); temp.setLocalTranslation( x
                     * * 8, 2, y * 8 ); map.add( ( Geometry ) temp );
                     * landNode.attachChild( temp );
                     */
                }
            }
        }
        /* road in thrid layer ramp */
        temp = roadRamp.clone( );
        temp.setLocalTranslation( 72f, 2, 8 );
        landNode.attachChild( temp );

        temp = office.clone( );
        temp.setLocalTranslation( 20f, 1f, 20f );
        landNode.attachChild( temp );

        temp = bigTree.clone( );
        temp.setLocalTranslation( 24f, 2f, 24f );
        landNode.attachChild( temp );

        temp = smallTree.clone( );
        temp.setLocalTranslation( 28f, 2f, 28f );
        landNode.attachChild( temp );

        rootNode.attachChild( landNode );

        /*
         * Mesh mesh = new Mesh( ); GeometryBatchFactory.mergeGeometries( map,
         * mesh ); rootNode.attachChild( landNode ); MeshCollisionShape shape =
         * new MeshCollisionShape( mesh ); RigidBodyControl landControl = new
         * RigidBodyControl( shape, 0 ); landNode.addControl( landControl );
         */
        CompoundCollisionShape landCollisionShape = ( CompoundCollisionShape ) CollisionShapeFactory.createMeshShape( landNode );
        RigidBodyControl landControl = new RigidBodyControl( landCollisionShape, 0 );
        landNode.addControl( landControl );

        bulletAppState.getPhysicsSpace( ).add( landNode );

        // bulletAppState.setDebugEnabled( true );

        playerNode = new Node( "playerNode" );

        player = new BetterCharacterControl( 0.3f, 1f, 1 );
        player.setJumpForce( new Vector3f( 0f, 5f, 0f ) );
        player.setGravity( new Vector3f( 0f, -10f, 0f ) );
        player.warp( new Vector3f( 8f, 25f, 8f ) );
        playerNode.addControl( player );

        /*
         * camNode = new CameraNode( "Camera Node", cam );
         * playerNode.attachChild( camNode ); camNode.setLocalTranslation( new
         * Vector3f( 0f, 5f, -5f ) ); camNode.lookAt(
         * playerNode.getLocalTranslation( ), Vector3f.UNIT_Y );
         */

        rootNode.attachChild( playerNode );

        bulletAppState.getPhysicsSpace( ).add( player );

        DirectionalLight sun = new DirectionalLight( );
        sun.setDirection( new Vector3f( -1, -4, -1 ).normalizeLocal( ) );
        sun.setColor( ColorRGBA.White.mult( 0.75f ) );
        rootNode.addLight( sun );

        AmbientLight al = new AmbientLight( );
        al.setColor( new ColorRGBA( 0.95f, 0.95f, 1f, 1f ) );
        rootNode.addLight( al );

        viewPort.setBackgroundColor( new ColorRGBA( 0.75f, 0.92f, 1f, 1f ) );

        // Texture west = assetManager.loadTexture( "Textures/dunes_left.jpg" );
        // Texture east = assetManager.loadTexture( "Textures/dunes_right.jpg"
        // );
        // Texture north = assetManager.loadTexture( "Textures/dunes_front.jpg"
        // );
        // Texture south = assetManager.loadTexture( "Textures/dunes_back.jpg"
        // );
        // Texture top = assetManager.loadTexture( "Textures/dunes_top.jpg" );
        // Texture bottom = assetManager.loadTexture(
        // "Textures/dunes_bottom.jpg" );

        // Spatial sky = SkyFactory.createSky( assetManager, east, west, south,
        // north, top, bottom, Vector3f.UNIT_XYZ );
        // rootNode.attachChild( sky );
    }

    public void onAction( String binding, boolean value, float tpf )
    {
        if ( binding.equals( "Left" ) )
        {
            if ( value )
            {
                left = true;
            }
            else
            {
                left = false;
            }
        }
        else if ( binding.equals( "Right" ) )
        {
            if ( value )
            {
                right = true;
            }
            else
            {
                right = false;
            }
        }
        else if ( binding.equals( "Up" ) )
        {
            if ( value )
            {
                up = true;
            }
            else
            {
                up = false;
            }
        }
        else if ( binding.equals( "Down" ) )
        {
            if ( value )
            {
                down = true;
            }
            else
            {
                down = false;
            }
        }
        else if ( binding.equals( "Jump" ) )
        {
            player.jump( );
        }
        else if ( binding.equals( "Warp" ) )
        {
            player.warp( new Vector3f( 45f, 25f, 45f ) );
        }
        else if ( binding.equals( "Crouch" ) )
        {
            if ( value )
            {
                player.setDucked( true );
            }
            else
            {
                player.setDucked( false );
            }
        }
    }

    private void setUpKeys( )
    {
        inputManager.addMapping( "Left", new KeyTrigger( KeyInput.KEY_A ) );
        inputManager.addMapping( "Right", new KeyTrigger( KeyInput.KEY_D ) );
        inputManager.addMapping( "Up", new KeyTrigger( KeyInput.KEY_W ) );
        inputManager.addMapping( "Down", new KeyTrigger( KeyInput.KEY_S ) );
        inputManager.addMapping( "Jump", new KeyTrigger( KeyInput.KEY_SPACE ) );
        inputManager.addMapping( "Warp", new KeyTrigger( KeyInput.KEY_Q ) );
        inputManager.addMapping( "Crouch", new KeyTrigger( KeyInput.KEY_LCONTROL ) );
        inputManager.addListener( this, "Left" );
        inputManager.addListener( this, "Right" );
        inputManager.addListener( this, "Up" );
        inputManager.addListener( this, "Down" );
        inputManager.addListener( this, "Jump" );
        inputManager.addListener( this, "Warp" );
        inputManager.addListener( this, "Crouch" );
    }

    @Override
    public void simpleInitApp( )
    {
        bulletAppState = new BulletAppState( );
        stateManager.attach( bulletAppState );
        if ( testNewFullMap )
        {
            createMap( );
        }

    }

    @Override
    public void simpleUpdate( float tpf )
    {
        if ( testPhysics )
        {
            Vector3f camDir = cam.getDirection( ).clone( ).multLocal( 2f );
            Vector3f camLeft = cam.getLeft( ).clone( ).multLocal( 2f );
            // TODO change from cam directed to playerNode directed?
            walkDirection.set( 0, 0, 0 );
            if ( left )
            {
                walkDirection.addLocal( camLeft );
            }
            if ( right )
            {
                walkDirection.addLocal( camLeft.negate( ) );
            }
            if ( up )
            {
                walkDirection.addLocal( camDir );
            }
            if ( down )
            {
                walkDirection.addLocal( new Vector3f( -camDir.x, 0, -camDir.z ) );
            }
            player.setWalkDirection( walkDirection );
            // player.warp(walkDirection);
            // cam.setLocation(player.getPhysicsLocation());
        }
    }
}