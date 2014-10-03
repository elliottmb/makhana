package com.cogitareforma.makhana.common.entities.filters;

import com.simsilica.es.ComponentFilter;
import com.simsilica.es.EntityComponent;

public class InstanceOfFilter< T extends EntityComponent > implements ComponentFilter< T >
{
	public static < T extends EntityComponent > InstanceOfFilter< T > create( Class< T > type )
	{
		return new InstanceOfFilter< T >( type );
	}

	private Class< T > type;

	public InstanceOfFilter( )
	{
	}

	public InstanceOfFilter( Class< T > type )
	{
		this.type = type;
	}

	@Override
	public boolean evaluate( EntityComponent c )
	{
		if ( c != null && type != null )
		{
			return type.isInstance( c );
		}
		return false;
	}

	@Override
	public Class< T > getComponentType( )
	{
		return type;
	}

}
