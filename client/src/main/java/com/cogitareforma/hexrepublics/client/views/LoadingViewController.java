package com.cogitareforma.hexrepublics.client.views;

import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.lang3.RandomUtils;

import trendli.me.makhana.client.util.NiftyFactory;
import trendli.me.makhana.common.entities.components.WorldTrait;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.EntityId;
import com.simsilica.es.client.RemoteEntityData;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class LoadingViewController extends GeneralPlayingController implements Controller
{

    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( LoadingViewController.class.getName( ) );

    private Element progressBar;
    private float progress;
    private String text;

    @Override
    public void bind( Nifty arg0, Screen arg1, Element arg2, Properties arg3, Attributes arg4 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void init( Properties arg0, Attributes arg1 )
    {
        // TODO Auto-generated method stub

    }

    public void initialize( AppStateManager stateManager, Application app )
    {
        setScreenId( "loading" );
        super.initialize( stateManager, app );

        progressBar = getApp( ).getNifty( ).getScreen( getScreenId( ) ).findElementByName( "inner" );

        byte seed = ( byte ) ( RandomUtils.nextInt( 0, 256 ) - 128 );
        RemoteEntityData entityData = getApp( ).getGameConnectionManager( ).getRemoteEntityData( );
        if ( entityData != null )
        {
            EntityId id = entityData.findEntity( null, WorldTrait.class );

            if ( id != null && id != EntityId.NULL_ID )
            {
                WorldTrait wt = entityData.getComponent( id, WorldTrait.class );
                if ( wt != null )
                {
                    seed = wt.getSeed( );
                }
            }
        }
        getApp( ).loadWorld( seed );
    }

    @Override
    public boolean inputEvent( NiftyInputEvent arg0 )
    {
        return false;
    }

    @Override
    public void onFocus( boolean arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void postExitToNetwork( )
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void preExitToNetwork( )
    {
        // TODO Auto-generated method stub

    }

    public void setLoading( float value, String text )
    {
        this.progress = value;
        this.text = text;
    }

    public void setProgress( float value, String loadingText )
    {
        if ( value < 0.0f )
        {
            value = 0.0f;
        }
        else if ( value > 1.0f )
        {
            value = 1.0f;
        }
        final int MIN_WIDTH = 32;
        int pixelWidth = ( int ) ( MIN_WIDTH + ( progressBar.getParent( ).getWidth( ) - MIN_WIDTH ) * value );
        progressBar.setConstraintWidth( new SizeValue( pixelWidth + "px" ) );
        progressBar.getParent( ).layoutElements( );
        // String text = String.format( "%3.0f%%", value * 100 );
        // progressText.setText(loadingText );
    }

    public void start( )
    {
        NiftyFactory.createHudView( getApp( ).getNifty( ) );
        gotoScreen( "hud", true, true, true, null, null );
    }

    @Override
    public void update( float tpf )
    {
        setProgress( progress, text );
        if ( progress == 1.0f )
        {
            start( );
        }
    }

}
