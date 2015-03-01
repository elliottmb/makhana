package trendli.me.makhana.client.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.OfflineClient;
import trendli.me.makhana.client.util.NiftyFactory;
import trendli.me.makhana.common.eventsystem.Event;
import trendli.me.makhana.common.eventsystem.EventHandler;

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
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;

/**
 * 
 * @author Ryan Grier
 * @author Elliott Butler
 */
public class StartViewController extends BaseScreenController implements KeyInputHandler
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

    public StartViewController( OfflineClient app )
    {
        super( app );
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
        PopupBuilder popup = new PopupBuilder( "loginPopup" )
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
        startScreen.build( app.getNifty( ) );

        popup.registerPopup( getApp( ).getNifty( ) );
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

    @Override
    public boolean handle( Event event )
    {
        // TODO Auto-generated method stub
        return false;
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
        getApp( ).getNifty( ).createPopupWithId( "loginPopup", "loginPopup" );
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
        // gotoScreen( "mainOptions", false, true, false, null, null );
        getApp( ).getNifty( ).gotoScreen( "mainOptions" );
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
        // TODO: Refactor

    }

}
