package com.cogitareforma.hexrepublics.client.states;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cogitareforma.hexrepublics.client.ClientMain;
import com.cogitareforma.hexrepublics.client.views.LoadingViewController;
import com.cogitareforma.hexrepublics.common.entities.Traits;
import com.cogitareforma.hexrepublics.common.entities.traits.ArcheryTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.CapitalTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.HealthTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.LocationTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.MoveableTrait;
import com.cogitareforma.hexrepublics.common.entities.traits.StablesTrait;
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
import com.jme3.math.Quaternion;
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
 * @author Ryan Grier
 * 
 */
public class WorldManager extends AbstractAppState
{

	private final static Logger logger = Logger.getLogger( WorldManager.class.getName( ) );

	private ClientMain app;
	private AssetManager assetManager;
	private Material matBuilding;
	private Node buildingRoot;
	private HashMap< EntityId, Spatial > buildings;
	private Material matHealthBar;
	private Camera miniCam;
	private ViewPort miniView;
	private BitmapFont myFont;
	private EntitySet locationSet;
	private EntitySet capitalSet;
	private EntitySet healthSet;
	private EntitySet tileSet;
	private Node rootNode;
	private Node unitRoot;
	private HashMap< EntityId, Node > units;
	private Node worldRoot;
	private HashMap< CreatedBy, Material > playerColors;

	private boolean attached;

	public WorldManager( ClientMain app, Node rootNode )
	{
		this.app = app;
		this.rootNode = rootNode;
		this.assetManager = app.getAssetManager( );
		this.unitRoot = new Node( "unitRoot" );
		this.buildingRoot = new Node( "buildingRoot" );
		this.myFont = assetManager.loadFont( "Interface/Fonts/AngerthasMoria.fnt" );
		this.units = new LinkedHashMap< EntityId, Node >( );
		this.buildings = new LinkedHashMap< EntityId, Spatial >( );
		this.matBuilding = new Material( assetManager, "Common/MatDefs/Misc/ShowNormals.j3md" );
		this.matHealthBar = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		this.playerColors = new LinkedHashMap<>( );
	}

	public void attachLevel( )
	{

		logger.log( Level.INFO, "Attach worldRoot" );
		rootNode.attachChild( worldRoot );

		Camera mainCam = app.getCamera( );
		mainCam.setLocation( new Vector3f( 0f, 300f, 300f ) );
		mainCam.lookAt( new Vector3f( 0, 0, 0 ), Vector3f.UNIT_Y );

		if ( miniCam == null )
		{
			miniCam = this.app.getCamera( ).clone( );
		}
		miniCam.setViewPort( 0.75f, 1f, 0f, 0.25f );

		miniCam.setParallelProjection( true );
		miniCam.setFrustumPerspective( 10, ( float ) this.app.getContext( ).getSettings( ).getWidth( )
				/ this.app.getContext( ).getSettings( ).getHeight( ), 1, 600 );
		miniCam.setLocation( new Vector3f( 0, 500f, 0 ) );
		miniCam.lookAt( new Vector3f( 0, 0, -1f ), Vector3f.UNIT_Y );
		miniCam.update( );

		if ( miniView == null )
		{
			miniView = this.app.getRenderManager( ).createMainView( "Minimap", miniCam );
		}

		miniView.setBackgroundColor( ColorRGBA.DarkGray );
		miniView.setClearFlags( true, true, true );
		miniView.attachScene( worldRoot );

		unitRoot = new Node( "unitRoot" );
		buildingRoot = new Node( "buildingRoot" );
		units = new LinkedHashMap< EntityId, Node >( );
		buildings = new LinkedHashMap< EntityId, Spatial >( );
		matBuilding = new Material( assetManager, "Common/MatDefs/Misc/ShowNormals.j3md" );
		matHealthBar = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
		playerColors = new LinkedHashMap<>( );

		worldRoot.attachChild( buildingRoot );
		worldRoot.attachChild( unitRoot );

		attached = true;
	}

	public void closeLevel( )
	{
		logger.log( Level.INFO, "Closing level" );

		worldRoot.detachAllChildren( );
		unitRoot = null;
		buildingRoot = null;
		units = null;
		buildings = null;
		matBuilding = null;
		matHealthBar = null;
		playerColors = null;
		if ( locationSet != null )
		{
			locationSet.clear( );
		}
		locationSet = null;

		if ( healthSet != null )
		{
			healthSet.clear( );
		}
		healthSet = null;

		if ( capitalSet != null )
		{
			capitalSet.clear( );
		}
		capitalSet = null;

		miniView.detachScene( worldRoot );
		rootNode.detachChild( worldRoot );

		( ( DesktopAssetManager ) assetManager ).clearCache( );

		attached = false;
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
	 * @param seed
	 */
	public void loadLevel( Byte seed )
	{
		LoadingViewController con = ( LoadingViewController ) this.app.getNifty( ).getScreen( "loading" ).getScreenController( );
		con.setLoading( 0.0f, "world" );
		logger.log( Level.INFO, "Load level with seed: " + seed );
		worldRoot = new Node( "worldRoot" );
		con.setLoading( 0.25f, "terrain" );

		logger.log( Level.INFO, "Load terrain" );
		WorldFactory.attachTerrainToNode( app.getCamera( ), assetManager, worldRoot, seed.byteValue( ) );
		con.setLoading( 0.5f, "lighting" );

		logger.log( Level.INFO, "Load lighting" );
		WorldFactory.attachLighting( worldRoot, app.getViewPort( ), assetManager );
		con.setLoading( 0.75f, "hexagons" );

		logger.log( Level.INFO, "Load hexagon grid" );
		WorldFactory.attachHexagonGridToNode( worldRoot, assetManager );
		con.setLoading( 1.0f, "Done" );
	}

	public void createBuilding( EntityData entityData, EntityId id, TerrainQuad terrain, LocationTrait locationTrait, TileTrait tile,
			CreatedBy createdBy )
	{

		if ( entityData.getComponent( id, StablesTrait.class ) != null )
		{
			Spatial build = assetManager.loadModel( "Materials/stablePrototype.obj" );

			build.setMaterial( matBuilding );
			// build.setLocalScale( .5f );
			build.setLocalRotation( new Quaternion( ).fromAngleAxis( 1.04719755f + ( locationTrait.getPosition( ) * 0.523598776f ),
					Vector3f.UNIT_Y ) );
			build.setLocalTranslation( Traits.getSpatialPosition( tile.getX( ), tile.getY( ), locationTrait.getPosition( ), terrain,
					FastMath.PI / 6, .25f ) );

			buildings.put( id, build );
			buildingRoot.attachChild( build );
		}
		else if ( entityData.getComponent( id, ArcheryTrait.class ) != null )
		{
			Spatial build = assetManager.loadModel( "Materials/archery.obj" );

			build.setMaterial( matBuilding );
			// build.setLocalScale( .4f );
			build.setLocalRotation( new Quaternion( ).fromAngleAxis( 1.04719755f + ( locationTrait.getPosition( ) * 0.523598776f ),
					Vector3f.UNIT_Y ) );
			build.setLocalTranslation( Traits.getSpatialPosition( tile.getX( ), tile.getY( ), locationTrait.getPosition( ), terrain,
					FastMath.PI / 6, .25f ) );

			buildings.put( id, build );
			buildingRoot.attachChild( build );
		}
		else
		{

			Box b = new Box( 1, 1, 1 );
			Geometry geo = new Geometry( "Box", b );
			geo.setMaterial( matBuilding );
			geo.setLocalTranslation( Traits.getSpatialPosition( tile.getX( ), tile.getY( ), locationTrait.getPosition( ), terrain,
					FastMath.PI / 6, 1.5f ) );

			buildings.put( id, geo );
			buildingRoot.attachChild( geo );
		}
	}

	public ColorRGBA generatePlayerColor( CreatedBy createdBy )
	{
		ColorRGBA color = null;
		if ( createdBy != null )
		{
			int src = ( int ) createdBy.getCreatorId( ).getId( ) % 255;
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
		}
		else
		{
			color = ColorRGBA.Gray;
		}
		return color;
	}

	@Override
	public void update( float tpf )
	{
		if ( app.getGameConnManager( ).isConnected( ) )
		{
			if ( worldRoot != null && unitRoot != null && buildingRoot != null && attached )
			{
				TerrainQuad terrain = ( TerrainQuad ) worldRoot.getChild( "terrain" );

				if ( terrain != null )
				{
					EntityData entityData = app.getGameConnManager( ).getRemoteEntityData( );
					if ( entityData != null )
					{

						if ( locationSet != null )
						{
							if ( locationSet.applyChanges( ) )
							{
								System.out.println( "There were changes: " + locationSet.size( ) );

								for ( Entity e : locationSet.getAddedEntities( ) )
								{
									LocationTrait locationTrait = e.get( LocationTrait.class );
									EntityId id = e.getId( );
									System.out.println( id + ", " + locationTrait );

									TileTrait tileTrait = entityData.getComponent( locationTrait.getTile( ), TileTrait.class );
									CreatedBy createdBy = entityData.getComponent( locationTrait.getTile( ), CreatedBy.class );

									if ( Traits.isUnit( entityData, id ) )
									{
										System.out.println( id + " is a unit!" );

										String display = "";
										float health = 0.0f;
										for ( Class< ? extends EntityComponent > ec : Traits.unitTraits )
										{
											MoveableTrait mt = ( MoveableTrait ) entityData.getComponent( id, ec );
											if ( mt != null )
											{
												display += mt.getClass( ).getSimpleName( ).charAt( 0 );
												health += mt.getInitialHealth( );
											}
										}

										Vector3f position = Traits.getSpatialPosition( tileTrait.getX( ), tileTrait.getY( ),
												locationTrait.getPosition( ), terrain, 0f, 3f );

										BitmapText unitBody = new BitmapText( myFont );
										unitBody.setQueueBucket( Bucket.Transparent );
										unitBody.setSize( 5 );
										ColorRGBA color = generatePlayerColor( createdBy );
										unitBody.setColor( color );
										unitBody.setText( display );
										unitBody.setName( "body" );

										BillboardControl billboardControl = new BillboardControl( );
										Geometry healthBar = new Geometry( "health", new Quad( 4, 0.2f ) );
										matHealthBar.setColor( "Color", ColorRGBA.Red );
										healthBar.setMaterial( matHealthBar );

										healthBar.addControl( billboardControl );
										healthBar.setUserData( "initialHealth", health );

										Node unitContainer = new Node( id.toString( ) );
										unitContainer.setLocalTranslation( position.x - ( unitBody.getLineWidth( ) / 2 ), position.y,
												position.z );

										unitContainer.attachChild( unitBody );
										unitContainer.attachChild( healthBar );

										units.put( id, unitContainer );
										unitRoot.attachChild( unitContainer );
									}
									else if ( Traits.isBuilding( entityData, id ) )
									{
										createBuilding( entityData, id, terrain, locationTrait, tileTrait, createdBy );
									}
								}

								for ( Entity e : locationSet.getChangedEntities( ) )
								{
									EntityId id = e.getId( );
									if ( Traits.isUnit( entityData, id ) )
									{
										Node unitContainer = units.get( id );
										if ( unitContainer != null )
										{
											LocationTrait locationTrait = entityData.getComponent( id, LocationTrait.class );
											TileTrait loc = entityData.getComponent( locationTrait.getTile( ), TileTrait.class );
											Vector3f position = Traits.getSpatialPosition( loc.getX( ), loc.getY( ),
													locationTrait.getPosition( ), terrain, 0f, 3f );
											Spatial unitSpatial = unitContainer.getChild( "body" );
											if ( unitSpatial != null && unitSpatial instanceof BitmapText )
											{
												unitContainer.setLocalTranslation(
														position.x - ( ( ( BitmapText ) unitSpatial ).getLineWidth( ) / 2 ), position.y,
														position.z );
											}
											else
											{
												unitContainer.setLocalTranslation( position );
											}
										}
									}
								}

								for ( Entity e : locationSet.getRemovedEntities( ) )
								{
									Spatial unitContainer = units.remove( e.getId( ) );
									if ( unitContainer != null )
									{
										unitRoot.detachChild( unitContainer );
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
							locationSet = entityData.getEntities( LocationTrait.class );
						}

						if ( capitalSet != null )
						{
							if ( capitalSet.applyChanges( ) )
							{
								for ( Entity e : capitalSet.getAddedEntities( ) )
								{
									EntityId id = e.getId( );
									TileTrait tileTrait = entityData.getComponent( id, TileTrait.class );

									if ( tileTrait != null )
									{

										Box b = new Box( 1, 5, 1 );
										Geometry geo = new Geometry( "Box", b );
										geo.setMaterial( matBuilding );
										Vector3f centerPoint = WorldFactory.createCenterPoint( 1025, 40f, tileTrait.getX( ) + 1,
												tileTrait.getY( ) + 1 );
										geo.setLocalTranslation( centerPoint );

										buildings.put( id, geo );
										buildingRoot.attachChild( geo );
									}
								}

								for ( Entity e : capitalSet.getRemovedEntities( ) )
								{
									Spatial capitalSpatial = buildings.remove( e.getId( ) );
									if ( capitalSpatial != null )
									{
										buildingRoot.detachChild( capitalSpatial );
									}
								}
							}
						}
						else
						{
							capitalSet = entityData.getEntities( CapitalTrait.class );
						}

						if ( tileSet != null )
						{
							if ( tileSet.applyChanges( ) )
							{
								for ( Entity e : tileSet.getAddedEntities( ) )
								{
									EntityId id = e.getId( );
									TileTrait tileTrait = entityData.getComponent( id, TileTrait.class );
									CreatedBy createdBy = entityData.getComponent( id, CreatedBy.class );

									if ( tileTrait != null && entityData.getComponent( id, CapitalTrait.class ) == null )
									{
										Box b = new Box( .25f, 10, .25f );
										Geometry geo = new Geometry( "Box", b );
										if ( !playerColors.containsKey( createdBy ) )
										{
											Material flag = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
											flag.setColor( "Color", generatePlayerColor( createdBy ) );
											playerColors.put( createdBy, flag );
										}
										geo.setMaterial( playerColors.get( createdBy ) );
										Vector3f centerPoint = WorldFactory.createCenterPoint( 1025, 40f, tileTrait.getX( ) + 1,
												tileTrait.getY( ) + 1 );
										geo.setLocalTranslation( centerPoint );

										buildings.put( id, geo );
										buildingRoot.attachChild( geo );
									}

								}
								for ( Entity e : tileSet.getChangedEntities( ) )
								{
									EntityId id = e.getId( );
									CreatedBy createdBy = entityData.getComponent( id, CreatedBy.class );

									Geometry geo = ( Geometry ) buildings.get( id );
									if ( geo != null )
									{
										if ( !playerColors.containsKey( createdBy ) )
										{
											Material flag = new Material( assetManager, "Common/MatDefs/Misc/Unshaded.j3md" );
											flag.setColor( "Color", generatePlayerColor( createdBy ) );
											playerColors.put( createdBy, flag );
										}
										geo.setMaterial( playerColors.get( createdBy ) );
									}

								}
								for ( Entity e : tileSet.getRemovedEntities( ) )
								{
									Spatial tileOwnedSpatial = buildings.remove( e.getId( ) );
									if ( tileOwnedSpatial != null )
									{
										buildingRoot.detachChild( tileOwnedSpatial );
									}
								}
							}
						}
						else
						{
							tileSet = entityData.getEntities( CreatedBy.class, TileTrait.class );
						}

						if ( healthSet != null )
						{
							if ( healthSet.applyChanges( ) )
							{
								for ( Entity e : healthSet.getChangedEntities( ) )
								{
									EntityId id = e.getId( );
									if ( Traits.isUnit( entityData, id ) )
									{
										Node unitContainer = units.get( id );
										if ( unitContainer != null )
										{
											HealthTrait ht = e.get( HealthTrait.class );
											if ( ht != null )
											{
												Spatial healthBar = unitContainer.getChild( "health" );
												if ( healthBar != null )
												{
													Vector3f currentScale = healthBar.getLocalScale( );
													Float initialHealth = healthBar.getUserData( "initialHealth" );
													if ( initialHealth != null )
													{
														healthBar.setLocalScale( ( ht.getHealth( ) / initialHealth ), currentScale.y,
																currentScale.z );
													}
												}
											}
										}
									}
								}
							}
						}
						else
						{
							healthSet = entityData.getEntities( HealthTrait.class );
						}
					}
				}
			}
		}

	}
}
