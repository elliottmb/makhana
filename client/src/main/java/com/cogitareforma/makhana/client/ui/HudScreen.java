package com.cogitareforma.makhana.client.ui;

import com.cogitareforma.makhana.common.ui.Screen;
import com.cogitareforma.makhana.common.ui.ScreenContext;
import com.cogitareforma.makhana.common.ui.ScreenManager;
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

		Camera cam = screenManager.getApp( ).getCamera( );
		ScreenContext sc = screenManager.getScreenContext( );
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
