package com.cogitareforma.hexrepublics.client.views;

import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Pair;

import com.cogitareforma.hexrepublics.client.util.NiftyFactory;
import com.cogitareforma.makhana.common.entities.traits.PlayerTrait;
import com.cogitareforma.makhana.common.net.msg.ReadyUpRequest;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.simsilica.es.EntityId;
import com.simsilica.es.client.RemoteEntityData;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Chat;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;

public class LobbyViewController extends GeneralPlayingController
{
	private final static Logger logger = Logger.getLogger( LobbyViewController.class.getName( ) );
	private final static int chatInterval = 1000;

	private Pair< Date, Boolean > lastMessage;
	private Chat chat;
	private DropDown< String > AIDropDown;
	private DropDown< String > worldSize;
	private DropDown< String > maxTurns;
	float updateLimiter = 0;
	private Screen players;
	private Nifty nifty;

	public void back( )
	{
		exitToNetwork( );
	}

	@SuppressWarnings( "unchecked" )
	public void initialize( AppStateManager stateManager, Application app )
	{
		setScreenId( "lobby" );
		super.initialize( stateManager, app );
		nifty = getApp( ).getNifty( );
		this.AIDropDown = nifty.getScreen( "lobby" ).findNiftyControl( "AIOptions", DropDown.class );
		this.worldSize = nifty.getScreen( "lobby" ).findNiftyControl( "worldSizeOptions", DropDown.class );
		this.maxTurns = nifty.getScreen( "lobby" ).findNiftyControl( "maxTurnssOptions", DropDown.class );
		players = getApp( ).getNifty( ).getCurrentScreen( );
		logger.log( Level.INFO, "Initialised " + this.getClass( ) );
		chat = getApp( ).getNifty( ).getCurrentScreen( ).findNiftyControl( "lobbyChat", Chat.class );
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
		getApp( ).sendLobbyChat( event.getText( ) );
	}

	@Override
	protected void postExitToNetwork( )
	{
	}

	@Override
	protected void preExitToNetwork( )
	{
	}

	public void readyUp( )
	{
		// TODO: LOGIC GOES HERE!
		// Issue #33
		getApp( ).getGameConnManager( ).send( new ReadyUpRequest( true ) );
		start( );
	}

	public void showPlayers( )
	{
		if ( getApp( ).getGameConnManager( ).isConnected( ) )
		{
			RemoteEntityData entityData = getApp( ).getGameConnManager( ).getRemoteEntityData( );
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
	}

	public void start( )
	{
		NiftyFactory.createLoadingScreen( getApp( ).getNifty( ) );
		gotoScreen( "loading", true, true, true, null, null );

	}

	@Override
	public void update( float tpf )
	{
		if ( "lobby".equals( getApp( ).getNifty( ).getCurrentScreen( ).getScreenId( ) ) )
		{
			if ( updateLimiter > 1 )
			{
				updateLimiter = 0;
				showPlayers( );
			}
			updateLimiter += tpf;
		}
	}

}
