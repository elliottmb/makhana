package com.cogitareforma.hexrepublics.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.net.ClientGameConnManager;
import com.cogitareforma.hexrepublics.client.net.ClientMasterConnManager;
import com.cogitareforma.hexrepublics.client.states.EntityManager;
import com.cogitareforma.hexrepublics.client.states.WorldManager;
import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.cogitareforma.hexrepublics.client.util.PlayerTraitListener;
import com.cogitareforma.makhana.common.entities.traits.PlayerTrait;
import com.cogitareforma.makhana.common.util.YamlConfig;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;

/**
 * The ClientMain is the core jMonkeyEngine application that represents a Game
 * Client. The actual client behavior is handled by Managers, AppStates, and so
 * on that are controlled by this application.
 * 
 * @author Elliott Butler
 * @author Ryan Grier
 */
public class ClientMain extends SimpleApplication
{
	public static void main( String[ ] args )
	{
		AppSettings settings = new AppSettings( true );

		ClientMain app = new ClientMain( );

		YamlConfig yamlConfig = YamlConfig.DEFAULT;

		if ( yamlConfig != null )
		{
			yamlConfig.buildAppSettings( settings );

			if ( yamlConfig.get( "client.input" ) == null )
			{
				yamlConfig.buildInputSettings( );
			}

			yamlConfig.save( );
		}
		app.setDisplayFps( false );
		app.setDisplayStatView( false );
		app.setSettings( settings );
		app.setShowSettings( false );
		app.start( );
	}

	/**
	 * The logger for this class
	 */
	private final static Logger logger = Logger.getLogger( ClientMain.class.getName( ) );

	/**
	 * The Client's Nifty Display instance.
	 */
	private NiftyJmeDisplay niftyDisplay;

	/**
	 * The Client's master connection manager.
	 */
	private ClientMasterConnManager masterConnection;

	/**
	 * The Client's game server connection manager.
	 */
	private ClientGameConnManager gameConnection;

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

	/**
	 * Determines if the Client is logged in and connected to the MasterServer.
	 * 
	 * @return true if Client is logged in and connected to the MasterServer,
	 *         else false.
	 */
	public boolean authenticated( )
	{
		if ( masterConnection != null )
		{
			return masterConnection.isLoggedIn( );
		}
		return false;
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
			gameConnection.connect( host, port );
			return null;
		} );
	}

	@Override
	public void destroy( )
	{
		masterConnection.close( );
		gameConnection.close( );
		super.destroy( );
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
	 * Returns Client's current game connection manager.
	 * 
	 * @return Client's game connection manager.
	 */
	public ClientGameConnManager getGameConnManager( )
	{
		return gameConnection;
	}

	/**
	 * Returns Client's current master connection manager.
	 * 
	 * @return Client's master connection manager.
	 */
	public ClientMasterConnManager getMasterConnManager( )
	{
		return masterConnection;
	}

	/**
	 * Returns the NiftyDisplay's Nifty instance
	 * 
	 * @return the current Nifty instance
	 */
	public Nifty getNifty( )
	{
		return getNiftyDisplay( ).getNifty( );
	}

	/**
	 * @return the niftyDisplay
	 */
	public NiftyJmeDisplay getNiftyDisplay( )
	{
		return niftyDisplay;
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
			if ( gameConnection.isConnected( ) )
			{
				gameConnection.sendMessage( message );
			}
			return null;
		} );
	}

	/**
	 * A thread safe method that sends a LoginRequest with the given accountname
	 * and password to the master server on the next available update loop.
	 * 
	 * 
	 * @param accountname
	 *            the given account name
	 * @param password
	 *            the given password
	 */
	public void sendLogin( final String accountname, final String password )
	{
		enqueue( ( ) ->
		{
			if ( masterConnection.isConnected( ) )
			{
				logger.log( Level.INFO, "Attempting to login with accountname " + accountname );
				masterConnection.sendLogin( accountname, password, false );
				logger.log( Level.INFO, "Account: " + masterConnection.getAccount( ) );
			}
			return null;
		} );
	}

	/**
	 * A thread safe method that sends a LogoutRequest to the master server on
	 * the next available update loop.
	 */
	public void sendLogout( )
	{
		enqueue( ( ) ->
		{
			if ( masterConnection.isConnected( ) )
			{
				logger.log( Level.INFO, "Attempting to logout" );
				masterConnection.sendLogout( );
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
			if ( masterConnection.isConnected( ) )
			{
				masterConnection.sendMessage( message );
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
	 * @param niftyDisplay
	 *            the niftyDisplay to set
	 */
	public void setNiftyDisplay( NiftyJmeDisplay niftyDisplay )
	{
		this.niftyDisplay = niftyDisplay;
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
		this.getContext( ).setTitle( "Hex Republics - Alpha" );
		YamlConfig yamlConfig = YamlConfig.DEFAULT;
		yamlConfig.buildAudioSettings( this.listener );

		startNifty( );

		flyCam.setEnabled( false );
		flyCam.setMoveSpeed( 50 );
		flyCam.setRotationSpeed( 15 );

		setEntityManager( new EntityManager( this ) );
		stateManager.attach( getEntityManager( ) );

		// TODO: Remove after done testing
		getEntityManager( ).addListener( new PlayerTraitListener( ), PlayerTrait.class );

		setWorldManager( new WorldManager( this, rootNode ) );
		stateManager.attach( worldManager );

		// Setup first view
		viewPort.setBackgroundColor( ColorRGBA.DarkGray );
		cam.setViewPort( 0f, 1f, 0f, 1f );

		// test multiview for gui
		guiViewPort.getCamera( ).setViewPort( 0f, 1f, 0f, 1f );

		masterConnection = new ClientMasterConnManager( this );
		gameConnection = new ClientGameConnManager( this );

		inputManager.addMapping( "showConsole", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.consoleKey" ) ) );
		inputManager.addMapping( "hideFPS", new KeyTrigger( KeyInput.KEY_F12 ) );
		consoleEnabled = ( Boolean ) yamlConfig.get( "client.input.console" );
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
		logger.log( Level.INFO, "Starting up Nifty" );
		guiNode.detachAllChildren( );
		guiNode.attachChild( fpsText );

		setNiftyDisplay( new NiftyJmeDisplay( assetManager, inputManager, audioRenderer, guiViewPort ) );
		guiViewPort.addProcessor( getNiftyDisplay( ) );

		getNifty( ).loadControlFile( "nifty-default-controls.xml" );
		getNifty( ).loadStyleFile( "Interface/Styles/nifty-custom-styles.xml" );

		NiftyFactory.createStartView( getNifty( ) );
		stateManager.attach( ( AppState ) getNifty( ).getScreen( "start" ).getScreenController( ) );
		getNifty( ).gotoScreen( "start" );

		NiftyFactory.createConsole( getNifty( ) );
		stateManager.attach( ( AppState ) getNifty( ).getScreen( "consoleScreen" ).getScreenController( ) );
		currentScreen = "start";
	}
}
