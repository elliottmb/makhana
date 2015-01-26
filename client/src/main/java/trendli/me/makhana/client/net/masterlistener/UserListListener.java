package trendli.me.makhana.client.net.masterlistener;

import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.net.MasterConnectionManager;
import trendli.me.makhana.common.net.msg.UserListResponse;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class UserListListener implements MessageListener< Client >
{

	private final static Logger logger = Logger.getLogger( UserListListener.class.getName( ) );
	/**
	 * The client's controller.
	 */
	private MasterConnectionManager manager;

	/**
	 * The default constructor accepts the client's manager.
	 * 
	 * @param manager
	 *            the client's manager
	 */
	public UserListListener( MasterConnectionManager manager )
	{
		this.manager = manager;
	}

	@Override
	public void messageReceived( Client source, Message message )
	{
		if ( message instanceof UserListResponse )
		{
			logger.log( Level.INFO, "Received a UserList response." );
			UserListResponse msg = ( UserListResponse ) message;

			manager.receiveUserList( msg );
		}

	}

}
