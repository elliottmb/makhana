package com.cogitareforma.makhana.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.client.ui.ScreenManager;
import com.cogitareforma.makhana.common.eventsystem.Event;
import com.cogitareforma.makhana.common.eventsystem.EventHandler;
import com.cogitareforma.makhana.common.eventsystem.EventManager;
import com.cogitareforma.makhana.common.eventsystem.ThreadSafeEventManager;
import com.cogitareforma.makhana.common.util.MakhanaConfig;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

public abstract class OfflineClient extends SimpleApplication
{
	/**
	 * The logger for this class
	 */
	private final static Logger logger = Logger.getLogger( OfflineClient.class.getName( ) );

	private MakhanaConfig configuration;

	private EventManager< Event, EventHandler > eventManager;

	private ScreenManager screenManager;

	public OfflineClient( )
	{
		super( );

		setConfiguration( new MakhanaConfig( ) );
		setScreenManager( new ScreenManager( this, guiNode ) );
		setEventManager( new ThreadSafeEventManager( this ) );

		AppSettings settings = new AppSettings( true );
		getConfiguration( ).configureAppSettings( settings );

		if ( getConfiguration( ).get( "client.input.console" ) == null )
		{
			logger.log( Level.INFO, "Putting default configuration values for input" );
			getConfiguration( ).putDefaultKeys( );
		}

		setShowSettings( false );
		setDisplayStatView( false );
		setDisplayFps( false );
		setSettings( settings );
	}

	/**
	 * @return the configuration
	 */
	public MakhanaConfig getConfiguration( )
	{
		return configuration;
	}

	/**
	 * @return the eventManager
	 */
	public EventManager< Event, EventHandler > getEventManager( )
	{
		return eventManager;
	}

	/**
	 * @return the screenManager
	 */
	public ScreenManager getScreenManager( )
	{
		return screenManager;
	}

	@Override
	public void initialize( )
	{
		logger.log( Level.INFO, "Initializaing base OfflineClient" );
		screenManager.getScreenContext( ).recalculateScalars( ( Integer ) getConfiguration( ).get( "client.graphics.height" ),
				( Integer ) getConfiguration( ).get( "client.graphics.width" ) );
		super.initialize( );
		// TODO
	}

	/**
	 * @param configuration
	 *            the configuration to set
	 */
	private void setConfiguration( MakhanaConfig configuration )
	{
		this.configuration = configuration;
	}

	/**
	 * @param eventManager
	 *            the eventManager to set
	 */
	private void setEventManager( EventManager< Event, EventHandler > eventManager )
	{
		this.eventManager = eventManager;
	}

	/**
	 * @param screenManager
	 *            the screenManager to set
	 */
	private void setScreenManager( ScreenManager screenManager )
	{
		stateManager.detach( this.screenManager );
		stateManager.attach( screenManager );
		this.screenManager = screenManager;
	}
}