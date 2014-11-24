package com.cogitareforma.makhana.client.ui;

import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class LoadingScreen extends Screen
{

	private ProgressBar bar;
	private boolean enabled;
	private ScreenManager screenManager;
	private Container top;
	private Panel background;

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

		background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ), 0 ) );

		top = new Container( new BoxLayout( Axis.X, FillMode.None ), "glass" );
		top.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		top.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		top.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ) * 0.1f, 0 ) );

		bar = new ProgressBar( "glass" );
		bar.setProgressPercent( 0 );
		bar.setLocalTranslation( 0, getContext( ).getHeight( ) * 0.1f, 0 );
		bar.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ) * 0.1f, 0 ) );
		bar.setMessage( ( int ) ( bar.getProgressPercent( ) * 100 ) + "%" );

		Button yes = new Button( "CLICK" );
		yes.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				enabled = true;
			}
		} );
		top.addChild( yes );
		Button no = new Button( "RESET" );
		no.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				bar.setProgressPercent( 0 );
				bar.setMessage( ( int ) ( bar.getProgressPercent( ) * 100 ) + "%" );
			}
		} );
		top.addChild( no );

		getScreenNode( ).attachChild( background );
		getScreenNode( ).attachChild( top );
		getScreenNode( ).attachChild( bar );
		enabled = false;
	}

	@Override
	public void reshape( int w, int h )
	{
		// TODO Auto-generated method stub
		super.reshape( w, h );

		background.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ), 0 ) );

		top.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		top.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ) * 0.1f, 0 ) );

		bar.setLocalTranslation( 0, getContext( ).getHeight( ) * 0.1f, 0 );
		bar.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ) * 0.1f, 0 ) );

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
