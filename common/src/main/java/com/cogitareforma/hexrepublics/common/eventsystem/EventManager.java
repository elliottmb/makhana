package com.cogitareforma.hexrepublics.common.eventsystem;

public interface EventManager< E, H >
{
	public void addEventHandler( Class< ? extends E > event, H handler );

	public void removeEventHandler( Class< ? extends E > event, H handler );

	/**
	 * Returns true if the event was handled by all appropriate event handlers,
	 * otherwise it returns false. It will return false if there are no handlers
	 * for the event, or one of them has canceled the event.
	 * 
	 * @param event
	 *            the triggering event
	 * @return true if the event was handled by all handlers, otherwise false
	 */
	public boolean triggerEvent( E event );
}
