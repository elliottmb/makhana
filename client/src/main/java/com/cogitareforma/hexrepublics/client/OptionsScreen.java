package com.cogitareforma.hexrepublics.client;

import com.cogitareforma.makhana.common.util.Screen;
import com.cogitareforma.makhana.common.util.ScreenManager;
import com.cogitareforma.makhana.common.util.YamlConfig;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Button.ButtonAction;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.TextField;
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
	private TextField northButton;
	private TextField southButton;
	private TextField eastButton;
	private TextField westButton;
	private TextField chatButton;
	private TextField scoreboardButton;

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
				activeContainer( graphics );
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
				activeContainer( audio );
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
				activeContainer( input );
			}
		} );
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		YamlConfig yamlConfig = YamlConfig.DEFAULT;

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

		graphics = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		graphics.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		graphics.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		graphics.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		audio = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		audio.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		audio.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		audio.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		input = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		input.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		input.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		input.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		resLabel = new Label( "" );
		resLabel.scale( scale );

		res = new Slider( new DefaultRangedValueModel( 0, 15, 1 ), "glass" );
		res.setName( "Resolution" );
		res.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );
		// TODO make this good for both bigger and smaller sizes for everything.
		resRef = res.getModel( ).createReference( );
		resLabel.setText( "Resolution: " + String.valueOf( resRef.get( ) ) );

		Checkbox fullscreen = new Checkbox( "FullScreen", "glass" );
		fullscreen.setFontSize( 17 * scale );
		fullscreen.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );

		quality = new Slider( new DefaultRangedValueModel( 0, 4, 4 ), "glass" );
		quality.setName( "Quality" );
		quality.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );
		qualityRef = quality.getModel( ).createReference( );

		Checkbox vsync = new Checkbox( "VSync" );
		vsync.setFontSize( 17 * scale );
		vsync.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );

		Label mainLabel = new Label( "Main Volume" );
		mainLabel.scale( scale );
		Label musicLabel = new Label( "Music Volume" );
		musicLabel.scale( scale );
		Label soundLabel = new Label( "Sound Volume" );
		soundLabel.scale( scale );

		Slider mainVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		mainVolume.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );
		Slider musicVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		musicVolume.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );
		Slider soundsVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		soundsVolume.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );
		mainVolumeRef = mainVolume.getModel( ).createReference( );
		musicVolumeRef = musicVolume.getModel( ).createReference( );
		soundVolumeRef = soundsVolume.getModel( ).createReference( );

		Checkbox console = new Checkbox( "Enable Dev Console: ", "glass" );
		console.setFontSize( 17 * scale );
		console.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );

		Vector3f inputSectionSize = new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .1f, 0 );
		Vector3f inputTextFieldSize = new Vector3f( cam.getWidth( ) * 0.5f, cam.getHeight( ) * 0.1f, 0 );
		Insets3f inputInsets = new Insets3f( cam.getHeight( ) * 0.02f, 0, 0, 0 );

		// Container northContainer = new Container( new BoxLayout( Axis.X,
		// FillMode.Even ) );
		// northContainer.setPreferredSize( inputSectionSize );
		// northContainer.setLocalTranslation( cam.getWidth( ) * .05f,
		// cam.getHeight( ) * .7f, 0 );
		Label northLabel = new Label( "Move North " );
		northLabel.scale( scale );
		// northContainer.addChild( northLabel );
		northButton = new TextField( "North Button", "glass" );
		northButton.setSingleLine( true );
		northButton.setFontSize( 17 * scale );
		northButton.setInsets( inputInsets );
		northButton.setLocalTranslation( cam.getWidth( ) * 0.1f, cam.getHeight( ) * 0.7f, 0 );
		input.addChild( northButton );
		// northContainer.addChild( northButton );
		// Container southContainer = new Container( new BoxLayout( Axis.X,
		// FillMode.Even ) );
		// southContainer.setPreferredSize( inputSectionSize );
		// southContainer.setLocalTranslation( cam.getWidth( ) * .05f,
		// cam.getHeight( ) * .6f, 0 );
		Label southLabel = new Label( "Move South " );
		southLabel.scale( scale );
		// southContainer.addChild( southLabel );
		southButton = new TextField( "South Button", "glass" );
		southButton.setSingleLine( true );
		southButton.setFontSize( 17 * scale );
		southButton.setInsets( inputInsets );
		southButton.setLocalTranslation( cam.getWidth( ) * 0.1f, cam.getHeight( ) * 0.6f, 0 );
		input.addChild( southButton );
		// southContainer.addChild( southButton );
		// Container eastContainer = new Container( new BoxLayout( Axis.X,
		// FillMode.Even ) );
		// eastContainer.setPreferredSize( inputSectionSize );
		// eastContainer.setLocalTranslation( cam.getWidth( ) * .05f,
		// cam.getHeight( ) * .5f, 0 );
		Label eastLabel = new Label( "Move East " );
		eastLabel.scale( scale );
		// eastContainer.addChild( eastLabel );
		eastButton = new TextField( "East Button", "glass" );
		eastButton.setSingleLine( true );
		eastButton.setFontSize( 17 * scale );
		eastButton.setInsets( inputInsets );
		eastButton.setLocalTranslation( cam.getWidth( ) * 0.1f, cam.getHeight( ) * 0.5f, 0 );
		input.addChild( eastButton );
		// eastContainer.addChild( eastButton );
		// Container westContainer = new Container( new BoxLayout( Axis.X,
		// FillMode.Even ) );
		// westContainer.setPreferredSize( inputSectionSize );
		// westContainer.setLocalTranslation( cam.getWidth( ) * .05f,
		// cam.getHeight( ) * .4f, 0 );
		Label westLabel = new Label( "Move West " );
		westLabel.scale( scale );
		// westContainer.addChild( westLabel );
		westButton = new TextField( "West Button", "glass" );
		westButton.setSingleLine( true );
		westButton.setFontSize( 17 * scale );
		westButton.setInsets( inputInsets );
		westButton.setLocalTranslation( cam.getWidth( ) * 0.1f, cam.getHeight( ) * 0.4f, 0 );
		input.addChild( westButton );
		// westContainer.addChild( westButton );
		// Container chatContainer = new Container( new BoxLayout( Axis.X,
		// FillMode.Even ) );
		// chatContainer.setPreferredSize( inputSectionSize );
		// chatContainer.setLocalTranslation( cam.getWidth( ) * .05f,
		// cam.getHeight( ) * .3f, 0 );
		Label chatLabel = new Label( "Chat       " );
		chatLabel.scale( scale );
		// chatContainer.addChild( chatLabel );
		chatButton = new TextField( "Chat Button", "glass" );
		chatButton.setSingleLine( true );
		chatButton.setFontSize( 17 * scale );
		chatButton.setInsets( inputInsets );
		chatButton.setLocalTranslation( cam.getWidth( ) * 0.1f, cam.getHeight( ) * 0.3f, 0 );
		input.addChild( chatButton );
		// chatContainer.addChild( chatButton );
		// Container scoreContainer = new Container( new BoxLayout( Axis.X,
		// FillMode.Even ) );
		// scoreContainer.setPreferredSize( inputSectionSize );
		// scoreContainer.setLocalTranslation( cam.getWidth( ) * .05f,
		// cam.getHeight( ) * .2f, 0 );
		Label scoreLabel = new Label( "Scoreboard" );
		scoreLabel.scale( scale );
		// scoreContainer.addChild( scoreLabel );
		scoreboardButton = new TextField( "Score Button", "glass" );
		scoreboardButton.setSingleLine( true );
		scoreboardButton.setFontSize( 17 * scale );
		scoreboardButton.setInsets( inputInsets );
		scoreboardButton.setLocalTranslation( cam.getWidth( ) * 0.1f, cam.getHeight( ) * 0.2f, 0 );
		input.addChild( scoreboardButton );
		// scoreContainer.addChild( scoreboardButton );

		activeContainer( graphics );

		graphics.addChild( resLabel );
		graphics.addChild( res );
		graphics.addChild( fullscreen );
		graphics.addChild( quality );
		graphics.addChild( vsync );
		audio.addChild( mainLabel );
		audio.addChild( mainVolume );
		audio.addChild( musicLabel );
		audio.addChild( musicVolume );
		audio.addChild( soundLabel );
		audio.addChild( soundsVolume );
		input.addChild( console );
		// input.addChild( northContainer );
		// input.addChild( southContainer );
		// input.addChild( eastContainer );
		// input.addChild( westContainer );
		// input.addChild( chatContainer );
		// input.addChild( scoreContainer );
		getScreenNode( ).attachChild( middle );
		getScreenNode( ).attachChild( top );

	}

	private void activeContainer( Container container )
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
