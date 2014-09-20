package com.cogitareforma.hexrepublics.common.eventsystem.handlers;

import com.cogitareforma.hexrepublics.common.eventsystem.EntityEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventHandler;
import com.cogitareforma.hexrepublics.common.eventsystem.events.PlayerJoinEvent;

public class PlayerJoinEventHandler implements EntityEventHandler
{

	@Override
	public byte getPriority( )
	{
		return 0;
	};

	@Override
	public boolean handle( EntityEvent event )
	{
		if ( event instanceof PlayerJoinEvent )
		{
			System.out.println( "Woo!" );
			// TODO Auto-generated method stub
		}
		return false;
	}

}