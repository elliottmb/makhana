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
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.core.VersionedReference;

public class OptionsScreen extends Screen
{

	private float scale;
	private Label resLabel;
	private Slider res;
	private Slider quality;
	private Button optionsExit;
	private Button optionsApply;

	private VersionedReference< Double > resRef;
	private VersionedReference< Double > qualityRef;
	private VersionedReference< Double > mainVolumeRef;
	private VersionedReference< Double > musicVolumeRef;
	private VersionedReference< Double > soundVolumeRef;
	private Container graphics;
	private Container audio;
	private Container input;
	private Container current;

	boolean original = false;
	boolean rolledPanel = true;
	private Button graphicsButton;
	private Button audioButton;
	private Button inputButton;

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@SuppressWarnings( "unchecked" )
	private void setUpButtons( ScreenManager screenManager )
	{
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
				System.out.println( "Exit Options Clicked: " + screenManager.setScreen( StartScreen.class ) );
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
		graphicsButton = new Button( "Graphics" );
		graphicsButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		graphicsButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		graphicsButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Graphics in options Clicked" );
				swapContainer( graphics );
			}
		} );
		audioButton = new Button( "Audio" );
		audioButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		audioButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		audioButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Audio in options Clicked" );
				swapContainer( audio );
			}
		} );
		inputButton = new Button( "Input" );
		inputButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		inputButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		inputButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				System.out.println( "Input in options Clicked" );
				swapContainer( input );
			}
		} );
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );

		Camera cam = screenManager.getApp( ).getCamera( );

		scale = cam.getHeight( ) * 0.0016f;

		Panel background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, cam.getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ), 0 ) );
		getScreenNode( ).attachChild( background );

		Container top = new Container( new BoxLayout( Axis.X, FillMode.Proportional ), "glass" );
		top.setLocalTranslation( 0, cam.getHeight( ), 0 );

		top.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		top.setPreferredSize( new Vector3f( cam.getWidth( ), cam.getHeight( ) * 0.1f, 0 ) );
		Label name = new Label( "Options" );
		name.scale( scale );
		name.setPreferredSize( new Vector3f( 0.8f, 0, 0 ) );

		Container buttons = new Container( new BoxLayout( Axis.X, FillMode.Even ), "glass" );
		buttons.setPreferredSize( new Vector3f( 0.2f, 0, 0 ) );

		top.addChild( name );
		top.addChild( buttons );

		Container middle = new Container( new BoxLayout( Axis.X, FillMode.Even ), "glass" );
		middle.setPreferredSize( new Vector3f( cam.getWidth( ) * .4f, cam.getHeight( ) * .05f, 0 ) );
		middle.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.85f, 0 );
		middle.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );

		setUpButtons( screenManager );

		middle.addChild( graphicsButton );
		middle.addChild( audioButton );
		middle.addChild( inputButton );

		optionsApply.setFontSize( 17 * scale );
		optionsExit.setFontSize( 17 * scale );
		graphicsButton.setFontSize( 17 * scale );
		audioButton.setFontSize( 17 * scale );
		inputButton.setFontSize( 17 * scale );

		graphics = new Container( new BoxLayout( Axis.Y, FillMode.Even ) );
		graphics.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		graphics.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		graphics.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		audio = new Container( new BoxLayout( Axis.Y, FillMode.Even ) );
		audio.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		audio.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		audio.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		input = new Container( new BoxLayout( Axis.Y, FillMode.Even ) );
		input.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		input.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		input.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		Label gLabel = new Label( "Graphics" );
		gLabel.scale( scale );

		Label aLabel = new Label( "Audio" );
		aLabel.scale( scale );

		Label iLabel = new Label( "Input" );
		iLabel.scale( scale );

		resLabel = new Label( "" );
		resLabel.scale( scale );

		res = new Slider( new DefaultRangedValueModel( 0, 15, 1 ), "glass" );
		res.setName( "Resolution" );
		resRef = res.getModel( ).createReference( );
		resLabel.setText( "Resolution: " + String.valueOf( resRef.get( ) ) );

		Checkbox fullscreen = new Checkbox( "FullScreen", "glass" );
		fullscreen.setFontSize( 17 * scale );

		quality = new Slider( new DefaultRangedValueModel( 0, 4, 4 ), "glass" );
		quality.setName( "Quality" );
		qualityRef = quality.getModel( ).createReference( );

		Checkbox vsync = new Checkbox( "VSync" );
		vsync.setFontSize( 17 * scale );

		Slider mainVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		Slider musicVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		Slider soundsVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		mainVolumeRef = mainVolume.getModel( ).createReference( );
		musicVolumeRef = musicVolume.getModel( ).createReference( );
		soundVolumeRef = soundsVolume.getModel( ).createReference( );

		Checkbox console = new Checkbox( "Enable Dev Console: ", "glass" );
		console.setFontSize( 17 * scale );

		swapContainer( graphics );

		// graphics.addChild( gLabel );
		graphics.addChild( resLabel );
		graphics.addChild( res );
		graphics.addChild( fullscreen );
		graphics.addChild( quality );
		graphics.addChild( vsync );
		// audio.addChild( aLabel );
		audio.addChild( mainVolume );
		audio.addChild( musicVolume );
		audio.addChild( soundsVolume );
		// input.addChild( iLabel );
		input.addChild( console );
		getScreenNode( ).attachChild( middle );
		getScreenNode( ).attachChild( top );

	}

	private void swapContainer( Container container )
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

		if ( resRef.update( ) )
		{
			res.getModel( ).setValue( Math.round( resRef.get( ) ) );
			resLabel.setText( "Resolution: " + String.valueOf( resRef.get( ) ) );
			System.out.println( "Resolution updated" );
		}
		if ( qualityRef.update( ) )
		{
			quality.getModel( ).setValue( Math.round( qualityRef.get( ) ) );
			System.out.println( "Quality updated" );
		}
		if ( mainVolumeRef.update( ) )
			System.out.println( "Main Volume updated" );
		if ( musicVolumeRef.update( ) )
			System.out.println( "Music Volume updated" );
		if ( soundVolumeRef.update( ) )
			System.out.println( "Sound Volume updated" );

	}

}
