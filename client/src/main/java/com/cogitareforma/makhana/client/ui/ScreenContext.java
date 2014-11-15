package com.cogitareforma.makhana.client.ui;

public class ScreenContext
{
	private static final float baseScalar = 0.0016f;

	// TODO: Write more helper methods! Use this class!

	public static float getHeadingFontSize( int height )
	{
		return height * 0.0512f;
	}

	public static float getHeightScalar( int height )
	{
		return height * baseScalar;
	}

	public static float getLargeFontSize( int height )
	{
		return height * 0.032f;
	}

	public static float getMediumFontSize( int height )
	{
		return height * 0.0256f;
	}

	public static float getSmallFontSize( int height )
	{
		return height * 0.0192f;
	}

	public static float getWidthScalar( int width )
	{
		return width * baseScalar;
	}
}
