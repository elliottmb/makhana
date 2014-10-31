package com.cogitareforma.makhana.client;

import com.cogitareforma.makhana.client.ui.ConsoleScreen;
import com.cogitareforma.makhana.client.ui.HudScreen;
import com.cogitareforma.makhana.client.ui.LoadingScreen;
import com.cogitareforma.makhana.client.ui.NetworkScreen;
import com.cogitareforma.makhana.client.ui.OptionsScreen;
import com.cogitareforma.makhana.client.ui.ScreenManager;
import com.cogitareforma.makhana.client.ui.StartScreen;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.Styles;

public class LemurTest extends SimpleApplication
{

	private Styles styles;

	private ScreenManager screenManager;

	private ConsoleScreen console;

	public static void main( String[ ] args )
	{
		LemurTest app = new LemurTest( );
		app.start( );
		app.setDisplayFps( false );
		app.setDisplayStatView( false );
	}

	@Override
	public void simpleInitApp( )
	{
		screenManager = new ScreenManager( this, guiNode );
		stateManager.attach( screenManager );
		inputManager.deleteMapping( SimpleApplication.INPUT_MAPPING_CAMERA_POS );
		inputManager.deleteMapping( SimpleApplication.INPUT_MAPPING_EXIT );
		inputManager.deleteMapping( SimpleApplication.INPUT_MAPPING_HIDE_STATS );
		inputManager.deleteMapping( SimpleApplication.INPUT_MAPPING_MEMORY );

		// Initialize the globals access so that the defualt
		// components can find what they need.
		GuiGlobals.initialize( this );
		// scale = cam.getHeight( ) * 0.0016f;
		// Remove the flycam because we don't want it in this
		// demo
		stateManager.detach( stateManager.getState( FlyCamAppState.class ) );

		// Now, let's create some styles in code.
		// For this demo, we'll just give some of the elements
		// different backgrounds as we define a "glass" style.
		// We also define a custom element type called "spacer" which
		// picks up a specific style.
		styles = GuiGlobals.getInstance( ).getStyles( );
		styles.getSelector( Slider.THUMB_ID, "glass" ).set( "text", "[]", false );
		styles.getSelector( Panel.ELEMENT_ID, "glass" ).set( "background",
				new QuadBackgroundComponent( new ColorRGBA( 0, 0.25f, 0.25f, 0.5f ) ) );
		styles.getSelector( Checkbox.ELEMENT_ID, "glass" ).set( "background",
				new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ) ) );
		styles.getSelector( "spacer", "glass" ).set( "background", new QuadBackgroundComponent( new ColorRGBA( 1, 0.0f, 0.0f, 0.0f ) ) );
		styles.getSelector( "header", "glass" ).set( "background", new QuadBackgroundComponent( new ColorRGBA( 0, 0.75f, 0.75f, 0.5f ) ) );
		// BaseStyles.loadGlassStyle( ); // TODO: should be included but gets
		// error: Groovy scripting engine not available.
		console = new ConsoleScreen( );
		screenManager.addScreen( new StartScreen( ) );
		screenManager.addScreen( new OptionsScreen( ) );
		screenManager.addScreen( new NetworkScreen( ) );
		screenManager.addScreen( new LoadingScreen( ) );
		screenManager.addScreen( console );
		screenManager.addScreen( new HudScreen( ) );
		screenManager.setScreen( StartScreen.class );

		inputManager.addMapping( "showConsole", new KeyTrigger( KeyInput.KEY_F1 ) );
		inputManager.addListener( baseActionListener, "showConsole" );
	}

	private ActionListener baseActionListener = ( String name, boolean keyPressed, float tpf ) ->
	{
		if ( name.equals( "showConsole" ) && !keyPressed )
		{
			// TODO find out how to do this
			if ( screenManager.showScreen( console ) == true )
			{
				screenManager.showScreen( console );
			}
			else
			{
				screenManager.hideScreen( console );
			}
		}
	};

	@Override
	public void simpleUpdate( float tpf )
	{

	}
}