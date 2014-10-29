package com.cogitareforma.makhana.common.eventsystem.handlers;

import com.cogitareforma.makhana.common.eventsystem.Event;
import com.cogitareforma.makhana.common.eventsystem.EventHandler;
import com.cogitareforma.makhana.common.eventsystem.events.PlayerJoinEvent;

public class PlayerJoinEventHandler implements EventHandler
{

	@Override
	public byte getPriority( )
	{
		return 0;
	};

	@Override
	public boolean handle( Event event )
	{
		if ( event instanceof PlayerJoinEvent )
		{
			System.out.println( "Woo!" );
			// TODO Auto-generated method stub
		}
		return false;
	}

}