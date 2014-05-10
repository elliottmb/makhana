package com.cogitareforma.hexrepublics.client.states;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.client.views.LoadingViewController;
import com.cogitareforma.hexrepublics.common.entities.Traits;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.MoveableTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.TileTrait;
import com.cogitareforma.hexrepublics.common.util.WorldFactory;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.simsilica.es.CreatedBy;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

/**
 * 
 * @author Elliott Butler
 * 
 */
public class WorldManager extends AbstractAppState
{

	private final static Logger logger = Logger.getLogger( WorldManager.class.getName( ) );

	private Node rootNode;
	private Node worldRoot;
	private ClientMain app;
	private AssetManager assetManager;
	private Camera minicam;
	ViewPort miniview;
	private Node unitRoot;
	private Node buildingRoot;
	private HashMap< EntityId, Spatial > unitsHealth;
	private HashMap< EntityId, Spatial > units;
	private HashMap< EntityId, Spatial > buildings;
	private Material buildingMat;
	BitmapFont myFont;
	float updateLimiter = 0;
	EntitySet objectSet;
	Material mathb;

	public WorldManager( ClientMain app, Node rootNode )
	{
		this.app = app;
		this.rootNode = rootNode;
		this.assetManager = app.getAssetManager( );
		this.unitRoot = new Node( "unitRoot" );
		this.buildingRoot = new Node( "buildingRoot" );
		this.myFont = assetManager.loadFont( "Interface/Fonts/AngerthasMoria.fnt" );
		this.units = new LinkedHashMap< EntityId, Spatial >( );
		this.buildings = new LinkedHashMap< EntityId, Spatial >( );
		this.unitsHealth = new LinkedHashMap< EntityId, Spatial >( );
		this.buildingMat = new Material( assetManager, "Common/MatDefs/Misc/ShowNormals.j3md" );
		this.mathb = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
	}

	public void attachLevel( )
	{

		logger.log( Level.INFO, "Attach worldRoot" );
		rootNode.attachChild( worldRoot );

		this.app.getCamera( ).setLocation( new Vector3f( 0f, 100f, 100f ) );
		this.app.getCamera( ).lookAt( new Vector3f( 0, 0, 0 ), Vector3f.UNIT_Y );

		if ( minicam == null )
		{
			minicam = this.app.getCamera( ).clone( );
		}
		minicam.setViewPort( 0.75f, 1f, 0f, 0.25f );

		minicam.setParallelProjection( true );
		minicam.setFrustumPerspective( 10, ( float ) this.app.getContext( ).getSettings( ).getWidth( )
				/ this.app.getContext( ).getSettings( ).getHeight( ), 1, 600 );
		minicam.setLocation( new Vector3f( 0, 500f, 0 ) );
		minicam.lookAt( new Vector3f( 0, 0, -1f ), Vector3f.UNIT_Y );
		minicam.update( );

		if ( miniview == null )
		{
			miniview = this.app.getRenderManager( ).createMainView( "Minimap", minicam );
		}

		miniview.setBackgroundColor( ColorRGBA.DarkGray );
		miniview.setClearFlags( true, true, true );
		miniview.attachScene( worldRoot );

		unitRoot = new Node( "unitRoot" );
		buildingRoot = new Node( "buildingRoot" );
		units = new LinkedHashMap< EntityId, Spatial >( );
		buildings = new LinkedHashMap< EntityId, Spatial >( );
		unitsHealth = new LinkedHashMap< EntityId, Spatial >( );
		buildingMat = new Material( assetManager, "Common/MatDefs/Misc/ShowNormals.j3md" );
		mathb = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );

		worldRoot.attachChild( buildingRoot );
		worldRoot.attachChild( unitRoot );
	}

	public void closeLevel( )
	{
		logger.log( Level.INFO, "Closing level" );

		worldRoot.detachAllChildren( );
		unitRoot = null;
		buildingRoot = null;
		units = null;
		buildings = null;
		buildingMat = null;
		unitsHealth = null;
		mathb = null;

		miniview.detachScene( worldRoot );
		rootNode.detachChild( worldRoot );

		( ( DesktopAssetManager ) assetManager ).clearCache( );
	}

	/**
	 * get the world root node (not necessarily the application rootNode!)
	 * 
	 * @return The world root node
	 */
	public Node getWorldRoot( )
	{
		return worldRoot;
	}

	/**
	 * loads the specified level node
	 * 
	 * @param name
	 */
	public void loadLevel( String name )
	{
		LoadingViewController con = ( LoadingViewController ) this.app.getNifty( ).getScreen( "loading" ).getScreenController( );
		con.setLoading( 0.0f, "world" );
		logger.log( Level.INFO, "Load level with seed: " + name );
		worldRoot = new Node( "worldRoot" );
		con.setLoading( 0.25f, "terrain" );

		logger.log( Level.INFO, "Load terrain" );
		WorldFactory.attachTerrainToNode( app.getCamera( ), assetManager, worldRoot, ( byte ) name.charAt( 0 ) );
		con.setLoading( 0.5f, "lighting" );

		logger.log( Level.INFO, "Load lighting" );
		WorldFactory.attachLighting( worldRoot, app.getViewPort( ), assetManager );
		con.setLoading( 0.75f, "hexagons" );

		logger.log( Level.INFO, "Load hexagon grid" );
		WorldFactory.attachHexagonGridToNode( worldRoot, assetManager );
		con.setLoading( 1.0f, "Done" );
	}

	@Override
	public void update( float tpf )
	{
		if ( updateLimiter > 1 )
		{
			if ( app.getGameConnManager( ).isConnected( ) )
			{
				if ( worldRoot != null && unitRoot != null && buildingRoot != null )
				{
					TerrainQuad terrain = ( TerrainQuad ) worldRoot.getChild( "terrain" );

					if ( terrain != null )
					{
						EntityData entityData = app.getGameConnManager( ).getRemoteEntityData( );
						if ( entityData != null )
						{
							Set< EntityId > objects = entityData.findEntities( null, LocationTrait.class );

							// New!
							if ( objectSet != null )
							{
								if ( objectSet.applyChanges( ) )
								{
									System.out.println( "There were changes!" );

									for ( Entity e : objectSet.getAddedEntities( ) )
									{
										LocationTrait locationTrait = e.get( LocationTrait.class );
										EntityId id = e.getId( );
										System.out.println( id + ", " + locationTrait );
										if ( Traits.isUnit( entityData, id ) )
										{
											System.out.println( id + " is a unit!" );
											Entity entityComponents = entityData.getEntity( id,
													Traits.unitTraits.toArray( new Class< ? >[ Traits.unitTraits.size( ) ] ) );

											TileTrait loc = entityData.getComponent( locationTrait.getTile( ), TileTrait.class );
											CreatedBy cb = entityData.getComponent( locationTrait.getTile( ), CreatedBy.class );

											String display = "";
											float health = 0.0f;
											for ( EntityComponent ec : entityComponents.getComponents( ) )
											{
												if ( ec instanceof MoveableTrait )
												{
													MoveableTrait mt = ( MoveableTrait ) ec;
													display += mt.getClass( ).getSimpleName( ).charAt( 0 );
													health += mt.getInitialHealth( );
												}
											}

											Vector3f position = Traits.getSpatialPosition( loc.getX( ), loc.getY( ),
													locationTrait.getPosition( ), terrain, 0f );

											BitmapText bitmapText = new BitmapText( myFont );
											bitmapText.setQueueBucket( Bucket.Transparent );
											bitmapText.setSize( 5 );
											if ( cb != null )
											{
												int src = ( int ) cb.getCreatorId( ).getId( ) % 255;
												ColorRGBA color = null;
												switch ( src % 3 )
												{
													case 0:
													{
														color = new ColorRGBA( ( float ) ( src * 0.003922 ), 0.5f, 0.5f, 1f );
														break;
													}
													case 1:
													{
														color = new ColorRGBA( 0.5f, ( float ) ( src * 0.003922 ), 0.5f, 1f );
														break;
													}
													default:
													{
														color = new ColorRGBA( 0.5f, 0.5f, ( float ) ( src * 0.003922 ), 1f );
														break;
													}
												}
												bitmapText.setColor( color );
											}
											else
											{
												bitmapText.setColor( ColorRGBA.Gray );
											}
											bitmapText.setText( display );
											bitmapText.setLocalTranslation( position.x - ( bitmapText.getLineWidth( ) / 2 ), position.y,
													position.z );

											BillboardControl bill = new BillboardControl( );
											Geometry healthbar = new Geometry( "healthbar", new Quad( health / 10 * 4, 0.2f ) );
											mathb.setColor( "Color", ColorRGBA.Red );
											healthbar.setMaterial( mathb );
											healthbar.center( );
											healthbar.move( position.x - ( bitmapText.getLineWidth( ) / 2 ), position.y, position.z );
											healthbar.addControl( bill );

											unitsHealth.put( id, healthbar );
											units.put( id, bitmapText );
											unitRoot.attachChild( healthbar );
											unitRoot.attachChild( bitmapText );
										}
										else if ( Traits.isBuilding( entityData, id ) )
										{
											Entity entityComponents = entityData.getEntity( id,
													Traits.buildingTraits.toArray( new Class< ? >[ Traits.buildingTraits.size( ) ] ) );
											TileTrait loc = entityData.getComponent( locationTrait.getTile( ), TileTrait.class );
											CreatedBy cb = entityData.getComponent( locationTrait.getTile( ), CreatedBy.class );

											Box b = new Box( 1, 1, 1 );
											Geometry geo = new Geometry( "Box", b );
											geo.setMaterial( buildingMat );
											geo.setLocalTranslation( Traits.getSpatialPosition( loc.getX( ), loc.getY( ),
													locationTrait.getPosition( ), terrain, FastMath.PI / 6 ) );

											buildings.put( id, geo );
											buildingRoot.attachChild( geo );
										}
									}

									for ( Entity e : objectSet.getChangedEntities( ) )
									{
										EntityId id = e.getId( );
										if ( Traits.isUnit( entityData, id ) )
										{
											Spatial unitSpatial = units.get( id );
											Spatial unitHealthSpatial = unitsHealth.get( id );
											if ( unitSpatial != null )
											{
												LocationTrait locationTrait = entityData.getComponent( id, LocationTrait.class );
												TileTrait loc = entityData.getComponent( locationTrait.getTile( ), TileTrait.class );
												Vector3f position = Traits.getSpatialPosition( loc.getX( ), loc.getY( ),
														locationTrait.getPosition( ), terrain, 0f );
												if ( unitSpatial instanceof BitmapText )
												{
													unitSpatial.setLocalTranslation(
															position.x - ( ( ( BitmapText ) unitSpatial ).getLineWidth( ) / 2 ),
															position.y, position.z );
													if ( unitHealthSpatial != null )
													{
														unitHealthSpatial.setLocalTranslation(
																position.x - ( ( ( BitmapText ) unitSpatial ).getLineWidth( ) / 2 ),
																position.y, position.z );
													}
												}
												else
												{
													unitSpatial.setLocalTranslation( position );
													if ( unitHealthSpatial != null )
													{
														unitHealthSpatial.setLocalTranslation( position );
													}
												}
											}
										}
									}

									for ( Entity e : objectSet.getRemovedEntities( ) )
									{
										Spatial unitSpatial = units.remove( e.getId( ) );
										if ( unitSpatial != null )
										{
											unitRoot.detachChild( unitSpatial );
										}

										Spatial unitHealth = unitsHealth.remove( e.getId( ) );
										if ( unitHealth != null )
										{
											unitRoot.detachChild( unitHealth );
										}

										Spatial buildingSpatial = buildings.remove( e.getId( ) );
										if ( buildingSpatial != null )
										{
											buildingRoot.detachChild( buildingSpatial );
										}
									}
									System.out.println( "!!!" );
								}
							}
							else
							{
								objectSet = entityData.getEntities( LocationTrait.class );
							}
						}
					}
				}
			}
			updateLimiter = 0;
		}
		updateLimiter += tpf;
	}
}
