package com.cogitareforma.hexrepublics.client.views;

import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.cogitareforma.hexrepublics.common.entities.traits.PlayerTrait;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.EntityId;
import com.simsilica.es.client.RemoteEntityData;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class LobbyViewController extends AbstractAppState implements ScreenController
{
	private final static Logger logger = Logger.getLogger( LobbyViewController.class.getName( ) );
	private final static int chatInterval = 1000;

	private ClientMain app;
	private Pair< Date, Boolean > lastMessage;
	private Chat chat;
	float updateLimiter = 0;
	private Screen players;

	public void back( )
	{
		app.enqueue( ( ) ->
		{
			NiftyFactory.createNetworkView( app.getNifty( ) );
			app.getStateManager( ).detach( ( AppState ) app.getNifty( ).getScreen( "lobby" ).getScreenController( ) );
			app.getStateManager( ).attach( ( AppState ) app.getNifty( ).getScreen( "network" ).getScreenController( ) );
			app.getNifty( ).removeScreen( "lobby" );
			app.getNifty( ).gotoScreen( "network" );
			app.getGameConnManager( ).close( );
			return null;
		} );
	}

	public void showPlayers( )
	{
		RemoteEntityData entityData = this.app.getGameConnManager( ).getRemoteEntityData( );
		if ( entityData != null )
		{
			Set< EntityId > playerSet = entityData.findEntities( null, PlayerTrait.class );
			int playerCount = 1;
			for ( EntityId id : playerSet )
			{
				String name = entityData.getComponent( id, PlayerTrait.class ).getAccount( ).getAccountName( );
				players.findNiftyControl( String.format( "player%sname", playerCount ), Label.class ).setText( name );
				playerCount++;
			}
		}
	}

	@Override
	public void update( float tpf )
	{
		if ( "lobby".equals( app.getNifty( ).getCurrentScreen( ).getScreenId( ) ) )
		{
			if ( updateLimiter > 1 )
			{
				updateLimiter = 0;
				showPlayers( );
			}
			updateLimiter += tpf;
		}
	}

	@Override
	public void bind( Nifty nifty, Screen screen )
	{
	}

	public void initialize( AppStateManager stateManager, Application app )
	{
		super.initialize( stateManager, app );
		this.app = ( ClientMain ) app;
		players = this.app.getNifty( ).getCurrentScreen( );
		logger.log( Level.INFO, "Initialised " + this.getClass( ) );
		chat = this.app.getNifty( ).getCurrentScreen( ).findNiftyControl( "lobbyChat", Chat.class );
		showPlayers( );
	}

	@NiftyEventSubscriber( id = "lobbyChat" )
	public void onChatTextSendEvent( final String id, final ChatTextSendEvent event )
	{
		/*
		 * Date timeNow = new Date( ); if ( lastMessage == null ||
		 * lastMessage.getLeft( ).getTime( ) <= timeNow.getTime( ) -
		 * chatInterval ) { logger.log( Level.INFO, "Chat -> " + event.getText(
		 * ) ); // app.sendNetworkChat( event.getText( ) ); app.sendLobbyChat(
		 * event.getText( ) ); lastMessage = Pair.of( timeNow, false ); } else {
		 * if ( lastMessage.getRight( ) == false ) { logger.log( Level.INFO,
		 * "Chat -> You are spamming the chat." ); chat.receivedChatLine(
		 * "Client Warning: You are spamming the chat", null ); lastMessage =
		 * Pair.of( lastMessage.getLeft( ), true ); } }
		 */
		app.sendLobbyChat( event.getText( ) );
	}

	@Override
	public void onEndScreen( )
	{
		logger.log( Level.INFO, "Lobby Screen Stopped" );
	}

	@Override
	public void onStartScreen( )
	{
		logger.log( Level.INFO, "Lobby Screen Started" );
	}

	public void readyUp( )
	{
		// TODO: LOGIC GOES HERE!
		start( );
	}

	public void start( )
	{
		app.enqueue( ( ) ->
		{
			NiftyFactory.createLoadingScreen( app.getNifty( ) );
			app.getStateManager( ).detach( ( AppState ) app.getNifty( ).getScreen( "lobby" ).getScreenController( ) );
			app.getStateManager( ).attach( ( AppState ) app.getNifty( ).getScreen( "loading" ).getScreenController( ) );
			app.getNifty( ).removeScreen( "lobby" );
			app.getNifty( ).gotoScreen( "loading" );
			return null;
		} );
	}

}
