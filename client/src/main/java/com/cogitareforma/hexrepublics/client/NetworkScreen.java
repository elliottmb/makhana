package com.cogitareforma.hexrepublics.client;

import com.cogitareforma.makhana.common.util.Screen;
import com.cogitareforma.makhana.common.util.ScreenManager;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.TabbedPanel;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class NetworkScreen extends Screen
{

	private float scale;
	private Button servers;
	private Button favorites;
	private Button history;
	private Container current;
	private Container serverContainer;
	private Container favoritesContainer;
	private Container historyContainer;
	private Button back;
	private Container server1;
	private Container server2;
	private Container server3;
	private Container server4;
	private Container server5;
	private Container server6;

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
				activateContainer( serverContainer );
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
				activateContainer( favoritesContainer );
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
				activateContainer( historyContainer );
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
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		Camera cam = screenManager.getApp( ).getCamera( );
		scale = cam.getHeight( ) * 0.0016f;

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
		name.scale( scale );
		name.setPreferredSize( new Vector3f( 0.8f, 0, 0 ) );

		Container buttons = new Container( new BoxLayout( Axis.X, FillMode.Even ), "glass" );
		buttons.setPreferredSize( new Vector3f( 0.05f, 0, 0 ) );

		top.addChild( name );
		top.addChild( buttons );

		back.setFontSize( 17 * scale );
		buttons.addChild( back );

		getScreenNode( ).attachChild( top );

		Container tabs = new Container( new BoxLayout( Axis.X, FillMode.None ) );
		tabs.setPreferredSize( new Vector3f( cam.getWidth( ) * .4f, cam.getHeight( ) * .05f, 0 ) );
		tabs.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.85f, 0 );
		tabs.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );

		Insets3f buttonInsets = new Insets3f( 0, 0, 0, cam.getWidth( ) * 0.05f );
		servers.setFontSize( 17 * scale );
		servers.setInsets( buttonInsets );

		favorites.setFontSize( 17 * scale );
		favorites.setInsets( buttonInsets );

		history.setFontSize( 17 * scale );
		history.setInsets( buttonInsets );

		tabs.addChild( servers );
		tabs.addChild( favorites );
		tabs.addChild( history );

		serverContainer = new Container( new BoxLayout( Axis.Y, FillMode.Proportional ) );
		serverContainer.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		serverContainer.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .65f, 0 ) );
		serverContainer.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		favoritesContainer = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		favoritesContainer.setBackground( new QuadBackgroundComponent( ColorRGBA.LightGray ) );
		favoritesContainer.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .65f, 0 ) );
		favoritesContainer.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		historyContainer = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		historyContainer.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		historyContainer.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .65f, 0 ) );
		historyContainer.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		Vector3f serverCellSize = new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .15f, 0 );
		Label s1 = new Label( "Server 1" );
		s1.setFontSize( 17 * scale );
		Label s2 = new Label( "Server 2" );
		s2.setFontSize( 17 * scale );
		Label s3 = new Label( "Server 3" );
		s3.setFontSize( 17 * scale );
		Label s4 = new Label( "Server 4" );
		s4.setFontSize( 17 * scale );
		Label s5 = new Label( "Server 5" );
		s5.setFontSize( 17 * scale );
		Label s6 = new Label( "Server 6" );
		s6.setFontSize( 17 * scale );

		server1 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server1.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		server1.setPreferredSize( serverCellSize );
		server1.addChild( s1 );

		server2 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server2.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		server2.setPreferredSize( serverCellSize );
		server2.addChild( s2 );

		server3 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server3.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		server3.setPreferredSize( serverCellSize );
		server3.addChild( s3 );

		server4 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server4.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		server4.setPreferredSize( serverCellSize );
		server4.addChild( s4 );

		server5 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server5.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		server5.setPreferredSize( serverCellSize );
		server5.addChild( s5 );

		server6 = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		server6.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		server6.setPreferredSize( serverCellSize );
		server6.addChild( s6 );

		current = serverContainer;
		activateContainer( serverContainer );
		activateServerLists( serverContainer, 1 );

		getScreenNode( ).attachChild( tabs );
	}

	private void activateContainer( Container container )
	{
		if ( getScreenNode( ).hasChild( current ) )
		{
			if ( current == container )
				return;
			getScreenNode( ).detachChild( current );
		}
		current = container;
		getScreenNode( ).attachChild( current );
	}

	/**
	 * Used to start each new tab screen.
	 * 
	 * @param container
	 * @param page
	 */
	private void activateServerLists( Container container, int page )
	{
		if ( getScreenNode( ).hasChild( current ) && current.hasChild( server1 ) )
		{

		}
		container.addChild( server1 );
		container.addChild( server2 );
		container.addChild( server3 );
		container.addChild( server4 );
		container.addChild( server5 );
		container.addChild( server6 );
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
