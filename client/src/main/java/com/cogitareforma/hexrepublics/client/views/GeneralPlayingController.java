package com.cogitareforma.hexrepublics.client.views;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;

public abstract class GeneralPlayingController extends GeneralController
{

	public void exitToNetwork( )
	{
		gotoScreen( "network", true, false, true, ( ) ->
		{
			preExitToNetwork( );
			return null;
		}, ( ) ->
		{
			getApp( ).getGameConnManager( ).close( );
			postExitToNetwork( );
			return null;
		} );
	}

	@Override
	public void initialize( AppStateManager stateManager, Application app )
	{
		super.initialize( stateManager, app );
	}

	abstract protected void postExitToNetwork( );

	abstract protected void preExitToNetwork( );

}
