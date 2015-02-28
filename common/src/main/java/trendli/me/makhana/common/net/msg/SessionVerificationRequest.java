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
public class SessionVerificationRequest extends AbstractMessage
{
    /**
     * The user Session that the server has associated us with.
     */
    private Session session;

    /**
     * Used by serializer.
     */
    public SessionVerificationRequest( )
    {
    }

    /**
     * Constructs a new SessionVerificationRequest with the given Session to be
     * verified
     * 
     * @param session
     *            given session to be verified
     */
    public SessionVerificationRequest( Session session )
    {
        this.session = session;
    }

    /**
     * Returns the Session that needs to be verified
     * 
     * @return Session to be verified
     */
    public Session getSession( )
    {
        return session;
    }

    /**
     * Sets the Session that needs to be verified
     * 
     * @param session
     *            Session to be verified
     */
    public void setSession( Session session )
    {
        this.session = session;
    }

}
