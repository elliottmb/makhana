package com.cogitareforma.makhana.masterserver.db;

/**
 * AccountRepository is an interface which is implemented by a backing database.
 * 
 * @author Elliott Butler
 * 
 */
public interface AccountRepository
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
