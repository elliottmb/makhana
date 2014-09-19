package com.cogitareforma.hexrepublics.gameserver.eventsystem.handlers;

import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventHandler;
import com.cogitareforma.hexrepublics.common.eventsystem.events.TileOwnerChangedEvent;

public class TileOwnerChangedEventHandler implements EntityEventHandler
{
	private final static Logger logger = Logger.getLogger( TileOwnerChangedEventHandler.class.getName( ) );

	@Override
	public byte getPriority( )
	{
		return 0;
	};

	@Override
	public boolean handle( EntityEvent event )
	{
		if ( event instanceof TileOwnerChangedEvent )
		{
			TileOwnerChangedEvent tileOwnerChanged = ( TileOwnerChangedEvent ) event;
			System.out.println( "WOO!!!" + tileOwnerChanged.getOldOwner( ) + ", " + tileOwnerChanged.getNewOwner( ) );
		}
		return true;
	}
}