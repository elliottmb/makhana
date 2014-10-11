package com.cogitareforma.makhana.common.net.msg;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import com.cogitareforma.makhana.common.data.LoginCredentials;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class SecureLoginRequest extends AbstractMessage
{

	private SealedObject sealedCredentials;

	/**
	 * The login type
	 */
	private boolean isServer;

	/**
	 * 
	 * @param loginCredentials
	 * @param publicKey
	 * @param isServer
	 * @throws IllegalStateException
	 */
	public SecureLoginRequest( LoginCredentials loginCredentials, Key publicKey, boolean isServer ) throws IllegalStateException
	{
		if ( loginCredentials == null )
			throw new IllegalStateException( "LoginCredentials can not be null" );
		if ( publicKey == null )
			throw new IllegalStateException( "Public key can not be null " );

		try
		{
			Cipher cipher = Cipher.getInstance( publicKey.getAlgorithm( ) );
			cipher.init( Cipher.ENCRYPT_MODE, publicKey );
			this.sealedCredentials = new SealedObject( loginCredentials, cipher );
			this.isServer = isServer;
		}
		catch ( NoSuchAlgorithmException e )
		{
			throw new IllegalStateException( "No Such Algorithm:" + e.getMessage( ) );
		}
		catch ( NoSuchPaddingException e )
		{
			throw new IllegalStateException( "No Such Padding:" + e.getMessage( ) );
		}
		catch ( InvalidKeyException e )
		{
			throw new IllegalStateException( "Invalid Key:" + e.getMessage( ) );
		}
		catch ( IllegalBlockSizeException e )
		{
			throw new IllegalStateException( "Illegal Block:" + e.getMessage( ) );
		}
		catch ( IOException e )
		{
			throw new IllegalStateException( "I/O Error:" + e.getMessage( ) );
		}

	}

	/**
	 * Returns the SealedObject containing the LoginCredentials object.
	 * 
	 * @return the Sealed LoginCredentials
	 */
	public SealedObject getSealedCredentials( )
	{
		return sealedCredentials;
	}

	/**
	 * Decrypts and returns the LoginCredentials contained. If any failure
	 * occurs during decryption, returns null;
	 * 
	 * @param privateKey
	 *            the private key to be used for decryption
	 * @return the decrypted LoginCredentials or null
	 */
	public LoginCredentials getLoginCredentials( Key privateKey )
	{
		try
		{
			return ( LoginCredentials ) sealedCredentials.getObject( privateKey );
		}
		catch ( InvalidKeyException e )
		{

		}
		catch ( ClassNotFoundException e )
		{

		}
		catch ( NoSuchAlgorithmException e )
		{

		}
		catch ( IOException e )
		{

		}

		return null;
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
	 * Returns a string representation of this DAO.
	 */
	public String toString( )
	{
		return "sealedCredentials=" + sealedCredentials + ", isServer=" + isServer;
	}

}
