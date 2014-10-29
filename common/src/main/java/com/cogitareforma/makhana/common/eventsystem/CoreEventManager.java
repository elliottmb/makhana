package com.cogitareforma.makhana.common.eventsystem;

import java.util.Collections;
import java.util.List;

import com.simsilica.es.EntityData;

public class CoreEventManager extends AbstractEventManager< Event, EventHandler >
{

	public CoreEventManager( EntityData entityData )
	{
		super( );
	}

	public void addEventHandler( EventHandler handler, Class< ? extends EntityEvent > event )
	{
		super.addEventHandler( handler, event );
		// Sort from highest priority to lowest
		Collections.sort( getEventHandlers( event, false ), Collections.reverseOrder( ) );
	}

	@Override
	public boolean triggerEvent( Event event )
	{
		List< EventHandler > handlers = getEventHandlers( event.getClass( ), false );
		if ( handlers.size( ) > 0 )
		{
			for ( EventHandler e : handlers )
			{
				if ( e.handle( event ) )
				{
					return false;
				}
			}
		}
		else
		{
			return false;
		}
		return true;
	}
}
