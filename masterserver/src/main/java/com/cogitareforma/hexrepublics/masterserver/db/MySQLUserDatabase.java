package com.cogitareforma.hexrepublics.masterserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.data.Account;

/**
 * MySQLUserDatabase is a User Database backed by MySQL.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
public class MySQLUserDatabase extends MySQLDatabase implements AccountDatabase
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( MySQLUserDatabase.class.getName( ) );

	protected MySQLUserDatabase( )
	{
		super( );
	}

	@Override
	public Account getAccount( String accountName )
	{
		logger.log( Level.INFO, "Getting user account from MySQLUserDatabase." );

		Account account = null;
		String sql = "SELECT * from accounts WHERE accountname = ?";
		SQLQuery query = new SQLQuery( sql, accountName );
		ResultSet rs = query.getResult( );
		try
		{
			if ( rs.next( ) )
			{ // there will only ever be one result
				account = new Account( accountName, rs.getString( "password" ), rs.getBoolean( "type" ) );
			}
		}
		catch ( SQLException e )
		{
			while ( e != null )
			{
				logger.log( Level.SEVERE, "Encoutered a MySQL error while reading a query's results.", e );
				e = e.getNextException( );
			}
		}

		logger.log( Level.INFO, "Closing MySQL query results." );
		query.closeResults( );

		logger.log( Level.INFO, "Returning account=" + ( ( account == null ) ? "null" : account.toString( ) ) );
		return account;
	}

}
