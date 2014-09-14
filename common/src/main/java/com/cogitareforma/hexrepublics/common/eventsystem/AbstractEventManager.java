package com.cogitareforma.hexrepublics.common.eventsystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractEventManager< E, H > implements EventManager< E, H >
{
	private Map< Class< ? extends E >, List< H > > listenerMap;

	public AbstractEventManager( )
	{
		this.listenerMap = new HashMap<>( );
	}

	public void addEventHandler( Class< ? extends E > eventType, H handler )
	{
		getEventHandlers( eventType, true ).add( handler );
	}

	protected List< H > getEventHandlers( Class< ? extends E > eventType, boolean create )
	{
		List< H > result = listenerMap.get( eventType );
		if ( result == null && create )
		{
			result = new CopyOnWriteArrayList<>( );
			listenerMap.put( eventType, result );
		}

		if ( result == null )
		{
			result = Collections.emptyList( );
		}
		return result;
	}

	public void removeEventHandler( Class< ? extends E > eventType, H handler )
	{
		List< H > list = getEventHandlers( eventType, false );
		list.remove( handler );
	}

	public abstract boolean triggerEvent( E event );
}
