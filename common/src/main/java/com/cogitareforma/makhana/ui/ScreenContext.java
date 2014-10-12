package com.cogitareforma.makhana.ui;

public class ScreenContext
{
	private static final float baseScalar = 0.0016f;

	private float heightScalar;

	private ScreenManager screenManager;
	private float widthScalar;

	public ScreenContext( ScreenManager screenManager )
	{
		this.screenManager = screenManager;
		this.heightScalar = screenManager.getApp( ).getCamera( ).getHeight( ) * baseScalar;
		this.widthScalar = screenManager.getApp( ).getCamera( ).getWidth( ) * baseScalar;
	}

	// TODO: Write more helper methods! Use this class!

	public float getHeadingFontSize( )
	{
		return getHeightScalar( ) * 32;
	}

	public float getHeightScalar( )
	{
		return heightScalar;
	}

	public float getLargeFontSize( )
	{
		return getHeightScalar( ) * 20;
	}

	public float getMediumFontSize( )
	{
		return getHeightScalar( ) * 16;
	}

	public float getSmallFontSize( )
	{
		return getHeightScalar( ) * 12;
	}

	public float getWidthScalar( )
	{
		return widthScalar;
	}

	public void recalculateScalars( )
	{
		this.heightScalar = screenManager.getApp( ).getCamera( ).getHeight( ) * baseScalar;
		this.widthScalar = screenManager.getApp( ).getCamera( ).getWidth( ) * baseScalar;
	}
}
