package trendli.me.makhana.common.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	 * If the account is currently in game (trivially true for servers)
	 */
	private transient boolean inGame;

	/**
	 * Used by the serializer.
	 */
	public Session( )
	{
	}

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

	@Override
	public boolean equals( Object obj )
	{
		if ( obj == null )
			return false;
		if ( this == obj )
			return true;
		if ( obj.getClass( ) != getClass( ) )
			return false;
		Session acct = ( Session ) obj;
		return new EqualsBuilder( ).append( sessionId, acct.sessionId ).isEquals( );
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName( )
	{
		return displayName;
	}

	/**
	 * @return the sessionId
	 */
	public long getSessionId( )
	{
		return sessionId;
	}

	@Override
	public int hashCode( )
	{
		return new HashCodeBuilder( ).append( sessionId ).toHashCode( );
	}

	/**
	 * @return the inGame
	 */
	public boolean isInGame( )
	{
		return inGame;
	}

	/**
	 * @param inGame
	 *            the inGame to set
	 */
	public void setInGame( boolean inGame )
	{
		this.inGame = inGame;
	}

	@Override
	public String toString( )
	{
		return String.format( "Session[%d, %s]", sessionId, displayName );
	}
}
