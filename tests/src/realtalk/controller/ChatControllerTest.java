package realtalk.controller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import realtalk.util.ChatRoomInfo;
import realtalk.util.UserInfo;

import android.test.AndroidTestCase;

/**
 * White box tests for ChatController.
 * Not much can be done without using real user and room data,
 * so these tests are not comprehensive.
 * 
 * @author Jory Rice 
 */
public class ChatControllerTest extends AndroidTestCase {

    private static final int TIMEOUT = 10000;
    private ChatController chatcontroller;
    
    // Test the class using a user that doesn't exist.
    // This won't be altering the db.
    private UserInfo userinfo;
    private String stName = "TestNonexistantUser";
    private String stPassword = "TestNonexistantPassword";
    private String stId = "TestNonexistantId";
    private ChatRoomInfo chatroominfo;
    private String stRoomName = "TestNonexistantRoomname";
    private String stRoomDesc = "TestNonexistantRoomdesc";
    private String stRoomId = "TestNonexistantRoomid";
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		chatcontroller = ChatController.getInstance();
		userinfo = new UserInfo(stName, stPassword, stId);
		chatcontroller.fInitialize(userinfo);
		chatroominfo = new ChatRoomInfo(stRoomName, stRoomId, stRoomDesc, 0, 0, stName, 0, 0);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		chatcontroller.uninitialize();
	}

	@Test(timeout = TIMEOUT)
	public void testGetInstance() {
		assertTrue(chatcontroller != null);
		assertTrue(chatcontroller.equals(ChatController.getInstance()));
	}

	@Test(timeout = TIMEOUT)
	public void testRefresh() {
		assertTrue(chatcontroller.fRefresh());
	}
	
	@Test(timeout = TIMEOUT)
	public void testGetUser() {
		assertEquals(chatcontroller.getUser(), userinfo);
	}

	@Test(timeout = TIMEOUT)
	public void testIsAlreadyJoined() {
		assertFalse(chatcontroller.fIsAlreadyJoined(chatroominfo));
	}

}
