package trendli.me.makhana.common.net.msg;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * @author Elliott Butler
 */
@Serializable
public class WelcomeMessage extends AbstractMessage
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( WelcomeMessage.class.getName( ) );

	/**
	 * 
	 */
	private String encodedPublicKey;

	/**
	 * 
	 */
	private String notice;

	/**
	 * Used by the serializer
	 */
	public WelcomeMessage( )
	{
	}

	public WelcomeMessage( Key publicKey, String notice )
	{
		this.encodedPublicKey = Base64.getEncoder( ).encodeToString( publicKey.getEncoded( ) );
		this.notice = notice;
	}

	/**
	 * 
	 * @return the public key
	 */
	public Key getPublicKey( )
	{
		try
		{
			byte[ ] keyBytes = Base64.getDecoder( ).decode( encodedPublicKey );
			X509EncodedKeySpec spec = new X509EncodedKeySpec( keyBytes );
			KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );

			return keyFactory.generatePublic( spec );
		}
		catch ( NoSuchAlgorithmException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
		catch ( InvalidKeySpecException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
		}
		return null;
	}

	/**
	 * @return the notice
	 */
	public String getNotice( )
	{
		return notice;
	}

	/**
	 * Returns a string representation of this DAO.
	 */
	public String toString( )
	{
		return String.format( "WelcomeMessage[publicKey=%s, notice=%s]", encodedPublicKey, notice );
	}
}
