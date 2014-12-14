package com.cogitareforma.makhana.client.ui;

import java.util.ArrayList;
import java.util.Set;

import com.google.common.collect.Sets;
import com.jme3.app.Application;
import com.jme3.input.KeyNames;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
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
import com.simsilica.lemur.component.BoxLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.core.VersionedReference;

/**
 * 
 * @author Ryan Grier
 *
 */
public class OptionsScreen extends Screen
{
	private Application app;
	private Container audio;
	private Button audioButton;
	private Set< String > avaiableKeys;
	private String buttonKey;

	private boolean buttonPressed;
	private Button chatButton;
	private Container current;
	private Button currentButton;
	private Button eastButton;
	private Container graphics;
	private Button graphicsButton;
	private Container input;
	private Button inputButton;

	private RawInputListener list;
	private VersionedReference< Double > mainVolumeRef;
	private VersionedReference< Double > musicVolumeRef;
	private Button northButton;
	private Button optionsApply;
	private Button optionsExit;
	private String prevKey;
	private Slider quality;
	private VersionedReference< Double > qualityRef;
	private Slider res;
	private Label resLabel;
	private ArrayList< String > resolutions;
	private VersionedReference< Double > resRef;
	private Button scoreboardButton;
	private VersionedReference< Double > soundVolumeRef;
	private Button southButton;
	private Button westButton;

	private Panel background;
	private Container top;
	private Container middle;

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

	private void activeListener( )
	{
		System.out.println( "Active Listener" );
		buttonKey = " ";
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
			public void onKeyEvent( KeyInputEvent arg0 )
			{
				System.out.println( "Key: " + arg0 );
				returnName( arg0.getKeyCode( ), arg0.getKeyChar( ) );
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
		};
		app.getInputManager( ).addRawInputListener( list );
		buttonPressed = true;
	}

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );
		this.app = app;
		buttonKey = " ";

		avaiableKeys = Sets.newHashSet( "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
				"U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "Up", "Down", "Left", "Right", "`", "Tab" );
		resolutions = new ArrayList< String >( );
		resolutions.add( "A" );
		resolutions.add( "B" );
		resolutions.add( "C" );
		resolutions.add( "D" );
		resolutions.add( "E" );
		resolutions.add( "F" );
		resolutions.add( "G" );
		resolutions.add( "H" );
		resolutions.add( "I" );
		resolutions.add( "J" );
		resolutions.add( "K" );
		resolutions.add( "L" );
		resolutions.add( "M" );
		resolutions.add( "N" );
		resolutions.add( "O" );
		resolutions.add( "P" );

		float mediumFontSize = getContext( ).getMediumFontSize( );

		background = new Panel( );
		background.setBackground( new QuadBackgroundComponent( ColorRGBA.Gray ) );
		background.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ), 0 ) );
		getScreenNode( ).attachChild( background );

		top = new Container( new BoxLayout( Axis.X, FillMode.Proportional ), "glass" );
		top.setBackground( new QuadBackgroundComponent( ColorRGBA.DarkGray ) );
		top.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		top.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ) * 0.1f, 0 ) );

		Label name = new Label( "Options" );
		name.setFontSize( mediumFontSize );
		name.setPreferredSize( new Vector3f( 0.8f, 0, 0 ) );

		Container buttons = new Container( new BoxLayout( Axis.X, FillMode.Even ), "glass" );
		buttons.setPreferredSize( new Vector3f( 0.2f, 0, 0 ) );

		top.addChild( name );
		top.addChild( buttons );

		middle = new Container( new BoxLayout( Axis.X, FillMode.Even ) );
		middle.setBackground( new QuadBackgroundComponent( ColorRGBA.Brown ) );
		middle.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .4f, getContext( ).getHeight( ) * .05f, 0 ) );
		middle.setLocalTranslation( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * 0.85f, 0 );

		setUpButtons( screenManager );

		middle.addChild( graphicsButton );
		middle.addChild( audioButton );
		middle.addChild( inputButton );

		optionsApply.setFontSize( mediumFontSize );
		optionsExit.setFontSize( mediumFontSize );
		buttons.addChild( optionsApply );
		buttons.addChild( optionsExit );
		graphicsButton.setFontSize( mediumFontSize );
		audioButton.setFontSize( mediumFontSize );
		inputButton.setFontSize( mediumFontSize );

		graphics = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		graphics.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		graphics.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .9f, getContext( ).getHeight( ) * .75f, 0 ) );
		graphics.setLocalTranslation( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * 0.8f, 0 );

		audio = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		audio.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		audio.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .9f, getContext( ).getHeight( ) * .75f, 0 ) );
		audio.setLocalTranslation( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * 0.8f, 0 );

		// input = new Container( new BoxLayout( Axis.Y, FillMode.None ) );
		input = new Container( new SpringGridLayout( Axis.X, Axis.Y, FillMode.None, FillMode.None ) );
		input.setBackground( new QuadBackgroundComponent( new ColorRGBA( .07f, .52f, .49f, 1 ) ) );
		input.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .9f, getContext( ).getHeight( ) * .75f, 0 ) );
		input.setLocalTranslation( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * 0.8f, 0 );

		resLabel = new Label( "" );
		resLabel.setFontSize( mediumFontSize );

		res = new Slider( new DefaultRangedValueModel( 0, 15, 1 ), "glass" );
		res.setName( "Resolution" );
		res.setInsets( new Insets3f( getContext( ).getHeight( ) * .02f, 0, getContext( ).getHeight( ) * .02f, 0 ) );
		resRef = res.getModel( ).createReference( );
		resLabel.setText( "Resolution: " + resolutions.get( resRef.get( ).intValue( ) ) );

		Checkbox fullscreen = new Checkbox( "FullScreen" );
		fullscreen.setFontSize( mediumFontSize );
		fullscreen.setInsets( new Insets3f( getContext( ).getHeight( ) * .02f, 0, getContext( ).getHeight( ) * .02f, 0 ) );

		quality = new Slider( new DefaultRangedValueModel( 0, 4, 4 ), "glass" );
		quality.setName( "Quality" );
		quality.setInsets( new Insets3f( getContext( ).getHeight( ) * .02f, 0, getContext( ).getHeight( ) * .02f, 0 ) );
		qualityRef = quality.getModel( ).createReference( );

		Checkbox vsync = new Checkbox( "VSync" );
		vsync.setFontSize( mediumFontSize );
		vsync.setInsets( new Insets3f( getContext( ).getHeight( ) * .02f, 0, getContext( ).getHeight( ) * .02f, 0 ) );

		Label mainLabel = new Label( "Main Volume" );
		mainLabel.setFontSize( mediumFontSize );
		Label musicLabel = new Label( "Music Volume" );
		musicLabel.setFontSize( mediumFontSize );
		Label soundLabel = new Label( "Sound Volume" );
		soundLabel.setFontSize( mediumFontSize );

		Slider mainVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		mainVolume.setInsets( new Insets3f( getContext( ).getHeight( ) * .02f, 0, getContext( ).getHeight( ) * .02f, 0 ) );
		Slider musicVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		musicVolume.setInsets( new Insets3f( getContext( ).getHeight( ) * .02f, 0, getContext( ).getHeight( ) * .02f, 0 ) );
		Slider soundsVolume = new Slider( new DefaultRangedValueModel( 0, 100, 100 ), "glass" );
		soundsVolume.setInsets( new Insets3f( getContext( ).getHeight( ) * .02f, 0, getContext( ).getHeight( ) * .02f, 0 ) );
		mainVolumeRef = mainVolume.getModel( ).createReference( );
		musicVolumeRef = musicVolume.getModel( ).createReference( );
		soundVolumeRef = soundsVolume.getModel( ).createReference( );

		Checkbox console = new Checkbox( "Enable Dev Console", "glass" );
		console.setFontSize( mediumFontSize );
		console.setInsets( new Insets3f( getContext( ).getHeight( ) * .02f, 0, getContext( ).getHeight( ) * .02f, 0 ) );

		Insets3f inputInsets = new Insets3f( 0, 0, getContext( ).getScalar( ) * 16f, 0 );

		Label northLabel = new Label( "Move North " );
		northLabel.setFontSize( mediumFontSize );
		input.addChild( northLabel, 1, 1 );

		northButton.setFontSize( mediumFontSize );
		northButton.setInsets( inputInsets );
		input.addChild( northButton, 2, 1 );

		Label southLabel = new Label( "Move South " );
		southLabel.setFontSize( mediumFontSize );
		input.addChild( southLabel, 1, 2 );

		southButton.setFontSize( mediumFontSize );
		southButton.setInsets( inputInsets );
		input.addChild( southButton, 2, 2 );

		Label eastLabel = new Label( "Move East " );
		eastLabel.setFontSize( mediumFontSize );
		input.addChild( eastLabel, 1, 3 );

		eastButton.setFontSize( mediumFontSize );
		eastButton.setInsets( inputInsets );
		input.addChild( eastButton, 2, 3 );

		Label westLabel = new Label( "Move West " );
		westLabel.setFontSize( mediumFontSize );
		input.addChild( westLabel, 1, 4 );

		westButton.setFontSize( mediumFontSize );
		westButton.setInsets( inputInsets );
		input.addChild( westButton, 2, 4 );

		Label chatLabel = new Label( "Chat       " );
		chatLabel.setFontSize( mediumFontSize );
		input.addChild( chatLabel, 1, 5 );

		chatButton.setFontSize( mediumFontSize );
		chatButton.setInsets( inputInsets );
		input.addChild( chatButton, 2, 5 );

		Label scoreLabel = new Label( "Scoreboard" );
		scoreLabel.setFontSize( mediumFontSize );
		input.addChild( scoreLabel, 1, 6 );

		scoreboardButton.setFontSize( mediumFontSize );
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
	public void reshape( int w, int h )
	{
		super.reshape( w, h );

		// TODO Auto-generated method stub
		background.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		background.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ), 0 ) );

		top.setLocalTranslation( 0, getContext( ).getHeight( ), 0 );
		top.setPreferredSize( new Vector3f( getContext( ).getWidth( ), getContext( ).getHeight( ) * 0.1f, 0 ) );

		middle.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .4f, getContext( ).getHeight( ) * .05f, 0 ) );
		middle.setLocalTranslation( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * 0.85f, 0 );

		graphics.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .9f, getContext( ).getHeight( ) * .75f, 0 ) );
		graphics.setLocalTranslation( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * 0.8f, 0 );

		audio.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .9f, getContext( ).getHeight( ) * .75f, 0 ) );
		audio.setLocalTranslation( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * 0.8f, 0 );

		input.setPreferredSize( new Vector3f( getContext( ).getWidth( ) * .9f, getContext( ).getHeight( ) * .75f, 0 ) );
		input.setLocalTranslation( getContext( ).getWidth( ) * .05f, getContext( ).getHeight( ) * 0.8f, 0 );

	}

	private String returnName( int number, char letter )
	{
		// TODO need to add back the old key to avaiableKeys.
		// avaiableKeys.add( charMap.get( prevKey ) );
		avaiableKeys.add( prevKey );
		KeyNames key = new KeyNames( );
		if ( avaiableKeys.contains( key.getName( number ) ) )
		{
			avaiableKeys.remove( key.getName( number ) );

			buttonKey = key.getName( number );
			return buttonKey;
		}
		buttonKey = " ";
		return buttonKey;
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
					prevKey = northButton.getText( );
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
					prevKey = southButton.getText( );
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
					prevKey = eastButton.getText( );
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
					prevKey = westButton.getText( );
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
					prevKey = chatButton.getText( );
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
					prevKey = scoreboardButton.getText( );
					scoreboardButton.setText( "<Press Key>" );
					activeListener( );
					setButtonName( scoreboardButton );
				}
			}
		} );
	}

	@Override
	public void update( float tpf )
	{

		if ( resRef.update( ) )
		{
			res.getModel( ).setValue( Math.round( resRef.get( ) ) );
			resLabel.setText( "Resolution: " + resolutions.get( resRef.get( ).intValue( ) ) );
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
		if ( buttonKey != " " )
		{
			app.getInputManager( ).removeRawInputListener( list );
			currentButton.setText( "" + buttonKey );
			buttonPressed = false;
			buttonKey = " ";
		}

	}

}
