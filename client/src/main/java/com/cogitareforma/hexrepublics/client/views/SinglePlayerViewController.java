package com.cogitareforma.hexrepublics.client.views;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.util.NiftyFactory;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;

public class SinglePlayerViewController extends GeneralPlayingController
{
    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( SinglePlayerViewController.class.getName( ) );

    private Nifty nifty;
    private DropDown< String > AIDropDown;
    private DropDown< String > worldSize;
    private DropDown< String > maxTurns;

    public void back( )
    {
        // TODO kill "gameserver"
        NiftyFactory.createStartView( getApp( ).getNifty( ) );
        gotoScreen( "start", true, true, true, null, null );
    }

    @SuppressWarnings( "unchecked" )
    public void initialize( AppStateManager stateManager, Application app )
    {
        setScreenId( "singlePlayerLobby" );
        super.initialize( stateManager, app );
        this.nifty = getApp( ).getNifty( );
        this.AIDropDown = nifty.getScreen( "singlePlayerLobby" ).findNiftyControl( "AIOptions", DropDown.class );
        this.worldSize = nifty.getScreen( "singlePlayerLobby" ).findNiftyControl( "worldSizeOptions", DropDown.class );
        this.maxTurns = nifty.getScreen( "singlePlayerLobby" ).findNiftyControl( "maxTurnssOptions", DropDown.class );
        startOfflineGameserver( );
    }

    public void onEndScreen( )
    {
        logger.log( Level.INFO, "Start Screen Stopped" );
    }

    public void onStartScreen( )
    {
        logger.log( Level.INFO, "Start Screen Started" );
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

    public void start( )
    {
        // NiftyFactory.createLoadingScreen( getApp( ).getNifty( ) );
        // gotoScreen( "loading", true, true, true, null, null );
    }

    public void startOfflineGameserver( )
    {
        // TODO create offline gameserver
    }
}
