package trendli.me.makhana.client.util;

import com.simsilica.es.EntityId;

public class EntityEntryModelClass
{
    private EntityId id;
    private String name;

    public EntityEntryModelClass( EntityId id, String name )
    {
        this.id = id;
        this.name = name;
    }

    public EntityId getEntityId( )
    {
        return this.id;
    }

    public String getName( )
    {
        return this.name;
    }

    public void setEntityId( EntityId id )
    {
        this.id = id;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public String toString( )
    {
        return this.name;
    }
}
