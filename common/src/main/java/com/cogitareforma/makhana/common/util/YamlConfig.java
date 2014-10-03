package com.cogitareforma.makhana.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.jme3.audio.Listener;
import com.jme3.input.KeyInput;
import com.jme3.system.AppSettings;

/**
 * 
 * @author Elliott Butler
 */
public class YamlConfig
{
	private final static Logger logger = Logger.getLogger( YamlConfig.class.getName( ) );
	public static YamlConfig DEFAULT = new YamlConfig( new File( "config.yml" ) );
	private File file;
	private Map< String, Object > data;
	private Yaml yaml;

	public YamlConfig( File file )
	{
		DumperOptions options = new DumperOptions( );
		options.setDefaultFlowStyle( DumperOptions.FlowStyle.BLOCK );
		this.file = file;
		this.yaml = new Yaml( options );
		this.data = new LinkedHashMap< String, Object >( );

		try
		{
			BufferedReader input = new BufferedReader( new InputStreamReader( this.getClass( ).getResourceAsStream( "/" + "default.yml" ) ) );
			if ( file.exists( ) )
			{
				input.close( );
				input = new BufferedReader( new FileReader( file ) );
				logger.log( Level.INFO, "Loading yaml configuration from config.yml" );
			}

			Map< ?, ? > raw = ( Map< ?, ? > ) yaml.load( input );
			input.close( );

			for ( Map.Entry< ?, ? > entry : raw.entrySet( ) )
			{
				put( entry.getKey( ).toString( ), entry.getValue( ) );
			}
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Error loading yaml configuration file.", e );
		}

	}

	public void buildAppSettings( AppSettings settings )
	{
		if ( settings == null )
		{
			throw new NullPointerException( );
		}

		settings.setHeight( 768 );
		settings.setWidth( 1024 );

		if ( containsKey( "client.graphics.height" ) && containsKey( "client.graphics.width" ) )
		{
			Object height = get( "client.graphics.height" );
			Object width = get( "client.graphics.width" );
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
		if ( containsKey( "client.graphics.fullscreen" ) )
		{
			Object value = get( "client.graphics.fullscreen" );
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
		if ( containsKey( "client.graphics.vsync" ) )
		{
			Object value = get( "client.graphics.vsync" );
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
		if ( containsKey( "client.graphics.samples" ) )
		{
			Object value = get( "client.graphics.samples" );
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

	public void buildAudioSettings( Listener listener )
	{
		if ( listener == null )
		{
			throw new NullPointerException( );
		}

		if ( containsKey( "client.audio.mainvolume" ) )
		{
			Object value = get( "client.audio.mainvolume" );
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

	public void buildInputSettings( )
	{

		putInputSettings( "console", KeyInput.KEY_GRAVE );
		putInputSettings( "score", KeyInput.KEY_TAB );
		putInputSettings( "chat", KeyInput.KEY_T );
		putInputSettings( "north", KeyInput.KEY_W );
		putInputSettings( "south", KeyInput.KEY_S );
		putInputSettings( "west", KeyInput.KEY_A );
		putInputSettings( "east", KeyInput.KEY_D );
		save( );
	}

	@SuppressWarnings( "unchecked" )
	private boolean containsKey( Map< String, Object > data, String[ ] levels, int level )
	{
		if ( levels.length > 0 && level < levels.length )
		{
			Object value = data.get( levels[ level ] );
			if ( value != null )
			{
				if ( level < levels.length - 1 )
				{
					if ( value instanceof Map< ?, ? > )
					{
						return containsKey( ( Map< String, Object > ) value, levels, level + 1 );
					}
					return false;
				}
				return true;
			}

		}
		return false;
	}

	public boolean containsKey( String theKey )
	{
		if ( theKey == null )
		{
			throw new NullPointerException( );
		}
		if ( theKey.length( ) > 0 )
		{
			String[ ] levels = theKey.split( "\\." );
			return containsKey( this.data, levels, 0 );
		}
		return false;
	}

	@SuppressWarnings( "unchecked" )
	private Object get( Map< String, Object > data, String[ ] levels, int level )
	{
		if ( levels.length > 0 && level < levels.length )
		{
			Object value = data.get( levels[ level ] );
			if ( value != null )
			{
				if ( level < levels.length - 1 )
				{
					if ( value instanceof Map< ?, ? > )
					{
						return get( ( Map< String, Object > ) value, levels, level + 1 );
					}
					return null;
				}
				return value;
			}

		}
		return null;
	}

	public Object get( String theKey )
	{
		if ( theKey == null )
		{
			throw new NullPointerException( );
		}
		if ( theKey.length( ) > 0 )
		{
			String[ ] levels = theKey.split( "\\." );
			return get( this.data, levels, 0 );
		}
		return null;
	}

	@SuppressWarnings( "unchecked" )
	private void put( Map< String, Object > data, Object theValue, String[ ] levels, int level )
	{
		if ( levels.length > 0 && level < levels.length )
		{
			if ( level == levels.length - 1 )
			{
				data.put( levels[ level ], theValue );
			}
			else
			{
				Object currentLevel = data.get( levels[ level ] );
				if ( currentLevel == null || !( currentLevel instanceof Map< ?, ? > ) )
				{
					currentLevel = new LinkedHashMap< String, Object >( );
					data.put( levels[ level ], currentLevel );

				}
				if ( currentLevel instanceof Map< ?, ? > )
				{
					level++;
					put( ( Map< String, Object > ) currentLevel, theValue, levels, level );
				}
			}
		}
	}

	public void put( String theKey, Object theValue )
	{
		if ( theKey == null || theValue == null )
		{
			throw new NullPointerException( );
		}

		if ( theKey.length( ) > 0 )
		{
			String[ ] levels = theKey.split( "\\." );
			int level = 0;
			put( this.data, theValue, levels, level );
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

	public void putInputSettings( String name, int key )
	{
		if ( name == null )
		{
			throw new NullPointerException( );
		}
		put( "client.input." + name + "Key", key );
	}

	public void save( )
	{
		try
		{
			FileWriter output = new FileWriter( this.file );
			yaml.dump( this.data, output );
			output.close( );
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "Error saving yaml configuration file.", e );
		}
	}
}
