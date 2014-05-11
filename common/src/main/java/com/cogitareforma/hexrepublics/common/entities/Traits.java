package com.cogitareforma.hexrepublics.common.entities;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.common.entities.traits.ActionTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.DefenseTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.FabricatingTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.HealthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.MoveableTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.StaticTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.StrengthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TypeTrait;
import com.cogitareforma.hexrepublics.common.util.PackageUtils;
import com.cogitareforma.hexrepublics.common.util.WorldFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 * 
 * @author Elliott Butler
 *
 */
@SuppressWarnings( "unchecked" )
public class Traits
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( Traits.class.getName( ) );

	public static final int BASE_MOVEMENT_DURATION = 60000; // change back to
															// 60000

	public static final float BASE_FABRICATING_TIME = 0.5f; // change back to
															// 0.5f

	public static final List< Class< ? >> allTypes = getAllTraits( );

	public static final List< Class< ? extends EntityComponent >> actionTraits = getSubTraits( ActionTrait.class );

	public static final List< Class< ? extends EntityComponent >> unitTraits = getSubTraits( MoveableTrait.class );

	public static final List< Class< ? extends EntityComponent >> buildingTraits = getSubTraits( StaticTrait.class );

	public static final List< List< Pair< Integer, Integer > >> neighbors = getNeighbors( );

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

	public static < T extends EntityComponent > int countBuildings( EntityData entityData, Set< EntityId > idSet )
	{
		int count = 0;
		for ( EntityId id : idSet )
		{
			for ( Class< ? > c : buildingTraits )
			{
				if ( entityData.getComponent( id, ( Class< T > ) c ) != null )
				{
					count++;
					break;
				}
			}
		}
		return count;
	}

	public static < T extends EntityComponent > int countUnits( EntityData entityData, Set< EntityId > idSet )
	{
		int count = 0;
		for ( EntityId id : idSet )
		{
			for ( Class< ? > c : unitTraits )
			{
				if ( entityData.getComponent( id, ( Class< T > ) c ) != null )
				{
					count++;
					break;
				}
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
	public static void entityParturition( EntityData entityData, EntityId id, EntityComponent[ ] components )
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
			entityData.setComponent( id, new HealthTrait( health ) );
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
			entityData.setComponent( id, new FabricatingTrait( new Date( ), ( long ) ( complexity * 60000 ) ) );
		}
	}

	public static < T extends EntityComponent > Pair< String, Double > getActionDetails( EntityData entityData, EntityId id )
	{
		for ( Class< ? > actionTrait : actionTraits )
		{
			ActionTrait at = ( ActionTrait ) entityData.getComponent( id, ( Class< T > ) actionTrait );
			if ( at != null )
			{
				double time = ( at.getStartTime( ).getTime( ) + at.getDuration( ) - new Date( ).getTime( ) ) / 60000.0;
				return Pair.of( at.toVerb( ), time );
			}
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
		for ( Class< ? > c : unitTraits )
		{
			if ( entityData.getComponent( id, ( Class< T > ) c ) != null )
			{
				str += ( ( TypeTrait ) entityData.getComponent( id, ( Class< T > ) c ) ).getInitialDefense( );
			}
		}
		return str;
	}

	public static < T extends EntityComponent > int getMovementModifier( EntityData entityData, EntityId id )
	{
		int movement = Traits.BASE_MOVEMENT_DURATION;
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

	public static EntityId getOwner( EntityData entityData, EntityId id )
	{
		EntityId ownerId = null;

		if ( id != null )
		{
			Entity theEntity = entityData.getEntity( id, CreatedBy.class, LocationTrait.class );

			if ( theEntity != null )
			{
				if ( theEntity.get( CreatedBy.class ) != null )
				{
					ownerId = theEntity.get( CreatedBy.class ).getCreatorId( );
				}

				if ( ownerId == null && theEntity.get( LocationTrait.class ) != null )
				{
					LocationTrait location = theEntity.get( LocationTrait.class );
					if ( location.getTile( ) != null )
					{
						ownerId = getOwner( entityData, location.getTile( ) );
					}
				}
			}
		}
		return ownerId;
	}

	public static Vector3f getSpatialPosition( int tileX, int tileY, byte position, TerrainQuad terrain, float adjustment )
	{
		Vector3f centerPoint = WorldFactory.createCenterPoint( 257, 10f, tileX + 1, tileY + 1 );

		float angle = 2 * FastMath.PI / 6 * position + adjustment;
		float x = centerPoint.x + 7f * FastMath.cos( angle );
		float z = centerPoint.z + 7f * FastMath.sin( angle );
		float y = 3;

		if ( !Float.isNaN( terrain.getHeight( new Vector2f( x, z ) ) ) )
		{
			y = terrain.getHeight( new Vector2f( x, z ) ) + 1.5f;
		}

		return new Vector3f( x, y, z );
	}

	public static < T extends EntityComponent > float getStrength( EntityData entityData, EntityId id )
	{
		float str = 0f;
		for ( Class< ? > c : unitTraits )
		{
			if ( entityData.getComponent( id, ( Class< T > ) c ) != null )
			{
				str += ( ( TypeTrait ) entityData.getComponent( id, ( Class< T > ) c ) ).getInitialStrength( );
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

	public static < T extends EntityComponent > boolean hasPrerequisites( EntityData entityData, Set< EntityId > idSet,
			Class< ? >... prerequisites )
	{
		if ( prerequisites.length > 0 )
		{
			boolean hasPrereq[ ] = new boolean[ prerequisites.length ];
			for ( int i = 0; i < prerequisites.length; i++ )
			{
				for ( EntityId id : idSet )
				{
					if ( entityData.getComponent( id, ( Class< T > ) prerequisites[ i ] ) != null )
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

	public static < T extends EntityComponent > boolean inAction( EntityData entityData, EntityId id )
	{
		for ( Class< ? > c : actionTraits )
		{
			if ( entityData.getComponent( id, ( Class< T > ) c ) != null )
			{
				return true;
			}
		}
		return false;
	}

	public static < T extends EntityComponent > boolean isBuilding( EntityData entityData, EntityId id )
	{
		for ( Class< ? > c : buildingTraits )
		{
			if ( entityData.getComponent( id, ( Class< T > ) c ) != null )
			{
				return true;
			}
		}
		return false;
	}

	public static < T extends EntityComponent > boolean isUnit( EntityData entityData, EntityId id )
	{
		for ( Class< ? > c : unitTraits )
		{
			if ( entityData.getComponent( id, ( Class< T > ) c ) != null )
			{
				return true;
			}
		}
		return false;

	}
}
