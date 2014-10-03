package com.cogitareforma.makhana.common.util;

import java.util.Set;

import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;

public interface TraitEventListener
{
	public void onAdded( EntityData entityData, Set< Entity > entities );

	public void onChanged( EntityData entityData, Set< Entity > entities );

	public void onRemoved( EntityData entityData, Set< Entity > entities );
}
