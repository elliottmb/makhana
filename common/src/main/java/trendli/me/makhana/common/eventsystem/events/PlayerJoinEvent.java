package trendli.me.makhana.common.eventsystem.events;

import trendli.me.makhana.common.entities.components.Player;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public class PlayerJoinEvent extends PlayerEvent
{

	public PlayerJoinEvent( EntityData entityData, EntityId source, Player playerTrait )
	{
		super( entityData, source, playerTrait );
	}

}
