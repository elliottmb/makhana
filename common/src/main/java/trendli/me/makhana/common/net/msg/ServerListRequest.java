package trendli.me.makhana.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class ServerListRequest extends AbstractMessage
{

    /**
     * Used by serializer.
     */
    public ServerListRequest( )
    {
    }

}
