package com.cogitareforma.hexrepublics.masterserver.db;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.cogitareforma.hexrepublics.common.data.Account;

/**
 * 
 * @author Elliott Butler
 *
 */
@Component
public class JdbcAccountRepository implements AccountRepository
{
	JdbcTemplate jdbcTemplate;

	public JdbcAccountRepository( DataSource dataSource )
	{
		jdbcTemplate = new JdbcTemplate( dataSource );
	}

	public Account getAccount( String accountname )
	{
		return jdbcTemplate.queryForObject( "select * from accounts where accountname = ?", new AccountRowMapper( ), accountname );
	}

}
