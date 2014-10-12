package com.cogitareforma.makhana.common.ui;

import com.jme3.app.Application;
import com.jme3.scene.Node;

public abstract class Screen
{

	private boolean enabled = false;
	protected boolean initialized = false;
	private Node screenNode;

	/**
	 * Called by ScreenManager when transitioning this Screen from terminating
	 * to detached. This method is called the following render pass after the
	 * Screen has been detached and is always called once and only once for each
	 * time initialize() is called. Either when the Screen is detached or when
	 * the application terminates (if it terminates normally).
	 */
	public abstract void cleanup( );

	public Node getScreenNode( )
	{
		return screenNode;
	}

	public void initialize( ScreenManager screenManager, Application app )
	{
		initialized = true;
		screenNode = new Node( );
	}

	public boolean isEnabled( )
	{
		return enabled;
	}

	public boolean isInitialized( )
	{
		return initialized;
	}

	/**
	 * Called by ScreenManager when transitioning this Screen from hidden to
	 * displayed. This is called before the Screen's containing node is attached
	 * to the ScreenManager's scene graph. It should be safe to modify the scene
	 * graph of the Screen.
	 *
	 * @param screenManager
	 *            Screen manager to which the screen was attached to.
	 */
	public abstract void screenAttached( ScreenManager screenManager );

	/**
	 * Called by ScreenManager when transitioning this Screen from displayed to
	 * hidden. This is called after the Screen's containing node is detached
	 * from the ScreenManager's scene graph. It should be safe to modify the
	 * scene graph of the Screen.
	 *
	 * @param screenManager
	 *            Screen manager to which the screen was attached to.
	 */
	public abstract void screenDetached( ScreenManager screenManager );

	/**
	 * Enable or disable the functionality of the Screen. The effect of this
	 * call depends on implementation. A Screen starts as being disabled by
	 * default. A disabled Screen does not get calls to update(float) and is not
	 * visible from its ScreenManager.
	 * 
	 * @param active
	 *            activate the Screen or not.
	 */
	public void setEnabled( boolean active )
	{
		enabled = active;
	}

	/**
	 * Called to update the Screen. This method will be called every render pass
	 * if the Screen is both attached and enabled.
	 * 
	 * @param tpf
	 *            Time since the last call to update(), in seconds.
	 */
	public abstract void update( float tpf );
}
