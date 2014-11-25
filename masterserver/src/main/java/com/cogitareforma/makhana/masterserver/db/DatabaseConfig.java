package com.cogitareforma.makhana.masterserver.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.cogitareforma.makhana.common.util.MakhanaConfig;

/**
 * 
 * @author Elliott Butler
 *
 */
@Configuration
public class DatabaseConfig
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( DatabaseConfig.class.getName( ) );

	@Bean
	public AccountRepository accountRepository( )
	{
		return new JdbcAccountRepository( dataSource( ) );
	}

	@Bean
	public DataSource dataSource( )
	{
		MakhanaConfig config = new MakhanaConfig( );
		config.save( );

		DriverManagerDataSource dataSource = new DriverManagerDataSource( );
		dataSource.setDriverClassName( "com.mysql.jdbc.Driver" );
		dataSource.setUrl( String.format( "jdbc:mysql://%s:%s/%s",
				( config.get( "mysqldb.hostname" ) != null ) ? ( String ) config.get( "mysqldb.hostname" ) : "localhost", ( config
						.get( "mysqldb.port" ) != null ) ? config.get( "mysqldb.port" ) : 3306,
				( config.get( "mysqldb.database" ) != null ) ? config.get( "mysqldb.database" ) : "network" ) );
		dataSource.setUsername( ( config.get( "mysqldb.username" ) != null ) ? ( String ) config.get( "mysqldb.username" ) : "root" );
		dataSource.setPassword( ( config.get( "mysqldb.password" ) != null ) ? ( String ) config.get( "mysqldb.password" ) : "" );

		logger.log( Level.INFO, String.format( "Creating a DataSource with hostname: %s, port: %s, database: %s, username: %s", ( config
				.get( "mysqldb.hostname" ) != null ) ? ( String ) config.get( "mysqldb.hostname" ) : "localhost", ( config
				.get( "mysqldb.port" ) != null ) ? config.get( "mysqldb.port" ) : 3306,
				( config.get( "mysqldb.database" ) != null ) ? config.get( "mysqldb.database" ) : "network" ), ( config
				.get( "mysqldb.username" ) != null ) ? ( String ) config.get( "mysqldb.username" ) : "root" );

		return dataSource;
	}
}
