package com.cogitareforma.makhana.client.util;

import java.util.Set;

import com.cogitareforma.makhana.common.util.TraitEventListener;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;

public class PlayerTraitListener implements TraitEventListener
{

	@Override
	public void onAdded( EntityData entityData, Set< Entity > entities )
	{
		// TODO Auto-generated method stub
		for ( Entity e : entities )
		{
			System.out.println( "PlayerEntityListener found new " + e.getId( ) );
		}

	}

	@Override
	public void onChanged( EntityData entityData, Set< Entity > entities )
	{
		// TODO Auto-generated method stub
		for ( Entity e : entities )
		{
			System.out.println( "PlayerEntityListener found changed " + e.getId( ) );
		}
	}

	@Override
	public void onRemoved( EntityData entityData, Set< Entity > entities )
	{
		// TODO Auto-generated method stub
		for ( Entity e : entities )
		{
			System.out.println( "PlayerEntityListener found removed " + e.getId( ) );
		}
	}

}
