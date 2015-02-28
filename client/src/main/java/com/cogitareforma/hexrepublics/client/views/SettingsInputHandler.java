package com.cogitareforma.hexrepublics.client.views;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

public class SettingsInputHandler implements RawInputListener
{
    /**
     * Logger for this class
     */
    private final static Logger logger = Logger.getLogger( SettingsInputHandler.class.getName( ) );

    private OptionsViewController appState;
    private String eventId;

    public SettingsInputHandler( OptionsViewController state, String eventId )
    {
        this.appState = state;
        this.eventId = eventId;
    }

    @Override
    public void beginInput( )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void endInput( )
    {
        // TODO Auto-generated method stub

    }

    public String getEventId( )
    {
        return eventId;
    }

    @Override
    public void onJoyAxisEvent( JoyAxisEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onJoyButtonEvent( JoyButtonEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onKeyEvent( KeyInputEvent arg0 )
    {
        try
        {
            appState.keyBindCallBack( arg0, eventId );
            // con.putInputSettings( "north", 6 );
        }
        catch ( Exception ex )
        {
            logger.log( Level.SEVERE, "Error in SettingsInputHandler onKeyEvent" );
        }

    }

    @Override
    public void onMouseButtonEvent( MouseButtonEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseMotionEvent( MouseMotionEvent arg0 )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTouchEvent( TouchEvent arg0 )
    {
        // TODO Auto-generated method stub

    }
}
