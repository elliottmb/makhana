package common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cogitareforma.hexrepublics.common.eventsystem.EntityEventManager;
import com.cogitareforma.hexrepublics.common.eventsystem.events.PlayerJoinEntityEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.events.PlayerLeaveEntityEvent;
import com.cogitareforma.hexrepublics.common.eventsystem.handlers.PlayerJoinEventHandler;

public class EntityEventSystemTester
{
	EntityEventManager eem;

	@Before
	public void setUp( ) throws Exception
	{
		eem = new EntityEventManager( );
		eem.addEventHandler( PlayerJoinEntityEvent.class, new PlayerJoinEventHandler( ) );
	}

	@After
	public void tearDown( ) throws Exception
	{
	}

	@Test
	public void test( )
	{
		assertTrue( eem.triggerEvent( new PlayerJoinEntityEvent( null, null ) ) );
	}

	@Test
	public void test2( )
	{
		assertFalse( eem.triggerEvent( new PlayerLeaveEntityEvent( null, null ) ) );
	}

}
