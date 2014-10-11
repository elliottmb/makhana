package com.cogitareforma.makhana.common.data;

import java.io.Serializable;
import java.security.Key;

public class LoginCredentials implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2449672755683144622L;

	/**
	 * The accountName to log in with.
	 */
	private String accountName;

	/**
	 * The password to log in with.
	 */
	private String password;

	/**
	 * The Cipher Key to be used when communicating
	 */
	private Key sessionKey;

	/**
	 * 
	 * @param accountName
	 * @param password
	 * @param sessionKey
	 */
	public LoginCredentials( String accountName, String password, Key sessionKey )
	{
		this.accountName = accountName;
		this.password = password;
		this.sessionKey = sessionKey;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName( )
	{
		return accountName;
	}

	/**
	 * @return the password
	 */
	public String getPassword( )
	{
		return password;
	}

	/**
	 * @return the sessionKey
	 */
	public Key getSessionKey( )
	{
		return sessionKey;
	}
}
