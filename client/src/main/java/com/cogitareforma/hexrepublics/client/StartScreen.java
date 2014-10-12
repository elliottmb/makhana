package com.cogitareforma.hexrepublics.client;

import com.cogitareforma.makhana.ui.Screen;
import com.cogitareforma.makhana.ui.ScreenContext;
import com.cogitareforma.makhana.ui.ScreenManager;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class StartScreen extends Screen
{

	// private Container hud;
	private Button playButton;
	private Button optionsButton;
	private Button exitButton;

	private Container loginContainer;
	private Button login;
	private Button cancel;
	private Label errors;
	private Button logout;
	private Container loginStats;

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
				getScreenNode( ).attachChild( loginContainer );
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
				screenManager.setScreen( OptionsScreen.class );
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
		login = new Button( "Login" );
		login.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		login.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		login.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Login Clicked" );
				screenManager.setScreen( NetworkScreen.class );
				getScreenNode( ).detachChild( loginContainer );
				getScreenNode( ).attachChild( loginStats );
			}
		} );
		cancel = new Button( "Cancel" );
		cancel.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		cancel.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		cancel.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Cancel Clicked" );
				getScreenNode( ).detachChild( loginContainer );
			}
		} );
		logout = new Button( "Logout" );
		logout.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		logout.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		logout.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Logout Clicked" );
				getScreenNode( ).detachChild( loginStats );
			}
		} );
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		Camera cam = screenManager.getApp( ).getCamera( );
		ScreenContext sc = screenManager.getScreenContext( );

		setUpButtons( screenManager );

		Panel background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, cam.getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ), 0 ) );

		Container buttonPanel = new Container( "glass" );
		buttonPanel.setPreferredSize( new Vector3f( cam.getWidth( ) * .2f, cam.getHeight( ) * .2f, 0 ) );
		buttonPanel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		buttonPanel.setLocalTranslation( cam.getWidth( ) * .03f, cam.getHeight( ) * .2f, 0 );

		buttonPanel.addChild( playButton );
		buttonPanel.addChild( optionsButton );
		buttonPanel.addChild( exitButton );

		Container titlePanel = new Container( "glass" );
		titlePanel.setPreferredSize( new Vector3f( cam.getWidth( ) * .2f, 70f, 0 ) );
		titlePanel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		titlePanel.setLocalTranslation( cam.getWidth( ) * .7f, cam.getHeight( ) * .85f, 0 );
		Label title = new Label( "Makhana" );
		title.setFontSize( sc.getHeadingFontSize( ) );
		titlePanel.addChild( title );

		loginContainer = new Container( new BoxLayout( Axis.Y, FillMode.None ), "glass" );
		loginContainer.setPreferredSize( new Vector3f( cam.getWidth( ) * .25f, cam.getHeight( ) * .35f, 0 ) );
		loginContainer.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		loginContainer.setLocalTranslation( ( cam.getWidth( ) - loginContainer.getPreferredSize( ).x ) / 2f,
				( cam.getHeight( ) + loginContainer.getPreferredSize( ).y ) / 2f, 0 );

		Insets3f textfieldInsets = new Insets3f( 0, 0, 16f * sc.getHeightScalar( ), 0 );
		Label usernameLabel = new Label( "Username: ", "glass" );
		usernameLabel.setFontSize( sc.getMediumFontSize( ) );
		loginContainer.addChild( usernameLabel );

		TextField username = new TextField( "sdfsdfsdf ", "glass" );
		username.setSingleLine( true );
		username.setFontSize( sc.getMediumFontSize( ) );
		username.setInsets( textfieldInsets );

		loginContainer.addChild( username );

		Label passwordLabel = new Label( "Password: ", "glass" );
		passwordLabel.setFontSize( sc.getMediumFontSize( ) );
		TextField password = new TextField( "sdfsdf", "glass" );
		password.setSingleLine( true );
		password.setFontSize( sc.getMediumFontSize( ) );
		password.setInsets( textfieldInsets );
		loginContainer.addChild( passwordLabel );
		loginContainer.addChild( password );

		Container buttons = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		buttons.setPreferredSize( new Vector3f( cam.getWidth( ) * .05f, cam.getHeight( ) * .05f, 0 ) );
		errors = new Label( "TEST" );
		errors.setFontSize( sc.getMediumFontSize( ) );
		errors.setInsets( textfieldInsets );
		loginContainer.addChild( errors );
		buttons.addChild( login );
		buttons.addChild( cancel );
		loginContainer.addChild( buttons );

		loginStats = new Container( new BoxLayout( Axis.Y, FillMode.Proportional ), "glass" );
		loginStats.setPreferredSize( new Vector3f( cam.getWidth( ) * .3f, cam.getHeight( ) * .1f, 0 ) );
		loginStats.setLocalTranslation( cam.getWidth( ) * .7f, cam.getHeight( ) * .15f, 0 );
		loginStats.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );

		Label loggedIn = new Label( "Logged in: TESTFUCK" );
		loggedIn.setFontSize( sc.getSmallFontSize( ) );
		loggedIn.setTextHAlignment( HAlignment.Right );
		logout.setFontSize( sc.getSmallFontSize( ) );
		logout.setTextHAlignment( HAlignment.Right );

		loginStats.addChild( loggedIn );
		loginStats.addChild( logout );

		playButton.setFontSize( sc.getMediumFontSize( ) );
		optionsButton.setFontSize( sc.getMediumFontSize( ) );
		exitButton.setFontSize( sc.getMediumFontSize( ) );
		login.setFontSize( sc.getMediumFontSize( ) );
		cancel.setFontSize( sc.getMediumFontSize( ) );

		getScreenNode( ).attachChild( titlePanel );
		getScreenNode( ).attachChild( buttonPanel );
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
