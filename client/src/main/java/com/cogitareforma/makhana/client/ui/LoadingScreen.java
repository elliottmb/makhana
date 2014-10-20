package com.cogitareforma.makhana.client.ui;

import com.cogitareforma.makhana.common.ui.Screen;
import com.cogitareforma.makhana.common.ui.ScreenContext;
import com.cogitareforma.makhana.common.ui.ScreenManager;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class LoadingScreen extends Screen
{

	private ProgressBar bar;
	private boolean enabled;
	private ScreenManager screenManager;

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		this.screenManager = screenManager;

		Camera cam = screenManager.getApp( ).getCamera( );

		Panel background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, cam.getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ), 0 ) );
		getScreenNode( ).attachChild( background );

		Container bottom = new Container( new BoxLayout( Axis.X, FillMode.None ), "glass" );
		bottom.setLocalTranslation( 0, cam.getHeight( ), 0 );
		bottom.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		bottom.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ) * 0.1f, 0 ) );

		bar = new ProgressBar( "glass" );
		bar.setProgressPercent( 0 );
		bar.setLocalTranslation( 0, cam.getHeight( ) * .1f, 0 );
		bar.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ) * .1f, 0 ) );
		bar.setMessage( ( int ) ( bar.getProgressPercent( ) * 100 ) + "%" );

		Button yes = new Button( "CLICK" );
		yes.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				enabled = true;
			}
		} );
		bottom.addChild( yes );
		Button no = new Button( "RESET" );
		no.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				bar.setProgressPercent( 0 );
				bar.setMessage( ( int ) ( bar.getProgressPercent( ) * 100 ) + "%" );
			}
		} );
		bottom.addChild( no );

		getScreenNode( ).attachChild( background );
		getScreenNode( ).attachChild( bottom );
		getScreenNode( ).attachChild( bar );
		enabled = false;
	}

	@Override
	public void screenAttached( ScreenManager screenManager )
	{
	}

	@Override
	public void screenDetached( ScreenManager screenManager )
	{
	}

	@Override
	public void update( float tpf )
	{
		if ( enabled )
		{
			bar.setProgressPercent( bar.getProgressPercent( ) + .05 );
			bar.setMessage( ( int ) ( bar.getProgressPercent( ) * 100 ) + "%" );
			enabled = false;
		}
		if ( bar.getProgressPercent( ) == 1 )
		{
			screenManager.setScreen( HudScreen.class );
		}

	}
}
