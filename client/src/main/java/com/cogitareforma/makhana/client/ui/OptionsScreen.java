package com.cogitareforma.makhana.client.ui;

import java.util.Set;

import com.cogitareforma.makhana.common.ui.Screen;
import com.cogitareforma.makhana.common.ui.ScreenContext;
import com.cogitareforma.makhana.common.ui.ScreenManager;
import com.cogitareforma.makhana.common.util.YamlConfig;
import com.google.common.collect.Sets;
import com.jme3.app.Application;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
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
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;

import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;

public class OptionsScreen extends Screen
{
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

	private Button graphicsButton;
	private Button audioButton;
	private Button inputButton;
	private Button northButton;
	private Button southButton;
	private Button eastButton;
	private Button westButton;
	private Button chatButton;
	private Button scoreboardButton;
	private Application app;
	private char buttonKey;
	private RawInputListener list;
	private Button currentButton;
	private boolean buttonPressed;
	private Set< Integer > avaiableKeys;
	private char prevKey;

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
				printInput( );
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
		northButton = new Button( "North Button: ", "glass" );
		northButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		northButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		northButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				if ( !buttonPressed )
				{
					System.out.println( "North Clicked" );
					prevKey = northButton.getText( ).charAt( 0 );
					northButton.setText( "<Press Key>" );
					activeListener( );
					setButtonName( northButton );
				}
			}
		} );
		southButton = new Button( "South Button: ", "glass" );
		southButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		southButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		southButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				if ( !buttonPressed )
				{
					System.out.println( "North Clicked" );
					prevKey = southButton.getText( ).charAt( 0 );
					southButton.setText( "<Press Key>" );
					activeListener( );
					setButtonName( southButton );
				}
			}
		} );
		eastButton = new Button( "East Button: ", "glass" );
		eastButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		eastButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		eastButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				if ( !buttonPressed )
				{
					System.out.println( "North Clicked" );
					prevKey = eastButton.getText( ).charAt( 0 );
					eastButton.setText( "<Press Key>" );
					activeListener( );
					setButtonName( eastButton );
				}
			}
		} );
		westButton = new Button( "West Button: ", "glass" );
		westButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		westButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		westButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				if ( !buttonPressed )
				{
					System.out.println( "North Clicked" );
					prevKey = westButton.getText( ).charAt( 0 );
					westButton.setText( "<Press Key>" );
					activeListener( );
					setButtonName( westButton );
				}
			}
		} );
		chatButton = new Button( "Chat Button: ", "glass" );
		chatButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		chatButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		chatButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				if ( !buttonPressed )
				{
					System.out.println( "North Clicked" );
					prevKey = chatButton.getText( ).charAt( 0 );
					chatButton.setText( "<Press Key>" );
					activeListener( );
					setButtonName( chatButton );
				}
			}
		} );
		scoreboardButton = new Button( "Score Button: ", "glass" );
		scoreboardButton.addCommands( ButtonAction.Down, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( 1, -1, 0 );
			}
		} );
		scoreboardButton.addCommands( ButtonAction.Up, new Command< Button >( )
		{
			public void execute( Button b )
			{
				b.move( -1, 1, 0 );
			}
		} );
		scoreboardButton.addCommands( ButtonAction.Click, new Command< Button >( )
		{
			public void execute( Button b )
			{
				if ( !buttonPressed )
				{
					System.out.println( "North Clicked" );
					prevKey = scoreboardButton.getText( ).charAt( 0 );
					scoreboardButton.setText( "<Press Key>" );
					activeListener( );
					setButtonName( scoreboardButton );
				}
			}
		} );
	}

	private void printInput( )
	{
		System.out.println( northButton.getText( ) );
		System.out.println( southButton.getText( ) );
		System.out.println( eastButton.getText( ) );
		System.out.println( westButton.getText( ) );
		System.out.println( chatButton.getText( ) );
		System.out.println( scoreboardButton.getText( ) );
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		this.app = app;
		buttonKey = ' ';
		YamlConfig yamlConfig = YamlConfig.DEFAULT;
		avaiableKeys = Sets.newHashSet(KeyInput.KEY_A, KeyInput.KEY_B, KeyInput.KEY_C, KeyInput.KEY_D, KeyInput.KEY_E, KeyInput.KEY_F, KeyInput.KEY_G, KeyInput.KEY_H,
				KeyInput.KEY_I, KeyInput.KEY_J, KeyInput.KEY_K, KeyInput.KEY_L, KeyInput.KEY_M, KeyInput.KEY_N, KeyInput.KEY_O, KeyInput.KEY_P,
				KeyInput.KEY_Q, KeyInput.KEY_R, KeyInput.KEY_S, KeyInput.KEY_T, KeyInput.KEY_U, KeyInput.KEY_V, KeyInput.KEY_W, KeyInput.KEY_X,
				KeyInput.KEY_Y, KeyInput.KEY_Z, KeyInput.KEY_1, KeyInput.KEY_2, KeyInput.KEY_3, KeyInput.KEY_4, KeyInput.KEY_5, KeyInput.KEY_6,
				KeyInput.KEY_7, KeyInput.KEY_8, KeyInput.KEY_9, KeyInput.KEY_0, KeyInput.KEY_UP, KeyInput.KEY_DOWN, KeyInput.KEY_LEFT,
				KeyInput.KEY_RIGHT, KeyInput.KEY_GRAVE, KeyInput.KEY_TAB );

		Camera cam = screenManager.getApp( ).getCamera( );
		ScreenContext sc = screenManager.getScreenContext( );

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
		name.scale( sc.getHeightScalar( ) );
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

		optionsApply.setFontSize( sc.getMediumFontSize( ) );
		optionsExit.setFontSize( sc.getMediumFontSize( ) );
		buttons.addChild( optionsApply );
		buttons.addChild( optionsExit );
		graphicsButton.setFontSize( sc.getMediumFontSize( ) );
		audioButton.setFontSize( sc.getMediumFontSize( ) );
		inputButton.setFontSize( sc.getMediumFontSize( ) );

		graphics = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		graphics.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		graphics.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		graphics.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		audio = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		audio.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		audio.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		audio.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		// input = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		input = new Container( new SpringGridLayout( Axis.X, Axis.Y, FillMode.None, FillMode.None ) );
		input.setPreferredSize( new Vector3f( cam.getWidth( ) * .9f, cam.getHeight( ) * .75f, 0 ) );
		input.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		input.setLocalTranslation( cam.getWidth( ) * .05f, cam.getHeight( ) * 0.8f, 0 );

		resLabel = new Label( "" );
		resLabel.scale( sc.getHeightScalar( ) );

		res = new Slider( new DefaultRangedValueModel( 0, 15, 1 ), "glass" );
		res.setName( "Resolution" );
		res.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );
		resRef = res.getModel( ).createReference( );
		resLabel.setText( "Resolution: " + String.valueOf( resRef.get( ) ) );

		Checkbox fullscreen = new Checkbox( "FullScreen", "glass" );
		fullscreen.setFontSize( sc.getMediumFontSize( ) );
		fullscreen.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );

		quality = new Slider( new DefaultRangedValueModel( 0, 4, 4 ), "glass" );
		quality.setName( "Quality" );
		quality.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );
		qualityRef = quality.getModel( ).createReference( );

		Checkbox vsync = new Checkbox( "VSync" );
		vsync.setFontSize( sc.getMediumFontSize( ) );
		vsync.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );

		Label mainLabel = new Label( "Main Volume" );
		mainLabel.scale( sc.getHeightScalar( ) );
		Label musicLabel = new Label( "Music Volume" );
		musicLabel.scale( sc.getHeightScalar( ) );
		Label soundLabel = new Label( "Sound Volume" );
		soundLabel.scale( sc.getHeightScalar( ) );

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
		console.setFontSize( sc.getMediumFontSize( ) );
		console.setInsets( new Insets3f( cam.getHeight( ) * .02f, 0, cam.getHeight( ) * .02f, 0 ) );

		Insets3f inputInsets = new Insets3f( 0, 0, sc.getHeightScalar( ) * 16f, 0 );

		Label northLabel = new Label( "Move North " );
		northLabel.scale( sc.getHeightScalar( ) );
		input.addChild( northLabel, 1, 1 );

		northButton.setFontSize( sc.getMediumFontSize( ) );
		northButton.setInsets( inputInsets );
		input.addChild( northButton, 2, 1 );

		Label southLabel = new Label( "Move South " );
		southLabel.scale( sc.getHeightScalar( ) );
		input.addChild( southLabel, 1, 2 );

		southButton.setFontSize( sc.getMediumFontSize( ) );
		southButton.setInsets( inputInsets );
		input.addChild( southButton, 2, 2 );

		Label eastLabel = new Label( "Move East " );
		eastLabel.scale( sc.getHeightScalar( ) );
		input.addChild( eastLabel, 1, 3 );

		eastButton.setFontSize( sc.getMediumFontSize( ) );
		eastButton.setInsets( inputInsets );
		input.addChild( eastButton, 2, 3 );

		Label westLabel = new Label( "Move West " );
		westLabel.scale( sc.getHeightScalar( ) );
		input.addChild( westLabel, 1, 4 );

		westButton.setFontSize( sc.getMediumFontSize( ) );
		westButton.setInsets( inputInsets );
		input.addChild( westButton, 2, 4 );

		Label chatLabel = new Label( "Chat       " );
		chatLabel.scale( sc.getHeightScalar( ) );
		input.addChild( chatLabel, 1, 5 );

		chatButton.setFontSize( sc.getMediumFontSize( ) );
		chatButton.setInsets( inputInsets );
		input.addChild( chatButton, 2, 5 );

		Label scoreLabel = new Label( "Scoreboard" );
		scoreLabel.scale( sc.getHeightScalar( ) );
		input.addChild( scoreLabel, 1, 6 );

		scoreboardButton.setFontSize( sc.getMediumFontSize( ) );
		scoreboardButton.setInsets( inputInsets );
		input.addChild( scoreboardButton, 2, 6 );

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
		input.addChild( console, 1, 7 );
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

	private char returnName( int number, char letter )
	{
		//TODO need to add back the old key to avaiableKeys.
		System.out.println((int)prevKey);
		currentButton.getText( );
		if(avaiableKeys.contains( number )){
			avaiableKeys.remove( number );
			buttonKey = letter;
			return letter;
		}
		buttonKey = ' ';
		return buttonKey;
	}

	private void activeListener( )
	{
		System.out.println( "Active Listener" );
		buttonKey = ' ';
		list = new RawInputListener( )
		{
			@Override
			public void beginInput( )
			{
			}

			@Override
			public void endInput( )
			{
			}

			@Override
			public void onJoyAxisEvent( JoyAxisEvent arg0 )
			{
			}

			@Override
			public void onJoyButtonEvent( JoyButtonEvent arg0 )
			{
			}

			@Override
			public void onMouseButtonEvent( MouseButtonEvent arg0 )
			{
			}

			@Override
			public void onMouseMotionEvent( MouseMotionEvent arg0 )
			{
			}

			@Override
			public void onTouchEvent( TouchEvent arg0 )
			{
			}

			@Override
			public void onKeyEvent( KeyInputEvent arg0 )
			{
				System.out.println( "Key: " + arg0 );
				returnName( arg0.getKeyCode( ), arg0.getKeyChar( ) );
			}
		};
		app.getInputManager( ).addRawInputListener( list );
		buttonPressed = true;
		// button.setText( buttonKey );
		// app.getInputManager().removeRawInputListener( list );
	}

	@Override
	public void screenAttached( ScreenManager screenManager )
	{
	}

	@Override
	public void screenDetached( ScreenManager screenManager )
	{
	}

	private void setButtonName( Button button )
	{
		currentButton = button;
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
		if ( buttonKey != ' ' )
		{
			app.getInputManager( ).removeRawInputListener( list );
			currentButton.setText( "" + buttonKey );
			buttonPressed = false;
			buttonKey = ' ';
		}

	}

}
