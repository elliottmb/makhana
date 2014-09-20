package com.cogitareforma.hexrepublics.gameserver.eventsystem.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventHandler;
import com.cogitareforma.hexrepublics.common.eventsystem.events.TileCapturedEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.events.TileClaimedEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.events.TileFreedEvent;

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