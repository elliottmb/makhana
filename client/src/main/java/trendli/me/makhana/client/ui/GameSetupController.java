package trendli.me.makhana.client.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.OfflineClient;
import trendli.me.makhana.common.eventsystem.Event;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;

public class GameSetupController extends BaseScreenController
{
    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( GameSetupController.class.getName( ) );

    private Nifty nifty;

    private DropDown< String > AIDropDown;
    private DropDown< String > worldSize;
    private DropDown< String > maxTurns;

    public GameSetupController( OfflineClient app )
    {
        super( app );
        // TODO Auto-generated constructor stub

        ScreenBuilder gameSetupBuilder = new ScreenBuilder( "gameSetup" )
        {
            {
                layer( new LayerBuilder( "" )
                {
                    {
                        childLayoutVertical( );
                        backgroundColor( "#838796ff" );

                        panel( new PanelBuilder( "singleHeader" )
                        {
                            {
                                width( "100%" );
                                height( "32px" );
                                valignBottom( );
                                childLayoutHorizontal( );
                                backgroundColor( "#616374ff" );
                                paddingLeft( "32px" );
                                paddingRight( "32px" );

                                text( new TextBuilder( "singleTitle" )
                                {
                                    {
                                        width( "50%" );
                                        height( "100%" );
                                        text( "Single Player" );
                                        textHAlignLeft( );
                                        textVAlignCenter( );
                                        font( "Interface/Fonts/Default.fnt" );

                                    }
                                } );

                                panel( new PanelBuilder( "backButton" )
                                {
                                    {
                                        width( "50%" );
                                        childLayoutHorizontal( );

                                        panel( new PanelBuilder( "spacer" )
                                        {
                                            {
                                                width( "*" );
                                            }
                                        } );

                                        control( new ButtonBuilder( "Back", "Back" )
                                        {
                                            {
                                                alignCenter( );
                                                valignCenter( );
                                                visibleToMouse( true );
                                                interactOnClick( "back()" );
                                            }
                                        } );
                                    }
                                } );
                            }
                        } );

                        panel( new PanelBuilder( "mainPanel" )
                        {
                            {
                                width( "66%" );
                                height( "100%" );
                                padding( "32px" );
                                alignCenter( );
                                valignCenter( );
                                childLayoutVertical( );

                                panel( new PanelBuilder( "" )
                                {
                                    {
                                        childLayoutVertical( );
                                        width( "100%" );
                                        style( "nifty-panel-brown" );

                                        panel( new PanelBuilder( "gameControls" )
                                        {
                                            {
                                                childLayoutHorizontal( );
                                                width( "100%" );
                                                style( "nifty-panel-inset-beige" );

                                                control( new ButtonBuilder( "start", "Start" )
                                                {

                                                    {
                                                        alignCenter( );
                                                        valignCenter( );
                                                        visibleToMouse( true );
                                                        interactOnClick( "start()" );
                                                    }
                                                } );
                                            }
                                        } );
                                        panel( new PanelBuilder( "gameOptions" )
                                        {
                                            {
                                                childLayoutVertical( );
                                                style( "nifty-panel-inset-beige" );
                                                // TODO TODO TODO
                                                // Options for world size,
                                                // number of AI and max turns
                                                // Need same for multiplayer
                                                // game lobby. Move names
                                                // vertical and options on right
                                                // side.
                                                panel( new PanelBuilder( "singleOptionsWorldSize" )
                                                {
                                                    {
                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "labelWorldSize", "World Size: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new DropDownBuilder( "worldSizeOptions" )
                                                        {
                                                            {

                                                            }
                                                        } );
                                                    }
                                                } );

                                                panel( new PanelBuilder( "singleOptionsAI" )
                                                {
                                                    {
                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "labelAICount", "Number of AI: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new DropDownBuilder( "AIOptions" )
                                                        {
                                                            {

                                                            }
                                                        } );
                                                    }
                                                } );

                                                panel( new PanelBuilder( "SingleOptionsMaxTurns" )
                                                {
                                                    {
                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "labelMaxTurns", "Max Turns: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new DropDownBuilder( "maxTurnssOptions" )
                                                        {
                                                            {

                                                            }
                                                        } );
                                                    }
                                                } );
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
        gameSetupBuilder.controller( this );
        gameSetupBuilder.build( app.getNifty( ) );
    }

    public void back( )
    {
        // TODO kill "gameserver"
        gotoScreen( "mainMenu", null, null );
    }

    @Override
    public boolean handle( Event event )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @SuppressWarnings( "unchecked" )
    public void initialize( AppStateManager stateManager, Application app )
    {
        setScreenId( "gameSetup" );
        // super.initialize( stateManager, app );
        this.nifty = getApp( ).getNifty( );
        this.AIDropDown = nifty.getScreen( "gameSetup" ).findNiftyControl( "AIOptions", DropDown.class );
        this.worldSize = nifty.getScreen( "gameSetup" ).findNiftyControl( "worldSizeOptions", DropDown.class );
        this.maxTurns = nifty.getScreen( "gameSetup" ).findNiftyControl( "maxTurnssOptions", DropDown.class );
        startOfflineGameserver( );
    }

    public void onEndScreen( )
    {
        logger.log( Level.INFO, "Start Screen Stopped" );
    }

    public void onStartScreen( )
    {
        logger.log( Level.INFO, "Start Screen Started" );
    }

    public void start( )
    {

        // gotoScreen( "loading", true, true, true, null, null );
    }

    public void startOfflineGameserver( )
    {
        // TODO create offline gameserver
    }
}
