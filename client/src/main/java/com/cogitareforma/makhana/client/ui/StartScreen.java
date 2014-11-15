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
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class StartScreen extends Screen
{

	private Button cancel;
	private Label errors;
	private Button exitButton;

	private Button login;
	private Container loginContainer;
	private Container loginStats;
	private Button logout;
	private Button optionsButton;
	// private Container hud;
	private Button playButton;

	private Container buttonPanel;
	private Label title;

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );

		setUpButtons( screenManager );

		float mediumFontSize = getContext( ).getMediumFontSize( );

		Panel background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, 1f, 0 );
		background.setPreferredSize( new Vector3f( 0.5f, 0.5f, 0 ) );

		buttonPanel = new Container( "glass" );
		buttonPanel.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.2f, getContext( ).getHeight( ) * 0.2f, 0 ) );
		buttonPanel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		buttonPanel.setLocalTranslation( getContext( ).getWidth( ) * 0.05f, getContext( ).getHeight( ) * 0.21f, 0 );

		buttonPanel.addChild( playButton );
		playButton.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.2f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		buttonPanel.addChild( optionsButton );
		buttonPanel.addChild( exitButton );

		title = new Label( "Makhana" );
		title.setLocalTranslation( getContext( ).getWidth( ) * 0.65f, getContext( ).getHeight( ) * 0.85f, 0f );
		title.setFontSize( getContext( ).getHeadingFontSize( ) );

		loginContainer = new Container( new BoxLayout( Axis.Y, FillMode.None ), "glass" );
		loginContainer.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		loginContainer.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.25f, getContext( ).getHeight( ) * 0.35f, 0 ) );
		loginContainer.setLocalTranslation( ( getContext( ).getWidth( ) - loginContainer.getPreferredSize( ).x ) / 2f, ( getContext( )
				.getHeight( ) + loginContainer.getPreferredSize( ).y ) / 2f, 0 );

		Insets3f textfieldInsets = new Insets3f( 0, 0, 16f * getContext( ).getScalar( ), 0 );
		Label usernameLabel = new Label( "Username: ", "glass" );
		usernameLabel.setFontSize( mediumFontSize );
		loginContainer.addChild( usernameLabel );

		TextField username = new TextField( "sdfsdfsdf ", "glass" );
		username.setSingleLine( true );
		username.setFontSize( mediumFontSize );
		username.setInsets( textfieldInsets );

		loginContainer.addChild( username );

		Label passwordLabel = new Label( "Password: ", "glass" );
		passwordLabel.setFontSize( mediumFontSize );
		TextField password = new TextField( "sdfsdf", "glass" );
		password.setSingleLine( true );
		password.setFontSize( mediumFontSize );
		password.setInsets( textfieldInsets );
		loginContainer.addChild( passwordLabel );
		loginContainer.addChild( password );

		Container buttons = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		buttons.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * .05f, 0 ) );
		errors = new Label( "TEST" );
		errors.setFontSize( mediumFontSize );
		errors.setInsets( textfieldInsets );
		loginContainer.addChild( errors );
		buttons.addChild( login );
		buttons.addChild( cancel );
		loginContainer.addChild( buttons );

		loginStats = new Container( new BoxLayout( Axis.Y, FillMode.Proportional ), "glass" );
		loginStats.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		loginStats.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .3f, getContext( ).getHeight( ) * .1f, 0 ) );
		loginStats.setLocalTranslation( getContext( ).getWidth( ) * .7f, getContext( ).getHeight( ) * .15f, 0 );

		Label loggedIn = new Label( "Logged in: TESTFUCK" );
		loggedIn.setFontSize( getContext( ).getSmallFontSize( ) );
		loggedIn.setTextHAlignment( HAlignment.Right );
		logout.setFontSize( getContext( ).getSmallFontSize( ) );
		logout.setTextHAlignment( HAlignment.Right );

		loginStats.addChild( loggedIn );
		loginStats.addChild( logout );

		playButton.setFontSize( mediumFontSize );
		optionsButton.setFontSize( mediumFontSize );
		exitButton.setFontSize( mediumFontSize );
		login.setFontSize( mediumFontSize );
		cancel.setFontSize( mediumFontSize );

		getScreenNode( ).attachChild( title );
		getScreenNode( ).attachChild( buttonPanel );

	}

	@Override
	public void reshape( int w, int h )
	{
		super.reshape( w, h );

		buttonPanel.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.2f, getContext( ).getHeight( ) * 0.2f, 0 ) );
		buttonPanel.setLocalTranslation( getContext( ).getWidth( ) * 0.05f, getContext( ).getHeight( ) * 0.21f, 0 );

		title.setLocalTranslation( getContext( ).getWidth( ) * 0.65f, getContext( ).getHeight( ) * 0.85f, 0f );
		title.setFontSize( getContext( ).getHeadingFontSize( ) );

		loginContainer.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.25f, getContext( ).getHeight( ) * 0.35f, 0 ) );
		loginContainer.setLocalTranslation( ( getContext( ).getWidth( ) - loginContainer.getPreferredSize( ).x ) / 2f, ( getContext( )
				.getHeight( ) + loginContainer.getPreferredSize( ).y ) / 2f, 0 );

		loginStats.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .3f, getContext( ).getHeight( ) * .1f, 0 ) );
		loginStats.setLocalTranslation( getContext( ).getWidth( ) * .7f, getContext( ).getHeight( ) * .15f, 0 );
		// TODO Auto-generated method stub
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
	public void update( float tpf )
	{
		// TODO Auto-generated method stub
	}
}
