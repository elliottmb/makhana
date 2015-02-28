package trendli.me.makhana.gameserver.eventsystem.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.entities.components.CapitalTrait;
import trendli.me.makhana.common.entities.components.TileTrait;
import trendli.me.makhana.common.eventsystem.Event;
import trendli.me.makhana.common.eventsystem.EventHandler;
import trendli.me.makhana.gameserver.eventsystem.events.ServerPlayerJoinEvent;

import com.simsilica.es.ComponentFilter;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.filter.AndFilter;
import com.simsilica.es.filter.FieldFilter;

public class ServerPlayerJoinEventHandler implements EventHandler
{
    private final static Logger logger = Logger.getLogger( ServerPlayerJoinEventHandler.class.getName( ) );

    @Override
    public byte getPriority( )
    {
        // Return almost the maximal priority, allowing ban scripts to run first
        return Byte.MAX_VALUE - 1;
    }

    @Override
    public boolean handle( Event event )
    {
        if ( event instanceof ServerPlayerJoinEvent )
        {
            ServerPlayerJoinEvent joinEvent = ( ServerPlayerJoinEvent ) event;
            if ( !joinEvent.isAlreadyExisting( ) )
            {
                EntityId playerEntity = joinEvent.getSource( );
                EntityData entityData = joinEvent.getEntityData( );

                logger.log( Level.INFO, "Processing ServerPlayerJoinEvent for " + playerEntity );

                int x = 0;
                int y = 0;

                switch ( joinEvent.getPlayerCount( ) )
                {
                    case 1:
                    {
                        x = 3;
                        y = 3;
                        break;
                    }
                    case 2:
                    {
                        x = 12;
                        y = 10;
                        break;
                    }
                    case 3:
                    {
                        x = 3;
                        y = 10;
                        break;
                    }
                    default:
                    {
                        x = 12;
                        y = 3;
                        break;
                    }
                }

                ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", x );
                ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", y );
                @SuppressWarnings( "unchecked" )
                ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );

                EntityId tileId = entityData.findEntity( completeFilter, TileTrait.class );

                entityData.setComponent( tileId, new CreatedBy( playerEntity ) );
                entityData.setComponent( tileId, new CapitalTrait( ) );
            }
        }

        return false;
    }

}
