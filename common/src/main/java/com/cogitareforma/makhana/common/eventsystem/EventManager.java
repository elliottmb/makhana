package com.cogitareforma.makhana.common.eventsystem;

public interface EventManager< E, H >
{
	/**
	 * 
	 * @param handler
	 * @param eventType
	 */
	public void addEventHandler( H handler, Class< ? extends E > eventType );

	/**
	 * 
	 * @param handler
	 * @param eventTypes
	 */
	public void addEventHandler( H handler, @SuppressWarnings( "unchecked" ) Class< ? extends E >... eventTypes );

	/**
	 * 
	 * @param handler
	 */
	public void removeEventHandler( H handler );

	/**
	 * 
	 * @param handler
	 * @param eventType
	 */
	public void removeEventHandler( H handler, Class< ? extends E > eventType );

	/**
	 * 
	 * @param handler
	 * @param eventTypes
	 */
	public void removeEventHandler( H handler, @SuppressWarnings( "unchecked" ) Class< ? extends E >... eventTypes );

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
