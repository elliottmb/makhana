package com.cogitareforma.hexrepublics.client.views;

import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.client.util.EntityEntryModelClass;
import com.cogitareforma.hexrepublics.common.entities.Traits;
import com.cogitareforma.hexrepublics.common.entities.traits.ArcherTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.ArcheryTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.AxemanTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.BarracksTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.CatapultTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.ClubmanTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.CrossbowTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.ForgeTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.KriegerTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.LongbowTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.MachineWorksTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.MountedTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.MoveTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.PikemanTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.SawmillTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.StablesTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.SwordsmanTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TrebuchetTrait;
import com.cogitareforma.hexrepublics.common.net.msg.EntityActionRequest;
import com.cogitareforma.hexrepublics.common.net.msg.EntityCreationRequest;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.es.client.RemoteEntityData;
import com.simsilica.es.filter.AndFilter;
import com.simsilica.es.filter.FieldFilter;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * 
 * @author Ryan Grier
 * @author Elliott Butler
 * 
 */
public class TileViewController extends AbstractAppState implements ScreenController
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( TileViewController.class.getName( ) );

	private ClientMain app;
	private ListBox< EntityEntryModelClass > current;
	private ListBox< String > build;
	private Pair< Integer, Integer > currentTile;
	private EntityId currentUnit;
	private EntitySet locationSet;
	private RemoteEntityData entityData;
	private EntityId findTile;

	private float refreshLimiter = 0;

	private Element move;

	public void bind( Nifty nifty, Screen screen )
	{
	}

	/**
	 * Close the tile view and return to the hud view.
	 */
	public void exitView( )
	{
		app.enqueue( ( ) ->
		{
			app.getStateManager( ).detach( ( AppState ) app.getNifty( ).getCurrentScreen( ).getScreenController( ) );
			app.getNifty( ).removeScreen( "tile" );
			app.getNifty( ).gotoScreen( "hud" );
			return null;
		} );
	}

	@Override
	public void update( float tpf )
	{
		if ( "tile".equals( app.getNifty( ).getCurrentScreen( ).getScreenId( ) ) )
		{

			if ( locationSet != null )
			{
				if ( refreshLimiter > 1 )
				{
					for ( Entity e : locationSet )
					{
						updateExisting( e );
					}
					
					current.refresh( );
					fillBuildables( );
					refreshLimiter = 0;
				}
				else
				{
					refreshLimiter += tpf;
				}

				if ( locationSet.applyChanges( ) )
				{
					for ( Entity e : locationSet.getAddedEntities( ) )
					{
						addToExisting( e );
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

		}
	}

	public void updateExisting( Entity entity )
	{
		for ( EntityEntryModelClass eemc : current.getItems( ) )
		{
			if ( eemc.getEntityId( ).equals( entity.getId( ) ) )
			{
				eemc.setName( createDisplayText( entity.getId( ) ) );
			}
		}
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

	public String createDisplayText( EntityId id )
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
		Pair< String, Double > action = Traits.getActionDetails( entityData, id );
		if ( action != null )
		{
			existing += String.format( " - %s: ", action.getLeft( ) );
			if ( action.getRight( ) > 1 )
			{
				existing += String.format( "%.1f minutes", action.getRight( ) );
			}
			else
			{
				existing += String.format( "%.0f seconds", action.getRight( ) * 60 );
			}
		}

		return existing;
	}

	public void addToExisting( Entity entity )
	{
		EntityId id = entity.getId( );

		EntityEntryModelClass eemc = new EntityEntryModelClass( id, createDisplayText( id ) );
		current.addItem( eemc );
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
					app.getGameConnManager( ).send(
							new EntityActionRequest( currentUnit, new MoveTrait( new Date( ), Traits.getMovementModifier( entityData,
									currentUnit ), nextTile ) ) );
				}
			}
		}

		app.getNifty( ).closePopup( "move" );
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
					app.getNifty( ).showPopup( app.getNifty( ).getCurrentScreen( ), "move", null );
				}
			}
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
			EntityId location = app.getGameConnManager( ).getRemoteEntityData( ).findEntity( completeFilter, TileTrait.class );
			// BUILDINGS
			if ( "Build Archery".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Tyring to build Archery" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ArcheryTrait( ) ) );
			}
			if ( "Build Barracks".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Barracks" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new BarracksTrait( ) ) );
			}
			if ( "Build Stables".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Stables" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new StablesTrait( ) ) );
			}
			if ( "Build Forge".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Forge" );
				app.getGameConnManager( ).send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ForgeTrait( ) ) );
			}
			if ( "Build Sawmill".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Sawmill" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new SawmillTrait( ) ) );
			}
			if ( "Build Machine Works".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Machine Works" );
				app.getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new MachineWorksTrait( ) ) );
			}

			// UNITS
			if ( "Build Archer".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Archer" );
				app.getGameConnManager( ).send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ArcherTrait( ) ) );
			}
			if ( "Build Clubman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Clubman" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ClubmanTrait( ) ) );
			}
			if ( "Build Mounted Archer".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Tyring to build Mounted Archer" );
				app.getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ArcherTrait( ), new MountedTrait( ) ) );
			}
			if ( "Build Mounted Clubman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Mounted Clubman" );
				app.getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new ClubmanTrait( ), new MountedTrait( ) ) );
			}
			if ( "Build Axeman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Axeman" );
				app.getGameConnManager( ).send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new AxemanTrait( ) ) );
			}
			if ( "Build Catapult".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Catapult" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new CatapultTrait( ) ) );
			}
			if ( "Build Crossbowman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Crossbowman" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new CrossbowTrait( ) ) );
			}
			if ( "Build Krieger".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Krieger" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new KriegerTrait( ) ) );
			}
			if ( "Build Longbowman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Longbowman" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new LongbowTrait( ) ) );
			}
			if ( "Build Pikeman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Pikeman" );
				app.getGameConnManager( )
						.send( new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new PikemanTrait( ) ) );
			}
			if ( "Build Swordsman".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Swordsman" );
				app.getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new SwordsmanTrait( ) ) );
			}
			if ( "Build Trebuchet".equals( event.getSelection( ).get( 0 ) ) )
			{
				System.out.println( "Trying to build Trebuchet" );
				app.getGameConnManager( ).send(
						new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), new TrebuchetTrait( ) ) );
			}
		}
	}

	public void fillBuildables( )
	{
		if ( entityData != null )
		{
			Stack< String > buildables = new Stack< String >( );

			if ( Traits.countBuildings( entityData, locationSet ) < 6 )
			{
				if ( !Traits.hasPrerequisites( entityData, locationSet, ArcheryTrait.class ) )
				{
					buildables.add( "Build Archery" );
				}
				if ( !Traits.hasPrerequisites( entityData, locationSet, BarracksTrait.class ) )
				{
					buildables.add( "Build Barracks" );
				}
				if ( !Traits.hasPrerequisites( entityData, locationSet, StablesTrait.class ) )
				{
					buildables.add( "Build Stables" );
				}
				if ( !Traits.hasPrerequisites( entityData, locationSet, ForgeTrait.class ) )
				{
					buildables.add( "Build Forge" );
				}
				if ( !Traits.hasPrerequisites( entityData, locationSet, MachineWorksTrait.class ) )
				{
					buildables.add( "Build Machine Works" );
				}
				if ( !Traits.hasPrerequisites( entityData, locationSet, SawmillTrait.class ) )
				{
					buildables.add( "Build Sawmill" );
				}
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
				if ( Traits.countBuildings( entityData, locationSet ) == 6 )
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
		}
	}

	/**
	 * Updates and displays selected tile info.
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void initialize( AppStateManager stateManager, Application app )
	{
		super.initialize( stateManager, app );
		this.app = ( ClientMain ) app;
		current = this.app.getNifty( ).getCurrentScreen( ).findNiftyControl( "existBox", ListBox.class );
		build = this.app.getNifty( ).getScreen( "tile" ).findNiftyControl( "buildBox", ListBox.class );
		move = this.app.getNifty( ).createPopupWithId( "move", "move" );
		entityData = this.app.getGameConnManager( ).getRemoteEntityData( );
		ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", this.currentTile.getLeft( ) );
		ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", this.currentTile.getRight( ) );
		ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );
		findTile = entityData.findEntity( completeFilter, TileTrait.class );

		ComponentFilter< LocationTrait > locationFilter = FieldFilter.create( LocationTrait.class, "tile", findTile );
		locationSet = entityData.getEntities( locationFilter, LocationTrait.class );

		if ( locationSet != null )
		{
			if ( locationSet.applyChanges( ) )
			{
				for ( Entity e : locationSet.getAddedEntities( ) )
				{
					addToExisting( e );
				}
			}
		}

		logger.log( Level.INFO, "Initialised " + this.getClass( ) );
		this.app.currentScreen = "tile";

		fillBuildables( );
	}

	public void setCoords( Pair< Integer, Integer > coords )
	{
		this.currentTile = coords;
	}

	public void closeMove( )
	{
		this.app.getNifty( ).closePopup( "move" );
	}

	public Pair< Integer, Integer > getCoords( )
	{
		return this.currentTile;
	}

	public void onEndScreen( )
	{
		if ( locationSet != null )
		{
			locationSet.clear( );
		}
		logger.log( Level.INFO, "Tile Screen Stopped" );
	}

	public void onStartScreen( )
	{
		logger.log( Level.INFO, "Tile Screen Started" );
	}

}
