package trendli.me.makhana.common.eventsystem;

public interface EventHandler extends Comparable< EventHandler >
{
	@Override
	public default int compareTo( EventHandler other )
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
	 * This function will handle the provided event and return true if the event
	 * has been completely handled and no further handlers should be called on
	 * the given even. If false, the event should be handled by any remaining
	 * lower priority event handlers.
	 * 
	 * @param event
	 *            the provided event to be handled
	 * @return true if the event has been handled completely and should cease,
	 *         otherwise false
	 */
	public boolean handle( Event event );

}
