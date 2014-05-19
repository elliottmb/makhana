package com.cogitareforma.hexrepublics.common.data;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.mindrot.jbcrypt.BCrypt;

import com.jme3.network.serializing.Serializable;

/**
 * Account serves as a DAO stored in a database. An Account consists of a
 * accountname, a hashed password, type for some account.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
@Serializable
public class Account
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( Account.class.getName( ) );

	/**
	 * The account's accountName.
	 */
	private String accountName;

	/**
	 * Current IP address of the Account
	 */
	private String address;

	/**
	 * The account's hashed password, not serialized across the network.
	 */
	private transient String hashedPassword;

	/**
	 * The account's type (false = user, true = server)
	 */
	private transient boolean isServer;

	/**
	 * If the account is currently in game (trivially true for servers)
	 */
	private transient boolean inGame;

	/**
	 * Used by the serializer.
	 */
	public Account( )
	{
	}

	/**
	 * Constructor accepts a accountName and hashed password that comprises a
	 * UserAccount.
	 * 
	 * @param accountName
	 *            the accountName for this Account
	 * @param hashedPassword
	 *            the hashed password for this Account
	 */
	public Account( String accountName, String hashedPassword, boolean isServer )
	{
		this.accountName = accountName;
		this.hashedPassword = hashedPassword;
		this.isServer = isServer;
	}

	public boolean equals( Object obj )
	{
		if ( obj == null )
			return false;
		if ( obj == this )
			return true;
		if ( obj.getClass( ) != getClass( ) )
			return false;
		Account acct = ( Account ) obj;
		return new EqualsBuilder( ).append( accountName, acct.accountName ).isEquals( );
	}

	/**
	 * Returns this account's accountName.
	 * 
	 * @return the accountName of the user
	 */
	public String getAccountName( )
	{
		return accountName;
	}

	/**
	 * Returns the current IP address of the account
	 * 
	 * @return the ip address
	 */
	public String getAddress( )
	{
		return address;
	}

	/**
	 * Returns this account's hashed password.
	 * 
	 * @return the hashed password of the user
	 */
	public String getHashedPassword( )
	{
		return hashedPassword;
	}

	public int hashCode( )
	{
		return new HashCodeBuilder( ).append( accountName ).toHashCode( );
	}

	/**
	 * @return the inGame
	 */
	public boolean isInGame( )
	{
		return inGame;
	}

	/**
	 * Returns this account's type.
	 * 
	 * @return the type of the account
	 */
	public boolean isServer( )
	{
		return isServer;
	}

	/**
	 * Determines if the provided candidate password is valid for this account's
	 * associated password hash.
	 * 
	 * @param candidate
	 *            the candidate password to check for this user
	 * @return true if the candidate password is this account's password, false
	 *         otherwise
	 */
	public boolean isValidPassword( String candidate )
	{
		logger.log( Level.INFO, "Validating password for account " + accountName + "." );
		if ( BCrypt.checkpw( candidate, hashedPassword ) )
		{
			logger.log( Level.INFO, "Password was accepted." );
			return true;
		}
		logger.log( Level.INFO, "Password was rejected." );
		return false;
	}

	/**
	 * Sets this account's accountName.
	 * 
	 * @param accountName
	 *            the accountName of the user
	 */
	public void setAccountName( String accountName )
	{
		this.accountName = accountName;
	}

	/**
	 * Sets the current ip address of the account
	 * 
	 * @param address
	 *            the ip address
	 */
	public void setAddress( String address )
	{
		this.address = address;
	}

	/**
	 * Sets the account's hashed password.
	 * 
	 * @param hashedPassword
	 *            the hashed password of the user
	 */
	public void setHashedPassword( String hashedPassword )
	{
		this.hashedPassword = hashedPassword;
	}

	/**
	 * @param inGame
	 *            the inGame to set
	 */
	public void setInGame( boolean inGame )
	{
		this.inGame = inGame;
	}

	/**
	 * Sets account to a server type
	 * 
	 * @param isServer
	 *            is server type
	 */
	public void setServer( boolean isServer )
	{
		this.isServer = isServer;
	}

	/**
	 * Returns the account's accountName as a view of this object.
	 */
	public String toString( )
	{
		return accountName;
	}

}
