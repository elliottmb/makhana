package com.cogitareforma.makhana.client.ui;

public class ScreenContext
{
	private static final float baseScalar = 0.0016f;

	private float heightScalar;

	private ScreenManager screenManager;
	private float widthScalar;

	public ScreenContext( ScreenManager screenManager )
	{
		this.screenManager = screenManager;

		this.heightScalar = 1024 * baseScalar;
		this.widthScalar = 768 * baseScalar;
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
		recalculateScalars( screenManager.getApp( ).getCamera( ).getHeight( ), screenManager.getApp( ).getCamera( ).getWidth( ) );
	}

	public void recalculateScalars( int height, int width )
	{
		this.heightScalar = height * baseScalar;
		this.widthScalar = width * baseScalar;
	}
}
