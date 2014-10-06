package com.cogitareforma.hexrepublics.client;

import com.cogitareforma.makhana.common.util.Screen;
import com.cogitareforma.makhana.common.util.ScreenManager;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;

public class StartScreen extends Screen
{

	// private Container hud;
	private Button playButton;
	private Button optionsButton;
	private Button exitButton;
	private float scale;

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub

	}

	@SuppressWarnings( "unchecked" )
	private void setUpButtons( ScreenManager screenManager )
	{
		playButton = new Button( "Play Game" );
		playButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		playButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		playButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Play Game Clicked" );
				screenManager.showScreen( NetworkScreen.class );
			}
		} );
		optionsButton = new Button( "Options" );
		optionsButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		optionsButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		optionsButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Options Clicked" );

				screenManager.showScreen( OptionsScreen.class );
			}
		} );
		exitButton = new Button( "Exit" );
		exitButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		exitButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		exitButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Exit Clicked" );
				screenManager.getApp( ).stop( );
			}
		} );
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		Camera cam = screenManager.getApp( ).getCamera( );
		scale = cam.getHeight( ) * 0.0016f;

		Node start = new Node( );

		Panel background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, cam.getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ), 0 ) );

		Container buttonPanel = new Container( "glass" );
		buttonPanel.setPreferredSize( new Vector3f( cam.getWidth( ) * .2f, cam.getHeight( ) * .2f, 0 ) );
		buttonPanel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		buttonPanel.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * .2f, 0 );

		setUpButtons( screenManager );

		playButton.setFontSize( 17 * scale );
		optionsButton.setFontSize( 17 * scale );
		exitButton.setFontSize( 17 * scale );

		buttonPanel.addChild( playButton );
		buttonPanel.addChild( optionsButton );
		buttonPanel.addChild( exitButton );

		Container titlePanel = new Container( "glass" );
		titlePanel.setPreferredSize( new Vector3f( cam.getWidth( ) * .15f, 50f, 0 ) );
		titlePanel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		titlePanel.setLocalTranslation( cam.getWidth( ) * .9f, cam.getHeight( ), 0 );
		Label title = new Label( "Makhana" );
		title.scale( scale );
		titlePanel.addChild( title );

		start.attachChild( titlePanel );
		start.attachChild( buttonPanel );

		getScreenNode( ).attachChild( start );
	}

	@Override
	public void screenAttached( ScreenManager screenManager )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void screenDetached( ScreenManager screenManager )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void update( float tpf )
	{
		// TODO Auto-generated method stub

	}

}
