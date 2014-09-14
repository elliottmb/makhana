package com.cogitareforma.hexrepublics.common.eventsystem;

public interface EntityEventHandler extends Comparable< EntityEventHandler >
{
	public default int compareTo( EntityEventHandler other )
	{
		return ( getPriority( ) < other.getPriority( ) ) ? -1 : ( ( getPriority( ) == other.getPriority( ) ) ? 0 : 1 );
	}

	/**
	 * Returns the byte value representing the priority of this handler.
	 * Handlers with a higher priority are handled first. This allows for an
	 * event handler to cancel an event or change event behavior before lower
	 * priority handlers are called. ( Byte.MAX_VALUE served first )
	 * 
	 * @return byte value representing priority ( Byte.MAX_VALUE served first )
	 */
	public default byte getPriority( )
	{
		return 0;
	}

	/**
	 * This function is to handle the provided event and return true if the
	 * event should continue to be handled by any other lower priority event
	 * handlers. If false is returned, the event will be closed and no further
	 * event handlers will be called.
	 * 
	 * @param event
	 *            the provided event to be handled
	 * @return true if the event should continue to be handled, otherwise false
	 */
	public boolean handle( EntityEvent event );

}
