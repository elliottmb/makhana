package trendli.me.makhana.masterserver.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Elliott Butler
 *
 */
@Component
public class JdbcAccountRepository implements AccountRepository
{
    JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcAccountRepository( DataSource dataSource )
    {
        jdbcTemplate = new JdbcTemplate( dataSource );
    }

    public Account getAccount( String accountname )
    {
        return jdbcTemplate.queryForObject( "select * from accounts where accountname = ?", new AccountRowMapper( ), accountname );
    }

}
