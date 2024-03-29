package trendli.me.makhana.common.entities.components;

import trendli.me.makhana.common.entities.ComponentUtil;

import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

/**
 * 
 * @author Elliott Butler
 */
@Serializable
public abstract class TypeTrait implements EntityComponent
{

    /**
     * Represents the amount of time complexity ( in seconds ) this trait
     * contributes to the fabricating of this Entity. If not > 0, will be
     * ignored. Default value specified in Traits, override if added strength,
     * defense, or health should warrant specific build time other than the
     * default duration.
     * 
     * @return time complexity ( in seconds )
     */
    public float getFabricatingTime( )
    {
        return ComponentUtil.BASE_FABRICATING_TIME;
    }

    /**
     * Represents the amount of defense points this trait contributes when
     * constructing the DefenseTrait for this Entity. If not > 0, will be
     * ignored. Only override if intending to supply health points on creation.
     * 
     * @return amount of defense points to contribute
     */
    public float getInitialDefense( )
    {
        return 0.0f;
    }

    /**
     * Represents the amount of health points this trait contributes when
     * constructing the HealthTrait for this Entity. If not > 0, will be
     * ignored. Only override if intending to supply health points on creation.
     * 
     * @return amount of health points to contribute
     */
    public float getInitialHealth( )
    {
        return 0.0f;
    }

    /**
     * Represents the amount of strength points this trait contributes when
     * constructing the StrengthTrait for this Entity. If not > 0, will be
     * ignored. Only override if intending to supply health points on creation.
     * 
     * @return amount of strength points to contribute
     */
    public float getInitialStrength( )
    {
        return 0.0f;
    }
}
