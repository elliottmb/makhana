package com.cogitareforma.makhana.common.eventsystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractEventManager< E, H > implements EventManager< E, H >
{
	private Map< Class< ? extends E >, List< H > > handlerMap;

	public AbstractEventManager( )
	{
		this.handlerMap = new HashMap<>( );
	}

	@Override
	public void addEventHandler( H handler, Class< ? extends E > eventType )
	{
		getEventHandlers( eventType, true ).add( handler );
	}

	@Override
	public void addEventHandler( H handler, @SuppressWarnings( "unchecked" ) Class< ? extends E >... eventTypes )
	{
		for ( Class< ? extends E > eventType : eventTypes )
		{
			addEventHandler( handler, eventType );
		}
	}

	protected List< H > getEventHandlers( Class< ? extends E > eventType, boolean create )
	{
		List< H > result = handlerMap.get( eventType );
		if ( result == null && create )
		{
			result = new CopyOnWriteArrayList<>( );
			handlerMap.put( eventType, result );
		}

		if ( result == null )
		{
			result = Collections.emptyList( );
		}
		return result;
	}

	@Override
	public void removeEventHandler( H handler )
	{
		for ( List< H > handlers : handlerMap.values( ) )
		{
			handlers.remove( handler );
		}
	}

	@Override
	public void removeEventHandler( H handler, @SuppressWarnings( "unchecked" ) Class< ? extends E >... eventTypes )
	{
		for ( Class< ? extends E > eventType : eventTypes )
		{
			removeEventHandler( handler, eventType );
		}
	}

	@Override
	public void removeEventHandler( H handler, Class< ? extends E > eventType )
	{
		List< H > list = getEventHandlers( eventType, false );
		list.remove( handler );
	}

	public abstract boolean triggerEvent( E event );
}
