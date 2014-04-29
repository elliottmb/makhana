package com.cogitareforma.hexrepublics.common.entities.traits;

import com.cogitareforma.hexrepublics.common.data.Account;
import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

@Serializable
public class PlayerTrait implements EntityComponent
{

	private Account account;

	/**
	 * Used by Serializer
	 */
	public PlayerTrait( )
	{

	}

	public PlayerTrait( Account account )
	{
		this.account = account;
	}

	public Account getAccount( )
	{
		return account;
	}

	@Override
	public String toString( )
	{
		return "PlayerTrait[" + account + "]";
	}
}
