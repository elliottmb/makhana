package com.cogitareforma.makhana.ui;

import java.util.Arrays;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.util.SafeArrayList;

public class ScreenManager extends AbstractAppState
{

	private Application app;

	private Class< ? extends Screen > foregroundScreen;

	/**
	 * 
	 */
	private final SafeArrayList< Screen > hiding;

	/**
	 * List holding the attached screens that are pending initialization. Once
	 * initialized they will be added to the running screens.
	 */
	private final SafeArrayList< Screen > initializing;

	/**
	 * 
	 */
	private final SafeArrayList< Screen > revealing;

	private Node rootGuiNode;

	private ScreenContext screenContext;

	private Node screenHolder;

	/**
	 * Holds the active screens once they are initialized.
	 */
	private final SafeArrayList< Screen > screens;

	/**
	 * List holding the detached screens that are pending cleanup.
	 */
	private final SafeArrayList< Screen > terminating;

	/**
	 * 
	 */
	private final SafeArrayList< Screen > visible;

	public ScreenManager( Application app, Node rootGuiNode )
	{
		this.app = app;
		this.initializing = new SafeArrayList< Screen >( Screen.class );
		this.screens = new SafeArrayList< Screen >( Screen.class );
		this.terminating = new SafeArrayList< Screen >( Screen.class );
		this.revealing = new SafeArrayList< Screen >( Screen.class );
		this.visible = new SafeArrayList< Screen >( Screen.class );
		this.hiding = new SafeArrayList< Screen >( Screen.class );
		this.foregroundScreen = null;

		this.screenHolder = new Node( "screenHolder" );
		this.screenContext = new ScreenContext( this );

		this.rootGuiNode = rootGuiNode;

		rootGuiNode.attachChild( screenHolder );
	}

	public boolean addScreen( Screen screen )
	{
		synchronized ( screens )
		{
			if ( !screens.contains( screen ) && !initializing.contains( screen ) )
			{
				screen.screenAttached( this );
				initializing.add( screen );
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	public int count( )
	{
		synchronized ( screens )
		{
			return screens.size( );
		}
	}

	/**
	 * @return the game
	 */
	public Application getApp( )
	{
		return app;
	}

	protected Screen[ ] getHiding( )
	{
		synchronized ( screens )
		{
			return hiding.getArray( );
		}
	}

	protected Screen[ ] getInitializing( )
	{
		synchronized ( screens )
		{
			return initializing.getArray( );
		}
	}

	protected Screen[ ] getRevealing( )
	{
		synchronized ( screens )
		{
			return revealing.getArray( );
		}
	}

	/**
	 * Returns the foreground {@link Screen}
	 * 
	 * @return the foreground {@link Screen}.
	 */
	public Screen getScreen( )
	{
		if ( foregroundScreen != null )
		{
			getScreen( foregroundScreen );
		}
		return null;
	}

	@SuppressWarnings( "unchecked" )
	public < T extends Screen > T getScreen( Class< T > screenClass )
	{
		synchronized ( screens )
		{
			Screen[ ] array = getScreens( );
			for ( Screen screen : array )
			{
				if ( screenClass.isAssignableFrom( screen.getClass( ) ) )
				{
					return ( T ) screen;
				}
			}

			// This may be more trouble than its worth but I think
			// it's necessary for proper decoupling of screens and provides
			// similar behavior to before where a screen could be looked
			// up even if it wasn't initialized. -pspeed
			array = getInitializing( );
			for ( Screen screen : array )
			{
				if ( screenClass.isAssignableFrom( screen.getClass( ) ) )
				{
					return ( T ) screen;
				}
			}
		}
		return null;
	}

	public ScreenContext getScreenContext( )
	{
		return screenContext;
	}

	protected Screen[ ] getScreens( )
	{
		synchronized ( screens )
		{
			return screens.getArray( );
		}
	}

	protected Screen[ ] getTerminating( )
	{
		synchronized ( screens )
		{
			return terminating.getArray( );
		}
	}

	protected Screen[ ] getVisible( )
	{
		synchronized ( screens )
		{
			return visible.getArray( );
		}
	}

	public boolean hasScreen( Screen screen )
	{
		synchronized ( screens )
		{
			return screens.contains( screen ) || initializing.contains( screen );
		}
	}

	protected void hidePending( )
	{
		Screen[ ] array = getHiding( );
		if ( array.length == 0 )
			return;

		for ( Screen screen : array )
		{
			screenHolder.detachChild( screen.getScreenNode( ) );
			screen.setEnabled( false );
		}
		synchronized ( screens )
		{
			// Remove just the screens that were terminated...
			// which might now be a subset of the total terminating
			// list.
			hiding.removeAll( Arrays.asList( array ) );
		}
	}

	public < T extends Screen > boolean hideScreen( Class< T > screenClass )
	{
		return hideScreen( getScreen( screenClass ) );
	}

	public boolean hideScreen( Screen screen )
	{
		synchronized ( screens )
		{
			if ( visible.contains( screen ) )
			{
				visible.remove( screen );
				hiding.add( screen );
				return true;
			}
			else if ( revealing.contains( screen ) )
			{
				revealing.remove( screen );
				return true;
			}

			return false;
		}
	}

	protected void initializePending( )
	{
		Screen[ ] array = getInitializing( );
		if ( array.length == 0 )
			return;

		synchronized ( screens )
		{
			// Move the screens that will be initialized
			// into the active array. In all but one case the
			// order doesn't matter but if we do this here then
			// a screen can detach itself in initialize(). If we
			// did it after then it couldn't.
			List< Screen > transfer = Arrays.asList( array );
			screens.addAll( transfer );
			initializing.removeAll( transfer );
		}
		for ( Screen screen : array )
		{
			screen.initialize( this, app );
		}
	}

	public < T extends Screen > boolean removeScreen( Class< T > screenClass )
	{
		return removeScreen( getScreen( screenClass ) );
	}

	public boolean removeScreen( Screen screen )
	{
		synchronized ( screens )
		{
			if ( screens.contains( screen ) )
			{
				screen.screenDetached( this );
				screens.remove( screen );
				hiding.add( screen );
				terminating.add( screen );
				return true;
			}
			else if ( initializing.contains( screen ) )
			{
				screen.screenDetached( this );
				hiding.add( screen );
				initializing.remove( screen );
				return true;
			}

			return false;

		}
	}

	protected void revealPending( )
	{
		Screen[ ] array = getRevealing( );
		if ( array.length == 0 )
			return;

		int depth = 0;
		synchronized ( screens )
		{
			depth = visible.size( );
			// Move the screens that will be initialized
			// into the active array. In all but one case the
			// order doesn't matter but if we do this here then
			// a screen can detach itself in initialize(). If we
			// did it after then it couldn't.
			List< Screen > transfer = Arrays.asList( array );
			visible.addAll( transfer );
			revealing.removeAll( transfer );
		}
		for ( Screen screen : array )
		{
			Node screenNode = screen.getScreenNode( );
			screenHolder.attachChild( screenNode );

			Vector3f pos = screenNode.getLocalTranslation( );
			screenNode.setLocalTranslation( new Vector3f( pos.x, pos.y, depth++ * 100 ) );

			screen.setEnabled( true );
		}
	}

	public < T extends Screen > boolean setScreen( Class< T > screenClass )
	{
		return setScreen( getScreen( screenClass ) );
	}

	public boolean setScreen( Screen screen )
	{
		Screen[ ] arrVisible = getVisible( );

		synchronized ( screens )
		{
			if ( screens.contains( screen ) || initializing.contains( screen ) )
			{
				List< Screen > visibleTransfer = Arrays.asList( arrVisible );
				hiding.addAll( visibleTransfer );
				visible.clear( );
				revealing.clear( );

				return showScreen( screen );
			}

			return false;
		}

	}

	public < T extends Screen > boolean showScreen( Class< T > screenClass )
	{
		return showScreen( getScreen( screenClass ) );
	}

	public boolean showScreen( Screen screen )
	{
		synchronized ( screens )
		{
			if ( screens.contains( screen ) || initializing.contains( screen ) )
			{
				if ( !visible.contains( screen ) && !revealing.contains( screen ) )
				{
					revealing.add( screen );
					return true;
				}
			}

			return false;
		}
	}

	@Override
	public void stateDetached( AppStateManager stateManager )
	{
		rootGuiNode.detachChild( screenHolder );
		super.stateDetached( stateManager );
	}

	protected void terminatePending( )
	{
		Screen[ ] array = getTerminating( );
		if ( array.length == 0 )
			return;

		for ( Screen screen : array )
		{
			screen.cleanup( );
		}
		synchronized ( screens )
		{
			// Remove just the screens that were terminated...
			// which might now be a subset of the total terminating
			// list.
			terminating.removeAll( Arrays.asList( array ) );
		}
	}

	@Override
	public void update( float tpf )
	{
		hidePending( );

		// Cleanup any screens pending
		terminatePending( );

		// Initialize any screens pending
		initializePending( );

		revealPending( );

		// Update enabled screens
		Screen[ ] array = getScreens( );
		for ( Screen screen : array )
		{
			if ( screen.isEnabled( ) )
			{
				screen.update( tpf );
			}
		}
	}
}
