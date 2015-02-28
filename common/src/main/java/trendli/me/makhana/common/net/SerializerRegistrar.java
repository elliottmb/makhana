package trendli.me.makhana.common.net;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import trendli.me.makhana.common.util.PackageUtils;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.FieldSerializer;
import com.simsilica.es.net.EntitySerializers;

/**
 * SerializerRegistrar is a helper class which automates the registration of
 * classes to the Serializer based on whole packages rather than individual
 * class names.
 * 
 * @author Justin Kaufman
 * @author Elliott Butler
 */
public class SerializerRegistrar
{

    /**
     * Registers all serializable packages.
     */
    public static void registerAllSerializable( )
    {
        try
        {
            // Load our Entity Components
            PackageUtils.getAllClassNamesInPackage( "trendli.me.makhana.common.entities.components" ).forEach( consumingSerializer );

            // Load our Data Types
            PackageUtils.getAllClassNamesInPackage( "trendli.me.makhana.common.data" ).forEach( consumingSerializer );

            // Load our Network Messages
            PackageUtils.getAllClassNamesInPackage( "trendli.me.makhana.common.net.msg" ).forEach( consumingSerializer );

            // Lazy Load Zay-ES Messages and Classes
            EntitySerializers.initialize( );

            // Load our Entity Filters
            Serializer fs = new FieldSerializer( );
            PackageUtils.getAllClassNamesInPackage( "trendli.me.makhana.common.entities.filters" ).forEach( ( String className ) ->
            {
                try
                {
                    logger.log( Level.FINEST, "Attempting to register class " + className + " with the Serializer" );
                    Serializer.registerClass( Class.forName( className ), fs );
                }
                catch ( Exception e )
                {
                    logger.log( Level.SEVERE, "Unable to register class " + className + " with the Serializer" );
                }
            } );

            Serializer.registerClass( Class.forName( "javax.crypto.SealedObject" ), new SerializableSerializer( ) );
        }
        catch ( Exception e )
        {
            logger.log( Level.SEVERE, "Unable to register classes to the Serializer.", e );
        }
    }

    /**
     * The logger for this class.
     */
    private final static Logger logger = Logger.getLogger( SerializerRegistrar.class.getName( ) );

    private static Consumer< ? super String > consumingSerializer = ( String className ) ->
    {
        try
        {
            logger.log( Level.FINEST, "Attempting to register class " + className + " with the Serializer" );
            Serializer.registerClass( Class.forName( className ) );
        }
        catch ( Exception e )
        {
            logger.log( Level.SEVERE, "Unable to register class " + className + " with the Serializer" );
        }
    };

}
