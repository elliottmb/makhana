package com.cogitareforma.hexrepublics.masterserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.common.util.YamlConfig;

/**
 * This class provides a wrapper and some utilities for which a MySQL database
 * can be queried and updated.
 * 
 * @author Justin Kaufman
 */
public abstract class MySQLDatabase
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( MySQLDatabase.class.getName( ) );

	/**
	 * The protected constructor restricts instantiation to the DatabaseFactory
	 * and loads the MySQL database driver.
	 */
	protected MySQLDatabase( )
	{
		try
		{
			Class.forName( "com.mysql.jdbc.Driver" ).newInstance( );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Unable to load the MySQL driver.", e );
		}
	}

	/**
	 * Returns the full connection string for this database.
	 * 
	 * @return the full connection string for this database
	 */
	protected static String getConnectString( )
	{
		return String.format( "jdbc:mysql://%s:%s/%s?user=%s&password=%s", YamlConfig.DEFAULT.get( "mysqldb.hostname" ),
				YamlConfig.DEFAULT.get( "mysqldb.port" ), YamlConfig.DEFAULT.get( "mysqldb.database" ),
				YamlConfig.DEFAULT.get( "mysqldb.username" ), YamlConfig.DEFAULT.get( "mysqldb.password" ) );
	}

	/**
	 * The inner class SQLQuery wraps a SQL query for convenience.
	 * 
	 * @author Justin Kaufman
	 */
	protected class SQLQuery
	{

		/**
		 * The database connection.
		 */
		private Connection conn;

		/**
		 * The prepared SQL statement.
		 */
		private PreparedStatement stmt;

		/**
		 * The results form the query.
		 */
		private ResultSet rs;

		/**
		 * The SQL query.
		 */
		private String sql;

		/**
		 * The objects to be placed into the prepared statement.
		 */
		private Object[ ] arguments;

		/**
		 * The constructor for SQL query accepts a prepared SQL query followed
		 * by any number of objects to be placed into the prepared statement.
		 * 
		 * @param sql
		 *            the SQL query
		 * @param arguments
		 *            the objects to put into the prepared statement
		 */
		public SQLQuery( String sql, Object... arguments )
		{
			this.sql = sql;
			this.arguments = arguments;
		}

		/**
		 * Returns a ResultSet containing the results from this query.
		 * Important: you must close this query with closeResults() after use.
		 * 
		 * @return the ResultSet containing the results from this query
		 */
		public ResultSet getResult( )
		{
			logger.log( Level.INFO, "Running MySQL query." );
			try
			{
				conn = DriverManager.getConnection( getConnectString( ) );
				stmt = conn.prepareStatement( sql );
				if ( arguments != null )
				{
					for ( int i = 0; i < arguments.length; i++ )
					{
						if ( arguments[ i ] instanceof String )
						{
							stmt.setString( i + 1, ( String ) arguments[ i ] );
						}
					}
				}
				rs = stmt.executeQuery( );
			}
			catch ( SQLException e )
			{
				logger.log( Level.SEVERE, "Error acccessing the MySQL database!" );
				while ( e != null )
				{
					logger.log( Level.FINE, "Unable to run SQL query successfully", e );
					e = e.getNextException( );
				}
				if ( stmt == null )
				{
					logger.log( Level.FINE, "No SQL statement was able to be formed." );
					logger.log( Level.FINE, "Here is our connect string: " + getConnectString( ) );
				}
				else
				{
					logger.log( Level.FINE, "Our SQL query was: " + stmt.toString( ) );
				}
				if ( stmt != null )
				{
					try
					{
						stmt.close( );
					}
					catch ( SQLException ex )
					{
					} // ignore
				}
				if ( conn != null )
				{
					try
					{
						conn.close( );
					}
					catch ( SQLException ex )
					{
					} // ignore
				}
			}
			logger.log( Level.INFO, "Returning MySQL query results." );
			return rs;
		}

		public void closeResults( )
		{
			logger.log( Level.INFO, "Closing the MySQL query results." );
			if ( rs != null )
			{
				try
				{
					rs.close( );
				}
				catch ( SQLException e )
				{
				} // ignore
			}
			if ( stmt != null )
			{
				try
				{
					stmt.close( );
				}
				catch ( SQLException e )
				{
				} // ignore
			}
			if ( conn != null )
			{
				try
				{
					conn.close( );
				}
				catch ( SQLException e )
				{
				} // ignore
			}
		}
	}

}
