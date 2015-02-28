package trendli.me.makhana.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.net.GameConnectionManager;
import trendli.me.makhana.client.net.MasterConnectionManager;
import trendli.me.makhana.client.states.EntityManager;
import trendli.me.makhana.client.states.WorldManager;
import trendli.me.makhana.client.util.NiftyFactory;
import trendli.me.makhana.client.util.PlayerTraitListener;
import trendli.me.makhana.common.entities.components.Player;

import com.jme3.app.state.AppState;
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
public class ClientMain extends OnlineClient
{
    public static void main( String[ ] args )
    {
        ClientMain app = new ClientMain( );
        app.start( );
    }

    /**
     * The logger for this class
     */
    private final static Logger logger = Logger.getLogger( ClientMain.class.getName( ) );

    /**
     * The Client's world manager.
     */
    private WorldManager worldManager;

    private EntityManager entityManager;

    /**
     * Name of screen that should be returned to after closing the console.
     */
    public String currentScreen;

    /**
     * Boolean to control if console is enabled. Set by YamlConfig.
     */
    public boolean consoleEnabled = false;

    private ActionListener baseActionListener = ( String name, boolean keyPressed, float tpf ) ->
    {
        if ( name.equals( "showConsole" ) && !keyPressed && consoleEnabled )
        {
            if ( "consoleScreen".equalsIgnoreCase( getNifty( ).getCurrentScreen( ).getScreenId( ) ) )
            {
                getNifty( ).gotoScreen( currentScreen );
            }
            else
            {
                currentScreen = getNifty( ).getCurrentScreen( ).getScreenId( );
                getNifty( ).gotoScreen( "consoleScreen" );
            }
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

    public ClientMain( )
    {
        super( );
    }

    /**
     * Thread safe method that tries to connect to the given game sever.
     * 
     * @param host
     *            Name of game server to connect to.
     * @param port
     *            Port number of game server to connect to.
     */
    public void connectToGameSever( final String host, final Integer port )
    {
        enqueue( ( ) ->
        {
            logger.log( Level.INFO, "Sending Game Server details to Connection Manager ( " + host + ":" + port + " )" );
            getGameConnectionManager( ).connect( host, port );
            return null;
        } );
    }

    /**
     * @return the baseActionListener
     */
    public ActionListener getBaseActionListener( )
    {
        return baseActionListener;
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager( )
    {
        return entityManager;
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
     * A thread safe method that sends a NetworkChatMessage with the given
     * String to the game server on the next available update loop.
     * 
     * @param message
     *            the text message to send to the game server
     */
    public void sendLobbyChat( final String message )
    {
        enqueue( ( ) ->
        {
            if ( getGameConnectionManager( ).isConnected( ) )
            {
                getGameConnectionManager( ).sendMessage( message );
            }
            return null;
        } );
    }

    /**
     * A thread safe method that sends a NetworkChatMessage with the given
     * String to the master server on the next available update loop.
     * 
     * @param message
     *            the text message to send to the master server
     */
    public void sendNetworkChat( final String message )
    {
        enqueue( ( ) ->
        {
            if ( getMasterConnectionManager( ) != null )
            {
                if ( getMasterConnectionManager( ).isConnected( ) )
                {
                    getMasterConnectionManager( ).sendMessage( message );
                }
            }
            return null;
        } );
    }

    /**
     * @param entityManager
     *            the entityManager to set
     */
    private void setEntityManager( EntityManager entityManager )
    {
        this.entityManager = entityManager;
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

        setEntityManager( new EntityManager( this ) );
        stateManager.attach( getEntityManager( ) );

        // TODO: Remove after done testing
        getEntityManager( ).addListener( new PlayerTraitListener( ), Player.class );

        setWorldManager( new WorldManager( this, rootNode ) );
        stateManager.attach( worldManager );

        // Setup first view
        viewPort.setBackgroundColor( ColorRGBA.DarkGray );
        cam.setViewPort( 0f, 1f, 0f, 1f );

        // test multiview for gui
        guiViewPort.getCamera( ).setViewPort( 0f, 1f, 0f, 1f );

        setMasterConnectionManager( new MasterConnectionManager( this ) );
        setGameConnectionManager( new GameConnectionManager( this ) );

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
        NiftyFactory.createStartView( getNifty( ) );
        stateManager.attach( ( AppState ) getNifty( ).getScreen( "start" ).getScreenController( ) );
        getNifty( ).gotoScreen( "start" );

        NiftyFactory.createConsole( getNifty( ) );
        stateManager.attach( ( AppState ) getNifty( ).getScreen( "consoleScreen" ).getScreenController( ) );
        currentScreen = "start";
    }

}
