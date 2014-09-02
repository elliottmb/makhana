package com.cogitareforma.hexrepublics.client;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.LayerComparator;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.DynamicInsetsComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DragHandler;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.style.Styles;

public class LemurTest extends SimpleApplication
{

	// Define some model references we will use in
	// update.
	private VersionedReference< Double > redRef;
	private VersionedReference< Double > greenRef;
	private VersionedReference< Double > blueRef;
	private VersionedReference< Double > alphaRef;
	private VersionedReference< Boolean > showStatsRef;
	private VersionedReference< Boolean > showFpsRef;

	private ColorRGBA boxColor = ColorRGBA.Blue.clone( );

	public static void main( String[ ] args )
	{
		LemurTest app = new LemurTest( );
		app.start( );
	}

	@Override
	public void simpleInitApp( )
	{

		// Initialize the globals access so that the defualt
		// components can find what they need.
		GuiGlobals.initialize( this );

		// Remove the flycam because we don't want it in this
		// demo
		stateManager.detach( stateManager.getState( FlyCamAppState.class ) );

		// Now, let's create some styles in code.
		// For this demo, we'll just give some of the elements
		// different backgrounds as we define a "glass" style.
		// We also define a custom element type called "spacer" which
		// picks up a specific style.
		Styles styles = GuiGlobals.getInstance( ).getStyles( );
		styles.getSelector( Slider.THUMB_ID, "glass" ).set( "text", "[]", false );
		styles.getSelector( Panel.ELEMENT_ID, "glass" ).set( "background",
				new QuadBackgroundComponent( new ColorRGBA( 0, 0.25f, 0.25f, 0.5f ) ) );
		styles.getSelector( Checkbox.ELEMENT_ID, "glass" ).set( "background",
				new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ) ) );
		styles.getSelector( "spacer", "glass" ).set( "background", new QuadBackgroundComponent( new ColorRGBA( 1, 0.0f, 0.0f, 0.0f ) ) );
		styles.getSelector( "header", "glass" ).set( "background", new QuadBackgroundComponent( new ColorRGBA( 0, 0.75f, 0.75f, 0.5f ) ) );

		// Now construct some HUD panels in the "glass" style that
		// we just configured above.
		Container hudPanel = new Container( "glass" );
		hudPanel.setLocalTranslation( 5, cam.getHeight( ) - 50, 0 );
		guiNode.attachChild( hudPanel );

		// Create a top panel for some stats toggles.
		Container panel = new Container( "glass" );
		hudPanel.addChild( panel );

		panel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		panel.addChild( new Label( "Stats Settings", new ElementId( "header" ), "glass" ) );
		panel.addChild( new Panel( 2, 2, ColorRGBA.Cyan, "glass" ) ).setUserData( LayerComparator.LAYER, 2 );

		// Adding components returns the component so we can set other things
		// if we want.
		Checkbox temp = panel.addChild( new Checkbox( "Show Stats" ) );
		temp.setChecked( true );
		showStatsRef = temp.getModel( ).createReference( );

		temp = panel.addChild( new Checkbox( "Show FPS" ) );
		temp.setChecked( true );
		showFpsRef = temp.getModel( ).createReference( );

		// Custom "spacer" element type
		hudPanel.addChild( new Panel( 10f, 10f, new ElementId( "spacer" ), "glass" ) );

		// Create a second panel in the same overall HUD panel
		// that lets us tweak things about the cube.
		panel = new Container( "glass" );
		panel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
		// Custom "header" element type.
		panel.addChild( new Label( "Cube Settings", new ElementId( "header" ), "glass" ) );
		panel.addChild( new Panel( 2, 2, ColorRGBA.Cyan, "glass" ) ).setUserData( LayerComparator.LAYER, 2 );
		panel.addChild( new Label( "Red:" ) );
		redRef = panel.addChild( new Slider( "glass" ) ).getModel( ).createReference( );
		panel.addChild( new Label( "Green:" ) );
		greenRef = panel.addChild( new Slider( "glass" ) ).getModel( ).createReference( );
		panel.addChild( new Label( "Blue:" ) );
		blueRef = panel.addChild( new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" ) ).getModel( ).createReference( );
		panel.addChild( new Label( "Alpha:" ) );
		alphaRef = panel.addChild( new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" ) ).getModel( ).createReference( );

		hudPanel.addChild( panel );
		guiNode.attachChild( hudPanel );

		// Increase the default size of the hud to be a little wider
		// if it would otherwise be smaller. Height is unaffected.
		Vector3f hudSize = new Vector3f( 200, 0, 0 );
		hudSize.maxLocal( hudPanel.getPreferredSize( ) );
		hudPanel.setPreferredSize( hudSize );

		// Note: after next nightly, this will also work:
		// hudPanel.setPreferredSize( new
		// Vector3f(200,0,0).maxLocal(hudPanel.getPreferredSize()) );

		// Something in scene
		Box box = new Box( Vector3f.ZERO, 1, 1, 1 );
		Geometry geom = new Geometry( "Box", box );
		Material mat = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		mat.setColor( "Color", boxColor );
		mat.getAdditionalRenderState( ).setBlendMode( BlendMode.Alpha );
		geom.setMaterial( mat );
		rootNode.attachChild( geom );

		// A draggable bordered panel
		Container testPanel = new Container( );
		testPanel.setPreferredSize( new Vector3f( 200, 200, 0 ) );
		testPanel.setBackground( TbtQuadBackgroundComponent.create( "/com/simsilica/lemur/icons/border.png", 1, 2, 2, 3, 3, 0, false ) );
		Label test = testPanel.addChild( new Label( "Border Test" ) );

		// Center the text in the box.
		test.setInsetsComponent( new DynamicInsetsComponent( 0.5f, 0.5f, 0.5f, 0.5f ) );
		testPanel.setLocalTranslation( 400, 400, 0 );

		CursorEventControl.addListenersToSpatial( testPanel, new DragHandler( ) );
		guiNode.attachChild( testPanel );
	}

	@Override
	public void simpleUpdate( float tpf )
	{
		if ( showStatsRef.update( ) )
		{
			setDisplayStatView( showStatsRef.get( ) );
		}
		if ( showFpsRef.update( ) )
		{
			setDisplayFps( showFpsRef.get( ) );
		}

		boolean updateColor = false;
		if ( redRef.update( ) )
			updateColor = true;
		if ( greenRef.update( ) )
			updateColor = true;
		if ( blueRef.update( ) )
			updateColor = true;
		if ( alphaRef.update( ) )
			updateColor = true;
		if ( updateColor )
		{
			boxColor.set( ( float ) ( redRef.get( ) / 100.0 ), ( float ) ( greenRef.get( ) / 100.0 ), ( float ) ( blueRef.get( ) / 100.0 ),
					( float ) ( alphaRef.get( ) / 100.0 ) );
		}
	}
}