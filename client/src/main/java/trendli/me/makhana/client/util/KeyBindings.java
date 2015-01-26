package trendli.me.makhana.client.util;

import java.lang.reflect.Field;

import trendli.me.makhana.common.util.MakhanaConfig;

import com.jme3.input.KeyInput;

public class KeyBindings
{
	public static String getKeyName( int key )
	{
		for ( Field field : KeyInput.class.getFields( ) )
		{
			try
			{
				if ( key == field.getInt( null ) )
				{
					return field.getName( );
				}
			}
			catch ( Exception ex )
			{

			}
		}
		return null;
	}

	public int chat;
	public int console;
	public int east;
	public int north;
	public int score;
	public int south;
	public int west;

	public KeyBindings( MakhanaConfig config )
	{
		north = ( int ) config.get( "client.input.northKey" );
		south = ( int ) config.get( "client.input.southKey" );
		east = ( int ) config.get( "client.input.eastKey" );
		west = ( int ) config.get( "client.input.westKey" );
		console = ( int ) config.get( "client.input.consoleKey" );
		chat = ( int ) config.get( "client.input.chatKey" );
		score = ( int ) config.get( "client.input.scoreKey" );
	}
}
