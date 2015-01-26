package trendli.me.makhana.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * LoginRequest is a DAO that encapsulates a client's request to log in.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
@Serializable
@Deprecated
public class LoginRequest extends AbstractMessage
{

	/**
	 * The accountName to log in as.
	 */
	private String accountName;

	/**
	 * The password to log in as.
	 */
	private String password;

	/**
	 * The login type
	 */
	private boolean isServer;

	/**
	 * Used by serializer.
	 */
	public LoginRequest( )
	{
	}

	/**
	 * Constructor that stores the specified accountName and password in this
	 * object.
	 * 
	 * @param accountName
	 *            the accountName to log in with
	 * @param password
	 *            the password to log in with
	 * @param isServer
	 *            if the request is coming from a game server
	 */
	public LoginRequest( String accountName, String password, boolean isServer )
	{
		this.accountName = accountName;
		this.password = password;
		this.isServer = isServer;
	}

	/**
	 * Returns the accountName associated with this login request.
	 * 
	 * @return the accountName
	 */
	public String getAccountName( )
	{
		return accountName;
	}

	/**
	 * Gets the password associated with this login request.
	 * 
	 * @return the password
	 */
	public String getPassword( )
	{
		return password;
	}

	/**
	 * Gets the type associated with this login request.
	 * 
	 * @return the type (true = server)
	 */
	public boolean isServer( )
	{
		return isServer;
	}

	/**
	 * Sets the accountName associated with this login request.
	 * 
	 * @param accountName
	 *            the accountName to set
	 */
	public void setAccountName( String accountName )
	{
		this.accountName = accountName;
	}

	/**
	 * Sets the password associated with this login request.
	 * 
	 * @param password
	 *            the password to set
	 */
	public void setPassword( String password )
	{
		this.password = password;
	}

	/**
	 * Sets the type associated with this login request.
	 * 
	 * @param isServer
	 *            the type to set (true = server)
	 */
	public void setServer( boolean isServer )
	{
		this.isServer = isServer;
	}

	/**
	 * Returns a string representation of this DAO.
	 */
	public String toString( )
	{
		return "accountName=" + accountName + ", password=" + password;
	}

}
