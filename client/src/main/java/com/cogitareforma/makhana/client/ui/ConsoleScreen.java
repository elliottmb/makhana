package com.cogitareforma.makhana.client.ui;

import java.util.ArrayList;
import java.util.Arrays;

import com.cogitareforma.makhana.client.util.ListBox;
import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.core.VersionedList;

public class ConsoleScreen extends Screen
{
	private Application app;

	VersionedList< String > consoleHistory;
	// TODO for use when lemurproto is integrated.
	ArrayList< String > list;
	private ListBox< String > listbox;
	private Container panel;
	private TextField text;

	private long version;

	private ActionListener consoleActionListener = ( String name, boolean keyPressed, float tpf ) ->
	{
		if ( name.equals( "sendCommand" ) && !keyPressed )
		{
			consoleHistory.add( text.getText( ) );
			listbox.moveToLast( );

			String[ ] tokenized = text.getText( ).trim( ).split( " " );
			if ( tokenized.length >= 3 )
			{
				if ( tokenized[ 0 ].equalsIgnoreCase( "resize" ) )
				{
					System.out.println( "Hai!" );
					app.getContext( ).getSettings( ).setWidth( Integer.parseInt( tokenized[ 1 ] ) );
					app.getContext( ).getSettings( ).setHeight( Integer.parseInt( tokenized[ 2 ] ) );
					app.restart( );
				}
			}
			text.setText( " " );
			System.out.println( listbox.getModel( ) );
			System.out.println( listbox.getVisibleItems( ) );

		}
	};

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

		// list = new ArrayList<String>();
		consoleHistory = new VersionedList< String >( Arrays.asList( "--------------------------", "Welcome to the Makhana Console",
				"--------------------------", "", "" ) );
		version = consoleHistory.getVersion( );

		panel = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		panel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		panel.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * 0.9f, getContext( ).getHeight( ) * 0.75f, 0 ) );
		panel.setLocalTranslation( getContext( ).getWidth( ) * 0.05f, getContext( ).getHeight( ) * 0.85f, 0 );
		panel.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );

		// DefaultCellRenderer< String > ren = new DefaultCellRenderer< String
		// >();
		listbox = new ListBox< String >( );
		listbox.setModel( consoleHistory );
		listbox.setVisibleItems( 15 );
		// listbox.setPreferredSize( new Vector3f( cam.getWidth( ) * .4f,
		// cam.getHeight( ) * .70f, 0 ) );
		panel.addChild( listbox );

		text = new TextField( " " );
		text.setSingleLine( true );
		text.setFontSize( getContext( ).getMediumFontSize( ) );
		panel.addChild( text );
		// TODO add listener for enter button to send what is in text to
		// the listbox
		getScreenNode( ).attachChild( panel );
		app.getInputManager( ).addMapping( "sendCommand", new KeyTrigger( KeyInput.KEY_RETURN ) );
		app.getInputManager( ).addListener( consoleActionListener, "sendCommand" );
	}

	@Override
	public void reshape( int w, int h )
	{
		super.reshape( w, h );
		// TODO Auto-generated method stub

	}

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
		listbox.updateGeometricState( );
		if ( consoleHistory.getVersion( ) != version )
		{
			// listbox.setModel( consoleHistory );
			// panel.detachChild( listbox );
			// listbox = new ListBox< String >( consoleHistory );
			// panel.addChild( listbox );
			// version = consoleHistory.getVersion( );
		}
	}
}
