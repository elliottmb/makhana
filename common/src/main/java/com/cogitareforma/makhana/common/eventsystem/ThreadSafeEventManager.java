package com.cogitareforma.makhana.common.eventsystem;

import java.util.Collections;
import java.util.List;

import com.jme3.app.Application;

public class ThreadSafeEventManager extends AbstractEventManager< Event, EventHandler >
{
	private Application app;

	public ThreadSafeEventManager( Application app )
	{
		super( );
		this.app = app;
	}

	public void addEventHandler( EventHandler handler, Class< ? extends Event > event )
	{
		super.addEventHandler( handler, event );
		// Sort from highest priority to lowest
		Collections.sort( getEventHandlers( event, false ), Collections.reverseOrder( ) );
	}

	@Override
	public void triggerEvent( Event event )
	{
		app.enqueue( ( ) ->
		{
			List< EventHandler > handlers = getEventHandlers( event.getClass( ), false );
			if ( handlers.size( ) > 0 )
			{
				for ( EventHandler e : handlers )
				{
					if ( e.handle( event ) )
					{
						break;
					}
				}
			}
			return null;
		} );
	}
}
