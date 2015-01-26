package trendli.me.makhana.common.net.msg;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import trendli.me.makhana.common.data.LoginCredentials;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class SecureLoginRequest extends AbstractMessage
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( SecureLoginRequest.class.getName( ) );

	private String encodedEncryptedAESKey;

	private SealedObject sealedCredentials;

	/**
	 * The login type
	 */
	private boolean isServer;

	/**
	 * Used by the serializer.
	 */
	public SecureLoginRequest( )
	{
	}

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
			Cipher keyCipher = Cipher.getInstance( publicKey.getAlgorithm( ) );
			keyCipher.init( Cipher.ENCRYPT_MODE, publicKey );

			KeyGenerator kgen = KeyGenerator.getInstance( "AES" );
			kgen.init( 128 );

			SecretKey key = kgen.generateKey( );
			byte[ ] aesKey = key.getEncoded( );
			SecretKeySpec aeskeySpec = new SecretKeySpec( aesKey, "AES" );
			Cipher credentialCipher = Cipher.getInstance( "AES" );
			credentialCipher.init( Cipher.ENCRYPT_MODE, aeskeySpec );

			this.encodedEncryptedAESKey = Base64.getEncoder( ).encodeToString( keyCipher.doFinal( aesKey ) );
			// TODO: Investigate implementing SerializableSerializer to handle
			// this class type, or look into extending it and using jMonkey's
			// @Serializable
			this.sealedCredentials = new SealedObject( loginCredentials, credentialCipher );

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
		catch ( BadPaddingException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

	}

	public Key getAESKey( Key privateKey )
	{
		Key key = null;
		try
		{
			Cipher keyCipher = Cipher.getInstance( privateKey.getAlgorithm( ) );
			keyCipher.init( Cipher.DECRYPT_MODE, privateKey );

			byte[ ] encryptedAESKeyBytes = Base64.getDecoder( ).decode( encodedEncryptedAESKey );

			byte[ ] aesKey = keyCipher.doFinal( encryptedAESKeyBytes );

			key = new SecretKeySpec( aesKey, "AES" );
		}
		catch ( NoSuchAlgorithmException e )
		{
			logger.log( Level.WARNING, "No Such Algorithm: " + e.getMessage( ) );
		}
		catch ( NoSuchPaddingException e )
		{
			logger.log( Level.WARNING, "No Such Padding: " + e.getMessage( ) );
		}
		catch ( InvalidKeyException e )
		{
			logger.log( Level.WARNING, "Invalid key: " + e.getMessage( ) );
		}
		catch ( IllegalBlockSizeException e )
		{
			logger.log( Level.WARNING, "Illegal Block Size: " + e.getMessage( ) );
		}
		catch ( BadPaddingException e )
		{
			logger.log( Level.WARNING, "Bad Padding: " + e.getMessage( ) );
		}

		return key;
	}

	/**
	 * 
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
			return ( LoginCredentials ) sealedCredentials.getObject( getAESKey( privateKey ) );
		}
		catch ( InvalidKeyException e )
		{
			logger.log( Level.WARNING, "Invalid key: " + e.getMessage( ) );
		}
		catch ( ClassNotFoundException e )
		{
			logger.log( Level.WARNING, "Class Not Found: " + e.getMessage( ) );
		}
		catch ( NoSuchAlgorithmException e )
		{
			logger.log( Level.WARNING, "No Such Algorithm: " + e.getMessage( ) );
		}
		catch ( IOException e )
		{
			logger.log( Level.WARNING, "IO Exception: " + e.getMessage( ) );
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
