package com.cogitareforma.hexrepublics.client.states;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import com.cogitareforma.makhana.client.ClientMain;
import com.cogitareforma.makhana.common.util.ComponentFilterEventListener;
import com.cogitareforma.makhana.common.util.TraitEventListener;
import com.jme3.app.state.AbstractAppState;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntitySet;

public class EntityManager extends AbstractAppState
{
	// TODO: Add support for Listeners that should act on filter-based EntitySet
	private Map< Set< Class< ? extends EntityComponent >>, Set< TraitEventListener >> listenerMap;
	private Map< Set< Class< ? extends EntityComponent >>, EntitySet > entitySetMap;
	private Map< ComponentFilter< ? >, List< ComponentFilterEventListener >> filterListenerMap;
	private Map< ComponentFilter< ? >, EntitySet > filterSetMap;
	private ClientMain app;

	public EntityManager( ClientMain app )
	{
		this.app = app;
		this.filterListenerMap = new ConcurrentHashMap<>( );
		this.filterSetMap = new ConcurrentHashMap<>( );
		this.listenerMap = new HashMap<>( );
		this.entitySetMap = new HashMap<>( );
	}

	public void addListener( TraitEventListener listener, Class< ? extends EntityComponent >... classes )
	{
		if ( listener instanceof ComponentFilterEventListener )
		{
			ComponentFilterEventListener filterListener = ( ComponentFilterEventListener ) listener;
			getFilterListeners( filterListener.getFilter( ), true ).add( filterListener );
		}
		else
		{
			getListeners( new HashSet<>( Arrays.asList( classes ) ), true ).add( listener );
		}
	}

	public EntitySet getEntitySet( Class< ? extends EntityComponent >... classes )
	{
		Set< Class< ? extends EntityComponent >> setOfClasses = new HashSet<>( Arrays.asList( classes ) );

		for ( Entry< Set< Class< ? extends EntityComponent >>, EntitySet > e : entitySetMap.entrySet( ) )
		{
			Set< Class< ? extends EntityComponent > > eKey = e.getKey( );
			if ( eKey != null && eKey.equals( setOfClasses ) )
			{
				return e.getValue( );
			}
		}

		return null;
	}

	protected List< ComponentFilterEventListener > getFilterListeners( ComponentFilter< ? > filter, boolean create )
	{
		List< ComponentFilterEventListener > result = filterListenerMap.get( filter );
		if ( result == null && create )
		{
			result = new CopyOnWriteArrayList<>( );
			filterListenerMap.put( filter, result );
		}

		if ( result == null )
		{
			result = Collections.emptyList( );
		}
		return result;
	}

	protected Set< TraitEventListener > getListeners( Set< Class< ? extends EntityComponent > > classes, boolean create )
	{
		Set< TraitEventListener > result = listenerMap.get( classes );
		if ( result == null && create )
		{
			result = new CopyOnWriteArraySet<>( );
			listenerMap.put( classes, result );
		}

		if ( result == null )
		{
			result = Collections.emptySet( );
		}
		return result;
	}

	public void removeListener( TraitEventListener listener, Class< ? extends EntityComponent >... classes )
	{
		if ( listener instanceof ComponentFilterEventListener )
		{
			ComponentFilterEventListener filterListener = ( ComponentFilterEventListener ) listener;
			List< ComponentFilterEventListener > list = getFilterListeners( filterListener.getFilter( ), false );
			list.remove( listener );
			if ( list.isEmpty( ) )
			{
				EntitySet entitySet = filterSetMap.remove( filterListener.getFilter( ) );
				if ( entitySet != null )
				{
					entitySet.release( );
				}
			}
		}
		else
		{
			Set< Class< ? extends EntityComponent >> classesSet = new HashSet<>( Arrays.asList( classes ) );
			Set< TraitEventListener > set = getListeners( classesSet, false );
			set.remove( listener );
			if ( set.isEmpty( ) )
			{
				EntitySet entitySet = entitySetMap.remove( classesSet );
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
				for ( Set< Class< ? extends EntityComponent >> s : listenerMap.keySet( ) )
				{
					if ( !entitySetMap.containsKey( s ) )
					{
						Class< ? >[ ] tempArray = new Class[ s.size( ) ];
						entitySetMap.put( s, entityData.getEntities( s.toArray( tempArray ) ) );
					}
				}

				for ( ComponentFilter< ? > filter : filterListenerMap.keySet( ) )
				{
					if ( !filterSetMap.containsKey( filter ) )
					{
						filterSetMap.put( filter, entityData.getEntities( filter ) );
					}
				}

				for ( Entry< Set< Class< ? extends EntityComponent >>, EntitySet > e : entitySetMap.entrySet( ) )
				{
					EntitySet entitySet = e.getValue( );
					if ( entitySet.applyChanges( ) )
					{
						for ( TraitEventListener listener : getListeners( e.getKey( ), false ) )
						{
							listener.onAdded( entityData, entitySet.getAddedEntities( ) );
							listener.onChanged( entityData, entitySet.getChangedEntities( ) );
							listener.onRemoved( entityData, entitySet.getRemovedEntities( ) );
						}
					}
				}

				for ( Entry< ComponentFilter< ? >, EntitySet > e : filterSetMap.entrySet( ) )
				{
					EntitySet entitySet = e.getValue( );
					if ( entitySet.applyChanges( ) )
					{
						for ( TraitEventListener listener : getFilterListeners( e.getKey( ), false ) )
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