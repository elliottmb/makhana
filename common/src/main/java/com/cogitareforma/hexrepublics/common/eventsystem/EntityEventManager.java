package com.cogitareforma.hexrepublics.common.eventsystem;

import java.util.Collections;
import java.util.List;

public class EntityEventManager extends AbstractEventManager< EntityEvent, EntityEventHandler >
{

	public EntityEventManager( )
	{
		super( );
	}

	public void addEventHandler( EntityEventHandler handler, Class< ? extends EntityEvent > event )
	{
		super.addEventHandler( handler, event );
		// Sort from highest priority to lowest
		Collections.sort( getEventHandlers( event, false ), Collections.reverseOrder( ) );
	}

	public boolean triggerEvent( EntityEvent event )
	{
		List< EntityEventHandler > handlers = getEventHandlers( event.getClass( ), false );
		if ( handlers.size( ) > 0 )
		{
			for ( EntityEventHandler e : handlers )
			{
				if ( e.handle( event ) )
				{
					continue;
				}
				return false;
			}
		}
		else
		{
			return false;
		}
		return true;
	}
}
