package realtalk.controller;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import realtalk.util.ChatRoomInfo;
import realtalk.util.ChatRoomResultSet;
import realtalk.util.IChatManager;
import realtalk.util.MessageInfo;
import realtalk.util.PullMessageResultSet;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;

/**
 * Mock Testing Component for Integration Testing.
 * White box tests for ChatController.
 * 
 * This includes Mock Testing whereby the Chat Manager is a mock object set in the
 * Chat Controller using EasyMock.
 * 
 * This is done so that the Chat Controller is tested in isolation of the server.
 * 
 * @author Jory Rice And Colin Kho
 */
public class ChatControllerTest extends TestCase {
    private static final int TIMEOUT = 10000;
    
    // predefined variable for anonymous login.
    private static final boolean ANON_DEFAULT = false;
    private ChatController chatcontroller;
    
    // Test the class using a user that doesn't exist.
    // This won't be altering the db.
    
    /*
     * Test Users
     */
    private UserInfo userinfo1;
    private String stName1 = "tester";
    private String stPassword1 = "tester";
    private String stId1 = "asdfghjkl";
    
    private UserInfo userinfo2;
    private String stName2 = "jory";
    private String stPassword2 = "jory";
    private String stId2 = "123345678";
    
    private UserInfo userinfo3;
    private String stName3 = "taylor";
    private String stPassword3 = "twswds";
    private String stId3 = "09877654";
    
    /*
     * Test Rooms
     */
    
    private ChatRoomInfo chatroominfo1;
    private static final String stRoomName1 = "TestNonexistantRoomname";
    private static final String stRoomDesc1 = "TestNonexistantRoomdesc";
    private static final String stRoomId1 = "TestNonexistantRoomid";
    
    private ChatRoomInfo chatroominfo2;
    private static final String stRoomName2 = "Colin";
    private static final String stRoomDesc2 = "Colins room";
    private static final String stRoomId2 = "1232121312321321313";
    
    private ChatRoomInfo chatroominfo3;
    private static final String stRoomName3 = "brandons room";
    private static final String stRoomDesc3 = "brandons room";
    private static final String stRoomId3 = "1232121321";
    private IChatManager mockChatManager;
    
    // Nearby Predefined Rooms
    private List<ChatRoomInfo> rgNearbyRooms = new ArrayList<ChatRoomInfo>();
    
    // ChatRoomResultSet Default
    private ChatRoomResultSet crrsDefault;
    
    // Predefined Result Sets for Mock
    private RequestResultSet rrsMockSuccess = new RequestResultSet(true, "", "");
    
    // Test Messages
    private MessageInfo mi1;
    private MessageInfo mi2;
    private MessageInfo mi3;
    private MessageInfo mi4;
    private MessageInfo mi5;
    private MessageInfo mi6;
    
    public static final String body1 = "Message 1";
    public static final String body2 = "Message 2";
    public static final String body3 = "Message 3";
    public static final String body4 = "Message 4";
    public static final String body5 = "Message 5";
    public static final String body6 = "Message 6";
    
    public static final String sender1 = "TestUser1";
    public static final String sender2 = "TestUser2";
    public static final String sender3 = "TestUser3";
    public static final String sender4 = "TestUser4";
    public static final String sender5 = "TestUser5";
    public static final String sender6 = "TestUser6";
    
    public static final long timestamp1 = 10000;
    public static final long timestamp2 = 100000;
    public static final long timestamp3 = 1000000;
    public static final long timestamp4 = 10000000;
    public static final long timestamp5 = 100000000;
    public static final long timestamp6 = 1000000000;
    
    // Message Lists
    private List<MessageInfo> mlist1 = new ArrayList<MessageInfo>();
    private List<MessageInfo> mlist2 = new ArrayList<MessageInfo>();
    private List<MessageInfo> mlist3 = new ArrayList<MessageInfo>();
    
    // Pull Message Result Sets
    private PullMessageResultSet pmrs1;
    private PullMessageResultSet pmrs2;
    private PullMessageResultSet pmrs3;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		chatcontroller = ChatController.getInstance();
		chatcontroller.setDebugMode(true);
		// Initialize Test Users
		userinfo1 = new UserInfo(stName1, stPassword1, stId1);
		userinfo2 = new UserInfo(stName2, stPassword2, stId2);
		userinfo3 = new UserInfo(stName3, stPassword3, stId3);
		
		// Initialize Test ChatRoomInfos
		chatroominfo1 = new ChatRoomInfo(stRoomName1, stRoomId1, stRoomDesc1, 0, 0, stName1, 0, 0);
		chatroominfo2 = new ChatRoomInfo(stRoomName2, stRoomId2, stRoomDesc2, 0, 0, stName2, 0, 0);
		chatroominfo3 = new ChatRoomInfo(stRoomName3, stRoomId3, stRoomDesc3, 0, 0, stName3, 0, 0);
		
		// Initialize Test Messages
		mi1 = new MessageInfo(body1, sender1, timestamp1);
	    mi2 = new MessageInfo(body2, sender2, timestamp2);
	    mi3 = new MessageInfo(body3, sender3, timestamp3);
	    mi4 = new MessageInfo(body3, sender4, timestamp4);
	    mi5 = new MessageInfo(body3, sender5, timestamp5);
	    mi6 = new MessageInfo(body3, sender6, timestamp6);
	    
	    // Initialize Test Message Lists
	    mlist1.add(mi1);
	    mlist1.add(mi2);
	    mlist2.add(mi3);
	    mlist2.add(mi4);
	    mlist3.add(mi5);
	    mlist3.add(mi6);
	    
	    // Create Pull Message Result Sets
	    pmrs1 = new PullMessageResultSet(true, mlist1, "", "");
	    pmrs2 = new PullMessageResultSet(true, mlist2, "", "");
	    pmrs3 = new PullMessageResultSet(true, mlist3, "", "");
	    
	    // Create Mock Chat Manager.
	    // By default it has these properties:
	    // 1. It returns success when getting nearby rooms and indicates that chatroominfo1 is nearby.
	    // 2. It returns success when getting chat logs for chatroominfo1, 2 and 3.
		mockChatManager = EasyMock.createNiceMock(IChatManager.class);
		rgNearbyRooms.add(chatroominfo1);
		crrsDefault = new ChatRoomResultSet(true, rgNearbyRooms,  "", "");
		EasyMock.expect(mockChatManager.crrsUsersChatrooms(userinfo1)).andReturn(crrsDefault);
		EasyMock.expect(mockChatManager.pmrsChatLogGet(chatroominfo1)).andReturn(pmrs1);
		EasyMock.expect(mockChatManager.pmrsChatLogGet(chatroominfo2)).andReturn(pmrs2);
		EasyMock.expect(mockChatManager.pmrsChatLogGet(chatroominfo3)).andReturn(pmrs3);		
	}
	
	/*
	 * This method has to be called after the mock is set up. This initializes the chatcontroller after calling
	 * replay on the mock chat manager.
	 * 
	 * As different tests require different expected values from the mock. Those values should be set first with
	 * EasyMock.expect. After all that is done, this method MUST be called to initialize the test controller.
	 */
	private void replayAndInitializeDefaultUser() {
		EasyMock.replay(mockChatManager);
		chatcontroller.setChatManager(mockChatManager);
		chatcontroller.fInitialize(userinfo1);
	}
	
	/*
	 * Similar to the above method but instead initializes using a custom user
	 */
	private void replayAndInitializeCustomUser(UserInfo userinfo) {
	    EasyMock.replay(mockChatManager);
        chatcontroller.setChatManager(mockChatManager);
        chatcontroller.fInitialize(userinfo);
	}
	
	/**
	 * Tears down the chatcontroller to be re-setup
	 */
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		chatcontroller.uninitialize();
	}

	/**
	 * Simple test that gets the instance of the controller. Should never be null.
	 */
	@Test(timeout = TIMEOUT)
	public void testGetInstance() {
		assertTrue(chatcontroller != null);
	}
	
	/**
	 * After initialization, the user should already be set.
	 */
	@Test(timeout = TIMEOUT)
	public void testGetUser() {
		replayAndInitializeDefaultUser();
		assertEquals(chatcontroller.getUser(), userinfo1);
	}
	
	/**
	 * Short Test testing the debug method for initializing without a need to refresh rooms
	 *
	 * Testing this for the chatcontroller stub.
	 */
	@Test(timeout = TIMEOUT)
	public void testInitializeTestMethod() {
	    chatcontroller.fInitializeTest(userinfo1);
	    assertFalse(chatcontroller.getUser().equals(userinfo2));
	}
	
	/**
	 * Test that refresh returns a failure when server returns a failure.
	 * 
	 * The server here is mocked using a mock chat manager that returns a failure result set.
	 *
	 */
	@Test(timeout = TIMEOUT)
	public void testRefreshWithFailureOnGetUsers() {
	    ChatRoomResultSet crrsFailure = new ChatRoomResultSet(false, new ArrayList<ChatRoomInfo>(),  "", "");
	    EasyMock.expect(mockChatManager.crrsUsersChatrooms(userinfo3)).andReturn(crrsFailure);
	    replayAndInitializeCustomUser(userinfo3);
	    assertFalse(chatcontroller.fRefresh());
	}
	
	/**
	 * Test that refresh fails because it failed to retrieve chat logs.
	 * 
	 * The server here is mocked using a mock chat manager that returns a failure result set.
	 */
	@Test(timeout = TIMEOUT)
	public void testRefreshWithFailureOnGetChatLogs() {
	    // Recreate a custom room that has no default chat log result set returning true
	    // instead its result set returns false.
	    ChatRoomInfo cri4 = new ChatRoomInfo("Test4", "ITest", "Test test", 0, 0, "yestest", 0, 0);
	    rgNearbyRooms.add(cri4);
	    ChatRoomResultSet crrsSuccess = new ChatRoomResultSet(true, rgNearbyRooms,  "", "");
	    EasyMock.expect(mockChatManager.crrsUsersChatrooms(userinfo3)).andReturn(crrsSuccess);	    
	    
	    // Since cri4 is not a default chat room set up in set-up, it should not return chat logs
	    PullMessageResultSet pmrs4 = new PullMessageResultSet(false, "", "");
	    EasyMock.expect(mockChatManager.pmrsChatLogGet(cri4)).andReturn(pmrs4);
	    replayAndInitializeCustomUser(userinfo3);
	    assertEquals(chatcontroller.getUser(), userinfo3);
	    assertFalse(chatcontroller.fRefresh());
	}
	
	/**
	 * Tests if already joined.
	 */
	@Test(timeout = TIMEOUT)
	public void testIsAlreadyJoined() {
		replayAndInitializeDefaultUser();
		assertTrue(chatcontroller.fIsAlreadyJoined(chatroominfo1));
	}
	
	/**
	 * Tests if not joined.
	 */
	@Test(timeout = TIMEOUT)
	public void testIsNotAlreadyJoined() {
	    replayAndInitializeDefaultUser();
	    assertTrue(!chatcontroller.fIsAlreadyJoined(chatroominfo3));
	}
	
	@Test(timeout = TIMEOUT)
	public void testJoinRoomUsingColinRoom() {
		testSuccessJoinRoom(chatroominfo2);
	}
	
	@Test(timeout = TIMEOUT)
	public void testJoinRoomUsingBrandonRoom() {
		testSuccessJoinRoom(chatroominfo3);
	}
	
	/**
	 * Tests joining a room on the chatcontroller and later on checking that if we are in that room.
	 * 
	 * This sets up the mock chatmanager before hand to tell the controller that joining was successful.
	 * 
	 * @param chatroominfo
	 */
	private void testSuccessJoinRoom(ChatRoomInfo chatroominfo) {
		EasyMock.expect(mockChatManager.rrsJoinRoom(userinfo1, chatroominfo, ANON_DEFAULT)).andReturn(rrsMockSuccess);
		replayAndInitializeDefaultUser();
		chatcontroller.joinRoom(chatroominfo, false);
		assertTrue(chatcontroller.fIsAlreadyJoined(chatroominfo));
	}
	
	@Test(timeout = TIMEOUT)
	public void testLeaveRoomColin() {
		testLeaveRoom(chatroominfo2);
	}
	
	@Test(timeout = TIMEOUT)
	public void testLeaveRoomBrandon() {
		testLeaveRoom(chatroominfo3);
	}
	
	/**
	 * Tests leaving a room that is already joined on chatcontroller.
	 * 
	 * This sets up the mock chat manager to return that leaving was successful. As this is a white box test,
	 * we know that the leave method checks the internal map storing the chatroom when it deletes. So we test that
	 * while mocking chat manager behaviour.
	 * 
	 * @param chatroominfo
	 */
	private void testLeaveRoom(ChatRoomInfo chatroominfo) {
		EasyMock.expect(mockChatManager.rrsJoinRoom(userinfo1, chatroominfo, ANON_DEFAULT)).andReturn(rrsMockSuccess);
		EasyMock.expect(mockChatManager.rrsLeaveRoom(userinfo1, chatroominfo)).andReturn(rrsMockSuccess);
		replayAndInitializeDefaultUser();
		chatcontroller.joinRoom(chatroominfo, false);
		assertTrue(chatcontroller.leaveRoom(chatroominfo));
	}
	
	@Test(timeout = TIMEOUT)
	public void testGetRoomsOneRoom() {
		List<ChatRoomInfo> rgcri = new ArrayList<ChatRoomInfo>();
		rgcri.add(chatroominfo1);
		replayAndInitializeDefaultUser();
		testGetControllerRooms(rgcri);
	}
	
	@Test(timeout = TIMEOUT)
	public void testGetRoomsTwoRooms() {
		List<ChatRoomInfo> rgcri = new ArrayList<ChatRoomInfo>();
		EasyMock.expect(mockChatManager.rrsJoinRoom(userinfo1, chatroominfo2, ANON_DEFAULT)).andReturn(rrsMockSuccess);
		replayAndInitializeDefaultUser();
		chatcontroller.joinRoom(chatroominfo2, false);
		rgcri.add(chatroominfo1);
		rgcri.add(chatroominfo2);
		testGetControllerRooms(rgcri);
	}
	
	/**
	 * Tests getting chat rooms in the controller.
	 * 
	 * Uses the mock chatmanager to simulate what the server returns for the rooms joined for the default user.
	 * 
	 * @param rgroomsToCheckAgainst rooms that the controller is supposed to have.
	 * 
	 */
	private void testGetControllerRooms(List<ChatRoomInfo> rgroomsToCheckAgainst) {
		List<ChatRoomInfo> rgcri = chatcontroller.getChatRooms();
		for (ChatRoomInfo cri : rgcri) {
			assertTrue(rgcri.contains(cri));
		}
	}
	
	/**
	 * Tests null is returned if there is no recent location stored.
	 */
	@Test(timeout = TIMEOUT)
	public void testNoRecentLocation() {
	    replayAndInitializeDefaultUser();
	    assertTrue(chatcontroller.getRecentLocation() == null);
	}
	
	/**
	 * Tests that correct messages are retrieved
	 */
	@Test(timeout = TIMEOUT)
	public void testGetMessages() {
	    replayAndInitializeCustomUser(userinfo1);
	    List<MessageInfo> rgmi = chatcontroller.getMessagesFromChatRoom(stRoomId1);
	    assertTrue(rgmi != null);
	    assertTrue(rgmi.equals(mlist1));
	}
}
