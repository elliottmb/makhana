package trendli.me.makhana.client.ui;

import trendli.me.makhana.client.OfflineClient;
import trendli.me.makhana.common.eventsystem.Event;
import trendli.me.makhana.common.eventsystem.EventHandler;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleCommands;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.ConsoleCommands.ConsoleCommand;
import de.lessvoid.nifty.controls.console.builder.ConsoleBuilder;
import de.lessvoid.nifty.screen.Screen;

public class ConsoleController extends BaseScreenController
{

    private Console console;
    private ConsoleCommands consoleCommands;

    public ConsoleController( OfflineClient app )
    {
        super( app );
        PopupBuilder console = new PopupBuilder( "consolePopup" )
        {
            {
                backgroundColor( "#0f0f0fff" );
                childLayoutCenter( );
                panel( new PanelBuilder( "loginPanel" )
                {
                    {
                        style( "nifty-panel-brown" );
                        childLayoutCenter( );
                        //width( "320px" );
                        alignCenter( );
                        valignCenter( );
                        panel( new PanelBuilder( "consolePanel" )
                        {
                            {
                                childLayoutCenter( );
                                width( "50%" );
                                height( "50%" );

                                control( new ConsoleBuilder( "console" )
                                {
                                    {
                                        width( "100%" );
                                        lines( 25 );
                                        alignCenter( );
                                        valignCenter( );
                                    }
                                } );
                            }
                        } );

                    }
                } );
            }
        };
        console.registerPopup( getApp( ).getNifty( ) );
    }

    public void bind( Nifty nifty, Screen screen )
    {
        //TODO bind doesnt run for pop up. 
        System.out.println( "Binding! " + nifty.toString( ) + " " + screen.toString( ) );
        console = nifty.getScreen( "consoleScreen" ).findNiftyControl( "console", Console.class );
        consoleCommands = new ConsoleCommands( nifty, console );
        consoleCommands.enableCommandCompletion( true );
        console.output( "Wecome to Makhana!" );
        console.getTextField( ).setFocus( );
        bindCommands( );
    }
    
    public void bindCommands( )
    {
        ConsoleCommand test = ( String[ ] arg0 ) ->
        {
            console.output( "Test command works" );

        };
        ConsoleCommand help = ( String[ ] arg0 ) ->
        {
            console.output( "Current commands:" );
            console.output( "test                           : this is just a test command." );
        };
        consoleCommands.registerCommand( "test", test );
        consoleCommands.registerCommand( "help", help );
        consoleCommands.enableCommandCompletion( true );
    }
    
    public void onStartScreen( )
    {
        TextField consoleInput = getApp( ).getNifty( ).getCurrentScreen( ).findNiftyControl( "console", Console.class ).getTextField( );
        consoleInput.setText( consoleInput.getRealText( ).replace( "`", "" ) );
        consoleInput.setCursorPosition( consoleInput.getRealText( ).length( ) );
    }

    @Override
    public boolean handle( Event event )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int compareTo( EventHandler arg0 )
    {
        // TODO Auto-generated method stub
        return 0;
    }

}
