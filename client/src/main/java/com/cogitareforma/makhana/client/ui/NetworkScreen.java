package com.cogitareforma.makhana.client.ui;

import java.util.ArrayList;

import com.cogitareforma.makhana.common.ui.Screen;
import com.cogitareforma.makhana.common.ui.ScreenContext;
import com.cogitareforma.makhana.common.ui.ScreenManager;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class NetworkScreen extends Screen
{

	private Button servers;
	private Button favorites;
	private Button history;
	private Container serverContainer;
	private Button back;
	private Container server1;
	private Container server2;
	private Container server3;
	private Container server4;
	private Container server5;
	private Container server6;
	private Container server7;
	private Container server8;
	private Container label;
	private Button serverNameLabel;
	private Button serverPlayersLabel;
	private Button serverPingLabel;
	private Button serverOtherLabel;
	private Button firstPage;
	private Button prevPage;
	private Button nextPage;
	private Button lastPage;

	private ArrayList< String > serverList;

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@SuppressWarnings( "unchecked" )
	private void setUpButtons( ScreenManager screenManager )
	{
		servers = new Button( "Servers" );
		servers.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		servers.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		servers.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Servers Clicked" );
				// activateContainer( serverContainer );
			}
		} );
		favorites = new Button( "Favorites" );
		favorites.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		favorites.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		favorites.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Favorites Clicked" );
				// activateContainer( favoritesContainer );
			}
		} );
		history = new Button( "History" );
		history.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		history.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		history.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "History Clicked" );
				// activateContainer( historyContainer );
			}
		} );
		back = new Button( "Back" );
		back.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		back.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		back.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Exit Network Clicked: " + screenManager.setScreen( StartScreen.class ) );
			}
		} );
		lastPage = new Button( "|>" );
		lastPage.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		lastPage.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		lastPage.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Last Page Clicked: " + screenManager.setScreen( LoadingScreen.class ) );
				
			}
		} );
		nextPage = new Button( ">" );
		nextPage.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		nextPage.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		nextPage.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Next Page Clicked" );
			}
		} );
		prevPage = new Button( "<" );
		prevPage.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		prevPage.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		prevPage.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Prev Page Clicked" );
			}
		} );
		firstPage = new Button( "<|" );
		firstPage.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		firstPage.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		firstPage.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "First Page Clicked" );
			}
		} );
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		Camera cam = screenManager.getApp( ).getCamera( );
		ScreenContext sc = screenManager.getScreenContext( );

		setUpButtons( screenManager );

		Panel background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, cam.getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ), 0 ) );
		getScreenNode( ).attachChild( background );

		Container top = new Container( new BoxLayout( Axis.X, FillMode.Proportional ), "glass" );
		top.setLocalTranslation( 0, cam.getHeight( ), 0 );
		top.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		top.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ) * 0.1f, 0 ) );
		Label name = new Label( "Network" );
		name.scale( sc.getHeightScalar( ) );
		name.setPreferredSize( new Vector3f( 0.8f, 0, 0 ) );

		Container buttons = new Container( new BoxLayout( Axis.X, FillMode.Even ), "glass" );
		buttons.setPreferredSize( new Vector3f( 0.05f, 0, 0 ) );

		top.addChild( name );
		top.addChild( buttons );

		back.setFontSize( sc.getMediumFontSize( ) );
		buttons.addChild( back );

		getScreenNode( ).attachChild( top );

		Container tabs = new Container( new BoxLayout( Axis.X, FillMode.None ) );
		tabs.setPreferredSize( new Vector3f( cam.getWidth( ) * .4f, cam.getHeight( ) * .05f, 0 ) );
		tabs.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.85f, 0 );
		tabs.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );

		Insets3f buttonInsets = new Insets3f( 0, 0, 0, cam.getWidth( ) * 0.05f );
		servers.setFontSize( sc.getMediumFontSize( ) );
		servers.setInsets( buttonInsets );

		favorites.setFontSize( sc.getMediumFontSize( ) );
		favorites.setInsets( buttonInsets );

		history.setFontSize( sc.getMediumFontSize( ) );
		history.setInsets( buttonInsets );

		tabs.addChild( servers );
		tabs.addChild( favorites );
		tabs.addChild( history );

		label = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		label.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .05f, 0 ) );
		label.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * .8f, 0 );
		label.setBackground( new QuadBackgroundComponent( ColorRGBA.LightGray ) );

		serverNameLabel = new Button( "Name" );
		serverNameLabel.setFontSize( sc.getMediumFontSize( ) );
		serverPlayersLabel = new Button( "Players" );
		serverPlayersLabel.setFontSize( sc.getMediumFontSize( ) );
		serverPingLabel = new Button( "Ping" );
		serverPingLabel.setFontSize( sc.getMediumFontSize( ) );
		serverOtherLabel = new Button( "Other" );
		serverOtherLabel.setFontSize( sc.getMediumFontSize( ) );
		label.addChild( serverNameLabel );
		label.addChild( serverPlayersLabel );
		label.addChild( serverPingLabel );
		label.addChild( serverOtherLabel );

		serverContainer = new Container( new BoxLayout( Axis.Y, FillMode.Proportional ) );
		serverContainer.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		serverContainer.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .65f, 0 ) );
		serverContainer.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.75f, 0 );

		Vector3f serverCellSize = new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * 0.1125f, 0 );

		server1 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server1.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		server1.setPreferredSize( serverCellSize.clone( ) );

		server2 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server2.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		server2.setPreferredSize( serverCellSize.clone( ) );

		server3 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server3.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		server3.setPreferredSize( serverCellSize.clone( ) );

		server4 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server4.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		server4.setPreferredSize( serverCellSize.clone( ) );

		server5 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server5.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		server5.setPreferredSize( serverCellSize.clone( ) );

		server6 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server6.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		server6.setPreferredSize( serverCellSize.clone( ) );

		server7 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server7.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		server7.setPreferredSize( serverCellSize.clone( ) );

		server8 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server8.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		server8.setPreferredSize( serverCellSize.clone( ) );

		Label server1Name = new Label( "Test1" );
		server1Name.setFontSize( sc.getMediumFontSize( ) );
		Label server1Size = new Label( "456" );
		server1Size.setFontSize( sc.getMediumFontSize( ) );
		Label server1Ping = new Label( "10" );
		server1Ping.setFontSize( sc.getMediumFontSize( ) );
		Label server1Other = new Label( "NO" );
		server1Other.setFontSize( sc.getMediumFontSize( ) );

		Label server2Name = new Label( "Test1" );
		server2Name.setFontSize( sc.getMediumFontSize( ) );
		Label server2Size = new Label( "654" );
		server2Size.setFontSize( sc.getMediumFontSize( ) );
		Label server2Ping = new Label( "50" );
		server2Ping.setFontSize( sc.getMediumFontSize( ) );
		Label server2Other = new Label( "NO" );
		server2Other.setFontSize( sc.getMediumFontSize( ) );

		Label server3Name = new Label( "Test1" );
		server3Name.setFontSize( sc.getMediumFontSize( ) );
		Label server3Size = new Label( "548" );
		server3Size.setFontSize( sc.getMediumFontSize( ) );
		Label server3Ping = new Label( "100" );
		server3Ping.setFontSize( sc.getMediumFontSize( ) );
		Label server3Other = new Label( "NO" );
		server3Other.setFontSize( sc.getMediumFontSize( ) );

		Label server4Name = new Label( "Test1" );
		server4Name.setFontSize( sc.getMediumFontSize( ) );
		Label server4Size = new Label( "5498" );
		server4Size.setFontSize( sc.getMediumFontSize( ) );
		Label server4Ping = new Label( "45" );
		server4Ping.setFontSize( sc.getMediumFontSize( ) );
		Label server4Other = new Label( "No" );
		server4Other.setFontSize( sc.getMediumFontSize( ) );

		Label server5Name = new Label( "Test1v" );
		server5Name.setFontSize( sc.getMediumFontSize( ) );
		Label server5Size = new Label( "6549" );
		server5Size.setFontSize( sc.getMediumFontSize( ) );
		Label server5Ping = new Label( "500" );
		server5Ping.setFontSize( sc.getMediumFontSize( ) );
		Label server5Other = new Label( "NO" );
		server5Other.setFontSize( sc.getMediumFontSize( ) );

		Label server6Name = new Label( "Test1" );
		server6Name.setFontSize( sc.getMediumFontSize( ) );
		Label server6Size = new Label( "6548" );
		server6Size.setFontSize( sc.getMediumFontSize( ) );
		Label server6Ping = new Label( "50" );
		server6Ping.setFontSize( sc.getMediumFontSize( ) );
		Label server6Other = new Label( "NO" );
		server6Other.setFontSize( sc.getMediumFontSize( ) );

		Label server7Name = new Label( "Test1" );
		server7Name.setFontSize( sc.getMediumFontSize( ) );
		Label server7Size = new Label( "1589" );
		server7Size.setFontSize( sc.getMediumFontSize( ) );
		Label server7Ping = new Label( "506" );
		server7Ping.setFontSize( sc.getMediumFontSize( ) );
		Label server7Other = new Label( "NO" );
		server7Other.setFontSize( sc.getMediumFontSize( ) );

		Label server8Name = new Label( "Test1" );
		server8Name.setFontSize( sc.getMediumFontSize( ) );
		Label server8Size = new Label( "987" );
		server8Size.setFontSize( sc.getMediumFontSize( ) );
		Label server8Ping = new Label( "1" );
		server8Ping.setFontSize( sc.getMediumFontSize( ) );
		Label server8Other = new Label( "YES" );
		server8Other.setFontSize( sc.getMediumFontSize( ) );

		server1.addChild( server1Name );
		server1.addChild( server1Size );
		server1.addChild( server1Ping );
		server1.addChild( server1Other );
		server2.addChild( server2Name );
		server2.addChild( server2Size );
		server2.addChild( server2Ping );
		server2.addChild( server2Other );
		server3.addChild( server3Name );
		server3.addChild( server3Size );
		server3.addChild( server3Ping );
		server3.addChild( server3Other );
		server4.addChild( server4Name );
		server4.addChild( server4Size );
		server4.addChild( server4Ping );
		server4.addChild( server4Other );
		server5.addChild( server5Name );
		server5.addChild( server5Size );
		server5.addChild( server5Ping );
		server5.addChild( server5Other );
		server6.addChild( server6Name );
		server6.addChild( server6Size );
		server6.addChild( server6Ping );
		server6.addChild( server6Other );
		server7.addChild( server7Name );
		server7.addChild( server7Size );
		server7.addChild( server7Ping );
		server7.addChild( server7Other );
		server8.addChild( server8Name );
		server8.addChild( server8Size );
		server8.addChild( server8Ping );
		server8.addChild( server8Other );

		Container pageButtons = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		pageButtons.setPreferredSize( new Vector3f( cam.getWidth( ) * .15f, cam.getHeight( ) * .05f, 0 ) );
		pageButtons.setLocalTranslation( cam.getWidth( ) * .8f, cam.getHeight( ) * .1f, 0 );
		pageButtons.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );

		firstPage.setFontSize( sc.getMediumFontSize( ) );
		prevPage.setFontSize( sc.getMediumFontSize( ) );
		Label currentPage = new Label( "12 of 15" );
		currentPage.setFontSize( sc.getMediumFontSize( ) );
		nextPage.setFontSize( sc.getMediumFontSize( ) );

		lastPage.setFontSize( sc.getMediumFontSize( ) );
		pageButtons.addChild( firstPage );
		pageButtons.addChild( prevPage );
		pageButtons.addChild( currentPage );
		pageButtons.addChild( nextPage );
		pageButtons.addChild( lastPage );

		activateServerLists( );

		getScreenNode( ).attachChild( tabs );
		getScreenNode( ).attachChild( label );
		getScreenNode( ).attachChild( pageButtons );
	}

	private void activateServerLists( )
	{
		serverContainer.addChild( server1 );
		serverContainer.addChild( server2 );
		serverContainer.addChild( server3 );
		serverContainer.addChild( server4 );
		serverContainer.addChild( server5 );
		serverContainer.addChild( server6 );
		serverContainer.addChild( server7 );
		serverContainer.addChild( server8 );
		getScreenNode( ).attachChild( serverContainer );
	}

	private void updateServers( int page, String type )
	{
		if ( type.equals( "Servers" ) )
		{
			// TODO show all servers
		}
		if ( type.equals( "Favorites" ) )
		{
			// TODO show favorites
		}
		if ( type.equals( "History" ) )
		{
			// TODO show history
		}
	}

	private void sortServers( Button button )
	{
		// TODO sort server list according to what button is pressed.
	}

	private void pageServers( int page )
	{
		// TODO change server list according to page and whether last, prev,
		// next or first page buttons are pressed.
	}

	@Override
	public void screenAttached( ScreenManager screenManager )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void screenDetached( ScreenManager screenManager )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void update( float tpf )
	{
		// TODO Auto-generated method stub
	}
}
