package com.cogitareforma.hexrepublics.client.views;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.xml.xpp3.Attributes;

public class LoadingViewController extends AbstractAppState implements ScreenController, Controller
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( LoadingViewController.class.getName( ) );
	private ClientMain app;
	private Element progressBar;
	private float progress;
	private String text;

	@Override
	public void bind( Nifty nifty, Screen screen )
	{
	}

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
		super.initialize( stateManager, app );
		this.app = ( ClientMain ) app;
		progressBar = this.app.getNifty( ).getScreen( "loading" ).findElementByName( "inner" );

		this.app.loadWorld( Integer.toHexString( ( int ) ( Math.random( ) * 1000 ) ) );
	}

	@Override
	public boolean inputEvent( NiftyInputEvent arg0 )
	{
		return false;
	}

	@Override
	public void onEndScreen( )
	{
		logger.log( Level.INFO, "Loading Screen Stopped" );
	}

	@Override
	public void onFocus( boolean arg0 )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen( )
	{
		logger.log( Level.INFO, "Loading Screen Started." );
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
		app.enqueue( ( ) ->
		{
			NiftyFactory.createHudView( app.getNifty( ) );
			app.getStateManager( ).detach( ( AppState ) app.getNifty( ).getScreen( "loading" ).getScreenController( ) );
			app.getStateManager( ).attach( ( AppState ) app.getNifty( ).getScreen( "hud" ).getScreenController( ) );
			app.getNifty( ).removeScreen( "loading" );
			app.getNifty( ).gotoScreen( "hud" );
			return null;
		} );
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
