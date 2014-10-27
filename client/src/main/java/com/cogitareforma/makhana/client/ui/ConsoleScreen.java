package com.cogitareforma.makhana.client.ui;

import java.util.ArrayList;

import com.cogitareforma.makhana.client.util.ListBox;
import com.cogitareforma.makhana.common.ui.Screen;
import com.cogitareforma.makhana.common.ui.ScreenContext;
import com.cogitareforma.makhana.common.ui.ScreenManager;
import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.core.VersionedList;

public class ConsoleScreen extends Screen
{
	// TODO for use when lemurproto is integrated.
	ArrayList< String > list;
	VersionedList< String > consoleHistory;

	private Application app;
	private ListBox< String > listbox;
	private long version;
	private Container panel;

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		this.app = app;
		super.initialize( screenManager, app );
		Camera cam = screenManager.getApp( ).getCamera( );
		ScreenContext sc = screenManager.getScreenContext( );
		// list = new ArrayList<String>();
		consoleHistory = new VersionedList< String >( );
		version = consoleHistory.getVersion( );

		panel = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		panel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		panel.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		panel.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.85f, 0 );
		panel.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );

		// DefaultCellRenderer< String > ren = new DefaultCellRenderer< String
		// >();
		listbox = new ListBox< String >( );
		// listbox.setPreferredSize( new Vector3f( cam.getWidth( ) * .4f,
		// cam.getHeight( ) * .70f, 0 ) );
		panel.addChild( listbox );

		TextField text = new TextField( "dfsdfsdf" );
		text.setSingleLine( true );
		text.setFontSize( sc.getMediumFontSize( ) );
		panel.addChild( text );
		// TODO add listener for enter button to send what is in text to
		// the listbox
		getScreenNode( ).attachChild( panel );
		app.getInputManager( ).addMapping( "sendCommand", new KeyTrigger( KeyInput.KEY_RETURN ) );
		app.getInputManager( ).addListener( consoleActionListener, "sendCommand" );
	}

	private ActionListener consoleActionListener = ( String name, boolean keyPressed, float tpf ) ->
	{
		if ( name.equals( "sendCommand" ) && !keyPressed )
		{
			consoleHistory.add( "DICKS" );
			System.out.println( "SLDFHSODFBOSDFH" );
			System.out.println( listbox.getModel( ) );
		}
	};

	@Override
	public void screenAttached( ScreenManager screenManager )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void screenDetached( ScreenManager screenManager )
	{
		// Not sure if this is correct.
		app.getInputManager( ).deleteTrigger( "sendCommand", new KeyTrigger( KeyInput.KEY_RETURN ) );
		app.getInputManager( ).removeListener( consoleActionListener );
	}

	@Override
	public void update( float tpf )
	{
		listbox.updateLogicalState( tpf );
		if ( consoleHistory.getVersion( ) != version )
		{
			// listbox.setModel( consoleHistory );
			panel.detachChild( listbox );
			listbox = new ListBox< String >( consoleHistory );
			panel.addChild( listbox );
			version = consoleHistory.getVersion( );
		}
	}
}
