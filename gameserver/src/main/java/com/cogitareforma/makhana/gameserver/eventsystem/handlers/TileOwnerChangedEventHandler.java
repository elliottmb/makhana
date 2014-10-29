package com.cogitareforma.makhana.gameserver.eventsystem.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.eventsystem.Event;
import com.cogitareforma.makhana.common.eventsystem.EventHandler;
import com.cogitareforma.makhana.common.eventsystem.events.TileCapturedEvent;
import com.cogitareforma.makhana.common.eventsystem.events.TileClaimedEvent;
import com.cogitareforma.makhana.common.eventsystem.events.TileFreedEvent;

public class TileOwnerChangedEventHandler implements EventHandler
{
	private final static Logger logger = Logger.getLogger( TileOwnerChangedEventHandler.class.getName( ) );

	@Override
	public byte getPriority( )
	{
		return 0;
	};

	@Override
	public boolean handle( Event event )
	{
		if ( event instanceof TileClaimedEvent )
		{
			TileClaimedEvent tileClaimed = ( TileClaimedEvent ) event;
			logger.log( Level.INFO,
					String.format( "Tile %s claimed by %s", tileClaimed.getTileTrait( ).toString( ), tileClaimed.getNewOwner( ) ) );
		}
		if ( event instanceof TileCapturedEvent )
		{
			TileCapturedEvent tileCaptured = ( TileCapturedEvent ) event;
			logger.log( Level.INFO, String.format( "%s's tile %s captured by %s", tileCaptured.getOldOwner( ), tileCaptured.getTileTrait( )
					.toString( ), tileCaptured.getNewOwner( ) ) );
		}

		if ( event instanceof TileFreedEvent )
		{
			TileFreedEvent tileFreed = ( TileFreedEvent ) event;
			logger.log( Level.INFO,
					String.format( "Tile %s freed from %s's grasp", tileFreed.getTileTrait( ).toString( ), tileFreed.getOldOwner( ) ) );
		}

		return false;
	}
}