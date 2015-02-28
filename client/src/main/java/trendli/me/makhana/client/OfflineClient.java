package trendli.me.makhana.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.ui.ScreenManager;
import trendli.me.makhana.common.eventsystem.Event;
import trendli.me.makhana.common.eventsystem.EventHandler;
import trendli.me.makhana.common.eventsystem.EventManager;
import trendli.me.makhana.common.eventsystem.ThreadSafeEventManager;
import trendli.me.makhana.common.util.MakhanaConfig;

import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import com.simsilica.es.EntityData;

import de.lessvoid.nifty.Nifty;

public abstract class OfflineClient extends SimpleApplication
{
    /**
     * The logger for this class
     */
    private final static Logger logger = Logger.getLogger( OfflineClient.class.getName( ) );

    private MakhanaConfig configuration;

    private EventManager< Event, EventHandler > eventManager;

    private ScreenManager screenManager;

    private EntityData entityData;

    /**
     * The Client's Nifty Display instance.
     */
    private NiftyJmeDisplay niftyDisplay;

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
     * @return the entityData
     */
    public EntityData getEntityData( )
    {
        return entityData;
    }

    /**
     * @return the eventManager
     */
    public EventManager< Event, EventHandler > getEventManager( )
    {
        return eventManager;
    }

    /**
     * Returns the NiftyDisplay's Nifty instance
     * 
     * @return the current Nifty instance
     */
    public Nifty getNifty( )
    {
        return getNiftyDisplay( ).getNifty( );
    }

    /**
     * @return the niftyDisplay
     */
    public NiftyJmeDisplay getNiftyDisplay( )
    {
        return niftyDisplay;
    }

    /**
     * @return the screenManager
     */
    public ScreenManager getScreenManager( )
    {
        return screenManager;
    }

    @Override
    public void reshape( int w, int h )
    {
        super.reshape( w, h );
        screenManager.reshape( w, h );
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
     * @param entityData
     *            the entityData to set
     */
    public void setEntityData( EntityData entityData )
    {
        this.entityData = entityData;
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
     * @param niftyDisplay
     *            the niftyDisplay to set
     */
    public void setNiftyDisplay( NiftyJmeDisplay niftyDisplay )
    {
        this.niftyDisplay = niftyDisplay;
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

    @Override
    public void simpleInitApp( )
    {
        logger.log( Level.INFO, "Initializaing base OfflineClient" );
        logger.log( Level.INFO, "Starting up Nifty" );
        guiNode.detachAllChildren( );
        guiNode.attachChild( fpsText );

        setNiftyDisplay( new NiftyJmeDisplay( assetManager, inputManager, audioRenderer, guiViewPort ) );
        guiViewPort.addProcessor( getNiftyDisplay( ) );

        getNifty( ).loadControlFile( "nifty-default-controls.xml" );
        getNifty( ).loadStyleFile( "Interface/Styles/nifty-custom-styles.xml" );
    }
}