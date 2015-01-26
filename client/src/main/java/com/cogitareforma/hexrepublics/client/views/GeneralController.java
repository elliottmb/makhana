package com.cogitareforma.hexrepublics.client.views;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.ClientMain;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public abstract class GeneralController extends AbstractAppState implements ScreenController
{
	private final static Logger logger = Logger.getLogger( GeneralController.class.getName( ) );
	private ClientMain app;
	private String screenId;

	@Override
	public void bind( Nifty arg0, Screen arg1 )
	{
		// TODO Auto-generated method stub
	}

	protected ClientMain getApp( )
	{
		return app;
	}

	/**
	 * @return the screenId
	 */
	public String getScreenId( )
	{
		return screenId;
	}

	public void gotoScreen( String nextScreenId, boolean detachCurrent, boolean attachNext, boolean removeCurrent,
			Callable< Void > preHook, Callable< Void > postHook )
	{
		getApp( ).enqueue( ( ) ->
		{
			if ( getApp( ).getNifty( ).getScreen( nextScreenId ) != null )
			{
				logger.log( Level.INFO, "Going to screen " + nextScreenId );
				if ( preHook != null )
				{
					logger.log( Level.INFO, "Calling preHook for " + nextScreenId );
					preHook.call( );
				}

				if ( detachCurrent && getApp( ).getStateManager( ).hasState( this ) )
				{
					getApp( ).getStateManager( ).detach( this );
				}

				AppState nextState = ( AppState ) getApp( ).getNifty( ).getScreen( nextScreenId ).getScreenController( );
				if ( attachNext && nextState != null && !getApp( ).getStateManager( ).hasState( nextState ) )
				{
					getApp( ).getStateManager( ).attach( nextState );
				}

				if ( removeCurrent )
				{
					getApp( ).getNifty( ).removeScreen( getScreenId( ) );
				}

				getApp( ).getNifty( ).gotoScreen( nextScreenId );

				if ( postHook != null )
				{
					logger.log( Level.INFO, "Calling postHook for " + nextScreenId );
					postHook.call( );
				}
			}
			return null;
		} );
	}

	@Override
	public void initialize( AppStateManager stateManager, Application app )
	{
		super.initialize( stateManager, app );
		this.app = ( ClientMain ) app;

		logger.log( Level.INFO, getScreenId( ) + " screen initialized." );
	}

	@Override
	public void onEndScreen( )
	{
		logger.log( Level.INFO, getScreenId( ) + " screen ended." );

	}

	@Override
	public void onStartScreen( )
	{

	}

	/**
	 * @param screenId
	 *            the screenId to set
	 */
	public void setScreenId( String screenId )
	{
		this.screenId = screenId;
	}

}
