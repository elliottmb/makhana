package com.cogitareforma.makhana.common.eventsystem.events;

import com.cogitareforma.makhana.common.entities.components.Health;
import com.cogitareforma.makhana.common.eventsystem.EntityEvent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

public abstract class EntityDamageEvent extends EntityEvent
{
	/**
	 * Player trait when the event was triggered
	 */
	private Health healthTrait;
	private float damage;

	/**
	 * An event involving a Player entity
	 * 
	 * @param entityData
	 *            the entityData for the entity
	 * @param source
	 *            the source entity
	 * @param healthTrait
	 *            health trait when the event was triggered
	 * @param damage
	 *            the amount of damage done to the entity
	 */
	public EntityDamageEvent( EntityData entityData, EntityId source, Health healthTrait, float damage )
	{
		super( entityData, source );
		this.healthTrait = healthTrait;
		this.damage = damage;
	}

	/**
	 * Returns the health trait associated to the entity when the event was
	 * triggered
	 * 
	 * @return health trait when the event was triggered
	 */
	public Health getHealthTrait( )
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
