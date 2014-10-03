package com.cogitareforma.makhana.masterserver.db;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.cogitareforma.makhana.common.util.YamlConfig;

/**
 * 
 * @author Elliott Butler
 *
 */
@Configuration
public class DatabaseConfig
{

	@Bean
	public AccountRepository accountRepository( )
	{
		return new JdbcAccountRepository( dataSource( ) );
	}

	@Bean
	public DataSource dataSource( )
	{
		YamlConfig config = YamlConfig.DEFAULT;
		DriverManagerDataSource dataSource = new DriverManagerDataSource( );
		dataSource.setDriverClassName( "com.mysql.jdbc.Driver" );
		dataSource.setUrl( String.format( "jdbc:mysql://%s:%s/%s", config.get( "mysqldb.hostname" ), config.get( "mysqldb.port" ),
				config.get( "mysqldb.database" ) ) );
		dataSource.setUsername( ( config.get( "mysqldb.username" ) != null ) ? ( String ) config.get( "mysqldb.username" ) : "root" );
		dataSource.setPassword( ( config.get( "mysqldb.password" ) != null ) ? ( String ) config.get( "mysqldb.password" ) : "" );
		return dataSource;
	}

}
