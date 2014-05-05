package com.cogitareforma.hexrepublics.masterserver.db;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseFactory is a factory pattern which controls and returns the
 * instantiation of all databases used outside of this package.
 * 
 * @author Justin Kaufman
 */
public class DatabaseFactory
{

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( DatabaseFactory.class.getName( ) );

	/**
	 * The User Database instantiation.
	 */
	private AccountDatabase userDb;

	/**
	 * The singleton instance of this class.
	 */
	private static final DatabaseFactory FACTORY = new DatabaseFactory( );

	/**
	 * Returns the user database associated with this factory.
	 * 
	 * @return the user database associated with this factory
	 */
	public static final synchronized AccountDatabase getUserDb( )
	{
		if ( FACTORY.userDb == null )
		{
			logger.log( Level.INFO, "The instantiation of the User Database was null." );
			FACTORY.userDb = new MySQLUserDatabase( );
			logger.log( Level.INFO, "Initialized the User Database." );
		}
		return FACTORY.userDb;
	}

}
