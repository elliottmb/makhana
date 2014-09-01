package com.cogitareforma.hexrepublics.client.views;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.client.DebugGlobals;
import com.cogitareforma.hexrepublics.client.states.EntityManager;
import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.cogitareforma.hexrepublics.common.entities.Traits;
import com.cogitareforma.hexrepublics.common.entities.traits.HealthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.WorldTrait;
import com.cogitareforma.hexrepublics.common.net.msg.ReadyUpRequest;
import com.cogitareforma.hexrepublics.common.util.EntityEventListener;
import com.cogitareforma.hexrepublics.common.util.WorldFactory;
import com.cogitareforma.hexrepublics.common.util.YamlConfig;
import com.jme3.app.Application;
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
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.Name;
import com.simsilica.es.client.RemoteEntityData;
import com.simsilica.es.filter.AndFilter;
import com.simsilica.es.filter.FieldFilter;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;

/**
 * 
 * @author Ryan Grier
 * @author Elliott Butler
 */
public class HudViewController extends GeneralPlayingController implements KeyInputHandler
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( HudViewController.class.getName( ) );

	private boolean scoreOpen = false;
	private boolean chatOpen = false;
	private boolean menuOpen = false;
	private int scoreKey;
	private int chatKey;
	private Pair< Integer, Integer > currentTile;
	private Element scoreboard;

	private EntitySet players;

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
		if ( !"consoleScreen".equalsIgnoreCase( getApp( ).getNifty( ).getCurrentScreen( ).getScreenId( ) ) )
		{
			if ( name.equals( "showScore" ) && !keyPressed )
			{
				if ( scoreOpen == false && isOpen( ) )
				{
					getApp( ).getNifty( ).showPopup( getApp( ).getNifty( ).getCurrentScreen( ), "scoreboardPopup", null );
					scoreOpen = true;
					setupScoreBoard( );
				}
				else
				{
					if ( scoreOpen == true )
					{
						getApp( ).getNifty( ).closePopup( "scoreboardPopup" );
						scoreOpen = false;
					}
				}
			}
			if ( name.equals( "showChat" ) && !keyPressed )
			{
				if ( chatOpen == false && isOpen( ) )
				{
					getApp( ).getNifty( ).showPopup( getApp( ).getNifty( ).getCurrentScreen( ), "ingameChat",
							getApp( ).getNifty( ).findPopupByName( "ingameChat" ).findElementByName( "gameChat#chat-text-input" ) );
					chatOpen = true;
				}
				else
				{
					if ( chatOpen == true )
					{
						getApp( ).getNifty( ).closePopup( "ingameChat" );
						chatOpen = false;
					}
				}
			}
			if ( name.equals( "showMenu" ) && !keyPressed )
			{
				if ( "tile".equalsIgnoreCase( getApp( ).getNifty( ).getCurrentScreen( ).getScreenId( ) ) && isOpen( ) )
				{
					TileViewController tvc = ( TileViewController ) getApp( ).getNifty( ).getCurrentScreen( ).getScreenController( );
					tvc.exitView( );
				}
				else if ( "mainOptions".equalsIgnoreCase( getApp( ).getNifty( ).getCurrentScreen( ).getScreenId( ) ) )
				{
					OptionsViewController ovc = ( OptionsViewController ) getApp( ).getNifty( ).getCurrentScreen( ).getScreenController( );
					ovc.exitMainOptions( );
				}
				else
				{
					if ( menuOpen == false && isOpen( ) )
					{
						getApp( ).getNifty( ).showPopup( getApp( ).getNifty( ).getCurrentScreen( ), "menu", null );
						menuOpen = true;
					}
					else
					{
						if ( menuOpen == true )
						{
							getApp( ).getNifty( ).closePopup( "menu" );
							menuOpen = false;
						}
					}
				}
			}
		}
	};
	private EntityEventListener worldListener = new EntityEventListener( )
	{
		// TODO Don't know if this needs more. This just current is here for
		// current turn counter.
		@Override
		public void onAdded( EntityData entityData, Set< Entity > entities )
		{
			for ( Entity e : entities )
			{
				WorldTrait wt = e.get( WorldTrait.class );
				if ( wt != null )
				{
					System.out.println( "HUD onAdded changed currentTurn" );
					updateCurrentTurnText( );
					break;
				}
			}
		}

		@Override
		public void onChanged( EntityData entityData, Set< Entity > entities )
		{
			for ( Entity e : entities )
			{
				WorldTrait wt = e.get( WorldTrait.class );
				if ( wt != null )
				{
					System.out.println( "HUD onChanged changed currentTurn" );
					updateCurrentTurnText( );
					break;
				}
			}
		}

		@Override
		public void onRemoved( EntityData entityData, Set< Entity > entities )
		{
		}
	};

	private AnalogListener analogListener = ( String name, float value, float tpf ) ->
	{
		if ( name.equals( "mouseClick" ) )
		{
			// Reset results list.
			CollisionResults results = new CollisionResults( );
			// Convert screen click to 3d position
			Vector2f click2d = getApp( ).getInputManager( ).getCursorPosition( );
			Vector3f click3d = getApp( ).getCamera( ).getWorldCoordinates( new Vector2f( click2d.x, click2d.y ), 0f ).clone( );
			Vector3f dir = getApp( ).getCamera( ).getWorldCoordinates( new Vector2f( click2d.x, click2d.y ), 1f ).subtractLocal( click3d )
					.normalizeLocal( );
			// Aim the ray from the clicked spot forwards.
			Ray ray = new Ray( click3d, dir );
			// Collect intersections between ray and all nodes in results
			// list.
			getApp( ).getRootNode( ).collideWith( ray, results );
			// (Print the results so we see what is going on:)
			if ( DebugGlobals.DEBUG_OBJECT_OUTPUT )
			{
				for ( int i = 0; i < results.size( ); i++ )
				{
					// (For each, we know distance, impact point, geometry.)
					float dist = results.getCollision( i ).getDistance( );
					Vector3f pt = results.getCollision( i ).getContactPoint( );
					String target = results.getCollision( i ).getGeometry( ).getName( );
					if ( DebugGlobals.DEBUG_TILE_SELECTION_OUTPUT )
					{
						System.out.println( "Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away." );
					}

					// Print out the colliding tile location
					// System.out.println( insideTile( new Vector3f( pt.x,
					// 0, pt.z ) ) );
					Pair< Integer, Integer > tile = insideTile( new Vector3f( pt.x, 0, pt.z ) );
					if ( tile != null )
					{
						printTileInfo( tile );
						goToTile( tile );
					}
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
		int theSpeed = 80;
		Vector3f v = getApp( ).getCamera( ).getLocation( );
		ViewPort miniview = getApp( ).getRenderManager( ).getMainView( "Minimap" );
		Camera minimap = null;
		Vector3f minimapLocation = null;
		if ( miniview != null )
		{
			minimap = miniview.getCamera( );
			minimapLocation = minimap.getLocation( );
		}

		if ( name.equals( "moveNorth" ) )
		{
			getApp( ).getCamera( ).setLocation( new Vector3f( v.x, v.y, v.z - value * theSpeed ) );
			if ( minimap != null && minimapLocation != null )
			{
				minimap.setLocation( new Vector3f( minimapLocation.x, minimapLocation.y, minimapLocation.z - value * theSpeed ) );
			}
		}
		if ( name.equals( "moveWest" ) )
		{
			getApp( ).getCamera( ).setLocation( new Vector3f( v.x - value * theSpeed, v.y, v.z ) );
			if ( minimap != null && minimapLocation != null )
			{
				minimap.setLocation( new Vector3f( minimapLocation.x - value * theSpeed, minimapLocation.y, minimapLocation.z ) );
			}
		}
		if ( name.equals( "moveEast" ) )
		{
			getApp( ).getCamera( ).setLocation( new Vector3f( v.x + value * theSpeed, v.y, v.z ) );
			if ( minimap != null && minimapLocation != null )
			{
				minimap.setLocation( new Vector3f( minimapLocation.x + value * theSpeed, minimapLocation.y, minimapLocation.z ) );
			}
		}
		if ( name.equals( "moveSouth" ) )
		{
			getApp( ).getCamera( ).setLocation( new Vector3f( v.x, v.y, v.z + value * theSpeed ) );
			if ( minimap != null && minimapLocation != null )
			{
				minimap.setLocation( new Vector3f( minimapLocation.x, minimapLocation.y, minimapLocation.z + value * theSpeed ) );
			}
		}
		if ( name.equals( "zoomIn" ) )
		{
			if ( v.y - value > 100f )
			{
				getApp( ).getCamera( ).setLocation( new Vector3f( v.x, v.y - value * 4, v.z - value * 4 ) );
			}
		}
		if ( name.equals( "zoomOut" ) )
		{
			if ( v.y + value <= 300f )
			{
				getApp( ).getCamera( ).setLocation( new Vector3f( v.x, v.y + value * 4, v.z + value * 4 ) );
			}
		}

	};

	private Label currentTurnText;

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
		getApp( ).getNifty( ).closePopup( "menu" );
		menuOpen = false;
	}

	/**
	 * Exits entire game and closes.
	 */
	public void exitGame( )
	{
		getApp( ).stop( );
	}

	public Pair< Integer, Integer > getTileCoord( )
	{
		return this.currentTile;
	}

	public void goToTile( Pair< Integer, Integer > coord )
	{
		if ( coord != null && getApp( ).getNifty( ).getScreen( "tile" ) == null && isOpen( ) )
		{
			this.currentTile = coord;
			RemoteEntityData entityData = getApp( ).getGameConnManager( ).getRemoteEntityData( );
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
					if ( getApp( ).getMasterConnManager( ).getAccount( ).equals( pt.getAccount( ) ) )
					{
						NiftyFactory.createTileView( getApp( ).getNifty( ) );

						gotoScreen( "tile", false, true, false,
								( ) ->
								{
									TileViewController tvc = ( TileViewController ) getApp( ).getNifty( ).getScreen( "tile" )
											.getScreenController( );
									tvc.setCoords( currentTile );
									// tvc.setCurrentTurn( currentTurn );
								return null;
							}, null );
					}
				}
			}
		}
	}

	/**
	 * Starts the hud view with all popup options and creates the games ingame
	 * state.
	 */
	@SuppressWarnings( "unchecked" )
	public void initialize( AppStateManager stateManager, Application app )
	{
		setScreenId( "hud" );
		super.initialize( stateManager, app );

		logger.log( Level.INFO, "Initialised " + this.getClass( ) );

		// NiftyFactory.createOptionsView( getApp( ).getNifty( ) );
		bindKeys( );
		refreshKeys( );
		getApp( ).getInputManager( ).addMapping( "showScore", new KeyTrigger( scoreKey ) );
		getApp( ).getInputManager( ).addMapping( "showChat", new KeyTrigger( chatKey ) );
		getApp( ).getInputManager( ).addMapping( "showMenu", new KeyTrigger( KeyInput.KEY_ESCAPE ) );
		getApp( ).getInputManager( ).addListener( actionListener, "showConsole", "showScore", "showChat", "showMenu" );
		scoreboard = getApp( ).getNifty( ).createPopupWithId( "scoreboardPopup", "scoreboardPopup" );
		getApp( ).getNifty( ).createPopupWithId( "menu", "menu" );
		getApp( ).getNifty( ).createPopupWithId( "ingameChat", "ingameChat" );
		// chat = getApp().getNifty( ).findPopupByName( "ingameChat"
		// ).findNiftyControl( "gameChat", Chat.class );
		getApp( ).currentScreen = "hud";

		currentTurnText = ( Label ) getApp( ).getNifty( ).getScreen( "hud" ).findNiftyControl( "turns", Label.class );
		currentTurnText.setText( "Turn: " + getApp( ).getWorldManager( ).getCurrentTurn( ) );

		tileCoords = new Vector3f[ 16 ][ 14 ];
		for ( int i = 0; i < 16; i++ )
		{
			for ( int j = 0; j < 14; j++ )
			{
				tileCoords[ i ][ j ] = WorldFactory.createCenterPoint( 1025, 40f, i + 1, j + 1 );
			}
		}

		setupScoreBoard( );
		EntityManager entityManager = getApp( ).getEntityManager( );
		if ( entityManager != null )
		{
			entityManager.addListener( worldListener, WorldTrait.class );
		}
	}

	public Pair< Integer, Integer > insideTile( Vector3f coord )
	{
		for ( int i = 0; i < tileCoords.length; i++ )
		{
			for ( int j = 0; j < tileCoords[ i ].length; j++ )
			{
				if ( coord.distance( tileCoords[ i ][ j ] ) <= 34 )
				{
					return Pair.of( i, j );
				}
			}
		}
		return null;
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
		getApp( ).sendLobbyChat( event.getText( ) );
	}

	public void openOptions( )
	{
		// gotoScreen( "options", false, false, false, null, null );
		NiftyFactory.createMainOptions( getApp( ).getNifty( ) );
		gotoScreen( "mainOptions", false, true, false, null, null );
	}

	@Override
	protected void postExitToNetwork( )
	{
		getApp( ).getInputManager( ).deleteMapping( "showScore" );
		getApp( ).getInputManager( ).deleteMapping( "showChat" );
		getApp( ).getInputManager( ).deleteMapping( "showMenu" );

		EntityManager entityManager = getApp( ).getEntityManager( );
		if ( entityManager != null )
		{
			entityManager.removeListener( worldListener, WorldTrait.class );
		}
	}

	@Override
	protected void preExitToNetwork( )
	{
		getApp( ).loadWorld( null );
	}

	private void printTileInfo( Pair< Integer, Integer > coord )
	{
		if ( coord != null )
		{
			ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", coord.getLeft( ) );
			ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", coord.getRight( ) );
			@SuppressWarnings( "unchecked" )
			ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );
			if ( getApp( ).getGameConnManager( ).getRemoteEntityData( ) != null )
			{
				EntityId tileId = getApp( ).getGameConnManager( ).getRemoteEntityData( ).findEntity( completeFilter, TileTrait.class );
				if ( tileId != null )
				{
					Entity tileEntity = getApp( ).getGameConnManager( ).getRemoteEntityData( )
							.getEntity( tileId, TileTrait.class, Name.class, HealthTrait.class, CreatedBy.class );
					if ( DebugGlobals.DEBUG_TILE_SELECTION_OUTPUT )
					{
						System.out.println( String.format( "Remote tile %s: %s, %s, %s, %s", tileId, tileEntity.get( TileTrait.class ),
								tileEntity.get( Name.class ), tileEntity.get( HealthTrait.class ), tileEntity.get( CreatedBy.class ) ) );
					}
				}
			}
		}
	}

	public void readyUp( )
	{
		// TODO: server needs to make sure all players are ready before
		// changing.
		getApp( ).getGameConnManager( ).send( new ReadyUpRequest( true ) );
	}

	/**
	 * Sets Clients input settings from YamlConfig file.
	 */
	public void refreshKeys( )
	{
		YamlConfig yamlConfig = YamlConfig.DEFAULT;
		getApp( ).getInputManager( ).clearMappings( );

		getApp( ).getInputManager( ).addMapping( "showConsole", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.consoleKey" ) ) );
		getApp( ).getInputManager( ).addMapping( "hideFPS", new KeyTrigger( KeyInput.KEY_F12 ) );
		getApp( ).getInputManager( ).addListener( getApp( ).getBaseActionListener( ), "showConsole", "hideFPS" );

		getApp( ).getInputManager( ).addMapping( "moveNorth", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.northKey" ) ) );
		getApp( ).getInputManager( ).addMapping( "moveWest", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.westKey" ) ) );
		getApp( ).getInputManager( ).addMapping( "moveEast", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.eastKey" ) ) );
		getApp( ).getInputManager( ).addMapping( "moveSouth", new KeyTrigger( ( Integer ) yamlConfig.get( "client.input.southKey" ) ) );
		getApp( ).getInputManager( ).addMapping( "mouseClick", new MouseButtonTrigger( MouseInput.BUTTON_LEFT ) );
		getApp( ).getInputManager( ).addMapping( "zoomIn", new MouseAxisTrigger( MouseInput.AXIS_WHEEL, false ) );
		getApp( ).getInputManager( ).addMapping( "zoomOut", new MouseAxisTrigger( MouseInput.AXIS_WHEEL, true ) );
		getApp( ).getInputManager( ).addListener( analogListener, "moveNorth", "moveWest", "moveEast", "moveSouth", "mouseClick", "zoomIn",
				"zoomOut" );
	}

	public void setupScoreBoard( )
	{
		System.out.println( "Setting up scoreboard." );
		RemoteEntityData entityData = getApp( ).getGameConnManager( ).getRemoteEntityData( );

		if ( entityData != null )
		{

			if ( players != null )
			{
				players.applyChanges( );
			}
			else
			{
				players = entityData.getEntities( PlayerTrait.class );
			}

			int playerCount = 1;
			for ( Entity e : players )
			{
				EntityId id = e.getId( );

				ComponentFilter< CreatedBy > tileFilter = FieldFilter.create( CreatedBy.class, "creatorId", id );
				Set< EntityId > tiles = entityData.findEntities( tileFilter, CreatedBy.class );
				int units = 0;
				int buildings = 0;
				for ( EntityId tile : tiles )
				{
					ComponentFilter< LocationTrait > locationFilter = FieldFilter.create( LocationTrait.class, "tile", tile );
					Set< EntityId > objectSet = entityData.findEntities( locationFilter, LocationTrait.class );
					units += Traits.countUnits( entityData, objectSet );
					buildings += Traits.countBuildings( entityData, objectSet );
				}
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
	}

	public void showMenu( )
	{
		getApp( ).getNifty( ).showPopup( getApp( ).getNifty( ).getCurrentScreen( ), "menu", null );
		menuOpen = true;
	}

	public void updateCurrentTurnText( )
	{
		currentTurnText.setText( "Turn: " + getApp( ).getWorldManager( ).getCurrentTurn( ) );
	}

}
