package com.cogitareforma.makhana.common.eventsystem;

import java.util.Collections;
import java.util.List;

import com.simsilica.es.EntityData;

public class EntityEventManager extends AbstractEventManager< EntityEvent, EntityEventHandler >
{
	private EntityData entityData;

	public EntityEventManager( EntityData entityData )
	{
		super( );

		this.entityData = entityData;
	}

	public void addEventHandler( EntityEventHandler handler, Class< ? extends EntityEvent > event )
	{
		super.addEventHandler( handler, event );
		// Sort from highest priority to lowest
		Collections.sort( getEventHandlers( event, false ), Collections.reverseOrder( ) );
	}

	public EntityData getEntityData( )
	{
		return entityData;
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
