package trendli.me.makhana.common.eventsystem.handlers;

import trendli.me.makhana.common.eventsystem.Event;
import trendli.me.makhana.common.eventsystem.EventHandler;
import trendli.me.makhana.common.eventsystem.events.PlayerJoinEvent;

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