package com.cogitareforma.makhana.masterserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.cogitareforma.makhana.common.data.Account;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class AccountExtractor implements ResultSetExtractor< Account >
{

	@Override
	public Account extractData( ResultSet resultSet ) throws SQLException, DataAccessException
	{
		Account account = new Account( );

		account.setAccountId( resultSet.getInt( "id" ) );
		account.setAccountName( resultSet.getString( "accountname" ) );
		account.setServer( resultSet.getBoolean( "type" ) );
		account.setHashedPassword( resultSet.getString( "password" ) );

		return account;
	}
}
