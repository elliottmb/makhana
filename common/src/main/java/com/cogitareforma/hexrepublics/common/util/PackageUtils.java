package com.cogitareforma.hexrepublics.common.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Package class is a utility class that contains utilities for working with
 * whole packages at a time.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
public class PackageUtils
{
	/**
	 * Instantiates all non-abstract classes within the given package. May throw
	 * an exception if a class can not be instantiated or the package can not be
	 * found.
	 * 
	 * @param packageName
	 *            given package name of classes to instantiate
	 * @return a list of instantiated objects
	 * @throws Exception
	 *             if the package can not be found or there is an instantiation
	 *             error
	 */
	public static List< Object > createAllInPackage( String packageName ) throws Exception
	{
		return createAllInPackage( packageName, null );
	}

	/**
	 * Instantiates all non-abstract classes within the given package with the
	 * given Object as a constructor argument. May throw an exception if a class
	 * can not be instantiated or the package can not be found.
	 * 
	 * @param packageName
	 *            given package name of classes to instantiate
	 * @param passed
	 *            object to be passed to each classes' constructor
	 * @return a list of instantiated objects
	 * @throws Exception
	 *             if the package can not be found or there is an instantiation
	 *             error
	 */
	public static List< Object > createAllInPackage( String packageName, Object passed ) throws Exception
	{
		List< Object > list = new LinkedList< Object >( );
		for ( String className : getAllClassNamesInPackage( packageName ) )
		{
			Class< ? > clazz = Class.forName( className );
			if ( !Modifier.isAbstract( clazz.getModifiers( ) ) && !Modifier.isInterface( clazz.getModifiers( ) ) )
			{
				if ( passed != null )
				{
					Constructor< ? > ctor = clazz.getConstructor( passed.getClass( ) );
					list.add( ctor.newInstance( passed ) );
				}
				else
				{

					Constructor< ? > ctor = clazz.getConstructor( );
					list.add( ctor.newInstance( ) );
				}
			}
		}
		return list;
	}

	/**
	 * Finds classes recursively given a specific path and package.
	 * 
	 * @param path
	 *            the path to look for classes
	 * @param packageName
	 *            the package of the classes to look for
	 * @return a TreeSet of all the names of the classes for the given path and
	 *         package
	 * @throws IOException
	 *             if the file system is not able to be traversed or the project
	 *             structure is somehow mangled
	 */
	private static TreeSet< String > findClasses( String path, String packageName ) throws IOException
	{
		TreeSet< String > classes = new TreeSet< String >( );

		if ( path.startsWith( "file:" ) && path.contains( "!" ) )
		{
			String[ ] split = path.split( "!" );
			URL jar = new URL( split[ 0 ] );
			ZipInputStream zip = new ZipInputStream( jar.openStream( ) );
			ZipEntry entry;
			while ( ( entry = zip.getNextEntry( ) ) != null )
			{
				if ( entry.getName( ).endsWith( ".class" ) )
				{
					String className = entry.getName( ).replaceAll( "[$].*", "" ).replaceAll( "[.]class", "" ).replace( '/', '.' );
					if ( className.startsWith( packageName ) )
					{
						classes.add( className );
					}
				}
			}
		}

		File dir = new File( path );
		if ( !dir.exists( ) )
		{
			return classes;
		}

		File[ ] files = dir.listFiles( );
		for ( File file : files )
		{
			if ( file.isDirectory( ) )
			{
				classes.addAll( findClasses( file.getAbsolutePath( ), packageName + "." + file.getName( ) ) );
			}
			else if ( file.getName( ).endsWith( ".class" ) )
			{
				String className = packageName + '.' + file.getName( ).substring( 0, file.getName( ).length( ) - 6 );
				classes.add( className );
			}
		}

		return classes;
	}

	public static List< Class< ? > > getAllClassesFromNames( Set< String > names )
	{
		List< Class< ? > > classes = new LinkedList< Class< ? > >( );
		try
		{
			for ( String className : names )
			{
				classes.add( Class.forName( className ) );
			}
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, "Failed to get classes from names: " + Arrays.toString( e.getStackTrace( ) ) );
		}
		return classes;
	}

	/**
	 * Registers a TreeSet of all classes in a package (recursively).
	 * 
	 * @param packageName
	 *            the package name where the classes are located
	 * @throws Exception
	 *             the exception thrown if the file structure of the project is
	 *             somehow unknown or unreadable
	 */
	public static TreeSet< String > getAllClassNamesInPackage( String packageName ) throws IOException
	{
		ClassLoader classLoader = Thread.currentThread( ).getContextClassLoader( );
		Enumeration< URL > resources = classLoader.getResources( packageName.replace( '.', '/' ) );
		List< String > dirs = new ArrayList< String >( );
		while ( resources.hasMoreElements( ) )
		{
			URL resource = ( URL ) resources.nextElement( );
			dirs.add( URLDecoder.decode( resource.getFile( ), "UTF-8" ) );
		}
		TreeSet< String > classes = new TreeSet< String >( );
		for ( String dir : dirs )
		{
			classes.addAll( findClasses( dir, packageName ) );
		}
		return classes;
	}

	public static List< Class< ? > > getAllConcreteClassesFromNames( Set< String > names )
	{
		List< Class< ? > > classes = new LinkedList< Class< ? > >( );
		for ( Class< ? > clazz : getAllClassesFromNames( names ) )
		{
			if ( !Modifier.isAbstract( clazz.getModifiers( ) ) && !Modifier.isInterface( clazz.getModifiers( ) ) )
			{
				classes.add( clazz );
			}
		}
		return classes;
	}

	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( PackageUtils.class.getName( ) );

}
