package com.cogitareforma.hexrepublics.client.util;

import com.simsilica.es.EntityId;

public class EntityEntryModelClass
{
	private EntityId id;
	private String name;

	public EntityEntryModelClass( EntityId id, String name )
	{
		this.id = id;
		this.name = name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getName( )
	{
		return this.name;
	}

	public void setEntityId( EntityId id )
	{
		this.id = id;
	}

	public EntityId getEntityId( )
	{
		return this.id;
	}

	@Override
	public String toString( )
	{
		return this.name;
	}
}
