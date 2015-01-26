package trendli.me.makhana.common.entities.components;

import java.util.HashMap;
import java.util.Map;

import trendli.me.makhana.common.entities.ActionType;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public class ActionTrait implements EntityComponent
{
	private int startTurn;
	private int duration;
	private ActionType type;
	private HashMap< String, Object > data;

	/**
	 * Used by Serializer
	 */
	public ActionTrait( )
	{

	}

	/**
	 * Used by subclasses to construct the base parts of the ActionTrait with
	 * the given start time and duration.
	 * 
	 * @param startTime
	 *            when the action begins
	 * @param duration
	 *            how long the action should last (milliseconds)
	 */
	public ActionTrait( int startTurn, int duration, ActionType type, Map< String, Object > data ) throws IllegalArgumentException
	{
		/*
		 * We need a valid type and we need the data for that type, and we need
		 * our start and duration to be valid
		 */
		if ( type == null || data == null || startTurn < 0 || duration <= 0 )
		{
			throw new IllegalArgumentException( );
		}

		HashMap< String, Object > tempData = new HashMap< String, Object >( data );

		tempData.keySet( ).retainAll( type.getDataKeys( ) );

		/* Check that we have the expected amount of data keys */
		if ( tempData.keySet( ).size( ) < type.getDataKeys( ).size( ) )
		{
			throw new IllegalArgumentException( );
		}

		setStartTurn( startTurn );
		setDuration( duration );
		setType( type );
		/*
		 * Let's make our stored data immutable for thread safety and the
		 * convention that you should replace EntityComponents when changes are
		 * made instead of editing them directly
		 */
		setData( tempData );

	}

	/**
	 * @return the data
	 */
	public HashMap< String, Object > getData( )
	{
		return data;
	}

	/**
	 * Returns how long the action should last in milliseconds
	 * 
	 * @return duration of action in milliseconds
	 */
	public int getDuration( )
	{
		return duration;
	}

	/**
	 * Returns the time that this action started
	 * 
	 * @return the start time of the action
	 */
	public int getStartTurn( )
	{
		return startTurn;
	}

	/**
	 * @return the type
	 */
	public ActionType getType( )
	{
		return type;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	private void setData( HashMap< String, Object > data )
	{
		this.data = data;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	private void setDuration( int duration )
	{
		this.duration = duration;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	private void setStartTurn( int startTurn )
	{
		this.startTurn = startTurn;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	private void setType( ActionType type )
	{
		this.type = type;
	}

	@Override
	public String toString( )
	{
		return "ActionTrait[" + toVerb( ) + ": " + getStartTurn( ) + ", " + getDuration( ) + "]";
	}

	public String toVerb( )
	{
		return getType( ).getVerb( );
	}
}
