package trendli.me.makhana.masterserver.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

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
