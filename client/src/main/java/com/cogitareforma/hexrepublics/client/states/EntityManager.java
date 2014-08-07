package com.cogitareforma.hexrepublics.client.states;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.common.util.EntityEventListener;
import com.jme3.app.state.AbstractAppState;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

public class EntityManager extends AbstractAppState
{
	// TODO: Add support for Listeners that should act on compound EntitySet
	// TODO: Add support for Listeners that should act on filter-based EntitySet
	private Map< Class< ? extends EntityComponent >, List< EntityEventListener >> listenerMap;
	private Map< Class< ? extends EntityComponent >, EntitySet > entitySetMap;
	private ClientMain app;

	public EntityManager( ClientMain app )
	{
		this.app = app;
		this.listenerMap = new ConcurrentHashMap<>( );
		this.entitySetMap = new ConcurrentHashMap<>( );
	}

	public void addListener( EntityEventListener listener, Class< ? extends EntityComponent >... classes )
	{
		for ( Class< ? extends EntityComponent > c : classes )
		{
			getListeners( c, true ).add( listener );
		}
	}

	public EntitySet getEntitySet( Class< ? extends EntityComponent > type )
	{
		return entitySetMap.get( type );
	}

	protected List< EntityEventListener > getListeners( Class< ? extends EntityComponent > c, boolean create )
	{
		List< EntityEventListener > result = listenerMap.get( c );
		if ( result == null && create )
		{
			result = new CopyOnWriteArrayList<>( );
			listenerMap.put( c, result );
		}

		if ( result == null )
		{
			result = Collections.emptyList( );
		}
		return result;
	}

	public void removeListener( EntityEventListener listener, Class< ? extends EntityComponent >... classes )
	{
		for ( Class< ? extends EntityComponent > c : classes )
		{
			List< EntityEventListener > list = getListeners( c, false );
			list.remove( listener );
			if ( list.isEmpty( ) )
			{
				EntitySet entitySet = entitySetMap.remove( c );
				if ( entitySet != null )
				{
					entitySet.release( );
				}
			}
		}
	}

	public void update( float tpf )
	{
		if ( app.getGameConnManager( ).isConnected( ) )
		{
			EntityData entityData = app.getGameConnManager( ).getRemoteEntityData( );
			if ( entityData != null )
			{
				for ( Class< ? extends EntityComponent > c : listenerMap.keySet( ) )
				{
					if ( !entitySetMap.containsKey( c ) )
					{
						entitySetMap.put( c, entityData.getEntities( c ) );
					}
				}

				for ( Entry< Class< ? extends EntityComponent >, EntitySet > e : entitySetMap.entrySet( ) )
				{
					EntitySet entitySet = e.getValue( );
					if ( entitySet.applyChanges( ) )
					{
						for ( EntityEventListener listener : getListeners( e.getKey( ), false ) )
						{
							listener.onAdded( entityData, entitySet.getAddedEntities( ) );
							listener.onChanged( entityData, entitySet.getChangedEntities( ) );
							listener.onRemoved( entityData, entitySet.getRemovedEntities( ) );
						}
					}
				}
			}
		}
	}
}