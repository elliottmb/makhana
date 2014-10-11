package com.cogitareforma.makhana.common.entities.components;

import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

@Serializable
public class Velocity implements EntityComponent
{
	private Vector3f linear;
	private Vector3f angular;

	/**
	 * Used by the serializer.
	 */
	public Velocity( )
	{
	}

	public Velocity( Vector3f linear )
	{
		this( linear, new Vector3f( ) );
	}

	public Velocity( Vector3f linear, Vector3f angular )
	{
		this.linear = linear;
		this.angular = angular;
	}

	public Vector3f getLinear( )
	{
		return linear;
	}

	public Vector3f getAngular( )
	{
		return angular;
	}

	@Override
	public String toString( )
	{
		return "Velocity[linear=" + linear + ", angular=" + angular + "]";
	}
}