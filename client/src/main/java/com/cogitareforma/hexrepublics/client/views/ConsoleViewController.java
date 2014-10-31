package com.cogitareforma.hexrepublics.client.views;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.makhana.common.entities.components.ArcherTrait;
import com.cogitareforma.makhana.common.entities.components.ArcheryTrait;
import com.cogitareforma.makhana.common.entities.components.LocationTrait;
import com.cogitareforma.makhana.common.entities.components.TileTrait;
import com.cogitareforma.makhana.common.net.msg.EntityCreationRequest;
import com.cogitareforma.makhana.common.net.msg.EntityDeletionRequest;
import com.cogitareforma.makhana.common.net.msg.ReadyUpRequest;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;
import com.simsilica.es.filter.AndFilter;
import com.simsilica.es.filter.FieldFilter;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.ConsoleCommands;
import de.lessvoid.nifty.controls.ConsoleCommands.ConsoleCommand;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;

public class ConsoleViewController extends GeneralController
{
	private Console console;
	private ConsoleCommands consoleCommands;

	@Override
	public void bind( Nifty nifty, Screen screen )
	{
		console = nifty.getScreen( "consoleScreen" ).findNiftyControl( "console", Console.class );
		consoleCommands = new ConsoleCommands( nifty, console );
		consoleCommands.enableCommandCompletion( true );
		console.output( "Wecome to Makhana!" );
		bindCommands( );
	}

	public void bindCommands( )
	{
		ConsoleCommand test = ( String[ ] arg0 ) ->
		{
			console.output( "Test command works" );

		};
		ConsoleCommand login = ( String[ ] arg0 ) ->
		{
			if ( arg0.length != 3 )
			{
				console.output( "Not enough arguments." );
			}
			else
			{
				if ( getApp( ).getMasterConnectionManager( ) != null )
				{
					if ( !getApp( ).getMasterConnectionManager( ).isLoggedIn( ) )
					{
						getApp( ).sendLogin( arg0[ 1 ], arg0[ 2 ] );
					}
					else
					{
						console.output( "Already Logged In" );
					}
				}
				else
				{
					console.output( "No connection with the master server" );
				}
			}
		};
		ConsoleCommand restart = ( String[ ] arg0 ) ->
		{
			getApp( ).restart( );

		};
		ConsoleCommand quit = ( String[ ] arg0 ) ->
		{
			getApp( ).stop( );
		};
		ConsoleCommand account = ( String[ ] arg0 ) ->
		{
			if ( getApp( ).getMasterConnectionManager( ) != null )
			{
				if ( getApp( ).getMasterConnectionManager( ).isLoggedIn( ) )
				{
					console.output( getApp( ).getMasterConnectionManager( ).getSession( ).getDisplayName( ) );
				}
				else
				{
					console.output( "No user is logged in" );
				}
			}
			else
			{
				console.output( "No connection with the master server" );
			}
		};
		// Make args be a trait type
		ConsoleCommand readyUpTester = ( String[ ] arg0 ) ->
		{
			if ( getApp( ).getGameConnectionManager( ).isConnected( ) )
			{
				if ( arg0.length >= 2 )
				{
					if ( "t".equalsIgnoreCase( arg0[ 1 ] ) || "true".equalsIgnoreCase( arg0[ 1 ] ) )
					{
						getApp( ).getGameConnectionManager( ).send( new ReadyUpRequest( true ) );
					}
					else if ( "f".equalsIgnoreCase( arg0[ 1 ] ) || "false".equalsIgnoreCase( arg0[ 1 ] ) )
					{
						getApp( ).getGameConnectionManager( ).send( new ReadyUpRequest( false ) );
					}
					else
					{
						console.output( "Nigga what?" );
					}

				}
				else
				{
					console.output( "Not enough arguments." );
				}

			}
			else
			{
				console.output( "Could not execute command, you are not connected to a game server" );
			}
		};

		ConsoleCommand entCreate = ( String[ ] arg0 ) ->
		{
			if ( getApp( ).getGameConnectionManager( ).isConnected( ) )
			{
				if ( arg0.length >= 2 )
				{
					HudViewController hvc = ( HudViewController ) getApp( ).getStateManager( ).getState( HudViewController.class );
					System.out.println( hvc != null ? hvc.toString( ) : "null" );
					if ( hvc != null )
					{
						Pair< Integer, Integer > coord = hvc.getTileCoord( );
						if ( coord != null )
						{
							ComponentFilter< TileTrait > xFilter = FieldFilter.create( TileTrait.class, "x", coord.getLeft( ) );
							ComponentFilter< TileTrait > yFilter = FieldFilter.create( TileTrait.class, "y", coord.getRight( ) );
							@SuppressWarnings( "unchecked" )
							ComponentFilter< TileTrait > completeFilter = AndFilter.create( TileTrait.class, xFilter, yFilter );
							EntityId location = getApp( ).getGameConnectionManager( ).getRemoteEntityData( )
									.findEntity( completeFilter, TileTrait.class );
							System.out.println( location != null ? location.toString( ) : "null" );
							ArrayList< EntityComponent > components = new ArrayList< EntityComponent >( );
							for ( int i = 1; i < arg0.length; i++ )
							{
								if ( "archer".equalsIgnoreCase( arg0[ i ] ) && !components.contains( new ArcherTrait( ) ) )
								{
									components.add( new ArcherTrait( ) );
								}
								if ( "archery".equalsIgnoreCase( arg0[ i ] ) && !components.contains( new ArcheryTrait( ) ) )
								{
									components.add( new ArcheryTrait( ) );
								}
								// TODO Add rest of units and buildings. Figure
								// out way for mounted units.
							}
							EntityComponent[ ] compArray = new EntityComponent[ components.size( ) ];
							for ( int i = 0; i < compArray.length; i++ )
							{
								compArray[ i ] = components.get( i );
							}

							getApp( ).getGameConnectionManager( ).send(
									new EntityCreationRequest( new LocationTrait( location, ( byte ) 0 ), compArray ) );
						}
					}
				}
				else
				{
					console.output( "Not enough arguments." );
				}

			}
			else
			{
				console.output( "Could not execute command, you are not connected to a game server" );
			}
		};
		ConsoleCommand entDelete = ( String[ ] arg0 ) ->
		{
			if ( getApp( ).getGameConnectionManager( ).isConnected( ) )
			{
				if ( arg0.length != 2 )
				{
					console.output( "Not enough arguments." );
				}
				else
				{
					getApp( ).getGameConnectionManager( ).send( new EntityDeletionRequest( new EntityId( Integer.parseInt( arg0[ 1 ] ) ) ) );
				}
			}
			else
			{
				console.output( "Could not execute command, you are not connected to a game server" );
			}
		};
		ConsoleCommand buyLand = ( String[ ] arg0 ) ->
		{
			if ( arg0.length >= 3 )
			{
				// TODO
			}
			else
			{
				console.output( "Not enough arguments." );
			}

		};

		ConsoleCommand connect = ( String[ ] arg0 ) ->
		{
			if ( getApp( ).getMasterConnectionManager( ).isLoggedIn( ) )
			{
				if ( arg0.length >= 2 )
				{
					if ( arg0[ 1 ].length( ) > 4 )
					{
						Integer port = 7331;

						if ( arg0.length > 2 )
						{
							if ( StringUtils.isNumeric( arg0[ 2 ] ) )
							{
								port = Integer.parseInt( arg0[ 2 ] );
							}
							else
							{
								console.output( "Could not parse port, using default. " );
							}
						}

						getApp( ).getGameConnectionManager( ).connect( arg0[ 1 ], port );
					}
					else
					{
						console.output( "Expected a host, got something too short!" );
					}
				}
				else
				{
					console.output( "Not enough arguments." );
				}
			}
			else
			{
				console.output( "Must be logged in to use this command" );
			}
		};
		ConsoleCommand help = ( String[ ] arg0 ) ->
		{
			console.output( "Current commands:" );
			console.output( "test                           : this is just a test command." );
			console.output( "login <accountname> <password> : Logs in with supplied accountname and password." );
			console.output( "restart                        : refreshes game." );
			console.output( "quit                           : Closes entire game" );
			console.output( "account                        : Shows account" );
			console.output( "entCreate <TraitType>          : Creates an Entity with given type. Needs to be in a Tile." );
			console.output( "entDelete <TraitType>          : Deletes an Entity with given type. Needs to be in a Tile." );
			console.output( "buyLand <x> <y>                : Gives ownership to user. Will not work if tile is owned by other." );
		};
		consoleCommands.registerCommand( "test", test );
		consoleCommands.registerCommand( "help", help );
		consoleCommands.registerCommand( "login", login );
		consoleCommands.registerCommand( "restart", restart );
		consoleCommands.registerCommand( "quit", quit );
		consoleCommands.registerCommand( "account", account );
		consoleCommands.registerCommand( "entcreate", entCreate );
		consoleCommands.registerCommand( "entdelete", entDelete );
		consoleCommands.registerCommand( "buyLand", buyLand );
		consoleCommands.registerCommand( "connect", connect );
		consoleCommands.registerCommand( "setready", readyUpTester );
		consoleCommands.registerCommand( "sr", readyUpTester );
		consoleCommands.enableCommandCompletion( true );
	}

	@Override
	public void onEndScreen( )
	{
	}

	@Override
	public void onStartScreen( )
	{
		TextField consoleInput = getApp( ).getNifty( ).getCurrentScreen( ).findNiftyControl( "console", Console.class ).getTextField( );
		consoleInput.setText( consoleInput.getRealText( ).replace( "`", "" ) );
		consoleInput.setCursorPosition( consoleInput.getRealText( ).length( ) );
	}

}
