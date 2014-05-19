/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cogitareforma.hexrepublics.common.states;

import jme3tools.optimize.LodGenerator;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LodControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;

/**
 * 
 * @author Elliott Butler
 */
@Deprecated
public class ObjectState extends AbstractAppState
{

	private int sphereDirection = 1;
	private Node objectRoot = new Node( "objectRoot" );
	private SimpleApplication app;
	private ActionListener actionListener;

	// private TerrainState terrainState;

	@Override
	public void cleanup( )
	{
		super.cleanup( );
		this.app.getInputManager( ).removeListener( actionListener );
		this.app.getInputManager( ).deleteMapping( "sphereFlip" );
	}

	private void createTestObjects( )
	{
		Box b = new Box( 1, 1, 1 );
		Sphere s = new Sphere( 16, 16, 3 );
		Torus t = new Torus( 16, 16, 0.5f, 1f );

		Geometry boxGeom = new Geometry( "Box", b );
		Geometry sphereGeom = new Geometry( "Sphere", s );
		Geometry torusGeom = new Geometry( "Torus", t );

		Material mat = new Material( this.app.getAssetManager( ), "Common/MatDefs/Misc/ShowNormals.j3md" );

		Material mat2 = new Material( this.app.getAssetManager( ), // Create new
																	// material
																	// and...
				"Common/MatDefs/Light/Lighting.j3md" ); // ... specify .j3md
														// file to use
														// (illuminated).
		mat2.setBoolean( "UseMaterialColors", true ); // Set some parameters,
														// e.g. blue.
		mat2.setColor( "Ambient", ColorRGBA.Blue ); // ... color of this object
		mat2.setColor( "Diffuse", ColorRGBA.Red ); // ... color of light being
													// reflected

		LodGenerator lodGenerator = new LodGenerator( sphereGeom );
		lodGenerator.bakeLods( LodGenerator.TriangleReductionMethod.PROPORTIONAL, 0.35f, 0.85f );
		LodControl lc = new LodControl( );
		sphereGeom.addControl( lc );

		lodGenerator = new LodGenerator( torusGeom );
		lodGenerator.bakeLods( LodGenerator.TriangleReductionMethod.PROPORTIONAL, 0.35f, 0.85f );
		lc = new LodControl( );
		torusGeom.addControl( lc );

		boxGeom.setMaterial( mat );
		sphereGeom.setMaterial( mat2 );
		torusGeom.setMaterial( mat2 );

		/*
		 * if ( terrainState != null ) { boxGeom.setLocalTranslation( 0f,
		 * terrainState.getTerrain( ).getHeight( new Vector2f( 0f, 0f ) ), 0f );
		 * sphereGeom.setLocalTranslation( 2f, terrainState.getTerrain(
		 * ).getHeight( new Vector2f( 1f, 3f ) ), 6f );
		 * torusGeom.setLocalTranslation( 4f, terrainState.getTerrain(
		 * ).getHeight( new Vector2f( 4f, -2f ) ), -2f ); }
		 */

		boxGeom.setShadowMode( RenderQueue.ShadowMode.CastAndReceive );
		sphereGeom.setShadowMode( RenderQueue.ShadowMode.CastAndReceive );
		torusGeom.setShadowMode( RenderQueue.ShadowMode.CastAndReceive );

		objectRoot.attachChild( boxGeom );
		objectRoot.attachChild( sphereGeom );
		objectRoot.attachChild( torusGeom );

	}

	public Node getRoot( )
	{
		return objectRoot;
	}

	@Override
	public void initialize( AppStateManager stateManager, Application app )
	{
		super.initialize( stateManager, app );
		this.app = ( SimpleApplication ) app;

		// this.app.getStateManager( ).getState( IngameState.class ).getRoot(
		// ).attachChild( getRoot( ) );
		actionListener = new ActionListener( )
		{
			public void onAction( String name, boolean keyPressed, float tpf )
			{

				if ( name.equals( "sphereFlip" ) && !keyPressed )
				{
					sphereDirection = -1 * sphereDirection;
				}
			}
		};

		this.app.getInputManager( ).addListener( actionListener, "sphereFlip" );
		this.app.getInputManager( ).addMapping( "sphereFlip", new KeyTrigger( KeyInput.KEY_SPACE ) );

		// terrainState = this.app.getStateManager( ).getState(
		// TerrainState.class );

		createTestObjects( );

	}

	@Override
	public void postRender( )
	{
		super.postRender( );

	}

	@Override
	public void render( RenderManager rm )
	{
		super.render( rm );

	}

	@Override
	public void stateAttached( AppStateManager stateManager )
	{
		super.stateAttached( stateManager );

	}

	@Override
	public void stateDetached( AppStateManager stateManager )
	{
		super.stateDetached( stateManager );

	}

	@Override
	public void update( float tpf )
	{
		super.update( tpf );

		// Move the sphere away! YAY!
		Spatial sphere = getRoot( ).getChild( "Sphere" );
		if ( sphere != null )
		{
			Vector3f sphereLocation = sphere.getLocalTranslation( );
			float newY = sphereLocation.y;
			/*
			 * if ( terrainState != null ) { newY = terrainState.getTerrain(
			 * ).getHeight( new Vector2f( sphereLocation.x, sphereLocation.z ) )
			 * - sphereLocation.y; }
			 */
			sphere.move( 0.3f * tpf * sphereDirection, newY, 0.3f * tpf * sphereDirection );
		}
	}
}
