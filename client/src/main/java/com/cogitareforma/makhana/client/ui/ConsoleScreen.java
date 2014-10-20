package com.cogitareforma.makhana.client.ui;

import com.cogitareforma.makhana.common.ui.Screen;
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

public class ConsoleScreen extends Screen
{
	// TODO for use when lemurproto is integrated.
	// VersionedList<String> consoleHistory = new VersionedList<String>();

	private Application app;

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

		Container panel = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		panel.setPreferredSize( new Vector3f( cam.getWidth( ) * .4f, cam.getHeight( ) * .05f, 0 ) );
		panel.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.85f, 0 );
		panel.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );

		// ListBox listbox = new ListBox(consoleHistory);
		//listbox.sestPreferredSize(new Vector3f(cam.getWidth()*.4f, cam.getHeight()*.05f, 0));
		// panel.addChild( listbox );

		TextField text = new TextField( "" );
		panel.addChild( text );
		// TODO add listener for enter button to send what every is in text to
		// the listbox
		getScreenNode( ).attachChild( panel );
		app.getInputManager( ).addMapping( "sendCommand", new KeyTrigger( KeyInput.KEY_RETURN ) );
		app.getInputManager( ).addListener( consoleActionListener, "sendCommand" );
	}

	private ActionListener consoleActionListener = ( String name, boolean keyPressed, float tpf ) ->
	{
		if ( name.equals( "sendCommand" ) && !keyPressed )
		{

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
		// TODO Auto-generated method stub
	}

}
