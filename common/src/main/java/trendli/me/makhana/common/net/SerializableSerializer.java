package trendli.me.makhana.common.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import com.jme3.network.serializing.Serializer;

public class SerializableSerializer extends Serializer
{

	@SuppressWarnings( "unchecked" )
	@Override
	public < T > T readObject( ByteBuffer data, Class< T > c ) throws IOException
	{
		// Read the null/non-null marker
		if ( data.get( ) == 0x0 )
			return null;

		byte[ ] byteArray = new byte[ data.remaining( ) ];

		data.get( byteArray );

		// Create a stream pathway from b to a
		ByteArrayInputStream b = new ByteArrayInputStream( byteArray );
		ObjectInput a = new extObjectInputStream( b );
		try
		{
			Object obj = a.readObject( );
			return ( T ) obj;
		}
		catch ( Exception e )
		{
			throw new IOException( e.toString( ) );
		}
		finally
		{
			a.close( );
		}

	}

	@Override
	public void writeObject( ByteBuffer buffer, Object object ) throws IOException
	{
		// Add the null/non-null marker
		buffer.put( ( byte ) ( object != null ? 0x1 : 0x0 ) );
		if ( object == null )
		{
			// Nothing left to do
			return;
		}

		if ( object instanceof Serializable )
		{
			// Create a stream pathway from a to b
			ByteArrayOutputStream b = new ByteArrayOutputStream( );
			ObjectOutput a = new ObjectOutputStream( b );
			byte[ ] content;
			try
			{
				// write and flush the object content to byte array
				a.writeObject( object );
				a.flush( );
				content = b.toByteArray( );
			}
			catch ( Exception e )
			{
				throw new IOException( e.toString( ) );
			}
			finally
			{
				a.close( );
			}

			buffer.put( content );

		}
	}

}

final class extObjectInputStream extends ObjectInputStream
{

	private static ClassLoader systemClassLoader = null;

	extObjectInputStream( InputStream in ) throws IOException, StreamCorruptedException
	{
		super( in );
	}

	protected Class< ? > resolveClass( ObjectStreamClass v ) throws IOException, ClassNotFoundException
	{

		try
		{
			/*
			 * Calling the super.resolveClass() first will let us pick up bug
			 * fixes in the super class (e.g., 4171142).
			 */
			return super.resolveClass( v );
		}
		catch ( ClassNotFoundException cnfe )
		{
			/*
			 * This is a workaround for bug 4224921.
			 */
			ClassLoader loader = Thread.currentThread( ).getContextClassLoader( );
			if ( loader == null )
			{
				if ( systemClassLoader == null )
				{
					systemClassLoader = ClassLoader.getSystemClassLoader( );
				}
				loader = systemClassLoader;
				if ( loader == null )
				{
					throw new ClassNotFoundException( v.getName( ) );
				}
			}

			return Class.forName( v.getName( ), false, loader );
		}
	}
}
