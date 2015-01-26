package trendli.me.makhana.common.net.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * 
 * 
 * @author Elliott Butler
 */
@Serializable
public class ReadyUpRequest extends AbstractMessage
{
	/**
	 * Whether the message is to state that the player has toggled on "Ready Up"
	 * or off
	 */
	private boolean isReady;

	/**
	 * Used by serializer.
	 */
	public ReadyUpRequest( )
	{
	}

	public ReadyUpRequest( boolean isReady )
	{
		this.isReady = isReady;
	}

	/**
	 * @return the isReady
	 */
	public boolean isReady( )
	{
		return isReady;
	}

	/**
	 * @param isReady
	 *            the isReady to set
	 */
	public void setReady( boolean isReady )
	{
		this.isReady = isReady;
	}

}
