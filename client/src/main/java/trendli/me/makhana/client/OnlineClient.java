package trendli.me.makhana.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.net.GameConnectionManager;
import trendli.me.makhana.client.net.MasterConnectionManager;

public abstract class OnlineClient extends OfflineClient
{
	/**
	 * The logger for this class
	 */
	private final static Logger logger = Logger.getLogger( OnlineClient.class.getName( ) );

	/**
	 * The Client's game server connection manager.
	 */
	private GameConnectionManager gameConnectionManager;

	/**
	 * The Client's master connection manager.
	 */
	private MasterConnectionManager masterConnectionManager;

	public OnlineClient( )
	{
		super( );
	}

	@Override
	public void destroy( )
	{
		if ( getMasterConnectionManager( ) != null )
		{
			getMasterConnectionManager( ).close( );
		}
		if ( getGameConnectionManager( ) != null )
		{
			getGameConnectionManager( ).close( );
		}
		super.destroy( );
	}

	/**
	 * @return the gameConnection
	 */
	public GameConnectionManager getGameConnectionManager( )
	{
		return gameConnectionManager;
	}

	/**
	 * @return the masterConnection
	 */
	public MasterConnectionManager getMasterConnectionManager( )
	{
		return masterConnectionManager;
	}

	/**
	 * A thread safe method that sends a LoginRequest with the given accountname
	 * and password to the master server on the next available update loop.
	 * 
	 * 
	 * @param accountname
	 *            the given account name
	 * @param password
	 *            the given password
	 */
	public void sendLogin( final String accountname, final String password )
	{
		enqueue( ( ) ->
		{
			if ( getMasterConnectionManager( ) != null )
			{
				if ( getMasterConnectionManager( ).isConnected( ) )
				{
					logger.log( Level.INFO, "Attempting to login with accountname " + accountname );
					getMasterConnectionManager( ).sendLogin( accountname, password );
					logger.log( Level.INFO, "Account: " + getMasterConnectionManager( ).getSession( ) );
				}
			}
			return null;
		} );
	}

	/**
	 * A thread safe method that sends a LogoutRequest to the master server on
	 * the next available update loop.
	 */
	public void sendLogout( )
	{
		enqueue( ( ) ->
		{
			if ( getMasterConnectionManager( ) != null )
			{
				if ( getMasterConnectionManager( ).isConnected( ) )
				{
					logger.log( Level.INFO, "Attempting to logout" );
					getMasterConnectionManager( ).sendLogout( );
				}
			}
			return null;
		} );
	}

	/**
	 * @param gameConnectionManager
	 *            the gameConnection to set
	 */
	protected void setGameConnectionManager( GameConnectionManager gameConnectionManager )
	{
		this.gameConnectionManager = gameConnectionManager;
	}

	/**
	 * @param masterConnectionManager
	 *            the masterConnection to set
	 */
	protected void setMasterConnectionManager( MasterConnectionManager masterConnectionManager )
	{
		this.masterConnectionManager = masterConnectionManager;
	}

}
