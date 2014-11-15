package com.cogitareforma.makhana.client.ui;

public class ScreenContext
{
	private static final float baseScalar = 0.0016f;
	private int height;
	private int width;

	// TODO: Write more helper methods! Use this class!

	public ScreenContext( int w, int h )
	{
		this.width = w;
		this.height = h;
	}

	public float getHeadingFontSize( )
	{
		return 0.064f * height;
	}

	/**
	 * @return the height
	 */
	public int getHeight( )
	{
		return height;
	}

	public float getLargeFontSize( )
	{
		return 0.04f * height;
	}

	public float getMediumFontSize( )
	{
		return 0.024f * height;
	}

	public float getScalar( )
	{
		return baseScalar;
	}

	public float getSmallFontSize( )
	{
		return 0.016f * height;
	}

	/**
	 * @return the width
	 */
	public int getWidth( )
	{
		return width;
	}
}
