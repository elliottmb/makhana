package com.cogitareforma.hexrepublics.client.views;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

public class SinglePlayerViewController extends GeneralPlayingController
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SinglePlayerViewController.class.getName( ) );

	public void initialize( AppStateManager stateManager, Application app )
	{
		setScreenId( "singlePlayerLobby" );
		super.initialize( stateManager, app );
		startOfflineGameserver( );
	}

	public void back( )
	{
		// TODO kill "gameserver"
		NiftyFactory.createStartView( getApp( ).getNifty( ) );
		gotoScreen( "start", true, true, true, null, null );
	}

	public void startOfflineGameserver( )
	{
		// TODO create offline gameserver
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

	public void onEndScreen( )
	{
		logger.log( Level.INFO, "Start Screen Stopped" );
	}

	public void onStartScreen( )
	{
		logger.log( Level.INFO, "Start Screen Started" );
	}
}
