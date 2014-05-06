package com.cogitareforma.hexrepublics.client.views;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.client.DebugGlobals;
import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.cogitareforma.hexrepublics.common.entities.Traits;
import com.cogitareforma.hexrepublics.common.entities.traits.HealthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.util.WorldFactory;
import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.Name;
import com.simsilica.es.client.RemoteEntityData;
import com.simsilica.es.filter.AndFilter;
import com.simsilica.es.filter.FieldFilter;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * 
 * @author Ryan Grier
 * @author Elliott Butler
 */
public class HudViewController extends AbstractAppState implements ScreenController, KeyInputHandler
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( HudViewController.class.getName( ) );

	private ClientMain app;
	private boolean scoreOpen = false;
	private boolean chatOpen = false;
	private boolean menuOpen = false;
	private int scoreKey;
	private int chatKey;
	private Pair< Integer, Integer > currentTile;
	private Element scoreboard;
	private Set< EntityId > players;
	private HashMap< EntityId, Integer > unitCount;
	private HashMap< EntityId, Integer > buildingCount;
	private HashMap< EntityId, Integer > tileCount;
	/**
	 * Array representation of all game tiles.
	 */
	private Vector3f[ ][ ] tileCoords;

	/**
	 * Controls keyboard input to open different views. Currently there are:
	 * tab: open/close score board esc: open/close menu tilde: open/close
	 * console F12: show/hide FPS and stats t: show/hide in-game chat
	 */
	private ActionListener actionListener = ( String name, boolean keyPressed, float tpf ) ->
	{
		if ( !"consoleScreen".equalsIgnoreCase( app.getNifty( ).getCurrentScreen( ).getScreenId( ) ) )
		{
			if ( name.equals( "showScore" ) && !keyPressed )
			{
				if ( scoreOpen == false && isOpen( ) )
				{
					app.getNifty( ).showPopup( app.getNifty( ).getCurrentScreen( ), "scoreboardPopup", null );
					scoreOpen = true;
					setupScoreBoard( );
				}
				else
				{
					if ( scoreOpen == true )
					{
						app.getNifty( ).closePopup( "scoreboardPopup" );
						scoreOpen = false;
					}
				}
			}
			if ( name.equals( "showChat" ) && !keyPressed )
			{
				if ( chatOpen == false && isOpen( ) )
				{
					app.getNifty( ).showPopup( app.getNifty( ).getCurrentScreen( ), "ingameChat",
							app.getNifty( ).findPopupByName( "ingameChat" ).findElementByName( "gameChat#chat-text-input" ) );
					chatOpen = true;
				}
				else
				{
					if ( chatOpen == true )
					{
						app.getNifty( ).closePopup( "ingameChat" );
						chatOpen = false;
					}
				}
			}
			if ( name.equals( "showMenu" ) && !keyPressed )
			{
				if ( "tile".equalsIgnoreCase( app.getNifty( ).getCurrentScreen( ).getScreenId( ) ) && isOpen( ) )
				{
					TileViewController tvc = ( TileViewController ) app.getNifty( ).getCurrentScreen( ).getScreenController( );
					tvc.exitView( );
				}
				else
				{
					if ( menuOpen == false && isOpen( ) )
					{
						app.getNifty( ).showPopup( app.getNifty( ).getCurrentScreen( ), "menu", null );
						menuOpen = true;
					}
					else
					{
						if ( menuOpen == true )
						{
							app.getNifty( ).closePopup( "menu" );
							menuOpen = false;
						}
					}
				}
			}
		}
	};

	private AnalogListener analogListener = ( String name, float value, float tpf ) ->
	{
		if ( name.equals( "mouseClick" ) )
		{
			// Reset results list.
			CollisionResults results = new CollisionResults( );
			// Convert screen click to 3d position
			Vector2f click2d = app.getInputManager( ).getCursorPosition( );
			Vector3f click3d = app.getCamera( ).getWorldCoordinates( new Vector2f( click2d.x, click2d.y ), 0f ).clone( );
			Vector3f dir = app.getCamera( ).getWorldCoordinates( new Vector2f( click2d.x, click2d.y ), 1f ).subtractLocal( click3d )
					.normalizeLocal( );
			// Aim the ray from the clicked spot forwards.
			Ray ray = new Ray( click3d, dir );
			// Collect intersections between ray and all nodes in results
			// list.
			app.getRootNode( ).collideWith( ray, results );
			// (Print the results so we see what is going on:)
			if ( DebugGlobals.DEBUG_OBJECT_OUTPUT )
			{
				for ( int i = 0; i < results.size( ); i++ )
				{
					// (For each, we know distance, impact point, geometry.)
					float dist = results.getCollision( i ).getDistance( );
					Vector3f pt = results.getCollision( i ).getContactPoint( );
					String target = results.getCollision( i ).getGeometry( ).getName( );
					System.out.println( "Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away." );

					// Print out the colliding tile location
					// System.out.println( insideTile( new Vector3f( pt.x,
					// 0, pt.z ) ) );
					printTileInfo( insideTile( new Vector3f( pt.x, 0, pt.z ) ) );
					goToTile( insideTile( new Vector3f( pt.x, 0, pt.z ) ) );
				}
			}
			// Use the results -- we rotate the selected geometry.
			if ( results.size( ) > 0 )
			{
				// The closest result is the target that the player picked:
				Geometry target = results.getClosestCollision( ).getGeometry( );
				// Here comes the action:
				if ( target.getName( ).equals( "Box" ) )
				{
					target.rotate( 0, -value, 0 );
				}
				if ( target.getName( ).equals( "Sphere" ) )
				{
					target.rotate( 0, value, 0 );
				}
				if ( target.getName( ).equals( "Torus" ) )
				{
					target.rotate( value, value, value );
				}
			}
		}
		int theSpeed = 20;
		Vector3f v = app.getCamera( ).getLocation( );
		ViewPort miniview = app.getRenderManager( ).getMainView( "Minimap" );
		Camera minimap = null;
		Vector3f minimapLocation = null;
		if ( miniview != null )
		{
			minimap = miniview.getCamera( );
			minimapLocation = minimap.getLocation( );
		}

		if ( name.equals( "moveNorth" ) )
		{
			app.getCamera( ).setLocation( new Vector3f( v.x, v.y, v.z - value * theSpeed ) );
			if ( minimap != null && minimapLocation != null )
			{
				minimap.setLocation( new Vector3f( minimapLocation.x, minimapLocation.y, minimapLocation.z - value * theSpeed ) );
			}
		}
		if ( name.equals( "moveWest" ) )
		{
			app.getCamera( ).setLocation( new Vector3f( v.x - value * theSpeed, v.y, v.z ) );
			if ( minimap != null && minimapLocation != null )
			{
				minimap.setLocation( new Vector3f( minimapLocation.x - value * theSpeed, minimapLocation.y, minimapLocation.z ) );
			}
		}
		if ( name.equals( "moveEast" ) )
		{
			app.getCamera( ).setLocation( new Vector3f( v.x + value * theSpeed, v.y, v.z ) );
			if ( minimap != null && minimapLocation != null )
			{
				minimap.setLocation( new Vector3f( minimapLocation.x + value * theSpeed, minimapLocation.y, minimapLocation.z ) );
			}
		}
		if ( name.equals( "moveSouth" ) )
		{
			app.getCamera( ).setLocation( new Vector3f( v.x, v.y, v.z + value * theSpeed ) );
			if ( minimap != null && minimapLocation != null )
			{
				minimap.setLocation( new Vector3f( minimapLocation.x, minimapLocation.y, minimapLocation.z + value * theSpeed ) );
			}
		}
		if ( name.equals( "zoomIn" ) )
		{
			if ( v.y - value > 50f )
			{
				app.getCamera( ).setLocation( new Vector3f( v.x, v.y - value, v.z - value ) );
			}
		}
		if ( name.equals( "zoomOut" ) )
		{
			if ( v.y + value <= 100f )
			{
				app.getCamera( ).setLocation( new Vector3f( v.x, v.y + value, v.z + value ) );
			}
		}

	};

	public Pair< Integer, Integer > insideTile( Vector3f coord )
	{
		for ( int i = 0; i < tileCoords.length; i++ )
		{
			for ( int j = 0; j < tileCoords[ i ].length; j++ )
			{
				if ( coord.distance( tileCoords[ i ][ j ] ) <= 8.5 )
				{
					return Pair.of( i, j );
				}
			}
		}
		return null;
	}

	public void goToTile( Pair< Integer, Integer > coord )
	{
		if ( coord != null && app.getNifty( ).getScreen( "tile" ) == null && isOpen( ) )
		{
			this.currentTile = coord;
			RemoteEntityData entityData = this.app.getGameConnManager( ).getRemoteEntityData( );
			ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", this.currentTile.getLeft( ) );
			ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", this.currentTile.getRight( ) );
			@SuppressWarnings( "unchecked" )
			ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );
			EntityId findTile = entityData.findEntity( completeFilter, TileTrait.class );
			CreatedBy cb = entityData.getComponent( findTile, CreatedBy.class );
			if ( cb != null )
			{
				PlayerTrait pt = entityData.getComponent( cb.getCreatorId( ), PlayerTrait.class );
				if ( pt != null )
				{
					if ( this.app.getMasterConnManager( ).getAccount( ).equals( pt.getAccount( ) ) )
					{
						app.enqueue( ( ) ->
						{
							logger.log( Level.INFO, "Entering Tile screen" );
							NiftyFactory.createTileView( app.getNifty( ) );
							TileViewController tvc = ( TileViewController ) app.getNifty( ).getScreen( "tile" ).getScreenController( );
							app.getStateManager( ).attach( ( AppState ) tvc );
							tvc.setCoords( currentTile );
							app.getNifty( ).gotoScreen( "tile" );
							return null;
						} );
					}
				}
			}
		}
	}

	private void printTileInfo( Pair< Integer, Integer > coord )
	{
		if ( coord != null )
		{
			ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", coord.getLeft( ) );
			ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", coord.getRight( ) );
			@SuppressWarnings( "unchecked" )
			ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );
			if ( app.getGameConnManager( ).getRemoteEntityData( ) != null )
			{
				EntityId tileId = app.getGameConnManager( ).getRemoteEntityData( ).findEntity( completeFilter, TileTrait.class );
				if ( tileId != null )
				{
					Entity tileEntity = app.getGameConnManager( ).getRemoteEntityData( )
							.getEntity( tileId, TileTrait.class, Name.class, HealthTrait.class, CreatedBy.class );
					System.out.println( String.format( "Remote tile %s: %s, %s, %s, %s", tileId, tileEntity.get( TileTrait.class ),
							tileEntity.get( Name.class ), tileEntity.get( HealthTrait.class ), tileEntity.get( CreatedBy.class ) ) );
				}
			}
		}
	}

	public Pair< Integer, Integer > getTileCoord( )
	{
		return this.currentTile;
	}

	public void bind( Nifty nifty, Screen screen )
	{
	}

	/**
	 * Binds saved key inputs.
	 */
	public void bindKeys( )
	{
		YamlConfig yamlConfig = YamlConfig.DEFAULT;
		scoreKey = ( Integer ) yamlConfig.get( "client.input.scoreKey" );
		chatKey = ( Integer ) yamlConfig.get( "client.input.chatKey" );
	}

	public void closeMenu( )
	{
		this.app.getNifty( ).closePopup( "menu" );
		menuOpen = false;
	}

	/**
	 * Exits entire game and closes.
	 */
	public void exitGame( )
	{
		this.app.stop( );
	}

	/**
	 * Stops the current game and returns to game lobby.
	 */
	public void exitToNetwork( )
	{
		app.loadWorld( "null" );
		app.enqueue( ( ) ->
		{
			NiftyFactory.createNetworkView( app.getNifty( ) );
			app.getStateManager( ).detach( ( AppState ) app.getNifty( ).getCurrentScreen( ).getScreenController( ) );
			app.getStateManager( ).attach( ( AppState ) app.getNifty( ).getScreen( "network" ).getScreenController( ) );
			app.getNifty( ).removeScreen( "hud" );
			app.getNifty( ).gotoScreen( "network" );
			app.getInputManager( ).deleteMapping( "showScore" );
			app.getInputManager( ).deleteMapping( "showChat" );
			app.getInputManager( ).deleteMapping( "showMenu" );
			app.getGameConnManager( ).close( );
			return null;
		} );
	}

	public void setupScoreBoard( )
	{
		System.out.println( "Setting up scoreboard." );
		RemoteEntityData entityData = this.app.getGameConnManager( ).getRemoteEntityData( );
		Set< EntityId > playerSet = entityData.findEntities( null, PlayerTrait.class );
		System.out.println( playerSet.isEmpty( ) );
		int playerCount = 1;
		for ( EntityId id : playerSet )
		{
			this.players.add( id );
			ComponentFilter< CreatedBy > tileFilter = FieldFilter.create( CreatedBy.class, "creatorId", id );
			Set< EntityId > tiles = entityData.findEntities( tileFilter, CreatedBy.class );
			tileCount.put( id, tiles.size( ) );
			int units = 0;
			int buildings = 0;
			for ( EntityId tile : tiles )
			{
				System.out.println( tile.getId( ) );
				ComponentFilter< LocationTrait > locationFilter = FieldFilter.create( LocationTrait.class, "tile", tile );
				Set< EntityId > objectSet = entityData.findEntities( locationFilter, LocationTrait.class );
				units += Traits.countUnits( entityData, objectSet );
				buildings += Traits.countBuildings( entityData, objectSet );
			}
			unitCount.put( id, units );
			buildingCount.put( id, buildings );
			System.out.println( "Player: " + id + ", Tile Count: " + tiles.size( ) + ", Unit Count: " + units + ", Building Count: "
					+ buildings );
			String name = entityData.getComponent( id, PlayerTrait.class ).getAccount( ).getAccountName( );
			scoreboard.findNiftyControl( String.format( "name%sLabel", playerCount ), Label.class ).setText( name );
			scoreboard.findNiftyControl( String.format( "unit%sLabel", playerCount ), Label.class ).setText(
					"Units: " + Integer.toString( units ) );
			scoreboard.findNiftyControl( String.format( "building%sLabel", playerCount ), Label.class ).setText(
					"Buildings: " + Integer.toString( buildings ) );
			playerCount++;
		}

	}

	/**
	 * Starts the hud view with all popup options and creates the games ingame
	 * state.
	 */
	public void initialize( AppStateManager stateManager, Application app )
	{
		super.initialize( stateManager, app );
		this.app = ( ClientMain ) app;
		logger.log( Level.INFO, "Initialised " + this.getClass( ) );

		NiftyFactory.createOptionsView( this.app.getNifty( ) );
		bindKeys( );
		refreshKeys( );
		app.getInputManager( ).addMapping( "showScore", new KeyTrigger( scoreKey ) );
		app.getInputManager( ).addMapping( "showChat", new KeyTrigger( chatKey ) );
		app.getInputManager( ).addMapping( "showMenu", new KeyTrigger( KeyInput.KEY_ESCAPE ) );
		app.getInputManager( ).addListener( actionListener, "showConsole", "showScore", "showChat", "showMenu" );
		scoreboard = this.app.getNifty( ).createPopupWithId( "scoreboardPopup", "scoreboardPopup" );
		this.app.getNifty( ).createPopupWithId( "menu", "menu" );
		this.app.getNifty( ).createPopupWithId( "ingameChat", "ingameChat" );
		// chat = this.app.getNifty( ).findPopupByName( "ingameChat"
		// ).findNiftyControl( "gameChat", Chat.class );
		this.app.currentScreen = "hud";
		tileCoords = new Vector3f[ 16 ][ 14 ];
		for ( int i = 0; i < 16; i++ )
		{
			for ( int j = 0; j < 14; j++ )
			{
				tileCoords[ i ][ j ] = WorldFactory.createCenterPoint( 257, 10f, i + 1, j + 1 );
			}
		}
		this.players = new HashSet< EntityId >( );
		this.tileCount = new HashMap< EntityId, Integer >( );
		this.buildingCount = new HashMap< EntityId, Integer >( );
		this.unitCount = new HashMap< EntityId, Integer >( );
		// setupScoreBoard( );
	}

	/**
	 * Sets Clients input settings from YamlConfig file.
	 */
	public void refreshKeys( )
	{
		YamlConfig yamlConfig = YamlConfig.DEFAULT;
		app.getInputManager( ).clearMappings( );

		app.getInputManager( ).addMapping( "showConsole", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.consoleKey" ) ) );
		app.getInputManager( ).addMapping( "hideFPS", new KeyTrigger( KeyInput.KEY_F12 ) );
		app.getInputManager( ).addListener( app.getBaseActionListener( ), "showConsole", "hideFPS" );

		app.getInputManager( ).addMapping( "moveNorth", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.northKey" ) ) );
		app.getInputManager( ).addMapping( "moveWest", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.westKey" ) ) );
		app.getInputManager( ).addMapping( "moveEast", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.eastKey" ) ) );
		app.getInputManager( ).addMapping( "moveSouth", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.southKey" ) ) );
		app.getInputManager( ).addMapping( "mouseClick", new MouseButtonTrigger( MouseInput.BUTTON_LEFT ) );
		app.getInputManager( ).addMapping( "zoomIn", new MouseAxisTrigger( MouseInput.AXIS_WHEEL, false ) );
		app.getInputManager( ).addMapping( "zoomOut", new MouseAxisTrigger( MouseInput.AXIS_WHEEL, true ) );
		app.getInputManager( ).addListener( analogListener, "moveNorth", "moveWest", "moveEast", "moveSouth", "mouseClick", "zoomIn",
				"zoomOut" );
	}

	/**
	 * Checks if any other popup or window is open.
	 * 
	 * @return True if no other popup or window is open, else false
	 */
	public boolean isOpen( )
	{
		if ( scoreOpen || chatOpen || menuOpen )
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean keyEvent( NiftyInputEvent inputEvent )
	{
		return false;
	}

	public void nextScreen( String screen )
	{
		this.app.getNifty( ).gotoScreen( screen );
	}

	/**
	 * Sends the text the user inputs into the chat to the server. Currently
	 * just prints to chat.
	 * 
	 * @param id
	 * @param event
	 */
	@NiftyEventSubscriber( id = "gameChat" )
	public void onChatTextSendEvent( final String id, final ChatTextSendEvent event )
	{
		// chat.receivedChatLine( event.getText( ), null );
		app.sendLobbyChat( event.getText( ) );
	}

	public void onEndScreen( )
	{
		logger.log( Level.INFO, "HUD Screen Stopped" );
	}

	public void onStartScreen( )
	{
		logger.log( Level.INFO, "HUD Screen Started" );
	}

	public void showMenu( )
	{
		this.app.getNifty( ).showPopup( this.app.getNifty( ).getCurrentScreen( ), "menu", null );
		menuOpen = true;
	}
}
