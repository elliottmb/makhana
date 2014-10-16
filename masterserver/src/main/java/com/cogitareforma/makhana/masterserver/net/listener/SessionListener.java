package com.cogitareforma.makhana.masterserver.net.listener;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.makhana.common.data.Account;
import com.cogitareforma.makhana.common.data.LoginCredentials;
import com.cogitareforma.makhana.common.net.SessionManager;
import com.cogitareforma.makhana.common.net.msg.LoginResponse;
import com.cogitareforma.makhana.common.net.msg.LogoutRequest;
import com.cogitareforma.makhana.common.net.msg.NetworkChatMessage;
import com.cogitareforma.makhana.common.net.msg.SecureLoginRequest;
import com.cogitareforma.makhana.masterserver.db.AccountRepository;
import com.cogitareforma.makhana.masterserver.net.MasterServerManager;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

/**
 * SessionListener serves as a handler for login and logout requests.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
public class SessionListener implements MessageListener< HostedConnection >
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SessionListener.class.getName( ) );

	/**
	 * The master server manager for the SessionListener.
	 */
	private MasterServerManager serverManager;

	/**
	 * Constructor for the SessionListener. Sets its master server manager.
	 * 
	 * @param serverManager
	 *            the master server manager to set.
	 */
	public SessionListener( MasterServerManager serverManager )
	{
		this.serverManager = serverManager;
	}

	@Override
	public void messageReceived( HostedConnection source, Message message )
	{
		SessionManager sessionManager = serverManager.getSessionManager( );

		if ( message instanceof SecureLoginRequest )
		{
			logger.log( Level.INFO, "Received a secure login request." );
			SecureLoginRequest secureLogin = ( SecureLoginRequest ) message;

			logger.log( Level.INFO, "Attempting to decrypt sealed login credentials." );
			LoginCredentials loginCredentials = secureLogin.getLoginCredentials( serverManager.getKeyPair( ).getPrivate( ) );

			if ( loginCredentials != null )
			{
				logger.log( Level.INFO, "Retrieving user account info from the User Database." );
				AccountRepository acountRepository = this.serverManager.getApp( ).getAccountRepository( );
				if ( acountRepository != null )
				{
					Account account = acountRepository.getAccount( loginCredentials.getAccountName( ) );
					if ( account != null )
					{

						if ( !sessionManager.containsAccount( account ) )
						{
							if ( account.isValidPassword( loginCredentials.getPassword( ) ) )
							{
								if ( secureLogin.isServer( ) == account.isServer( ) )
								{
									logger.log( Level.INFO, "User logged in with a valid accountName and password." );
									account.setAddress( source.getAddress( ) );
									sessionManager.put( source, account );
									source.send( new LoginResponse( account ) );
									if ( !account.isServer( ) )
									{
										for ( HostedConnection hc : sessionManager.getAllSessions( ) )
										{
											Account act = sessionManager.getFromSession( hc );
											if ( !act.isServer( ) && !act.isInGame( ) )
											{
												hc.send( new NetworkChatMessage( null, String.format( "Server Notice: %s is now online.",
														account.getAccountName( ) ) ) );
											}
										}
									}
								}
								else
								{ /* login with the wrong account type */
									logger.log( Level.INFO, "User failed to log in with the wrong account type." );
									source.send( new LoginResponse( "Attempted to log in withthe wrong account type" ) );
								}
							}
							else
							{ /* login with a bad password */
								logger.log( Level.INFO, "User failed to log in with a valid accountName but a bad password." );
								source.send( new LoginResponse( "Attempted to log in with a bad password" ) );
							}
						}
						else
						{ /* account already online */
							logger.log( Level.INFO, "User failed to log in to an already active account." );
							source.send( new LoginResponse( "Attempted to log in to an already active account." ) );
						}
					}
					else
					{ /* login for a accountName that doesn't exist */
						logger.log( Level.INFO, "User failed to log in with a bad account name." );
						source.send( new LoginResponse( "Attempted to log in with a bad account name." ) );
					}
				}
				else
				{
					/* Account Repository failure */
					logger.log( Level.INFO, "Accessing the Account Repository failed." );
					source.send( new LoginResponse( "Error verifying information." ) );
				}
			}
			else
			{
				logger.log( Level.WARNING, "Login Credentials was returned null" );
				source.send( new LoginResponse( "Error verifying information." ) );
			}
		}
		if ( message instanceof LogoutRequest )
		{
			logger.log( Level.INFO, "Received a logout request." );
			Account account = serverManager.getSessionManager( ).getFromSession( source );
			serverManager.getSessionManager( ).removeSession( source );
			if ( account != null && !account.isServer( ) )
			{
				for ( HostedConnection hc : sessionManager.getAllSessions( ) )
				{
					Account act = sessionManager.getFromSession( hc );
					if ( !act.isServer( ) && !act.isInGame( ) )
					{
						hc.send( new NetworkChatMessage( null,
								String.format( "Server Notice: %s is now offline.", account.getAccountName( ) ) ) );
					}
				}
				serverManager.broadcastUserList( );
			}
		}
	}

}
