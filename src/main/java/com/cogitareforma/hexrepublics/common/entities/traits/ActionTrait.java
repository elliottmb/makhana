package com.cogitareforma.hexrepublics.common.entities.traits;

import java.util.Date;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 * 
 */
@Serializable
public abstract class ActionTrait implements EntityComponent
{

	Date startTime;
	long duration;

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
	protected ActionTrait( Date startTime, long duration )
	{
		this.startTime = startTime;
		this.duration = duration;
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
	 * Returns how long the action should last in milliseconds
	 * 
	 * @return duration of action in milliseconds
	 */
	public long getDuration( )
	{
		return duration;
	}

	@Override
	public String toString( )
	{
		return "ActionTrait[" + getStartTime( ) + ", " + getDuration( ) + "]";
	}

}
