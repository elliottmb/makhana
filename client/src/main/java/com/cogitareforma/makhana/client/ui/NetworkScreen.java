package com.cogitareforma.makhana.client.ui;

import java.util.ArrayList;

import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
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

	private Button back;
	private Button favorites;
	private Button firstPage;
	private Button history;
	private Container label;
	private Button lastPage;
	private Button nextPage;
	private Button prevPage;
	private Container server1;
	private Container server2;
	private Container server3;
	private Container server4;
	private Container server5;
	private Container server6;
	private Container server7;
	private Container server8;
	private Container serverContainer;
	private ArrayList< String > serverList;
	private Button serverNameLabel;
	private Button serverOtherLabel;
	private Button serverPingLabel;
	private Button serverPlayersLabel;

	private Button servers;

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

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );

		float mediumFontSize = getContext( ).getMediumFontSize( );

		setUpButtons( screenManager );

		Panel background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ), 0 ) );
		getScreenNode( ).attachChild( background );

		Container top = new Container( new BoxLayout( Axis.X, FillMode.Proportional ), "glass" );
		top.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		top.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		top.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ) * 0.1f, 0 ) );
		Label name = new Label( "Network" );
		name.scale( getContext( ).getScalar( ) );
		name.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.8f, 0, 0 ) );

		Container buttons = new Container( new BoxLayout( Axis.X, FillMode.Even ), "glass" );
		buttons.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.05f, 0, 0 ) );

		top.addChild( name );
		top.addChild( buttons );

		back.setFontSize( mediumFontSize );
		buttons.addChild( back );

		getScreenNode( ).attachChild( top );

		Container tabs = new Container( new BoxLayout( Axis.X, FillMode.None ) );
		tabs.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.4f, getContext( ).getHeight( ) * 0.05f, 0 ) );
		tabs.setLocalTranslation( getContext( ).getWidth( ) * 0.05f, getContext( ).getHeight( ) * 0.85f, 0 );
		tabs.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );

		Insets3f buttonInsets = new Insets3f( 0, 0, 0, 0.05f );
		servers.setFontSize( mediumFontSize );
		servers.setInsets( buttonInsets );

		favorites.setFontSize( mediumFontSize );
		favorites.setInsets( buttonInsets );

		history.setFontSize( mediumFontSize );
		history.setInsets( buttonInsets );

		tabs.addChild( servers );
		tabs.addChild( favorites );
		tabs.addChild( history );

		label = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		label.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.9f, getContext( ).getHeight( ) * 0.05f, 0 ) );
		label.setLocalTranslation( getContext( ).getWidth( ) * 0.05f, getContext( ).getHeight( ) * 0.8f, 0 );
		label.setBackground( new QuadBackgroundComponent( ColorRGBA.LightGray ) );

		serverNameLabel = new Button( "Name" );
		serverNameLabel.setFontSize( mediumFontSize );
		serverPlayersLabel = new Button( "Players" );
		serverPlayersLabel.setFontSize( mediumFontSize );
		serverPingLabel = new Button( "Ping" );
		serverPingLabel.setFontSize( mediumFontSize );
		serverOtherLabel = new Button( "Other" );
		serverOtherLabel.setFontSize( mediumFontSize );
		label.addChild( serverNameLabel );
		label.addChild( serverPlayersLabel );
		label.addChild( serverPingLabel );
		label.addChild( serverOtherLabel );

		serverContainer = new Container( new BoxLayout( Axis.Y, FillMode.Proportional ) );
		serverContainer.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		serverContainer.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.9f, getContext( ).getHeight( ) * 0.65f, 0 ) );
		serverContainer.setLocalTranslation( getContext( ).getWidth( ) * 0.05f, getContext( ).getHeight( ) * 0.75f, 0 );

		Vector3f serverCellSize = new Vector3f( 0.9f, 0.1125f, 0 );

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
		server1Name.setFontSize( mediumFontSize );
		Label server1Size = new Label( "456" );
		server1Size.setFontSize( mediumFontSize );
		Label server1Ping = new Label( "10" );
		server1Ping.setFontSize( mediumFontSize );
		Label server1Other = new Label( "NO" );
		server1Other.setFontSize( mediumFontSize );

		Label server2Name = new Label( "Test1" );
		server2Name.setFontSize( mediumFontSize );
		Label server2Size = new Label( "654" );
		server2Size.setFontSize( mediumFontSize );
		Label server2Ping = new Label( "50" );
		server2Ping.setFontSize( mediumFontSize );
		Label server2Other = new Label( "NO" );
		server2Other.setFontSize( mediumFontSize );

		Label server3Name = new Label( "Test1" );
		server3Name.setFontSize( mediumFontSize );
		Label server3Size = new Label( "548" );
		server3Size.setFontSize( mediumFontSize );
		Label server3Ping = new Label( "100" );
		server3Ping.setFontSize( mediumFontSize );
		Label server3Other = new Label( "NO" );
		server3Other.setFontSize( mediumFontSize );

		Label server4Name = new Label( "Test1" );
		server4Name.setFontSize( mediumFontSize );
		Label server4Size = new Label( "5498" );
		server4Size.setFontSize( mediumFontSize );
		Label server4Ping = new Label( "45" );
		server4Ping.setFontSize( mediumFontSize );
		Label server4Other = new Label( "No" );
		server4Other.setFontSize( mediumFontSize );

		Label server5Name = new Label( "Test1v" );
		server5Name.setFontSize( mediumFontSize );
		Label server5Size = new Label( "6549" );
		server5Size.setFontSize( mediumFontSize );
		Label server5Ping = new Label( "500" );
		server5Ping.setFontSize( mediumFontSize );
		Label server5Other = new Label( "NO" );
		server5Other.setFontSize( mediumFontSize );

		Label server6Name = new Label( "Test1" );
		server6Name.setFontSize( mediumFontSize );
		Label server6Size = new Label( "6548" );
		server6Size.setFontSize( mediumFontSize );
		Label server6Ping = new Label( "50" );
		server6Ping.setFontSize( mediumFontSize );
		Label server6Other = new Label( "NO" );
		server6Other.setFontSize( mediumFontSize );

		Label server7Name = new Label( "Test1" );
		server7Name.setFontSize( mediumFontSize );
		Label server7Size = new Label( "1589" );
		server7Size.setFontSize( mediumFontSize );
		Label server7Ping = new Label( "506" );
		server7Ping.setFontSize( mediumFontSize );
		Label server7Other = new Label( "NO" );
		server7Other.setFontSize( mediumFontSize );

		Label server8Name = new Label( "Test1" );
		server8Name.setFontSize( mediumFontSize );
		Label server8Size = new Label( "987" );
		server8Size.setFontSize( mediumFontSize );
		Label server8Ping = new Label( "1" );
		server8Ping.setFontSize( mediumFontSize );
		Label server8Other = new Label( "YES" );
		server8Other.setFontSize( mediumFontSize );

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
		pageButtons.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.15f, getContext( ).getHeight( ) * 0.05f, 0 ) );
		pageButtons.setLocalTranslation( getContext( ).getWidth( ) * 0.8f, getContext( ).getHeight( ) * 0.1f, 0 );
		pageButtons.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );

		firstPage.setFontSize( mediumFontSize );
		prevPage.setFontSize( mediumFontSize );
		Label currentPage = new Label( "12 of 15" );
		currentPage.setFontSize( mediumFontSize );
		nextPage.setFontSize( mediumFontSize );

		lastPage.setFontSize( mediumFontSize );
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

	private void pageServers( int page )
	{
		// TODO change server list according to page and whether last, prev,
		// next or first page buttons are pressed.
	}

	@Override
	public void reshape( int w, int h )
	{
		// TODO Auto-generated method stub
		super.reshape( w, h );
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

	private void sortServers( Button button )
	{
		// TODO sort server list according to what button is pressed.
	}

	@Override
	public void update( float tpf )
	{
		// TODO Auto-generated method stub
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
}
