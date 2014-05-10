package com.cogitareforma.hexrepublics.common.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.jme3.network.serializing.Serializable;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public class ServerStatus
{
	/**
	 * The Server's ip address
	 */
	private String address;

	/**
	 * The Server's current player count
	 */
	private int currentPlayers;

	/**
	 * The Server's current turn
	 */
	private int currentTurn;

	/**
	 * The Server's max player count
	 */
	private int maxPlayers;

	/**
	 * The Server's current port
	 */
	private int port;

	/**
	 * The Server's serverName.
	 */
	private String serverName;

	/**
	 * If the server status has changed since last being processed
	 */
	private transient boolean changed;

	/**
	 * Used by the serializer.
	 */
	public ServerStatus( )
	{
	}

	/**
	 * Constructs a ServerStatus with given server name, maximum players,
	 * current number of players, current turn number, ip address, and server
	 * port.
	 * 
	 * @param serverName
	 *            Name of server.
	 * @param maxPlayers
	 *            Maximum number of players allowed on the server.
	 * @param currentPlayers
	 *            Current number of players on the server.
	 * @param currentTurn
	 *            Current turn number.
	 * @param serverPort
	 *            Port to be used for connecting to the server.
	 */
	public ServerStatus( String serverName, int maxPlayers, int currentPlayers, int currentTurn, int serverPort )
	{
		this.maxPlayers = maxPlayers;
		this.serverName = serverName;
		this.currentPlayers = currentPlayers;
		this.currentTurn = currentTurn;
		this.port = serverPort;
		this.address = "unknown";
		this.changed = false;
	}

	/**
	 * Returns true if two ServerStatus' are the same, else false.
	 */
	public boolean equals( Object obj )
	{
		if ( obj == null )
			return false;
		if ( obj == this )
			return true;
		if ( obj.getClass( ) != getClass( ) )
			return false;
		ServerStatus ss = ( ServerStatus ) obj;
		return new EqualsBuilder( ).append( serverName, ss.serverName ).isEquals( );
	}

	/**
	 * Returns this ServerState's ip address.
	 * 
	 * @return The current ip address of the ServerState.
	 */
	public String getAddress( )
	{
		return address;
	}

	/**
	 * Returns this ServerState's current number of players.
	 * 
	 * @return the current number of players of the ServerState.
	 */
	public int getCurrentPlayers( )
	{
		return currentPlayers;
	}

	/**
	 * Returns this ServerState's current turn number.
	 * 
	 * @return The current turn number of the ServerState.
	 */
	public int getCurrentTurn( )
	{
		return currentTurn;
	}

	/**
	 * Returns this ServerState's max player count.
	 * 
	 * @return the max player count of the ServerState
	 */
	public int getMaxPlayers( )
	{
		return maxPlayers;
	}

	/**
	 * Returns this ServerState's port.
	 * 
	 * @return the port number of the ServerState.
	 */
	public int getPort( )
	{
		return port;
	}

	/**
	 * Returns this ServerState's serverName.
	 * 
	 * @return the serverName of the ServerState
	 */
	public String getServerName( )
	{
		return serverName;
	}

	public int hashCode( )
	{
		return new HashCodeBuilder( ).append( serverName ).toHashCode( );
	}

	/**
	 * @return the changed
	 */
	public boolean isChanged( )
	{
		return changed;
	}

	/**
	 * Sets the ServerState's ip address.
	 * 
	 * @param serverAddress
	 *            the ip address of the ServerState.
	 */
	public void setAddress( String serverAddress )
	{
		this.address = serverAddress;
		setChanged( true );
	}

	/**
	 * @param changed
	 *            the changed to set
	 */
	public void setChanged( boolean changed )
	{
		this.changed = changed;
	}

	/**
	 * Sets the ServerState's current number of players.
	 * 
	 * @param currentPlayers
	 *            the current number players of the ServerState
	 */
	public void setCurrentPlayers( int currentPlayers )
	{
		this.currentPlayers = currentPlayers;
		setChanged( true );
	}

	/**
	 * Sets the serverState's current turn number.
	 * 
	 * @param currentTurn
	 *            the current turn number of the ServerState.
	 */
	public void setCurrentTurn( int currentTurn )
	{
		this.currentTurn = currentTurn;
		setChanged( true );
	}

	/**
	 * Sets the ServerState's max player count.
	 * 
	 * @param maxPlayers
	 *            the max player count of the ServerState
	 */
	public void setMaxPlayers( int maxPlayers )
	{
		this.maxPlayers = maxPlayers;
		setChanged( true );
	}

	public void setPort( int serverPort )
	{
		this.port = serverPort;
		setChanged( true );
	}

	/**
	 * Sets this ServerState's serverName.
	 * 
	 * @param serverName
	 *            the serverName of the ServerState
	 */
	public void setServerName( String serverName )
	{
		if ( serverName == null )
		{
			throw new NullPointerException( "Server name must not be null" );
		}
		this.serverName = serverName;
		setChanged( true );
	}

	/**
	 * Returns the ServerState's serverName as a view of this object.
	 */
	public String toString( )
	{
		return String.format( "%-50s | Players: %d/%-30d | Address: %s:%d", serverName, currentPlayers, maxPlayers, address, port );
	}

}
