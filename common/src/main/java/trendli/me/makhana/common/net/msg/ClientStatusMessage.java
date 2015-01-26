package trendli.me.makhana.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * 
 * @author Elliott Butler
 */
@Serializable
public class ClientStatusMessage extends AbstractMessage
{
	private boolean inGame;

	/**
	 * Used by serializer.
	 */
	public ClientStatusMessage( )
	{
		this( false );
	}

	public ClientStatusMessage( boolean inGame )
	{

		this.inGame = inGame;
	}

	public boolean isInGame( )
	{
		return inGame;
	}

}
