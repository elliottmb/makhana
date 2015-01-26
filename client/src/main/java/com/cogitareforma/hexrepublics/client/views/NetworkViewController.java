package com.cogitareforma.hexrepublics.client.views;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import trendli.me.makhana.client.util.NiftyFactory;
import trendli.me.makhana.common.data.ServerStatus;
import trendli.me.makhana.common.net.msg.ServerListRequest;
import trendli.me.makhana.common.net.msg.UserListRequest;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

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

/**
 * 
 * @author Ryan Grier
 */
public class NetworkViewController extends GeneralController implements KeyInputHandler
{
	/**
	 * The logger for this class.
	 */
	private final static Logger logger = Logger.getLogger( NetworkViewController.class.getName( ) );

	private final static int chatInterval = 1000;

	private Chat chat;
	private Pair< Date, Boolean > lastMessage;
	private boolean search;
	private String searchText;
	private String serverSelected = "";
	private int port = 0;

	public void applyFilters( )
	{
		@SuppressWarnings( "unchecked" )
		ListBox< ServerStatus > networks = getApp( ).getNifty( ).getScreen( "network" ).findNiftyControl( "networkScroll", ListBox.class );
		networks.clear( );
		RadioButton full = getApp( ).getNifty( ).getScreen( "network" ).findNiftyControl( "option-2", RadioButton.class );
		RadioButton empty = getApp( ).getNifty( ).getScreen( "network" ).findNiftyControl( "option-3", RadioButton.class );
		CheckBox hasPlayers = getApp( ).getNifty( ).getScreen( "network" ).findNiftyControl( "hasPlayer", CheckBox.class );
		for ( ServerStatus stat : getApp( ).getMasterConnectionManager( ).getServers( ) )
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

	public void getUsers( )
	{

	}

	/**
	 * Starts chat and lists all legal game lobbies that can be joined.
	 */
	public void initialize( AppStateManager stateManager, Application app )
	{
		setScreenId( "network" );
		super.initialize( stateManager, app );

		getApp( ).getMasterConnectionManager( ).send( new UserListRequest( ) );
		getApp( ).getMasterConnectionManager( ).send( new ServerListRequest( ) );
		logger.log( Level.INFO, "Initialised " + this.getClass( ) );
		getApp( ).getNifty( ).getCurrentScreen( ).findElementByName( "chat" );
		chat = getApp( ).getNifty( ).getCurrentScreen( ).findNiftyControl( "networkChat", Chat.class );
		getApp( ).currentScreen = "network";
	}

	public void joinServer( )
	{
		if ( this.serverSelected.length( ) > 0 || this.port != 0 )
		{
			System.out.println( this.serverSelected + " , " + this.port );
			getApp( ).connectToGameSever( serverSelected, port );
		}
	}

	public boolean keyEvent( NiftyInputEvent inputEvent )
	{
		return false;
	}

	public void logout( )
	{
		NiftyFactory.createStartView( getApp( ).getNifty( ) );
		gotoScreen( "start", true, true, true, ( ) ->
		{
			getApp( ).sendLogout( );
			return null;
		}, null );
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
			getApp( ).sendNetworkChat( event.getText( ) );
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
		getApp( ).getMasterConnectionManager( ).send( new ServerListRequest( ) );
	}
}
