package com.cogitareforma.hexrepublics.client.views;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.AudioNode;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.input.mapping.DefaultInputMapping;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * 
 * @author Ryan Grier
 * @author Elliott Butler
 */
public class StartViewController extends AbstractAppState implements ScreenController, KeyInputHandler
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( StartViewController.class.getName( ) );

	private ClientMain app;
	private Element login;
	private TextField username;
	private TextField password;
	private Label loginFail;
	private AudioNode startMusic;
	Button loginButton;

	public void bind( Nifty nifty, Screen screen )
	{
	}

	public void closeLogin( )
	{
		this.app.getNifty( ).closePopup( "loginPopup" );
	}

	public void gotoNetwork( )
	{
		app.enqueue( ( ) ->
		{
			NiftyFactory.createNetworkView( app.getNifty( ) );
			app.getStateManager( ).detach( ( AppState ) app.getNifty( ).getCurrentScreen( ).getScreenController( ) );
			app.getStateManager( ).attach( ( AppState ) app.getNifty( ).getScreen( "network" ).getScreenController( ) );
			app.getNifty( ).removeScreen( "start" );
			app.getNifty( ).gotoScreen( "network" );
			app.getAudioRenderer( ).stopSource( startMusic );
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
		this.app = ( ClientMain ) app;
		this.app.getNifty( ).getCurrentScreen( ).addPreKeyboardInputHandler( new DefaultInputMapping( )
		{
			public NiftyInputEvent convert( final KeyboardInputEvent inputEvent )
			{
				if ( inputEvent.getKey( ) == KeyboardInputEvent.KEY_RETURN && inputEvent.isKeyDown( ) )
					return NiftyInputEvent.SubmitText;
				else
					return super.convert( inputEvent );
			}
		}, this );
		this.login = this.app.getNifty( ).createPopupWithId( "loginPopup", "loginPopup" );
		this.username = login.findNiftyControl( "username", TextField.class );
		this.password = login.findNiftyControl( "password", TextField.class );
		this.password.enablePasswordChar( '*' );
		this.loginFail = login.findNiftyControl( "loginFail", Label.class );

		this.app.currentScreen = "start";

		this.loginButton = this.app.getNifty( ).getCurrentScreen( ).findNiftyControl( "startLogin", Button.class );

		if ( !this.app.getMasterConnManager( ).isConnected( ) )
		{
			setLoginEnabled( false );
		}
		startMusic = new AudioNode( this.app.getAssetManager( ), "Sounds/mainMellody.wav", false );
		startMusic.setLooping( true );
		startMusic.setPositional( false );
		this.app.getAudioRenderer( ).playSource( startMusic );

		logger.log( Level.INFO, "Initialised " + this.getClass( ) );
	}

	public boolean keyEvent( NiftyInputEvent inputEvent )
	{
		if ( inputEvent != null )
		{
			Element e = this.app.getNifty( ).getCurrentScreen( ).getFocusHandler( ).getKeyboardFocusElement( );
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
		this.app.getNifty( ).showPopup( this.app.getNifty( ).getCurrentScreen( ), "loginPopup", null );
	}

	/**
	 * Creates options menu and attaches it.
	 */
	public void openOptions( )
	{
		app.enqueue( ( ) ->
		{
			NiftyFactory.createMainOptions( app.getNifty( ) );
			app.getStateManager( ).attach( ( AppState ) app.getNifty( ).getScreen( "mainOptions" ).getScreenController( ) );
			app.getNifty( ).gotoScreen( "mainOptions" );
			return null;
		} );
	}

	/**
	 * Closes the app.
	 */
	public void quit( )
	{
		app.stop( );
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
		else if ( !app.authenticated( ) )
		{
			app.sendLogin( username.getText( ), password.getText( ) );
		}

		if ( app.authenticated( ) )
		{
			loginFail.setText( "" );
			gotoNetwork( );
		}

	}

	@Override
	public void update( float tpf )
	{
		super.update( tpf );
		if ( this.app.getNifty( ).getCurrentScreen( ).getScreenId( ) == "start" )
		{
			if ( app.authenticated( ) )
			{
				loginFail.setText( "" );
				gotoNetwork( );
			}

			if ( app.getMasterConnManager( ).isConnected( ) )
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
