package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.traits.HealthTrait;
import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
import com.cogitareforma.makhana.common.eventsystem.EntityEventManager;
import com.simsilica.es.EntityId;

public abstract class EntityDamageEvent extends EntityEvent
{
	/**
	 * Player trait when the event was triggered
	 */
	private HealthTrait healthTrait;
	private float damage;

	/**
	 * An event involving a Player entity
	 * 
	 * @param entityEventManager
	 *            the entityEventManager which triggered this event
	 * @param source
	 *            the source entity
	 * @param healthTrait
	 *            health trait when the event was triggered
	 * @param damage
	 *            the amount of damage done to the entity
	 */
	public EntityDamageEvent( EntityEventManager entityEventManager, EntityId source, HealthTrait healthTrait, float damage )
	{
		super( entityEventManager, source );
		this.healthTrait = healthTrait;
		this.damage = damage;
	}

	/**
	 * Returns the health trait associated to the entity when the event was
	 * triggered
	 * 
	 * @return health trait when the event was triggered
	 */
	public HealthTrait getHealthTrait( )
	{
		return healthTrait;
	}

	/**
	 * @return the amount of damage done to the entity
	 */
	public float getDamage( )
	{
		return damage;
	}

}
