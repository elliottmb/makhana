package trendli.me.makhana.common.eventsystem;

/**
 * 
 * @author Elliott Butler
 *
 * @param <E>
 * @param <H>
 */
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
     * Passes the given event to all appropriate event handlers.
     * 
     * @param event
     *            the triggering event
     */
    public void triggerEvent( E event );
}
