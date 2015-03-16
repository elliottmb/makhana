package trendli.me.makhana.client.ui;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.OfflineClient;
import trendli.me.makhana.common.eventsystem.EventHandler;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public abstract class BaseScreenController implements ScreenController, EventHandler
{
    private final static Logger logger = Logger.getLogger( BaseScreenController.class.getName( ) );

    private OfflineClient app;
    private Screen screen;

    private Nifty nifty;

    public BaseScreenController( OfflineClient app )
    {
        this.app = app;
    }

    @Override
    public void bind( Nifty nifty, Screen screen )
    {
        logger.log( Level.INFO, screen.getScreenId( ) + " bound." );
        this.nifty = nifty;
        this.screen = screen;
    }

    protected OfflineClient getApp( )
    {
        return app;
    }

    /**
     * @return the nifty
     */
    protected Nifty getNifty( )
    {
        return nifty;
    }

    /**
     * @return the screen
     */
    protected Screen getScreen( )
    {
        return screen;
    }

    public void gotoScreen( String nextScreenId, Callable< Void > preHook, Callable< Void > postHook )
    {
        getApp( ).enqueue( ( ) ->
        {
            if ( getApp( ).getNifty( ).getScreen( nextScreenId ) != null )
            {
                logger.log( Level.INFO, "Going to screen " + nextScreenId );
                getApp( ).getNifty( ).gotoScreen( nextScreenId );
                // TODO: Refactor!
            }
            return null;
        } );
    }

    @Override
    public void onEndScreen( )
    {
        logger.log( Level.INFO, screen.getScreenId( ) + " screen ended." );

    }

    @Override
    public void onStartScreen( )
    {
        logger.log( Level.INFO, screen.getScreenId( ) + " screen started." );
    }

}
