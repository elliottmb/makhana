package com.cogitareforma.hexrepublics.client.views;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.client.util.NiftyFactory;
import com.jme3.app.Application;
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
	Button loginButton;

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

		if ( !getApp( ).getMasterConnManager( ).isConnected( ) )
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
		else if ( !getApp( ).authenticated( ) )
		{
			getApp( ).sendLogin( username.getText( ), password.getText( ) );
		}

		if ( getApp( ).authenticated( ) )
		{
			loginFail.setText( "" );
			gotoNetwork( );
		}

	}

	@Override
	public void update( float tpf )
	{
		super.update( tpf );
		if ( getApp( ).getNifty( ).getCurrentScreen( ).getScreenId( ) == "start" )
		{
			if ( getApp( ).authenticated( ) )
			{
				loginFail.setText( "" );
				gotoNetwork( );
			}

			if ( getApp( ).getMasterConnManager( ).isConnected( ) && getApp( ).getMasterConnManager( ).getPublicKey( ) != null )
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
