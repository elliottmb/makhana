package com.cogitareforma.makhana.client.ui;

import com.jme3.app.Application;
import com.jme3.renderer.Camera;

public class HudScreen extends Screen
{

	@Override
	public void cleanup( )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize( ScreenManager screenManager, Application app )
	{
		super.initialize( screenManager, app );

		Camera cam = app.getCamera( );
	}

	@Override
	public void reshape( int w, int h )
	{
		// TODO Auto-generated method stub
		super.reshape( w, h );
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
		// TODO Auto-generated method stub
	}
}
