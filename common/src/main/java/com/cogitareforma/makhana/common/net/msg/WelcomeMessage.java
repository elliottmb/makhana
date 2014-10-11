package com.cogitareforma.makhana.common.net.msg;

import java.security.Key;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * @author Elliott Butler
 */
@Serializable
public class WelcomeMessage extends AbstractMessage
{

	/**
	 * 
	 */
	private Key publicKey;

	/**
	 * 
	 */
	private String notice;

	public WelcomeMessage( Key publicKey, String notice )
	{
		this.publicKey = publicKey;
		this.notice = notice;
	}

	/**
	 * 
	 * @return the public key
	 */
	public Key getPublicKey( )
	{
		return publicKey;
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
		return String.format( "WelcomeMessage[publicKey=%s, notice=%s]", publicKey, notice );
	}
}
