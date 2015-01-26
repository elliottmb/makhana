package trendli.me.makhana.common.net.msg;

import trendli.me.makhana.common.data.Session;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * 
 * @author Elliott Butler
 */
@Serializable
public class SessionResponse extends AbstractMessage
{

	/**
	 * The session that the server has associated us with.
	 */
	private Session session;

	/**
	 * Used by the serializer.
	 */
	public SessionResponse( )
	{
	}

	public SessionResponse( Session session )
	{
		this.session = session;
	}

	public Session getSession( )
	{
		return session;
	}

	public void setSession( Session session )
	{
		this.session = session;
	}

	/**
	 * Returns a string representation of this DAO.
	 */
	public String toString( )
	{
		return "session=" + ( ( session == null ) ? "null" : session.toString( ) );
	}

}
