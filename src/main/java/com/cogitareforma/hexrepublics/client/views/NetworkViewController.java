package com.cogitareforma.hexrepublics.client.views;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.cogitareforma.hexrepublics.common.data.ServerStatus;
import com.cogitareforma.hexrepublics.common.net.msg.ServerListRequest;
import com.cogitareforma.hexrepublics.common.net.msg.UserListRequest;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.RadioButton;
import de.lessvoid.nifty.controls.RadioButtonGroupStateChangedEvent;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * 
 * @author Ryan Grier
 */
public class NetworkViewController extends AbstractAppState implements ScreenController, KeyInputHandler
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( NetworkViewController.class.getName( ) );

	private final static int chatInterval = 1000;

	private ClientMain app;
	private Chat chat;
	private Pair< Date, Boolean > lastMessage;
	private boolean search;
	private String searchText;
	private String serverSelected = "";
	private int port = 0;

	public void applyFilters( )
	{
		@SuppressWarnings( "unchecked" )
		ListBox< ServerStatus > networks = this.app.getNifty( ).getScreen( "network" ).findNiftyControl( "networkScroll", ListBox.class );
		networks.clear( );
		RadioButton full = this.app.getNifty( ).getScreen( "network" ).findNiftyControl( "option-2", RadioButton.class );
		RadioButton empty = this.app.getNifty( ).getScreen( "network" ).findNiftyControl( "option-3", RadioButton.class );
		CheckBox hasPlayers = this.app.getNifty( ).getScreen( "network" ).findNiftyControl( "hasPlayer", CheckBox.class );
		for ( ServerStatus stat : this.app.getMasterConnManager( ).getServers( ) )
		{
			if ( search == true )
			{
				String serverName = stat.getServerName( ).toLowerCase( );
				String search = this.searchText.toLowerCase( );
				if ( serverName.contains( search ) || search.contains( serverName ) )
				{
					networks.addItem( stat );
				}
			}
			else
			{
				if ( full.isActivated( ) )
				{
					if ( stat.getCurrentPlayers( ) == stat.getMaxPlayers( ) )
					{
						networks.addItem( stat );
					}
				}
				else if ( empty.isActivated( ) )
				{
					if ( stat.getCurrentPlayers( ) == 0 )
					{
						networks.addItem( stat );
					}
				}
				else
				{
					if ( hasPlayers.isChecked( ) )
					{
						if ( stat.getCurrentPlayers( ) > 0 && stat.getCurrentPlayers( ) < stat.getMaxPlayers( ) )
						{
							networks.addItem( stat );
						}
					}
					else
					{
						networks.addItem( stat );
					}
				}
			}
		}
	}

	public void bind( Nifty nifty, Screen screen )
	{
	}

	public void getUsers( )
	{

	}

	/**
	 * Starts chat and lists all legal game lobbies that can be joined.
	 */
	public void initialize( AppStateManager stateManager, Application app )
	{
		super.initialize( stateManager, app );
		this.app = ( ClientMain ) app;
		this.app.getMasterConnManager( ).send( new UserListRequest( ) );
		this.app.getMasterConnManager( ).send( new ServerListRequest( ) );
		logger.log( Level.INFO, "Initialised " + this.getClass( ) );
		this.app.getNifty( ).getCurrentScreen( ).findElementByName( "chat" );
		chat = this.app.getNifty( ).getCurrentScreen( ).findNiftyControl( "networkChat", Chat.class );
		this.app.currentScreen = "network";
	}

	public void joinServer( )
	{
		if ( this.serverSelected.length( ) > 0 || this.port != 0 )
		{
			System.out.println( this.serverSelected + " , " + this.port );
			app.connectToGameSever( serverSelected, port );
			start( );
		}
	}

	public boolean keyEvent( NiftyInputEvent inputEvent )
	{
		return false;
	}

	public void logout( )
	{
		app.sendLogout( );
		app.enqueue( ( ) ->
		{
			NiftyFactory.createStartView( app.getNifty( ) );
			app.getStateManager( ).detach( ( AppState ) app.getNifty( ).getCurrentScreen( ).getScreenController( ) );
			app.getNifty( ).removeScreen( "network" );
			app.getNifty( ).gotoScreen( "start" );
			app.getStateManager( ).attach( ( AppState ) app.getNifty( ).getScreen( "start" ).getScreenController( ) );
			return null;
		} );
	}

	public void nextScreen( String screen )
	{
		if ( screen.equals( "hud" ) )
		{
			NiftyFactory.createHudView( this.app.getNifty( ) );
			this.app.getNifty( ).removeScreen( "start" );
			this.app.getNifty( ).removeScreen( "network" );
			this.app.getNifty( ).gotoScreen( screen );
		}
		else
		{
			this.app.getNifty( ).gotoScreen( screen );
		}
	}

	/**
	 * Sends the text the user inputs into the chat to the server.
	 * 
	 * @param id
	 * @param event
	 */
	@NiftyEventSubscriber( id = "networkChat" )
	public void onChatTextSendEvent( final String id, final ChatTextSendEvent event )
	{
		Date timeNow = new Date( );
		if ( lastMessage == null || lastMessage.getLeft( ).getTime( ) <= timeNow.getTime( ) - chatInterval )
		{
			logger.log( Level.INFO, "Chat -> " + event.getText( ) );
			app.sendNetworkChat( event.getText( ) );
			lastMessage = Pair.of( timeNow, false );
		}
		else
		{
			if ( lastMessage.getRight( ) == false )
			{
				logger.log( Level.INFO, "Chat -> You are spamming the chat." );
				chat.receivedChatLine( "Client Warning: You are spamming the chat", null );
				lastMessage = Pair.of( lastMessage.getLeft( ), true );
			}
		}
	}

	public void onEndScreen( )
	{
		logger.log( Level.INFO, "Network Screen Stopped" );
	}

	/**
	 * Gives which game lobby the user has selected. Currently just logs
	 * selected.
	 * 
	 * @param id
	 * @param event
	 */
	@NiftyEventSubscriber( id = "networkScroll" )
	public void onListBoxSelectionChanged( final String id, final ListBoxSelectionChangedEvent< ServerStatus > event )
	{
		if ( event != null )
		{
			if ( event.getSelection( ).size( ) > 0 )
			{
				List< ServerStatus > stat = event.getSelection( );
				serverSelected = stat.get( 0 ).getAddress( );
				port = stat.get( 0 ).getPort( );
				logger.log( Level.INFO, stat.toString( ) );
			}
		}
		// TODO: do something with the selection

	}

	@NiftyEventSubscriber( id = "RadioGroup-1" )
	public void onRadioGroup1Changed( final String id, final RadioButtonGroupStateChangedEvent event )
	{
		/*
		 * System.out.println( "RadioButton [" + event.getSelectedId( ) +
		 * "] is now selected. The old selection was [" +
		 * event.getPreviousSelectedId( ) + "]" );
		 */
	}

	public void onStartScreen( )
	{
		logger.log( Level.INFO, "Network Screen Started" );
	}

	@NiftyEventSubscriber( id = "search" )
	public void onTextFieldChanged( final String id, final TextFieldChangedEvent event )
	{
		if ( event.getText( ).equals( "" ) )
		{
			search = false;
			searchText = "";
		}
		else
		{
			search = true;
			searchText = event.getText( );
		}
	}

	public void refreshServerList( )
	{
		this.app.getMasterConnManager( ).send( new ServerListRequest( ) );
	}

	/**
	 * Starts the hud view and detaches the network lobby.
	 */
	public void start( )
	{
		app.enqueue( ( ) ->
		{
			NiftyFactory.createGameLobby( app.getNifty( ) );
			app.getStateManager( ).detach( ( AppState ) app.getNifty( ).getScreen( "network" ).getScreenController( ) );
			app.getStateManager( ).attach( ( AppState ) app.getNifty( ).getScreen( "lobby" ).getScreenController( ) );
			app.getNifty( ).removeScreen( "network" );
			app.getNifty( ).gotoScreen( "lobby" );
			return null;
		} );
	}
}
