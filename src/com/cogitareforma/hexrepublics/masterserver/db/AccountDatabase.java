package com.cogitareforma.hexrepublics.masterserver.db;

import com.cogitareforma.hexrepublics.common.data.Account;

/**
 * UserDatabase is an interface which is implemented by a backing database.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
public interface AccountDatabase
{

	/**
	 * Returns the user account from this database by accountname reference.
	 * 
	 * @param accountName
	 *            the accountname of the account to return
	 * @return the user account with the specified accountname
	 */
	public Account getAccount( String accountName );

}
