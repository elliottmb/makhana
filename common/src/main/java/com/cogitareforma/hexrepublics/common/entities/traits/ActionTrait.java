package com.cogitareforma.hexrepublics.common.entities.traits;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cogitareforma.hexrepublics.common.entities.ActionType;
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
	private Date startTime;
	private long duration;
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
	public ActionTrait( Date startTime, long duration, ActionType type, Map< String, Object > data ) throws IllegalArgumentException
	{
		/*
		 * We need a valid type and we need the data for that type, and we need
		 * our start and duration to be valid
		 */
		if ( type == null || data == null || startTime == null || duration <= 0 )
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

		setStartTime( startTime );
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
	public long getDuration( )
	{
		return duration;
	}

	/**
	 * Returns the time that this action started
	 * 
	 * @return the start time of the action
	 */
	public Date getStartTime( )
	{
		return startTime;
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
	private void setDuration( long duration )
	{
		this.duration = duration;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	private void setStartTime( Date startTime )
	{
		this.startTime = startTime;
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
		return "ActionTrait[" + toVerb( ) + ": " + getStartTime( ) + ", " + getDuration( ) + "]";
	}

	public String toVerb( )
	{
		return getType( ).getVerb( );
	}
}
