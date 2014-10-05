package com.cogitareforma.makhana.common.entities;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.makhana.common.entities.components.ActionTrait;
import com.cogitareforma.makhana.common.entities.components.DefenseTrait;
import com.cogitareforma.makhana.common.entities.components.Health;
import com.cogitareforma.makhana.common.entities.components.LocationTrait;
import com.cogitareforma.makhana.common.entities.components.MoveableTrait;
import com.cogitareforma.makhana.common.entities.components.StaticTrait;
import com.cogitareforma.makhana.common.entities.components.StrengthTrait;
import com.cogitareforma.makhana.common.entities.components.TileTrait;
import com.cogitareforma.makhana.common.entities.components.TypeTrait;
import com.cogitareforma.makhana.common.util.PackageUtils;
import com.cogitareforma.makhana.common.util.WorldFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

/**
 * 
 * @author Elliott Butler
 *
 */
@SuppressWarnings( "unchecked" )
public class ComponentUtil
{
	public static boolean areNeighbors( EntityData entityData, EntityId source, EntityId target )
	{
		TileTrait srcTile = entityData.getComponent( source, TileTrait.class );
		TileTrait trgTile = entityData.getComponent( target, TileTrait.class );
		if ( srcTile != null && trgTile != null )
		{
			int parity = srcTile.getX( ) % 2;
			for ( Pair< Integer, Integer > p : neighbors.get( parity ) )
			{
				if ( srcTile.getX( ) + p.getLeft( ) == trgTile.getX( ) && srcTile.getY( ) + p.getRight( ) == trgTile.getY( ) )
				{
					return true;
				}
			}

		}
		return false;
	}

	public static int countBuildings( EntityData entityData, EntitySet entitySet )
	{
		Set< EntityId > idSet = new HashSet< EntityId >( entitySet.size( ) );
		for ( Entity e : entitySet )
		{
			idSet.add( e.getId( ) );
		}
		return countBuildings( entityData, idSet );
	}

	public static int countBuildings( EntityData entityData, Set< EntityId > idSet )
	{
		int count = 0;
		for ( EntityId id : idSet )
		{
			if ( isBuilding( entityData, id ) )
			{
				count++;
			}
		}
		return count;
	}

	public static int countUnits( EntityData entityData, EntitySet entitySet )
	{
		Set< EntityId > idSet = new HashSet< EntityId >( entitySet.size( ) );
		for ( Entity e : entitySet )
		{
			idSet.add( e.getId( ) );
		}
		return countUnits( entityData, idSet );
	}

	public static int countUnits( EntityData entityData, Set< EntityId > idSet )
	{
		int count = 0;
		for ( EntityId id : idSet )
		{
			if ( isUnit( entityData, id ) )
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * Attempts to build and add a HealthTrait, StrengthTrait, DefenseTrait,
	 * FabricatingTrait for the given EntityId given the given EntityComponents,
	 * essentially bringing it to life.
	 * 
	 * @param entityData
	 *            EntityData where the Entity resides
	 * @param id
	 *            The Id of the Entity to birth
	 * @param components
	 *            The given Components to consider when constructing the traits
	 */
	public static void entityParturition( EntityData entityData, EntityId id, EntityComponent[ ] components, int currentTurn )
	{
		float health = 0;
		float strength = 0;
		float defense = 0;
		float complexity = 0;
		for ( EntityComponent ec : components )
		{
			if ( ec instanceof TypeTrait )
			{
				TypeTrait type = ( TypeTrait ) ec;
				health += ( type.getInitialHealth( ) >= 0 ) ? type.getInitialHealth( ) : 0;
				strength += ( type.getInitialStrength( ) >= 0 ) ? type.getInitialStrength( ) : 0;
				defense += ( type.getInitialDefense( ) >= 0 ) ? type.getInitialDefense( ) : 0;
				complexity += ( type.getFabricatingTime( ) >= 0 ) ? type.getFabricatingTime( ) : 0;
			}
		}
		if ( health > 0 )
		{
			logger.log( Level.INFO, String.format( "Adding HealthTrait(%f) to entity %s", health, id ) );
			entityData.setComponent( id, new Health( health ) );
		}
		if ( strength > 0 )
		{
			logger.log( Level.INFO, String.format( "Adding StrengthTrait(%f) to entity %s", strength, id ) );
			entityData.setComponent( id, new StrengthTrait( strength ) );
		}
		if ( defense > 0 )
		{
			logger.log( Level.INFO, String.format( "Adding DefenseTrait(%f) to entity %s", defense, id ) );
			entityData.setComponent( id, new DefenseTrait( defense ) );
		}

		if ( complexity > 0 )
		{
			logger.log( Level.INFO, String.format( "Adding FabricatingTrait(%d) to entity %s", ( long ) ( complexity * 60000 ), id ) );

			entityData.setComponent( id,
					new ActionTrait( currentTurn, ( int ) ( complexity * 6 ), ActionType.FABRICATING, Collections.emptyMap( ) ) );
		}
	}

	public static Pair< String, Integer > getActionRemainingTurns( EntityData entityData, EntityId id, int currentTurn )
	{
		ActionTrait at = entityData.getComponent( id, ActionTrait.class );
		if ( at != null )
		{
			// TODO:
			System.out.println( at.getStartTurn( ) + " + " + at.getDuration( ) + " - " + currentTurn + " = "
					+ ( ( at.getStartTurn( ) + at.getDuration( ) ) - currentTurn ) );
			int time = ( at.getStartTurn( ) + at.getDuration( ) ) - currentTurn;
			return Pair.of( at.toVerb( ), time );
		}
		return null;
	}

	private static List< Class< ? >> getAllTraits( )
	{
		try
		{
			return PackageUtils.getAllConcreteClassesFromNames( PackageUtils
					.getAllClassNamesInPackage( "com.cogitareforma.hexrepublics.common.entities.traits" ) );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Failed to get all trait types" );
		}
		return Collections.emptyList( );
	}

	public static < T extends EntityComponent > float getDefense( EntityData entityData, EntityId id )
	{
		float str = 0f;
		for ( Class< ? extends EntityComponent > c : unitTraits )
		{
			TypeTrait tt = ( TypeTrait ) entityData.getComponent( id, c );
			if ( tt != null )
			{
				str += tt.getInitialDefense( );
			}
		}
		return str;
	}

	public static < T extends EntityComponent > int getMovementModifier( EntityData entityData, EntityId id )
	{
		int movement = ComponentUtil.BASE_MOVEMENT_DURATION;
		for ( Class< ? > c : unitTraits )
		{
			MoveableTrait mt = ( MoveableTrait ) entityData.getComponent( id, ( Class< T > ) c );
			if ( mt != null )
			{
				movement = ( int ) ( movement * mt.getMovementMultiplier( ) );
			}
		}

		return movement;
	}

	private static List< List< Pair< Integer, Integer >>> getNeighbors( )
	{
		List< Pair< Integer, Integer >> one = new LinkedList< Pair< Integer, Integer >>( );
		one.add( Pair.of( 1, 1 ) ); // NW
		one.add( Pair.of( 1, 0 ) ); // SW
		one.add( Pair.of( 0, -1 ) ); // S
		one.add( Pair.of( -1, 0 ) ); // SE
		one.add( Pair.of( -1, +1 ) ); // NE
		one.add( Pair.of( 0, 1 ) ); // N

		List< Pair< Integer, Integer >> two = new LinkedList< Pair< Integer, Integer >>( );
		two.add( Pair.of( 1, 0 ) ); // NW = 0
		two.add( Pair.of( 1, -1 ) ); // SW = 1
		two.add( Pair.of( 0, -1 ) ); // S = 2
		two.add( Pair.of( -1, -1 ) ); // SE = 3
		two.add( Pair.of( -1, 0 ) ); // NE = 4
		two.add( Pair.of( 0, 1 ) ); // N = 5

		List< List< Pair< Integer, Integer >>> result = new LinkedList< List< Pair< Integer, Integer >>>( );
		result.add( one );
		result.add( two );
		return result;
	}

	public static byte getOpenBuildingPosition( EntityData entityData, Set< EntityId > idSet )
	{
		return getOpenOfKindPosition( entityData, idSet, ( EntityData ed, EntityId id ) ->
		{
			return isBuilding( ed, id );
		} );
	}

	private static byte getOpenOfKindPosition( EntityData entityData, Set< EntityId > idSet, BiPredicate< EntityData, EntityId > isOfKind )
	{
		boolean[ ] slots = new boolean[ 6 ];
		for ( EntityId id : idSet )
		{
			if ( isOfKind.test( entityData, id ) )
			{
				LocationTrait lt = entityData.getComponent( id, LocationTrait.class );
				if ( lt != null )
				{
					slots[ lt.getPosition( ) % 6 ] = true;
				}
			}
		}

		for ( byte i = 0; i < slots.length; i++ )
		{
			if ( !slots[ i ] )
			{
				return i;
			}
		}

		return 0;
	}

	public static byte getOpenUnitPosition( EntityData entityData, Set< EntityId > idSet )
	{
		return getOpenOfKindPosition( entityData, idSet, ( EntityData ed, EntityId id ) ->
		{
			return isUnit( ed, id );
		} );
	}

	public static EntityId getOwner( EntityData entityData, EntityId id )
	{
		EntityId ownerId = null;

		if ( id != null )
		{
			Entity theEntity = entityData.getEntity( id, CreatedBy.class, LocationTrait.class );

			if ( theEntity != null )
			{
				CreatedBy createdBy = theEntity.get( CreatedBy.class );
				if ( createdBy != null )
				{
					ownerId = createdBy.getCreatorId( );
				}

				if ( ownerId == null )
				{
					LocationTrait location = theEntity.get( LocationTrait.class );
					if ( location != null )
					{
						if ( location.getTile( ) != null )
						{
							ownerId = getOwner( entityData, location.getTile( ) );
						}
					}
				}
			}
		}
		return ownerId;
	}

	public static Vector3f getSpatialPosition( int tileX, int tileY, byte position, TerrainQuad terrain, float initalRotation,
			float heightAdjustment )
	{
		Vector3f centerPoint = WorldFactory.createCenterPoint( terrain.getTerrainSize( ), ( terrain.getTerrainSize( ) - 1 ) / 256 * 10f,
				tileX + 1, tileY + 1 );

		float angle = 2 * FastMath.PI / 6 * position + initalRotation;
		float x = centerPoint.x + 24f * FastMath.cos( angle );
		float z = centerPoint.z + 24f * FastMath.sin( angle );
		float y = 3;

		if ( !Float.isNaN( terrain.getHeight( new Vector2f( x, z ) ) ) )
		{
			y = terrain.getHeight( new Vector2f( x, z ) ) + heightAdjustment;
		}

		return new Vector3f( x, y, z );
	}

	public static float getStrength( EntityData entityData, EntityId id )
	{
		float str = 0f;
		for ( Class< ? extends EntityComponent > c : unitTraits )
		{
			TypeTrait tt = ( TypeTrait ) entityData.getComponent( id, c );
			if ( tt != null )
			{
				str += tt.getInitialStrength( );
			}
		}
		return str;
	}

	private static List< Class< ? extends EntityComponent > > getSubTraits( Class< ? extends EntityComponent > parent )
	{
		List< Class< ? extends EntityComponent >> subTraits = new LinkedList< Class< ? extends EntityComponent >>( );
		for ( Class< ? > c : allTypes )
		{
			if ( parent.isAssignableFrom( c ) )
			{
				subTraits.add( ( Class< ? extends EntityComponent > ) c );
			}
		}
		return subTraits;
	}

	@SafeVarargs
	public static boolean hasPrerequisites( EntityData entityData, EntitySet entitySet, Class< ? extends EntityComponent >... prerequisites )
	{
		if ( prerequisites.length > 0 )
		{
			Set< EntityId > idSet = new HashSet< EntityId >( entitySet.size( ) );
			for ( Entity e : entitySet )
			{
				idSet.add( e.getId( ) );
			}
			return hasPrerequisites( entityData, idSet, prerequisites );
		}
		return false;
	}

	public static boolean hasPrerequisites( EntityData entityData, Set< EntityId > idSet,
			Class< ? extends EntityComponent >... prerequisites )
	{
		if ( prerequisites.length > 0 )
		{
			boolean hasPrereq[ ] = new boolean[ prerequisites.length ];
			for ( int i = 0; i < prerequisites.length; i++ )
			{
				for ( EntityId id : idSet )
				{
					if ( entityData.getComponent( id, prerequisites[ i ] ) != null )
					{
						if ( !inAction( entityData, id ) )
						{
							hasPrereq[ i ] = true;
							break;
						}
					}
				}
			}

			for ( boolean prereq : hasPrereq )
			{
				if ( !prereq )
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean inAction( EntityData entityData, EntityId id )
	{
		return entityData.getComponent( id, ActionTrait.class ) != null;
	}

	public static boolean isBuilding( EntityData entityData, EntityId id )
	{
		return isOfKind( entityData, id, buildingTraits );
	}

	private static boolean isOfKind( EntityData entityData, EntityId id, List< Class< ? extends EntityComponent >> traits )
	{
		for ( Class< ? extends EntityComponent > c : traits )
		{
			if ( entityData.getComponent( id, c ) != null )
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isUnit( EntityData entityData, EntityId id )
	{
		return isOfKind( entityData, id, unitTraits );
	}

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( ComponentUtil.class.getName( ) );

	public static final int BASE_MOVEMENT_DURATION = 6; // change back to 6

	public static final float BASE_FABRICATING_TIME = 0.5f; // change back to
															// 0.5f

	public static final List< Class< ? >> allTypes = getAllTraits( );

	public static final List< Class< ? extends EntityComponent >> unitTraits = getSubTraits( MoveableTrait.class );

	public static final List< Class< ? extends EntityComponent >> buildingTraits = getSubTraits( StaticTrait.class );

	public static final List< List< Pair< Integer, Integer > >> neighbors = getNeighbors( );
}
