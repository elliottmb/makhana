package com.cogitareforma.hexrepublics.client;

import com.cogitareforma.makhana.common.util.Screen;
import com.cogitareforma.makhana.common.util.ScreenManager;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.TabbedPanel;
import com.simsilica.lemur.component.QuadBackgroundComponent;

public class NetworkScreen extends Screen
{

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		Camera cam = screenManager.getApp( ).getCamera( );

		Node networkNode = new Node( );

		Panel background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, cam.getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ), 0 ) );
		networkNode.attachChild( background );

		Container exitContainer = new Container( );
		exitContainer.setPreferredSize( new Vector3f( cam.getWidth( ) * .1f, 50f, 0 ) );
		exitContainer.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		exitContainer.setLocalTranslation( cam.getWidth( ) * .9f, cam.getHeight( ), 0 );

		Button back = new Button( "Back" );

		exitContainer.addChild( back );

		Container networkList = new Container( );
		networkList.setPreferredSize( new Vector3f( cam.getWidth( ) * .8f, cam.getHeight( ) * .8f, 0 ) );
		networkList.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		networkList.setLocalTranslation( cam.getWidth( ) * .1f, cam.getHeight( ) * .9f, 0 );

		TabbedPanel serverTabs = new TabbedPanel( );
		serverTabs.setPreferredSize( new Vector3f( cam.getWidth( ) * .8f, cam.getHeight( ) * .8f, 0 ) );
		serverTabs.setLocalTranslation( cam.getWidth( ) * .1f, cam.getHeight( ) * .9f, 0 );

		Panel servers = new Panel( );
		servers.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		servers.setPreferredSize( new Vector3f( cam.getWidth( ) * .8f, cam.getHeight( ) * .8f, 0 ) );
		servers.setLocalTranslation( cam.getWidth( ) * .1f, cam.getHeight( ) * .9f, 0 );

		Panel favorites = new Panel( );
		favorites.setBackground( new QuadBackgroundComponent( ColorRGBA.LightGray ) );
		favorites.setPreferredSize( new Vector3f( cam.getWidth( ) * .8f, cam.getHeight( ) * .8f, 0 ) );
		favorites.setLocalTranslation( cam.getWidth( ) * .1f, cam.getHeight( ) * .9f, 0 );

		Panel history = new Panel( );
		history.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		history.setPreferredSize( new Vector3f( cam.getWidth( ) * .8f, cam.getHeight( ) * .8f, 0 ) );
		history.setLocalTranslation( cam.getWidth( ) * .1f, cam.getHeight( ) * .9f, 0 );

		serverTabs.addTab( "Servers", servers );
		serverTabs.addTab( "Favorites", favorites );
		serverTabs.addTab( "History", history );

		networkList.addChild( serverTabs );

		networkNode.attachChild( networkList );

		getScreenNode( ).attachChild( networkNode );
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
