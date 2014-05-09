package com.cogitareforma.hexrepublics.masterserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.cogitareforma.hexrepublics.common.data.Account;

/**
 * 
 * @author Elliott Butler
 *
 */
public class AccountRowMapper implements RowMapper< Account >
{
	@Override
	public Account mapRow( ResultSet resultSet, int rowNum ) throws SQLException
	{
		AccountExtractor courseExtractor = new AccountExtractor( );
		return courseExtractor.extractData( resultSet );
	}
}
