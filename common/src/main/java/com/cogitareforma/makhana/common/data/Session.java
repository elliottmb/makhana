package com.cogitareforma.makhana.common.data;

import com.jme3.network.serializing.Serializable;

@Serializable
public class Session
{
	/**
	 * 
	 */
	private long sessionId;
	/**
	 * 
	 */
	private String displayName;

	/**
	 * 
	 * @param sessionId
	 * @param displayName
	 */
	public Session( long sessionId, String displayName )
	{
		this.sessionId = sessionId;
		this.displayName = displayName;
	}

	/**
	 * @return the sessionId
	 */
	public long getSessionId( )
	{
		return sessionId;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName( )
	{
		return displayName;
	}

}
