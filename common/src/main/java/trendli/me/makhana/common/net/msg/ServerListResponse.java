package trendli.me.makhana.common.net.msg;

import java.util.ArrayList;

import trendli.me.makhana.common.data.ServerStatus;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class ServerListResponse extends AbstractMessage
{

    /**
     * The user account that the server has associated us with.
     */
    private ArrayList< ServerStatus > serverList;

    /**
     * Used by the serializer.
     */
    public ServerListResponse( )
    {
    }

    /**
     * Constructor to pass the List of ServerStates to the object
     * 
     * @param serverList
     *            the list of ServerStates
     */
    public ServerListResponse( ArrayList< ServerStatus > serverList )
    {
        this.serverList = serverList;
    }

    /**
     * Returns the list of ServerStates.
     * 
     * @return the list of ServerStates
     */
    public ArrayList< ServerStatus > getServerList( )
    {
        return serverList;
    }

    /**
     * Sets the list of ServerStates
     * 
     * @param serverList
     *            the List of ServerStates to set
     */
    public void setServerList( ArrayList< ServerStatus > serverList )
    {
        this.serverList = serverList;
    }

    /**
     * Returns a string representation of this response.
     */
    public String toString( )
    {
        return "serverList=" + ( ( serverList == null ) ? "null" : serverList.toString( ) );
    }

}
