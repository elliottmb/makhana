package com.cogitareforma.hexrepublics.client.views;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.client.states.EntityManager;
import com.cogitareforma.hexrepublics.client.util.EntityEntryModelClass;
import com.cogitareforma.makhana.common.entities.ActionType;
import com.cogitareforma.makhana.common.entities.Traits;
import com.cogitareforma.makhana.common.entities.traits.ActionTrait;
import com.cogitareforma.makhana.common.entities.traits.ArcherTrait;
import com.cogitareforma.makhana.common.entities.traits.ArcheryTrait;
import com.cogitareforma.makhana.common.entities.traits.AxemanTrait;
import com.cogitareforma.makhana.common.entities.traits.BarracksTrait;
import com.cogitareforma.makhana.common.entities.traits.CatapultTrait;
import com.cogitareforma.makhana.common.entities.traits.ClubmanTrait;
import com.cogitareforma.makhana.common.entities.traits.CrossbowTrait;
import com.cogitareforma.makhana.common.entities.traits.ForgeTrait;
import com.cogitareforma.makhana.common.entities.traits.KriegerTrait;
import com.cogitareforma.makhana.common.entities.traits.LocationTrait;
import com.cogitareforma.makhana.common.entities.traits.LongbowTrait;
import com.cogitareforma.makhana.common.entities.traits.MachineWorksTrait;
import com.cogitareforma.makhana.common.entities.traits.MountedTrait;
import com.cogitareforma.makhana.common.entities.traits.PikemanTrait;
import com.cogitareforma.makhana.common.entities.traits.SawmillTrait;
import com.cogitareforma.makhana.common.entities.traits.StablesTrait;
import com.cogitareforma.makhana.common.entities.traits.SwordsmanTrait;
import com.cogitareforma.makhana.common.entities.traits.TileTrait;
import com.cogitareforma.makhana.common.entities.traits.TrebuchetTrait;
import com.cogitareforma.makhana.common.entities.traits.WorldTrait;
import com.cogitareforma.makhana.common.net.msg.EntityActionRequest;
import com.cogitareforma.makhana.common.net.msg.EntityCreationRequest;
import com.cogitareforma.makhana.common.util.TraitEventListener;
import com.cogitareforma.makhana.common.util.WorldFactory;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.client.RemoteEntityData;
import com.simsilica.es.filter.AndFilter;
import com.simsilica.es.filter.FieldFilter;

import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;

/**
 * 
 * @author Ryan Grier
 * @author Elliott Butler
 * 
 */
public class TileViewController extends GeneralPlayingController
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( TileViewController.class.getName( ) );

	private ListBox< EntityEntryModelClass > current;

	private ListBox< String > build;
	private Pair< Integer, Integer > currentTile;
	private EntityId currentUnit;
	private EntitySet locationSet;
	private EntitySet actionSet;
	private RemoteEntityData entityData;
	private EntityId findTile;
	private LinkedList< EntityId > toUpdate;
	private Element move;
	private boolean justUpdated = false;
	private Vector3f prevLocation;

	private TraitEventListener worldListener = new TraitEventListener( )
	{
		@Override
		public void onAdded( EntityData entityData, Set< Entity > entities )
		{
			if ( actionSet != null )
			{
				for ( Entity e : actionSet )
				{
					EntityId id = e.getId( );
					if ( !toUpdate.contains( id ) )
					{
						toUpdate.add( id );
					}
				}
				updateExisting( toUpdate );

				current.refresh( );
				fillBuildables( );
			}
			System.out.println( "onAdded current turn: " + getApp( ).getWorldManager( ).getCurrentTurn( ) );
		}

		@Override
		public void onChanged( EntityData entityData, Set< Entity > entities )
		{
			if ( actionSet != null )
			{
				for ( Entity e : actionSet )
				{
					EntityId id = e.getId( );
					if ( !toUpdate.contains( id ) )
					{
						toUpdate.add( id );
					}
				}
				updateExisting( toUpdate );

				current.refresh( );
				fillBuildables( );
			}
			System.out.println( "onChanged current turn: " + getApp( ).getWorldManager( ).getCurrentTurn( ) );
		}

		@Override
		public void onRemoved( EntityData entityData, Set< Entity > entities )
		{
			// Should never happen?
		}
	};

	private Vector3f prevMiniLocation;

	public void addToExisting( Entity entity, int currentTurn )
	{
		EntityId id = entity.getId( );

		EntityEntryModelClass eemc = new EntityEntryModelClass( id, createDisplayText( id, currentTurn ) );
		current.addItem( eemc );
	}

	public void closeMove( )
	{
		getApp( ).getNifty( ).closePopup( "move" );
	}

	public String createDisplayText( EntityId id, int currentTurn )
	{
		String existing = "";

		// Prefixes
		if ( entityData.getComponent( id, MountedTrait.class ) != null )
		{
			existing += "Mounted ";
		}

		// Roots / Combining Forms
		if ( entityData.getComponent( id, ArcheryTrait.class ) != null )
		{
			existing += "Archery";
		}
		if ( entityData.getComponent( id, BarracksTrait.class ) != null )
		{
			existing += "Barracks";
		}
		if ( entityData.getComponent( id, StablesTrait.class ) != null )
		{
			existing += "Stables";
		}
		if ( entityData.getComponent( id, ArcherTrait.class ) != null )
		{
			existing += "Archer";
		}
		if ( entityData.getComponent( id, ClubmanTrait.class ) != null )
		{
			existing += "Clubman";
		}
		if ( entityData.getComponent( id, AxemanTrait.class ) != null )
		{
			existing += "Axeman";
		}
		if ( entityData.getComponent( id, CatapultTrait.class ) != null )
		{
			existing += "Catapult";
		}
		if ( entityData.getComponent( id, CrossbowTrait.class ) != null )
		{
			existing += "Crossbowman";
		}
		if ( entityData.getComponent( id, ForgeTrait.class ) != null )
		{
			existing += "Forge";
		}
		if ( entityData.getComponent( id, KriegerTrait.class ) != null )
		{
			existing += "Krieger";
		}
		if ( entityData.getComponent( id, LongbowTrait.class ) != null )
		{
			existing += "Longbowman";
		}
		if ( entityData.getComponent( id, MachineWorksTrait.class ) != null )
		{
			existing += "Machine Works";
		}
		if ( entityData.getComponent( id, PikemanTrait.class ) != null )
		{
			existing += "Pikeman";
		}
		if ( entityData.getComponent( id, SawmillTrait.class ) != null )
		{
			existing += "Sawmill";
		}
		if ( entityData.getComponent( id, SwordsmanTrait.class ) != null )
		{
			existing += "Swordsman";
		}
		if ( entityData.getComponent( id, TrebuchetTrait.class ) != null )
		{
			existing += "Trebuchet";
		}

		// Suffix
		Pair< String, Integer > action = Traits.getActionRemainingTurns( entityData, id, currentTurn );
		if ( action != null )
		{
			existing += String.format( " - %s: %d turns", action.getLeft( ), action.getRight( ) );
		}

		return existing;
	}

	/**
	 * Close the tile view and return to the hud view.
	 */
	public void exitView( )
	{
		gotoScreen( "hud", true, false, true, null, null );
	}

	public void fillBuildables( )
	{
		if ( !justUpdated )
		{
			if ( entityData != null )
			{
				Stack< String > buildables = new Stack< String >( );

				if ( Traits.countBuildings( entityData, locationSet ) < 6 )
				{
					buildables.add( "Build Archery" );
					buildables.add( "Build Barracks" );
					buildables.add( "Build Stables" );
					buildables.add( "Build Forge" );
					buildables.add( "Build Machine Works" );
					buildables.add( "Build Sawmill" );
				}
				if ( Traits.countUnits( entityData, locationSet ) < 6 )
				{
					if ( Traits.hasPrerequisites( entityData, locationSet, ArcheryTrait.class ) )
					{
						buildables.add( "Build Archer" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, BarracksTrait.class ) )
					{
						buildables.add( "Build Clubman" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, ArcheryTrait.class, StablesTrait.class ) )
					{
						buildables.add( "Build Mounted Archer" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, BarracksTrait.class, StablesTrait.class ) )
					{
						buildables.add( "Build Mounted Clubman" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, ForgeTrait.class, BarracksTrait.class ) )
					{
						buildables.add( "Build Axeman" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, ArcheryTrait.class, ForgeTrait.class, SawmillTrait.class,
							MachineWorksTrait.class ) )
					{
						buildables.add( "Build Catapult" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, ArcheryTrait.class, ForgeTrait.class, SawmillTrait.class ) )
					{
						buildables.add( "Build Crossbowman" );
					}
					if ( Traits.countBuildings( entityData, locationSet ) == 6 && hasSameBuildings( ) )
					{
						buildables.add( "Build Krieger" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, SawmillTrait.class, ArcheryTrait.class ) )
					{
						buildables.add( "Build Longbowman" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, BarracksTrait.class, ForgeTrait.class, SawmillTrait.class ) )
					{
						buildables.add( "Build Pikeman" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, BarracksTrait.class, ForgeTrait.class ) )
					{
						buildables.add( "Build Swordsman" );
					}
					if ( Traits.hasPrerequisites( entityData, locationSet, ArcheryTrait.class, ForgeTrait.class, SawmillTrait.class,
							StablesTrait.class ) )
					{
						buildables.add( "Build Trebuchet" );
					}
				}

				build.clear( );
				build.addAllItems( buildables );
				justUpdated = true;
			}
		}
		else
		{
			justUpdated = false;
		}
	}

	public Pair< Integer, Integer > getCoords( )
	{
		return this.currentTile;
	}

	public boolean hasSameBuildings( )
	{
		// TODO
		for ( Entity e : locationSet )
		{

		}
		return false;
	}

	/**
	 * Updates and displays selected tile info.
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void initialize( AppStateManager stateManager, Application app )
	{
		setScreenId( "tile" );
		super.initialize( stateManager, app );

		current = getApp( ).getNifty( ).getCurrentScreen( ).findNiftyControl( "existBox", ListBox.class );
		build = getApp( ).getNifty( ).getScreen( "tile" ).findNiftyControl( "buildBox", ListBox.class );
		move = getApp( ).getNifty( ).createPopupWithId( "move", "move" );
		entityData = getApp( ).getGameConnManager( ).getRemoteEntityData( );
		ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", this.currentTile.getLeft( ) );
		ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", this.currentTile.getRight( ) );
		ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );
		findTile = entityData.findEntity( completeFilter, TileTrait.class );

		ComponentFilter< LocationTrait > locationFilter = FieldFilter.create( LocationTrait.class, "tile", findTile );
		locationSet = entityData.getEntities( locationFilter, LocationTrait.class );

		toUpdate = new LinkedList< EntityId >( );

		int currentTurn = getApp( ).getWorldManager( ).getCurrentTurn( );
		if ( locationSet != null )
		{
			if ( locationSet.applyChanges( ) )
			{
				for ( Entity e : locationSet.getAddedEntities( ) )
				{
					addToExisting( e, currentTurn );
				}
			}
		}

		EntityManager entityManager = getApp( ).getEntityManager( );
		if ( entityManager != null )
		{
			entityManager.addListener( worldListener, WorldTrait.class );
		}

		logger.log( Level.INFO, "Initialised " + this.getClass( ) );
		getApp( ).currentScreen = "tile";

		fillBuildables( );
		justUpdated = false;
		moveCamera( );
	}

	public void moveCamera( )
	{
		Camera mainCamera = getApp( ).getCamera( );
		Camera miniCamera = getApp( ).getRenderManager( ).getMainView( "Minimap" ).getCamera( );
		prevLocation = mainCamera.getLocation( ).clone( );
		prevMiniLocation = miniCamera.getLocation( ).clone( );
		System.out.println( "Moving camera" );
		Vector3f centerPoint = WorldFactory
				.createCenterPoint( 1025, 40f, this.currentTile.getLeft( ) + 1, this.currentTile.getRight( ) + 1 );
		mainCamera.setLocation( new Vector3f( centerPoint.x, centerPoint.y + 100f, centerPoint.z + 100f ) );
		miniCamera.setLocation( new Vector3f( centerPoint.x, centerPoint.y + 2000f, centerPoint.z ) );
	}

	@SuppressWarnings( "unchecked" )
	public void moveCommand( String direction )
	{
		String unit = move.findNiftyControl( "unit", Label.class ).getText( );
		System.out.println( "Trying to move " + unit + " with EntityId of " + currentUnit + " " + direction );
		if ( currentUnit != null )
		{
			int x = this.currentTile.getLeft( );
			int y = this.currentTile.getRight( );

			List< List< Pair< Integer, Integer > >> neighbors = Traits.neighbors;

			int parity = x % 2;
			switch ( direction )
			{

				case "NW":
				{
					x += neighbors.get( parity ).get( 0 ).getLeft( );
					y += neighbors.get( parity ).get( 0 ).getRight( );
					break;
				}
				case "SW":
				{
					x += neighbors.get( parity ).get( 1 ).getLeft( );
					y += neighbors.get( parity ).get( 1 ).getRight( );
					break;
				}
				case "S":
				{
					x += neighbors.get( parity ).get( 2 ).getLeft( );
					y += neighbors.get( parity ).get( 2 ).getRight( );
					break;
				}

				case "SE":
				{
					x += neighbors.get( parity ).get( 3 ).getLeft( );
					y += neighbors.get( parity ).get( 3 ).getRight( );
					break;
				}
				case "NE":
				{
					x += neighbors.get( parity ).get( 4 ).getLeft( );
					y += neighbors.get( parity ).get( 4 ).getRight( );
					break;
				}
				case "N":
				{
					x += neighbors.get( parity ).get( 5 ).getLeft( );
					y += neighbors.get( parity ).get( 5 ).getRight( );
					break;
				}
			}

			if ( x >= 0 && y >= 0 && x < 16 && y < 14 )
			{
				ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", x );
				ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", y );
				ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );
				EntityId nextTile = entityData.findEntity( completeFilter, TileTrait.class );

				if ( nextTile != null )
				{
					HashMap< String, Object > data = new HashMap< String, Object >( );
					data.put( "newTile", nextTile );
					getApp( ).getGameConnManager( ).send(
							new EntityActionRequest( currentUnit, new ActionTrait( getApp( ).getWorldManager( ).getCurrentTurn( ), Traits
									.getMovementModifier( entityData, currentUnit ), ActionType.MOVE, data ) ) );
				}
			}
		}

		getApp( ).getNifty( ).closePopup( "move" );
	}

	public void onEndScreen( )
	{
		getApp( ).getCamera( ).setLocation( prevLocation );
		getApp( ).getRenderManager( ).getMainView( "Minimap" ).getCamera( ).setLocation( prevMiniLocation );
		super.onEndScreen( );
		if ( locationSet != null )
		{
			locationSet.release( );
			locationSet = null;
		}
		if ( actionSet != null )
		{
			actionSet.release( );
			actionSet = null;
		}

		EntityManager entityManager = getApp( ).getEntityManager( );
		if ( entityManager != null )
		{
			entityManager.removeListener( worldListener, WorldTrait.class );
		}

	}

	@NiftyEventSubscriber( id = "buildBox" )
	public void onListBoxSelectionChanged( final String id, final ListBoxSelectionChangedEvent< String > event )
	{
		if ( event != null && event.getSelection( ) != null && event.getSelection( ).size( ) > 0 )
		{
			ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", this.currentTile.getLeft( ) );
			ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", this.currentTile.getRight( ) );
			@SuppressWarnings( "unchecked" )
			ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );
			EntityId location = getApp( ).getGameConnManager( ).getRemoteEntityData( ).findEntity( completeFilter, TileTrait.class );
			// BUILDINGS
			if ( "Build Archery".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Tyring to build Archery" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ArcheryTrait( ) ) );
			}
			if ( "Build Barracks".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Barracks" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new BarracksTrait( ) ) );
			}
			if ( "Build Stables".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Stables" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new StablesTrait( ) ) );
			}
			if ( "Build Forge".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Forge" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ForgeTrait( ) ) );
			}
			if ( "Build Sawmill".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Sawmill" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new SawmillTrait( ) ) );
			}
			if ( "Build Machine Works".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Machine Works" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new MachineWorksTrait( ) ) );
			}

			// UNITS
			if ( "Build Archer".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Archer" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ArcherTrait( ) ) );
			}
			if ( "Build Clubman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Clubman" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ClubmanTrait( ) ) );
			}
			if ( "Build Mounted Archer".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Tyring to build Mounted Archer" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ArcherTrait( ), new MountedTrait( ) ) );
			}
			if ( "Build Mounted Clubman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Mounted Clubman" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ClubmanTrait( ), new MountedTrait( ) ) );
			}
			if ( "Build Axeman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Axeman" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new AxemanTrait( ) ) );
			}
			if ( "Build Catapult".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Catapult" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new CatapultTrait( ) ) );
			}
			if ( "Build Crossbowman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Crossbowman" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new CrossbowTrait( ) ) );
			}
			if ( "Build Krieger".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Krieger" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new KriegerTrait( ) ) );
			}
			if ( "Build Longbowman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Longbowman" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new LongbowTrait( ) ) );
			}
			if ( "Build Pikeman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Pikeman" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new PikemanTrait( ) ) );
			}
			if ( "Build Swordsman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Swordsman" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new SwordsmanTrait( ) ) );
			}
			if ( "Build Trebuchet".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Trebuchet" );
				getApp( ).getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new TrebuchetTrait( ) ) );
			}
		}
	}

	@NiftyEventSubscriber( id = "existBox" )
	public void onListBoxSelectionChanged2( final String id, final ListBoxSelectionChangedEvent< EntityEntryModelClass > event )
	{
		if ( event != null && event.getSelection( ) != null && event.getSelection( ).size( ) > 0 )
		{
			if ( Traits.isUnit( entityData, event.getSelection( ).get( 0 ).getEntityId( ) ) )
			{
				if ( !Traits.inAction( entityData, event.getSelection( ).get( 0 ).getEntityId( ) ) )
				{
					Label unit = move.findNiftyControl( "unit", Label.class );
					if ( "Archer".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "A" );
					}
					if ( "Mounted Archer".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "MA" );
					}
					if ( "Clubman".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "C" );
					}
					if ( "Mounted Clubman".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "MC" );
					}
					if ( "Axeman".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "AX" );
					}
					if ( "Catapult".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "CA" );
					}
					if ( "Crossbowman".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "CB" );
					}
					if ( "Krieger".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "K" );
					}
					if ( "Longbowman".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "LB" );
					}
					if ( "Pikeman".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "P" );
					}
					if ( "Swordsman".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "S" );
					}
					if ( "Trebuchet".equals( event.getSelection( ).get( 0 ).getName( ) ) )
					{
						unit.setText( "T" );
					}
					currentUnit = event.getSelection( ).get( 0 ).getEntityId( );
					getApp( ).getNifty( ).showPopup( getApp( ).getNifty( ).getCurrentScreen( ), "move", null );
				}
			}
		}

	}

	@Override
	protected void postExitToNetwork( )
	{
		if ( getApp( ).getNifty( ).getScreen( "hud" ) != null )
		{
			AppState hudState = ( AppState ) getApp( ).getNifty( ).getCurrentScreen( ).getScreenController( );
			if ( hudState != null && getApp( ).getStateManager( ).hasState( hudState ) )
			{
				getApp( ).getStateManager( ).detach( hudState );
			}
			getApp( ).getNifty( ).removeScreen( "hud" );
		}
	}

	@Override
	protected void preExitToNetwork( )
	{
		// TODO Auto-generated method stub

	}

	public void removeFromExisting( Entity entity )
	{
		EntityEntryModelClass toRemove = null;
		for ( EntityEntryModelClass eemc : current.getItems( ) )
		{
			if ( eemc.getEntityId( ).equals( entity.getId( ) ) )
			{
				toRemove = eemc;
				break;
			}
		}

		if ( toRemove != null )
		{
			current.removeItem( toRemove );
		}
	}

	public void setCoords( Pair< Integer, Integer > coords )
	{
		this.currentTile = coords;
	}

	@Override
	public void update( float tpf )
	{
		if ( "tile".equals( getApp( ).getNifty( ).getCurrentScreen( ).getScreenId( ) ) )
		{

			if ( locationSet != null )
			{
				if ( locationSet.applyChanges( ) )
				{
					int currentTurn = getApp( ).getWorldManager( ).getCurrentTurn( );
					for ( Entity e : locationSet.getAddedEntities( ) )
					{
						addToExisting( e, currentTurn );
					}
					for ( Entity e : locationSet.getRemovedEntities( ) )
					{
						removeFromExisting( e );
					}

					fillBuildables( );
				}
			}
			else
			{
				ComponentFilter< LocationTrait > locationFilter = FieldFilter.create( LocationTrait.class, "tile", findTile );
				locationSet = entityData.getEntities( locationFilter, LocationTrait.class );
			}

			if ( actionSet != null )
			{
				if ( actionSet.applyChanges( ) )
				{
					System.out.println( actionSet.size( ) );
					for ( Entity e : actionSet.getAddedEntities( ) )
					{
						EntityId id = e.getId( );
						if ( !toUpdate.contains( id ) )
						{
							toUpdate.add( id );
						}
					}

					for ( Entity e : actionSet.getChangedEntities( ) )
					{
						// TODO: For when we go turned based
					}

					for ( Entity e : actionSet.getRemovedEntities( ) )
					{
						EntityId id = e.getId( );
						if ( !toUpdate.contains( id ) )
						{
							toUpdate.add( id );
						}
					}

					updateExisting( toUpdate );

					current.refresh( );
					fillBuildables( );

				}
			}
			else
			{
				ComponentFilter< LocationTrait > locationFilter = FieldFilter.create( LocationTrait.class, "tile", findTile );
				actionSet = entityData.getEntities( locationFilter, ActionTrait.class );
			}
		}
	}

	public void updateExisting( LinkedList< EntityId > entities )
	{
		int currentTurn = getApp( ).getWorldManager( ).getCurrentTurn( );
		for ( EntityEntryModelClass eemc : current.getItems( ) )
		{
			if ( entities.contains( eemc.getEntityId( ) ) )
			{
				eemc.setName( createDisplayText( eemc.getEntityId( ), currentTurn ) );
			}
		}
		justUpdated = false;
		entities.clear( );
	}

}
