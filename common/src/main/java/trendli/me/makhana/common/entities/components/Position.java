package trendli.me.makhana.common.entities.components;

import trendli.me.makhana.common.data.Chunk;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.simsilica.es.EntityComponent;

@Serializable
public class Position implements EntityComponent
{
    private Quaternion facing;
    private Vector3f location;

    /**
     * Used by the serializer.
     */
    public Position( )
    {
    }

    public Position( Vector3f location, Quaternion facing )
    {
        this.location = location;
        this.facing = facing;
    }

    public Chunk getChunk( )
    {
        return new Chunk( ( int ) location.x / 100, ( int ) location.y / 100 );
    }

    public Quaternion getFacing( )
    {
        return facing;
    }

    public Vector3f getLocation( )
    {
        return location;
    }

    public int getQuadrant( )
    {
        float x = location.x / 100f;
        float y = location.y / 100f;
        if ( x != 0 || y != 0 )
        {
            if ( x >= 0 )
            {
                return y >= 0 ? 1 : 4;
            }
            else
            {
                return y >= 0 ? 2 : 3;
            }
        }
        return 0;
    }

    @Override
    public String toString( )
    {
        return "Position[" + getChunk( ) + ", " + getFacing( ) + ", " + getLocation( ) + "]";
    }
}