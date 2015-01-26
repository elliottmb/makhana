package trendli.me.makhana.common.util;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.audio.Listener;
import com.jme3.input.KeyInput;
import com.jme3.system.AppSettings;

/**
 * 
 * @author Elliott Butler
 */
public class MakhanaConfig extends ConfigObject
{
	private final static ConfigSlurper cs = new ConfigSlurper( );

	private final static Logger logger = Logger.getLogger( MakhanaConfig.class.getName( ) );

	private File file;

	public MakhanaConfig( )
	{
		this( new File( "config.groovy" ) );
	}

	public MakhanaConfig( File file )
	{
		super( null );

		this.setFile( file );

		if ( !file.exists( ) )
		{
			try
			{
				file.createNewFile( );
			}
			catch ( IOException e )
			{
				logger.log( Level.SEVERE, "Error creating configuration file.", e );
			}
		}

		// Put all the default values (flattened)
		putAll( cs.parse( MakhanaConfig.class.getResource( "/default.groovy" ) ).flatten( ) );

		ConfigObject clientConfig;
		try
		{
			clientConfig = cs.parse( file.toURI( ).toURL( ) );
			// Put all of the client's values (flattened)
			putAll( clientConfig.flatten( ) );
		}
		catch ( MalformedURLException e )
		{
			logger.log( Level.SEVERE, "Error loading configuration file.", e );
		}
	}

	public void configureAppSettings( AppSettings settings )
	{
		if ( settings == null )
		{
			throw new NullPointerException( );
		}

		settings.setHeight( 768 );
		settings.setWidth( 1024 );

		// TODO: Investigate removing the usage of flatten()
		Map< ?, ? > flattenedConfig = flatten( );

		if ( flattenedConfig.containsKey( "client.graphics.height" ) && flattenedConfig.containsKey( "client.graphics.width" ) )
		{
			Object height = flattenedConfig.get( "client.graphics.height" );
			Object width = flattenedConfig.get( "client.graphics.width" );
			if ( height != null && width != null )
			{
				if ( height instanceof Integer && width instanceof Integer )
				{
					settings.setResolution( ( Integer ) width, ( Integer ) height );
				}
				else
				{
					logger.log( Level.WARNING, "Error setting resolution, expected ( Integer, Integer ), got ( "
							+ height.getClass( ).getSimpleName( ) + ", " + width.getClass( ).getSimpleName( ) + " )" );
				}
			}
		}
		if ( flattenedConfig.containsKey( "client.graphics.fullscreen" ) )
		{
			Object value = flattenedConfig.get( "client.graphics.fullscreen" );
			if ( value != null )
			{
				if ( value instanceof Boolean )
				{
					settings.setFullscreen( ( Boolean ) value );
				}
				else
				{
					logger.log( Level.WARNING, "Error setting fullscreen, expected Boolean, got " + value.getClass( ).getSimpleName( ) );
				}
			}
		}
		if ( flattenedConfig.containsKey( "client.graphics.vsync" ) )
		{
			Object value = flattenedConfig.get( "client.graphics.vsync" );
			if ( value != null )
			{
				if ( value instanceof Boolean )
				{
					settings.setVSync( ( Boolean ) value );
				}
				else
				{
					logger.log( Level.WARNING, "Error setting vsync, expected Boolean, got " + value.getClass( ).getSimpleName( ) );
				}
			}
		}
		if ( flattenedConfig.containsKey( "client.graphics.samples" ) )
		{
			Object value = flattenedConfig.get( "client.graphics.samples" );
			if ( value != null )
			{
				if ( value instanceof Integer )
				{
					settings.setSamples( ( Integer ) value );
				}
				else
				{
					logger.log( Level.WARNING, "Error setting samples, expected Integer, got " + value.getClass( ).getSimpleName( ) );
				}
			}
		}
	}

	public void configureAudioSettings( Listener listener )
	{
		if ( listener == null )
		{
			throw new NullPointerException( );
		}
		// TODO: Investigate removing the usage of flatten()
		Map< ?, ? > flattenedConfig = flatten( );

		if ( flattenedConfig.containsKey( "client.audio.mainvolume" ) )
		{
			Object value = flattenedConfig.get( "client.audio.mainvolume" );
			if ( value != null )
			{
				if ( value instanceof Double )
				{
					listener.setVolume( ( ( Double ) value ).floatValue( ) / 50.0f );
				}
				else
				{
					logger.log( Level.WARNING, "Error setting main volume, expected Double, got " + value.getClass( ).getSimpleName( ) );
				}
			}
		}
	}

	public void putAppSettings( AppSettings settings )
	{
		if ( settings == null )
		{
			throw new NullPointerException( );
		}

		put( "client.graphics.height", settings.getHeight( ) );
		put( "client.graphics.width", settings.getWidth( ) );
		put( "client.graphics.fullscreen", settings.isFullscreen( ) );
		put( "client.graphics.vsync", settings.isVSync( ) );
		put( "client.graphics.samples", settings.getSamples( ) );
		put( "client.graphics.frequency", settings.getFrequency( ) );
	}

	public void putAudioSettings( Listener listener )
	{
		if ( listener == null )
		{
			throw new NullPointerException( );
		}

		put( "client.audio.mainvolume", new Double( listener.getVolume( ) ) );
	}

	public void putDefaultKeys( )
	{
		setProperty( "client.input.consoleKey", KeyInput.KEY_GRAVE );
		setProperty( "client.input.scoreKey", KeyInput.KEY_TAB );
		setProperty( "client.input.chatKey", KeyInput.KEY_T );
		setProperty( "client.input.northKey", KeyInput.KEY_W );
		setProperty( "client.input.southKey", KeyInput.KEY_S );
		setProperty( "client.input.westKey", KeyInput.KEY_A );
		setProperty( "client.input.eastKey", KeyInput.KEY_D );
	}

	public void save( )
	{
		save( this.file );
	}

	public void save( File file )
	{
		try
		{
			FileWriter output = new FileWriter( file );
			writeTo( output );
			output.close( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Error saving configuration file.", e );
		}
	}

	/**
	 * @return the file
	 */
	public File getFile( )
	{
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile( File file )
	{
		this.file = file;
	}
}
