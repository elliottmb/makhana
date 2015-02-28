package trendli.me.makhana.common.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ActionType
{
    MOVE( "Moving", "newTile" ), FABRICATING( "Fabricating" );

    private final String verb;
    private final List< String > dataKeys;

    private ActionType( String verb, String... dataKeys )
    {
        this.verb = verb;
        if ( dataKeys != null )
        {
            this.dataKeys = Arrays.asList( dataKeys );
        }
        else
        {
            this.dataKeys = Collections.emptyList( );
        }
    }

    /**
     * @return the dataKeys
     */
    public List< String > getDataKeys( )
    {
        return dataKeys;
    }

    /**
     * @return the verb
     */
    public String getVerb( )
    {
        return verb;
    }
}
