package trendli.me.makhana.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.states.WorldManager;
import trendli.me.makhana.client.ui.StartViewController;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial.CullHint;

/**
 * The ClientMain is the core jMonkeyEngine application that represents a Game
 * Client. The actual client behavior is handled by Managers, AppStates, and so
 * on that are controlled by this application.
 * 
 * @author Elliott Butler
 * @author Ryan Grier
 */
public class NiftyReboot extends OfflineClient
{
    public static void main( String[ ] args )
    {
        NiftyReboot app = new NiftyReboot( );
        app.start( );
    }

    /**
     * The logger for this class
     */
    private final static Logger logger = Logger.getLogger( NiftyReboot.class.getName( ) );

    /**
     * Boolean to control if console is enabled. Set by YamlConfig.
     */
    public boolean consoleEnabled = false;

    /**
     * The Client's world manager.
     */
    private WorldManager worldManager;

    private ActionListener baseActionListener = ( String name, boolean keyPressed, float tpf ) ->
    {
        if ( name.equals( "showConsole" ) && !keyPressed && consoleEnabled )
        {
            // TODO: REDO
        }
        if ( name.equals( "hideFPS" ) && !keyPressed )
        {
            if ( fpsText.getCullHint( ) == CullHint.Never )
            {
                setDisplayFps( false );
                setDisplayStatView( false );
            }
            else
            {
                setDisplayFps( true );
                setDisplayStatView( true );
            }
        }
    };

    public NiftyReboot( )
    {
        super( );
    }

    /**
     * @return the baseActionListener
     */
    public ActionListener getBaseActionListener( )
    {
        return baseActionListener;
    }

    /**
     * @return the worldManager
     */
    public WorldManager getWorldManager( )
    {
        return worldManager;
    }

    /**
     * Thread safe method that loads the games world by either the given seed or
     * a random one.
     * 
     * @param seed
     *            Seed to be used to generate the world. Can be null.
     */
    public void loadWorld( final Byte seed )
    {
        logger.log( Level.INFO, "ClientMain loadWorld: " + seed );
        if ( seed == null )
        {
            enqueue( ( ) ->
            {

                worldManager.closeLevel( );
                return null;

            } );
            return;
        }
        new Thread( ( ) ->
        {
            try
            {
                worldManager.loadLevel( seed );

                enqueue( ( ) ->
                {

                    worldManager.attachLevel( );
                    return null;

                } ).get( );
            }
            catch ( Exception e )
            {
                e.printStackTrace( );
            }
        } ).start( );
    }

    /**
     * @param worldManager
     *            the worldManager to set
     */
    private void setWorldManager( WorldManager worldManager )
    {
        this.worldManager = worldManager;
    }

    @Override
    public void simpleInitApp( )
    {
        super.simpleInitApp( );

        this.getContext( ).setTitle( "Makhana - Alpha" );

        getConfiguration( ).configureAudioSettings( this.listener );

        startNifty( );

        flyCam.setEnabled( false );
        flyCam.setMoveSpeed( 50 );
        flyCam.setRotationSpeed( 15 );

        // setWorldManager( new WorldManager( this, rootNode ) );
        // stateManager.attach( worldManager );

        // Setup first view
        viewPort.setBackgroundColor( ColorRGBA.DarkGray );
        cam.setViewPort( 0f, 1f, 0f, 1f );

        // test multiview for gui
        guiViewPort.getCamera( ).setViewPort( 0f, 1f, 0f, 1f );

        inputManager.addMapping( "showConsole", new KeyTrigger( ( Integer ) getConfiguration( ).get( "client.input.consoleKey" ) ) );
        inputManager.addMapping( "hideFPS", new KeyTrigger( KeyInput.KEY_F12 ) );
        consoleEnabled = ( Boolean ) getConfiguration( ).get( "client.input.console" );
        inputManager.addListener( getBaseActionListener( ), "showConsole", "hideFPS" );
    }

    @Override
    public void simpleRender( RenderManager rm )
    {
        // Unused currently
    }

    @Override
    public void simpleUpdate( float tpf )
    {
        // Unused currently
    }

    /**
     * A method to initialize the NiftyDisplay instance and load our main screen
     * and console screen.
     */
    private void startNifty( )
    {

        // NiftyFactory.createStartView( getNifty( ) );
        // stateManager.attach( ( AppState ) getNifty( ).getScreen( "start"
        // ).getScreenController( ) );
        // stateManager.attach( new StartViewController( getNifty( ) ) );
        new StartViewController( this );
        getNifty( ).gotoScreen( "start" );
    }

}
