package com.cogitareforma.hexrepublics.client.views;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.util.NiftyFactory;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;

/**
 * 
 * @author Ryan Grier
 * @author Elliott Butler
 */
public class StartViewController extends GeneralController implements KeyInputHandler
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( StartViewController.class.getName( ) );

	private Element login;
	private TextField username;
	private TextField password;
	private Label loginFail;
	private AudioNode startMusic;
	private ScreenBuilder startScreen = null;
	Button loginButton;

	public StartViewController( Nifty nifty )
	{
		startScreen = new ScreenBuilder( "start" )
		{
			{
				// controller( new StartViewController( ) );

				layer( new LayerBuilder( "layer" )
				{

					{
						childLayoutCenter( );
						backgroundColor( "#1a1a1aff" );

						panel( new PanelBuilder( "startPanel" )
						{

							{
								height( "100%" );
								childLayoutCenter( );
								style( "nifty-panel-blue" );

								panel( new PanelBuilder( "" )
								{

									{
										height( "25%" );
										width( "25%" );
										childLayoutCenter( );
										alignCenter( );
										valignTop( );
										style( "nifty-panel-beige" );

										text( new TextBuilder( )
										{
											{
												text( "Makhana" );
												font( "Interface/Fonts/Default.fnt" );
												height( "100%" );
												width( "100%" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "startButtonsPanel" )
								{

									{
										style( "nifty-panel-beige" );
										alignCenter( );
										valignCenter( );
										childLayoutVertical( );

										control( new ButtonBuilder( "startSingle", "Singleplayer" )
										{

											{
												alignCenter( );
												visibleToMouse( true );
												interactOnClick( "gotoSinglePlayer( )" );
											}
										} );

										control( new ButtonBuilder( "startLogin", "Multiplayer" )
										{

											{
												alignCenter( );
												visibleToMouse( true );
												interactOnClick( "openLogin()" );
											}
										} );

										control( new ButtonBuilder( "startSettings", "Settings" )
										{
											{
												alignCenter( );
												visibleToMouse( true );
												interactOnClick( "openOptions()" );
											}
										} );

										control( new ButtonBuilder( "startExit", "Exit" )
										{

											{
												alignCenter( );
												visibleToMouse( true );
												interactOnClick( "quit()" );
											}
										} );
									}
								} );
							}
						} );
					}
				} );
			}
		};
		// TODO change popup to hidden panel?
		new PopupBuilder( "loginPopup" )
		{
			{
				backgroundColor( "#0f0f0fff" );
				childLayoutCenter( );
				panel( new PanelBuilder( "loginPanel" )
				{
					{
						style( "nifty-panel-brown" );
						childLayoutVertical( );
						width( "320px" );
						alignCenter( );
						valignCenter( );

						panel( new PanelBuilder( "loginInputs" )
						{
							{
								style( "nifty-panel-inset-beige" );
								childLayoutVertical( );
								panel( new PanelBuilder( "loginUsername" )
								{
									{
										childLayoutHorizontal( );
										height( "32px" );

										control( new LabelBuilder( "labelUsername", "Username: " )
										{
											{
												width( "25%" );
											}
										} );
										control( new TextFieldBuilder( "username", "" )
										{
											{
												alignCenter( );
												maxLength( 32 );
												width( "75%" );
											}
										} );
									}
								} );

								panel( new PanelBuilder( "loginPassword" )
								{
									{
										childLayoutHorizontal( );
										height( "32px" );

										control( new LabelBuilder( "labelPassword", "Password: " )
										{
											{
												width( "25%" );
											}
										} );
										control( new TextFieldBuilder( "password", "" )
										{
											{
												alignCenter( );
												maxLength( 32 );
												width( "75%" );
											}
										} );
									}
								} );
							}
						} );

						control( new LabelBuilder( "loginFail" )
						{
							{
								alignCenter( );
								valignBottom( );
								height( "32px" );
								width( "100%" );
								wrap( true );
								color( "#d2b290ff" );
							}
						} );

						panel( new PanelBuilder( "loginButtons" )
						{
							{
								backgroundColor( "#404040ff" );
								childLayoutHorizontal( );
								textHAlignCenter( );
								alignCenter( );

								control( new ButtonBuilder( "login", "Login" )
								{
									{
										alignCenter( );
										valignBottom( );
										visibleToMouse( true );
										interactOnClick( "startGame()" );
									}
								} );
								control( new ButtonBuilder( "back", "Back" )
								{
									{
										alignCenter( );
										valignBottom( );
										visibleToMouse( true );
										interactOnClick( "closeLogin()" );
									}
								} );
							}
						} );

					}
				} );
			}
		};
		startScreen.controller( this );
		startScreen.build( nifty );
	}

	public void bind( Nifty nifty, Screen screen )
	{
	}

	public void closeLogin( )
	{
		getApp( ).getNifty( ).closePopup( "loginPopup" );
	}

	public void gotoNetwork( )
	{
		NiftyFactory.createNetworkView( getApp( ).getNifty( ) );
		gotoScreen( "network", true, true, true, null, ( ) ->
		{
			getApp( ).getAudioRenderer( ).stopSource( startMusic );
			return null;
		} );
	}

	public void gotoSinglePlayer( )
	{
		NiftyFactory.createSinglePlayerLobby( getApp( ).getNifty( ) );
		gotoScreen( "singlePlayerLobby", true, true, true, null, ( ) ->
		{
			getApp( ).getAudioRenderer( ).stopSource( startMusic );
			return null;
		} );
	}

	/**
	 * Passes in main app and creates login screen.
	 */
	@SuppressWarnings( "deprecation" )
	@Override
	public void initialize( AppStateManager stateManager, Application app )
	{
		super.initialize( stateManager, app );

		getApp( ).getNifty( ).getCurrentScreen( ).addPreKeyboardInputHandler( new DefaultInputMapping( )
		{
			public NiftyInputEvent convert( final KeyboardInputEvent inputEvent )
			{
				if ( inputEvent.getKey( ) == KeyboardInputEvent.KEY_RETURN && inputEvent.isKeyDown( ) )
					return NiftyInputEvent.SubmitText;
				else
					return super.convert( inputEvent );
			}
		}, this );
		this.login = getApp( ).getNifty( ).createPopupWithId( "loginPopup", "loginPopup" );
		this.username = login.findNiftyControl( "username", TextField.class );
		this.password = login.findNiftyControl( "password", TextField.class );
		this.password.enablePasswordChar( '*' );
		this.loginFail = login.findNiftyControl( "loginFail", Label.class );

		getApp( ).currentScreen = "start";

		this.loginButton = getApp( ).getNifty( ).getCurrentScreen( ).findNiftyControl( "startLogin", Button.class );

		if ( !getApp( ).getMasterConnectionManager( ).isConnected( ) )
		{
			setLoginEnabled( false );
		}
		startMusic = new AudioNode( getApp( ).getAssetManager( ), "Sounds/mainMellody.wav", false );
		startMusic.setLooping( true );
		startMusic.setPositional( false );
		getApp( ).getAudioRenderer( ).playSource( startMusic );

		logger.log( Level.INFO, "Initialised " + this.getClass( ) );
	}

	public boolean keyEvent( NiftyInputEvent inputEvent )
	{
		if ( inputEvent != null )
		{
			Element e = getApp( ).getNifty( ).getCurrentScreen( ).getFocusHandler( ).getKeyboardFocusElement( );
			if ( e != null )
			{
				if ( inputEvent.equals( NiftyInputEvent.SubmitText )
						&& ( e.getId( ).equals( "username" ) || e.getId( ).equals( "password" ) ) )
				{
					startGame( );
				}
			}
		}
		return false;
	}

	public void onEndScreen( )
	{
		logger.log( Level.INFO, "Start Screen Stopped" );
	}

	public void onStartScreen( )
	{
		logger.log( Level.INFO, "Start Screen Started" );
	}

	public void openLogin( )
	{
		getApp( ).getNifty( ).showPopup( getApp( ).getNifty( ).getCurrentScreen( ), "loginPopup", null );
	}

	/**
	 * Creates options menu and attaches it.
	 */
	public void openOptions( )
	{
		NiftyFactory.createMainOptions( getApp( ).getNifty( ) );
		gotoScreen( "mainOptions", false, true, false, null, null );
	}

	/**
	 * Closes the app.
	 */
	public void quit( )
	{
		getApp( ).stop( );
	}

	public void setFailText( String notice )
	{
		loginFail.setText( notice );
	}

	private void setLoginEnabled( boolean enabled )
	{
		System.out.println( "setLoginEnabled entered" );
		System.out.println( "setLoginEnabled correct screen and value of " + enabled );
		loginButton.setEnabled( enabled );
		if ( enabled )
		{
			loginButton.setText( "Multiplayer" );
		}
		else
		{
			loginButton.setText( "Offline" );
		}
	}

	/**
	 * Checks login credentials. If valid, creates network lobby.
	 */
	@SuppressWarnings( "deprecation" )
	public void startGame( )
	{
		logger.log( Level.INFO, "Login attempt: " + username.getText( ) + ", " + password.getText( ) );
		if ( username.getDisplayedText( ).isEmpty( ) || password.getDisplayedText( ).isEmpty( ) )
		{
			logger.log( Level.INFO, "Failed login attempt: Username and/or password must be non-empty" );
			loginFail.setText( "Username and/or password must be non-empty" );
		}
		else
		{
			if ( getApp( ).getMasterConnectionManager( ) != null )
			{
				if ( !getApp( ).getMasterConnectionManager( ).isLoggedIn( ) )
				{
					getApp( ).sendLogin( username.getText( ), password.getText( ) );
				}
			}
		}

		if ( getApp( ).getMasterConnectionManager( ) != null )
		{
			if ( getApp( ).getMasterConnectionManager( ).isLoggedIn( ) )
			{
				loginFail.setText( "" );
				gotoNetwork( );
			}
		}

	}

	@Override
	public void update( float tpf )
	{
		super.update( tpf );
		if ( getApp( ).getNifty( ).getCurrentScreen( ).getScreenId( ) == "start" )
		{
			if ( getApp( ).getMasterConnectionManager( ) != null )
			{
				if ( getApp( ).getMasterConnectionManager( ).isLoggedIn( ) )
				{
					loginFail.setText( "" );
					gotoNetwork( );
				}
			}

			if ( getApp( ).getMasterConnectionManager( ).isConnected( ) && getApp( ).getMasterConnectionManager( ).getPublicKey( ) != null )
			{
				if ( !loginButton.isEnabled( ) )
				{
					setLoginEnabled( true );
				}
			}
			else
			{
				if ( loginButton.isEnabled( ) )
				{
					setLoginEnabled( false );
				}
			}
		}
	}
}
