package com.cogitareforma.makhana.common.util;

import com.simsilica.es.ComponentFilter;

public interface ComponentFilterEventListener extends TraitEventListener
{
	public ComponentFilter< ? > getFilter( );
}
