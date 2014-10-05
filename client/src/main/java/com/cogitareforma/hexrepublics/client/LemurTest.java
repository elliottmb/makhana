package com.cogitareforma.hexrepublics.client;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.LayerComparator;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.DynamicInsetsComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
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
	private Container panel2;
	private boolean showTest = false;
	private Button singleButton;
	private Button multiButton;
	private Button optionsButton;
	private Button exitButton;
	private Button optionsExit;
	private Button optionsApply;
	private Container hud;
	private Container options;

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

		if ( showTest )
		{
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

			// Adding components returns the component so we can set other
			// things
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

			panel2 = new Container( "glass" );
			panel2.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
			panel2.addChild( new Button( "Start" ) );
			hudPanel.addChild( panel2 );

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
			Box box = new Box( 1, 1, 1 );
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
		else
		{
			/*
			 * SpringGridLayout grid = new SpringGridLayout( Axis.Y, Axis.X,
			 * FillMode.Proportional, FillMode.Proportional ); Container
			 * hudPanel = new Container( "glass" );
			 * hudPanel.setLocalTranslation( 5, cam.getHeight( ) - 50, 0 );
			 * hudPanel.setLayout( grid ); guiNode.attachChild( hudPanel );
			 * 
			 * Container panel = new Container( "glass" ); hudPanel.addChild(
			 * panel );
			 * 
			 * panel.setBackground( new QuadBackgroundComponent( new ColorRGBA(
			 * 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, true ) ); panel.addChild( new
			 * Label( "Hex Republics", new ElementId( "header" ), "glass" ) );
			 * panel.setLocalTranslation( cam.getWidth( ) / 2, cam.getHeight( )
			 * - 10, 0 );
			 * 
			 * panel = new Container( "glass" );
			 * 
			 * setUpButtons( ); panel.addChild( singleButton ); panel.addChild(
			 * multiButton ); panel.addChild( optionsButton ); panel.addChild(
			 * exitButton ); hudPanel.addChild( panel );
			 * 
			 * guiNode.attachChild( hudPanel );
			 * 
			 * Vector3f hudSize = new Vector3f( 200, 0, 0 ); hudSize.maxLocal(
			 * hudPanel.getPreferredSize( ) ); hudPanel.setPreferredSize(
			 * hudSize );
			 */
			boolean test2 = true;
			if ( test2 )
			{
				hud = new Container( new SpringGridLayout( Axis.Y, Axis.X, FillMode.Even, FillMode.Last ) );
				Container buttonPanel = new Container( "glass" );
				Container titlePanel = new Container( "glass" );
				hud.addChild( titlePanel );
				hud.addChild( new Panel( 10f, 50f, new ElementId( "spacer" ), "glass" ) );
				hud.addChild( buttonPanel );

				buttonPanel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );
				titlePanel.setBackground( new QuadBackgroundComponent( new ColorRGBA( 0, 0.5f, 0.5f, 0.5f ), 5, 5, 0.02f, false ) );

				titlePanel.addChild( new Label( "Hex Republics" ) );

				setUpButtons( );

				buttonPanel.addChild( singleButton );
				buttonPanel.addChild( multiButton );
				buttonPanel.addChild( optionsButton );
				buttonPanel.addChild( exitButton );

				float scale = cam.getHeight( ) / 720f;
				Vector3f pref = hud.getPreferredSize( );
				hud.setLocalTranslation( cam.getWidth( ) * 0.5f - pref.x * 0.5f * scale, cam.getHeight( ) * 0.5f + pref.y * 0.5f * scale,
						10 );

				hud.setLocalScale( scale );
				System.out.println( "HUD Size" + hud.getSize( ) );
				guiNode.attachChild( hud );
			}

		}
	}

	private void gotoOptions( )
	{
		if ( hud != null && guiNode.hasChild( hud ) )
		{
			guiNode.detachChild( hud );
			// options = new Container( new BoxLayout( Axis.Y, FillMode.None )
			// );
			options = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
			// Container top = options.addChild( new Container( new BoxLayout(
			// Axis.X, FillMode.Even ) ) );
			// Container top = options.addChild( new Container( new BoxLayout(
			// Axis.X, FillMode.Even ) ), BorderLayout.Position.North );
			Container top = options.addChild( new Container( new BoxLayout( Axis.X, FillMode.Even ) ) );
			top.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
			top.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ) / 16, 0 ) );

			Label name = top.addChild( new Label( "Options" ) );

			Container buttons = top.addChild( new Container( new BoxLayout( Axis.X, FillMode.Even ) ) );
			Button apply = buttons.addChild( new Button( "Apply" ) );
			// apply.setInsetsComponent( new DynamicInsetsComponent( 0, 0, 0, 0
			// ) );
			Button exit = buttons.addChild( new Button( "Exit" ) );
			// exit.setInsetsComponent( new DynamicInsetsComponent( 0, 1, 0, 0 )
			// );

			options.setLocalTranslation( 0, cam.getHeight( ), 0 );
			// options.setPreferredSize( new Vector3f( cam.getWidth( ) / 2,
			// cam.getHeight( ) / 2, 0 ) );
			options.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );

			Container op = options.addChild( new Container( ) );
			op.setPreferredSize( new Vector3f( cam.getWidth( ) / 16, cam.getHeight( ) / 16, 0 ) );

			op.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
			Label graphics = op.addChild( new Label( "Graphics" ) );

			guiNode.attachChild( options );
			System.out.println( top.getSize( ) + " " + op.getSize( ) );
			System.out.println( top.getPreferredSize( ) + " " + op.getPreferredSize( ) );
		}
	}

	@SuppressWarnings( "unchecked" )
	private void setUpButtons( )
	{
		singleButton = new Button( "Singleplayer" );
		singleButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		singleButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		singleButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Single Clicked" );
			}
		} );
		multiButton = new Button( "Multiplayer" );
		multiButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		multiButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		multiButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Multi Clicked" );
			}
		} );
		optionsButton = new Button( "Options" );
		optionsButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		optionsButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		optionsButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Options Clicked" );
				gotoOptions( );
			}
		} );
		exitButton = new Button( "Exit" );
		exitButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		exitButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		exitButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Exit Clicked" );
			}
		} );
		optionsExit = new Button( "Exit" );
		optionsExit.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		optionsExit.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		optionsExit.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Exit in options Clicked" );
			}
		} );
		optionsApply = new Button( "Apply" );
		optionsApply.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		optionsApply.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		optionsApply.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Apply in options Clicked" );
			}
		} );
	}

	@Override
	public void simpleUpdate( float tpf )
	{
		if ( showTest )
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
				boxColor.set( ( float ) ( redRef.get( ) / 100.0 ), ( float ) ( greenRef.get( ) / 100.0 ),
						( float ) ( blueRef.get( ) / 100.0 ), ( float ) ( alphaRef.get( ) / 100.0 ) );
			}
		}
	}
}