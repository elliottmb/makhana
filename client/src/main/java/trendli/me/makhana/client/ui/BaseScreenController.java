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
	private String screenId;

	public BaseScreenController( OfflineClient app )
	{
		this.app = app;
	}

	@Override
	public void bind( Nifty arg0, Screen arg1 )
	{
		// TODO Auto-generated method stub
	}

	protected OfflineClient getApp( )
	{
		return app;
	}

	/**
	 * @return the screenId
	 */
	public String getScreenId( )
	{
		return screenId;
	}

	public void gotoScreen( String nextScreenId, boolean detachCurrent, boolean attachNext, boolean removeCurrent,
			Callable< Void > preHook, Callable< Void > postHook )
	{
		getApp( ).enqueue( ( ) ->
		{
			if ( getApp( ).getNifty( ).getScreen( nextScreenId ) != null )
			{
				logger.log( Level.INFO, "Going to screen " + nextScreenId );
				// TODO: Refactor!
			}
			return null;
		} );
	}

	@Override
	public void onEndScreen( )
	{
		logger.log( Level.INFO, getScreenId( ) + " screen ended." );

	}

	@Override
	public void onStartScreen( )
	{

	}

	/**
	 * @param screenId
	 *            the screenId to set
	 */
	public void setScreenId( String screenId )
	{
		this.screenId = screenId;
	}

}
