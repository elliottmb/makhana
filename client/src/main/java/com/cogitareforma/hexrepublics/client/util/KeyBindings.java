package com.cogitareforma.hexrepublics.client.util;

import java.lang.reflect.Field;

import com.cogitareforma.hexrepublics.common.util.YamlConfig;
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

	private YamlConfig con = YamlConfig.DEFAULT;
	public int north = ( int ) con.get( "client.input.northKey" );
	public int south = ( int ) con.get( "client.input.southKey" );
	public int east = ( int ) con.get( "client.input.eastKey" );
	public int west = ( int ) con.get( "client.input.westKey" );
	public int console = ( int ) con.get( "client.input.consoleKey" );
	public int chat = ( int ) con.get( "client.input.chatKey" );

	public int score = ( int ) con.get( "client.input.scoreKey" );
}
