package com.cogitareforma.hexrepublics.client.views;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.client.util.KeyBindings;
import trendli.me.makhana.common.util.MakhanaConfig;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.audio.Listener;
import com.jme3.input.KeyInput;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.system.AppSettings;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.CheckBox;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Slider;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.checkbox.builder.CheckboxBuilder;
import de.lessvoid.nifty.controls.dropdown.builder.DropDownBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.slider.builder.SliderBuilder;
import de.lessvoid.nifty.screen.Screen;

/**
 * 
 * @author Ryan Grier
 */
public class OptionsViewController extends GeneralController
{
    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( OptionsViewController.class.getName( ) );
    private Nifty nifty;
    private DropDown< String > graphic;
    private CheckBox fullscreen;
    private Slider mainVolume;
    private KeyBindings keyBinds;
    private SettingsInputHandler handle;
    private CheckBox console;
    private LinkedHashMap< Integer, String > k = new LinkedHashMap< Integer, String >( );
    private String[ ] values =
    {
            "KEY_A", "KEY_B", "KEY_C", "KEY_D", "KEY_E", "KEY_F", "KEY_G", "KEY_H", "KEY_I", "KEY_J", "KEY_K", "KEY_L", "KEY_M", "KEY_N",
            "KEY_O", "KEY_P", "KEY_Q", "KEY_R", "KEY_S", "KEY_T", "KEY_U", "KEY_V", "KEY_W", "KEY_X", "KEY_Y", "KEY_Z", "KEY_1", "KEY_2",
            "KEY_3", "KEY_4", "KEY_5", "KEY_6", "KEY_7", "KEY_8", "KEY_9", "KEY_0", "KEY_UP", "KEY_DOWN", "KEY_LEFT", "KEY_RIGHT",
            "KEY_GRAVE", "KEY_TAB", "KEY_F1", "KEY_F2", "KEY_F3", "KEY_F4", "KEY_F5", "KEY_F6", "KEY_F7", "KEY_F8", "KEY_F9", "KEY_F10",
            "KEY_F11", "KEY_F12"
    };
    private int[ ] keys =
    {
            KeyInput.KEY_A, KeyInput.KEY_B, KeyInput.KEY_C, KeyInput.KEY_D, KeyInput.KEY_E, KeyInput.KEY_F, KeyInput.KEY_G, KeyInput.KEY_H,
            KeyInput.KEY_I, KeyInput.KEY_J, KeyInput.KEY_K, KeyInput.KEY_L, KeyInput.KEY_M, KeyInput.KEY_N, KeyInput.KEY_O, KeyInput.KEY_P,
            KeyInput.KEY_Q, KeyInput.KEY_R, KeyInput.KEY_S, KeyInput.KEY_T, KeyInput.KEY_U, KeyInput.KEY_V, KeyInput.KEY_W, KeyInput.KEY_X,
            KeyInput.KEY_Y, KeyInput.KEY_Z, KeyInput.KEY_1, KeyInput.KEY_2, KeyInput.KEY_3, KeyInput.KEY_4, KeyInput.KEY_5, KeyInput.KEY_6,
            KeyInput.KEY_7, KeyInput.KEY_8, KeyInput.KEY_9, KeyInput.KEY_0, KeyInput.KEY_UP, KeyInput.KEY_DOWN, KeyInput.KEY_LEFT,
            KeyInput.KEY_RIGHT, KeyInput.KEY_GRAVE, KeyInput.KEY_TAB, KeyInput.KEY_F1, KeyInput.KEY_F2, KeyInput.KEY_F3, KeyInput.KEY_F4,
            KeyInput.KEY_F5, KeyInput.KEY_F6, KeyInput.KEY_F7, KeyInput.KEY_F8, KeyInput.KEY_F9, KeyInput.KEY_F10, KeyInput.KEY_F11,
            KeyInput.KEY_F12
    };

    private ScreenBuilder optionsScreen = null;

    public OptionsViewController( Nifty nifty )
    {
        optionsScreen = new ScreenBuilder( "options" )
        {
            {
                layer( new LayerBuilder( "layer" )
                {
                    {
                        backgroundColor( "#838796ff" );
                        childLayoutVertical( );

                        panel( new PanelBuilder( "optionsHeader" )
                        {
                            {
                                width( "100%" );
                                height( "32px" );
                                valignBottom( );
                                childLayoutHorizontal( );
                                backgroundColor( "#616374ff" );
                                paddingLeft( "32px" );
                                paddingRight( "32px" );

                                text( new TextBuilder( "optionsTitle" )
                                {
                                    {
                                        width( "50%" );
                                        height( "100%" );
                                        text( "Options" );
                                        textHAlignLeft( );
                                        textVAlignCenter( );
                                        font( "Interface/Fonts/Default.fnt" );

                                    }
                                } );

                                panel( new PanelBuilder( "optionsButtons" )
                                {
                                    {
                                        width( "50%" );
                                        childLayoutHorizontal( );

                                        panel( new PanelBuilder( "spacer" )
                                        {
                                            {
                                                width( "*" );
                                            }
                                        } );
                                        control( new ButtonBuilder( "selectGraphics", "Apply" )
                                        {
                                            {
                                                alignCenter( );
                                                valignCenter( );
                                                visibleToMouse( true );
                                                interactOnClick( "applySettings()" );
                                            }
                                        } );
                                        control( new ButtonBuilder( "backtostart", "Back" )
                                        {
                                            {
                                                alignCenter( );
                                                valignCenter( );
                                                visibleToMouse( true );
                                                interactOnClick( "exitMainOptions()" );
                                            }
                                        } );
                                    }
                                } );

                            }
                        } );
                        panel( new PanelBuilder( "optionsTabs" )
                        {
                            {
                                width( "66%" );
                                height( "100%" );
                                padding( "32px" );
                                alignCenter( );
                                valignCenter( );
                                childLayoutVertical( );

                                panel( new PanelBuilder( "graphics" )
                                {
                                    {
                                        style( "nifty-panel-brown" );
                                        childLayoutVertical( );

                                        control( new LabelBuilder( "labelGraphics", "Graphics" )
                                        {
                                            {
                                                textHAlignLeft( );
                                                textVAlignBottom( );
                                                width( "100%" );
                                            }
                                        } );

                                        panel( new PanelBuilder( "graphicsSettings" )
                                        {
                                            {
                                                style( "nifty-panel-inset-beige" );
                                                childLayoutVertical( );

                                                panel( new PanelBuilder( "optionsResolution" )
                                                {
                                                    {
                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "labelResolution", "Screen Resolution: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new DropDownBuilder( "graphicsOptions" )
                                                        {
                                                            {

                                                            }
                                                        } );
                                                    }
                                                } );
                                                panel( new PanelBuilder( "optionsFullscreen" )
                                                {
                                                    {
                                                        childLayoutHorizontal( );

                                                        control( new LabelBuilder( "labelFullscreen", "Fullscreen: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new CheckboxBuilder( "graphicsFullscreen" )
                                                        {
                                                            {

                                                            }
                                                        } );
                                                    }
                                                } );
                                                panel( new PanelBuilder( "optionsQuality" )
                                                {
                                                    {
                                                        childLayoutHorizontal( );

                                                        control( new LabelBuilder( "labelQuality", "Quality: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );

                                                        control( new SliderBuilder( "sliderA", false )
                                                        {
                                                            {
                                                                buttonStepSize( 1.0f );
                                                                max( 2.0f );
                                                            }
                                                        } );

                                                    }
                                                } );
                                                panel( new PanelBuilder( "optionsVSync" )
                                                {
                                                    {
                                                        childLayoutHorizontal( );

                                                        control( new LabelBuilder( "labelVSync", "VSync: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new CheckboxBuilder( "graphicsVSync" )
                                                        {
                                                            {

                                                            }
                                                        } );
                                                    }
                                                } );
                                            }
                                        } );
                                    }
                                } );

                                panel( new PanelBuilder( "audio" )
                                {
                                    {
                                        style( "nifty-panel-brown" );
                                        childLayoutVertical( );

                                        control( new LabelBuilder( "audioOptions", "Audio" )
                                        {
                                            {
                                                textHAlignLeft( );
                                                textVAlignBottom( );
                                                width( "100%" );
                                            }
                                        } );

                                        panel( new PanelBuilder( "audioSettings" )
                                        {
                                            {
                                                style( "nifty-panel-inset-beige" );
                                                childLayoutVertical( );
                                                panel( new PanelBuilder( "optionsMainAudio" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "labelMainVolume", "Main Volume: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );

                                                        control( new SliderBuilder( "mainVolumeSlider", false )
                                                        {
                                                            {
                                                                buttonStepSize( 5.0f );
                                                                max( 100.0f );
                                                            }
                                                        } );
                                                    }
                                                } );
                                                panel( new PanelBuilder( "optionsMusic" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "labelMusic", "Music Volume: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );

                                                        control( new SliderBuilder( "musicSlider", false )
                                                        {
                                                            {
                                                                buttonStepSize( 5.0f );
                                                                max( 100.0f );
                                                            }
                                                        } );
                                                    }
                                                } );
                                                panel( new PanelBuilder( "optionsSounds" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "labelSounds", "Sounds Volume: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );

                                                        control( new SliderBuilder( "soundsSlider", false )
                                                        {
                                                            {
                                                                buttonStepSize( 5.0f );
                                                                max( 100.0f );
                                                            }
                                                        } );
                                                    }
                                                } );
                                            }
                                        } );
                                    }
                                } );
                                panel( new PanelBuilder( "input" )
                                {
                                    {
                                        style( "nifty-panel-brown" );
                                        childLayoutVertical( );
                                        control( new LabelBuilder( "inputOptions", "Input" )
                                        {
                                            {
                                                textHAlignLeft( );
                                                textVAlignBottom( );
                                                width( "100%" );
                                            }
                                        } );

                                        panel( new PanelBuilder( "inputSettings" )
                                        {
                                            {
                                                style( "nifty-panel-inset-beige" );
                                                childLayoutVertical( );

                                                panel( new PanelBuilder( "consoleCheck" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "consoleLabel", "Enable Dev Console: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new CheckboxBuilder( "consoleCheckBox" )
                                                        {
                                                            {
                                                                checked( false );
                                                            }
                                                        } );
                                                    }
                                                } );
                                                panel( new PanelBuilder( "inputNorth" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "northLabel", "Move North: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new ButtonBuilder( "northButton", "" )
                                                        {
                                                            {
                                                                visibleToMouse( true );
                                                                interactOnClick( "setKeyBinding(northButton)" );
                                                            }
                                                        } );
                                                    }
                                                } );

                                                panel( new PanelBuilder( "inputSouth" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "southLabel", "Move South: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new ButtonBuilder( "southButton", "" )
                                                        {
                                                            {
                                                                visibleToMouse( true );
                                                                interactOnClick( "setKeyBinding(southButton)" );
                                                            }
                                                        } );
                                                    }
                                                } );

                                                panel( new PanelBuilder( "inputEast" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "eastLabel", "Move East: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new ButtonBuilder( "eastButton", "" )
                                                        {
                                                            {
                                                                visibleToMouse( true );
                                                                interactOnClick( "setKeyBinding(eastButton)" );
                                                            }
                                                        } );
                                                    }
                                                } );

                                                panel( new PanelBuilder( "inputWest" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "westLabel", "Move West: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new ButtonBuilder( "westButton", "" )
                                                        {
                                                            {
                                                                visibleToMouse( true );
                                                                interactOnClick( "setKeyBinding(westButton)" );
                                                            }
                                                        } );
                                                    }
                                                } );

                                                panel( new PanelBuilder( "inputChat" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "chatLabel", "Chat: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new ButtonBuilder( "chatButton", "" )
                                                        {
                                                            {
                                                                visibleToMouse( true );
                                                                interactOnClick( "setKeyBinding(chatButton)" );
                                                            }
                                                        } );
                                                    }
                                                } );

                                                panel( new PanelBuilder( "inputScore" )
                                                {
                                                    {

                                                        childLayoutHorizontal( );
                                                        control( new LabelBuilder( "scoreLabel", "Scoreboard: " )
                                                        {
                                                            {
                                                                textHAlignLeft( );
                                                                width( "25%" );
                                                            }
                                                        } );
                                                        control( new ButtonBuilder( "scoreButton", "" )
                                                        {
                                                            {
                                                                visibleToMouse( true );
                                                                interactOnClick( "setKeyBinding(scoreButton)" );
                                                            }
                                                        } );
                                                    }
                                                } );
                                            }
                                        } );
                                    }
                                } );
                            }
                        } );
                    }
                } );
            }
        };
        optionsScreen.controller( this );
        optionsScreen.build( nifty );
    }

    /**
     * If the user has made any changes, saves the changes and updates the
     * client with user defined changes.
     */
    public void applySettings( )
    {
        String selected = this.graphic.getSelection( );
        AppSettings settings = getApp( ).getContext( ).getSettings( );
        Listener listener = getApp( ).getListener( );
        float volume = this.mainVolume.getValue( );
        if ( hasChanged( selected, settings, listener, volume ) )
        {
            int first = Integer.parseInt( selected.split( "x" )[ 0 ] );
            int second = Integer.parseInt( selected.split( "x" )[ 1 ] );
            listener.setVolume( volume );

            MakhanaConfig config = getApp( ).getConfiguration( );

            config.putAudioSettings( listener );
            config.configureAudioSettings( listener );

            if ( hasGraphicsChanged( selected, settings ) )
            {
                settings.setResolution( first, second );
                settings.setTitle( "Hex Republics - Alpha" );

                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment( ).getDefaultScreenDevice( );
                settings.setFullscreen( fullscreen.isChecked( ) && device.isFullScreenSupported( ) );
                getApp( ).setSettings( settings );

                config.putAppSettings( settings );

                getApp( ).restart( );
            }
            if ( hasInputChanged( ) )
            {
                config.put( "client.input.northKey", getKeyByValue( k, displayedKey( "northButton" ) ) );
                config.put( "client.input.southKey", getKeyByValue( k, displayedKey( "southButton" ) ) );
                config.put( "client.input.eastKey", getKeyByValue( k, displayedKey( "eastButton" ) ) );
                config.put( "client.input.westKey", getKeyByValue( k, displayedKey( "westButton" ) ) );
                config.put( "client.input.scoreKey", getKeyByValue( k, displayedKey( "scoreButton" ) ) );
                config.put( "client.input.chatKey", getKeyByValue( k, displayedKey( "chatButton" ) ) );
            }

            if ( console.isChecked( ) )
            {
                getApp( ).consoleEnabled = true;
                config.put( "client.input.console", true );
            }
            else
            {
                getApp( ).consoleEnabled = false;
                config.put( "client.input.console", false );
            }

            config.save( );
        }
        if ( console.isChecked( ) )
        {
            getApp( ).consoleEnabled = true;
        }
        else
        {
            getApp( ).consoleEnabled = false;
        }

    }

    public void bind( Nifty nifty, Screen screen )
    {
        this.nifty = nifty;
    }

    /**
     * Gets the displayed text on the button with the name key.
     * 
     * @param key
     * @return Displayed text on button key.
     */
    private String displayedKey( String key )
    {
        Button button = nifty.getCurrentScreen( ).findNiftyControl( key, Button.class );
        return button.getText( );
    }

    public void exitMainOptions( )
    {
        if ( nifty.getScreen( "hud" ) != null )
        {
            HudViewController hud = ( HudViewController ) nifty.getScreen( "hud" ).getScreenController( );
            hud.refreshKeys( );
            gotoScreen( "hud", true, false, true, null, null );
        }
        else
        {
            gotoScreen( "start", true, false, true, null, null );
        }
    }

    /**
     * Adds all available resolution options to drop down menu
     */
    public void fillGraphicsOptions( )
    {
        this.graphic.addItem( "1024x768" );
        this.graphic.addItem( "1280x720" );
        this.graphic.addItem( "1280x768" );
        this.graphic.addItem( "1280x800" );
        this.graphic.addItem( "1280x1024" );
        this.graphic.addItem( "1360x768" );
        this.graphic.addItem( "1366x768" );
        this.graphic.addItem( "1440x900" );
        this.graphic.addItem( "1600x900" );
        this.graphic.addItem( "1680x1050" );
        this.graphic.addItem( "1920x1080" );
        this.graphic.addItem( "1920x1200" );
        this.graphic.addItem( "2048x1152" );
        this.graphic.addItem( "2560x1440" );
        this.graphic.addItem( "2560x1600" );
        if ( getApp( ).getContext( ).getSettings( ).isFullscreen( ) )
        {
            this.fullscreen.check( );
        }
    }

    public void fillkeys( )
    {

        startKeyBinding( "northButton", "client.input.northKey" );
        startKeyBinding( "southButton", "client.input.southKey" );
        startKeyBinding( "eastButton", "client.input.eastKey" );
        startKeyBinding( "westButton", "client.input.westKey" );
        startKeyBinding( "scoreButton", "client.input.scoreKey" );
        startKeyBinding( "chatButton", "client.input.chatKey" );
        /*
         * if ( getApp().consoleEnabled ) { console.check( ); }
         */
        Boolean con = ( Boolean ) getApp( ).getConfiguration( ).get( "client.input.console" );
        if ( con == true )
        {
            console.check( );
            getApp( ).consoleEnabled = true;
        }
        else
        {
            getApp( ).consoleEnabled = false;
        }
    }

    public KeyBindings getKeyBindings( )
    {
        return keyBinds;
    }

    /**
     * Returns corresponding key from given string value.
     * 
     * @param map
     *            LinkedHashMap representing the different input keys.
     * @param value
     *            Value to find matching key to.
     * @return Key that matches the inputed value.
     */
    public < T, E > T getKeyByValue( LinkedHashMap< T, E > map, E value )
    {
        for ( Entry< T, E > entry : map.entrySet( ) )
        {
            if ( value.equals( entry.getValue( ) ) )
            {
                return entry.getKey( );
            }
        }
        return null;
    }

    public void getSelected( )
    {
        String select = this.graphic.getSelection( );
        System.out.println( select );
    }

    /**
     * Checks to see if user has made any changes to options.
     * 
     * @param selected
     *            What resolution is selected.
     * @param settings
     *            Game's settings.
     * @param listener
     *            Game's audio settings.
     * @param volume
     *            Game's audio volume settings.
     * @return Returns true if the volume, graphics, or input settings have
     *         changed, else false.
     */
    private boolean hasChanged( String selected, AppSettings settings, Listener listener, float volume )
    {
        if ( listener.getVolume( ) != volume || hasGraphicsChanged( selected, settings ) || hasInputChanged( ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Checks to see if user has made any changes to graphics options.
     * 
     * @param selected
     *            What resolution is selected.
     * @param settings
     *            Game's settings.
     * @return Returns true if the graphics settings have changed, else false.
     */

    private boolean hasGraphicsChanged( String selected, AppSettings settings )
    {
        if ( !selected.equals( settings.getWidth( ) + "x" + settings.getHeight( ) )
                || this.fullscreen.isChecked( ) != settings.isFullscreen( ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Checks if input settings have been changed. If so return true.
     * 
     * @return Returns true if input settings have changed from the YamlConfig
     *         file, else false.
     */
    private boolean hasInputChanged( )
    {
        MakhanaConfig config = getApp( ).getConfiguration( );
        int north = ( int ) config.get( "client.input.northKey" );
        int south = ( int ) config.get( "client.input.southKey" );
        int east = ( int ) config.get( "client.input.eastKey" );
        int west = ( int ) config.get( "client.input.westKey" );
        int score = ( int ) config.get( "client.input.scoreKey" );
        int chat = ( int ) config.get( "client.input.chatKey" );
        if ( north != getKeyByValue( k, displayedKey( "northButton" ) ) || south != getKeyByValue( k, displayedKey( "southButton" ) )
                || east != getKeyByValue( k, displayedKey( "eastButton" ) ) || west != getKeyByValue( k, displayedKey( "westButton" ) )
                || score != getKeyByValue( k, displayedKey( "scoreButton" ) ) || chat != getKeyByValue( k, displayedKey( "chatButton" ) ) )
        {
            return true;
        }
        return false;
    }

    /**
     * Creates either main options or in-game options. Main options lets the
     * user change resolution and graphics where in-game won't.
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public void initialize( AppStateManager stateManager, Application app )
    {
        super.initialize( stateManager, app );

        this.nifty = getApp( ).getNifty( );
        logger.log( Level.INFO, "Initialised " + this.getClass( ) );
        if ( nifty.getScreen( "mainOptions" ).isRunning( ) )
        {
            this.graphic = nifty.getScreen( "mainOptions" ).findNiftyControl( "graphicsOptions", DropDown.class );
            this.fullscreen = nifty.getScreen( "mainOptions" ).findNiftyControl( "graphicsFullscreen", CheckBox.class );
            this.mainVolume = nifty.getScreen( "mainOptions" ).findNiftyControl( "mainVolumeSlider", Slider.class );
            this.console = nifty.getScreen( "mainOptions" ).findNiftyControl( "consoleCheckBox", CheckBox.class );
            fillGraphicsOptions( );
            getApp( ).currentScreen = "mainOptions";
        }
        else
        {
            getApp( ).currentScreen = "options";
        }
        for ( int i = 0; i < keys.length; i++ )
        {
            k.put( keys[ i ], values[ i ] );
        }
        fillkeys( );
        graphic.selectItem( getApp( ).getContext( ).getSettings( ).getWidth( ) + "x" + getApp( ).getContext( ).getSettings( ).getHeight( ) );
        mainVolume.setValue( ( getApp( ).getListener( ).getVolume( ) ) * 50.0f );
        keyBinds = new KeyBindings( getApp( ).getConfiguration( ) );
    }

    /**
     * Receives KeyInputEvent and puts corresponding text on the correct button.
     * 
     * @param evt
     * @param eventId
     */
    public void keyBindCallBack( KeyInputEvent evt, String eventId )
    {
        Screen screen = nifty.getCurrentScreen( );
        Button button = screen.findNiftyControl( eventId, Button.class );
        button.setText( "" + KeyBindings.getKeyName( evt.getKeyCode( ) ) );
        mapNiftyBindings( eventId, keyBinds, evt.getKeyCode( ) );
        getApp( ).getInputManager( ).removeRawInputListener( handle );
        handle = null;
    }

    /**
     * Binds keys by given KeyInputEvent name.
     * 
     * @param eventId
     * @param keyBindings
     * @param keyCode
     */
    public void mapNiftyBindings( String eventId, KeyBindings keyBindings, int keyCode )
    {
        if ( eventId.equals( "north" ) )
        {
            keyBindings.north = keyCode;
        }
        if ( eventId.equals( "south" ) )
        {
            keyBindings.south = keyCode;
        }
        if ( eventId.equals( "west" ) )
        {
            keyBindings.west = keyCode;
        }
        if ( eventId.equals( "east" ) )
        {
            keyBindings.east = keyCode;
        }
        if ( eventId.equals( "chat" ) )
        {
            keyBindings.chat = keyCode;
        }
        if ( eventId.equals( "score" ) )
        {
            keyBindings.score = keyCode;
        }
    }

    public void nextScreen( String screen )
    {
        gotoScreen( screen, false, false, false, null, null );
    }

    public void onEndScreen( )
    {

    }

    public void onStartScreen( )
    {

    }

    /**
     * Changes the text on the input buttons. If there is already a key bound,
     * removes the text and puts <press any key>
     * 
     * @param eventId
     */
    public void setKeyBinding( String eventId )
    {
        Screen screen = nifty.getCurrentScreen( );
        if ( handle != null )
        {
            Button button = screen.findNiftyControl( handle.getEventId( ), Button.class );
            button.setText( "" );
            getApp( ).getInputManager( ).removeRawInputListener( handle );
        }
        Button button = screen.findNiftyControl( eventId, Button.class );
        button.setText( "<press any key>" );
        handle = new SettingsInputHandler( this, eventId );
        getApp( ).getInputManager( ).addRawInputListener( handle );
    }

    /**
     * Fills in button text with key bindings from yaml config file.
     * 
     * @param keyButton
     * @param yamlKey
     */
    public void startKeyBinding( String keyButton, String yamlKey )
    {
        Button button = nifty.getCurrentScreen( ).findNiftyControl( keyButton, Button.class );
        button.setText( KeyBindings.getKeyName( ( int ) getApp( ).getConfiguration( ).get( yamlKey ) ) );
    }
}
