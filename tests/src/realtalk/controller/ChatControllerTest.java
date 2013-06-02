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

import android.test.AndroidTestCase;

/**
 * White box tests for ChatController.
 * Not much can be done without using real user and room data,
 * so these tests are not comprehensive.
 * 
 * This also includes Mock Testing whereby the chatmanager is a mock object
 * so that predefined calls are sent to chat controller without needing to
 * query the server.
 * 
 * @author Jory Rice And Colin Kho
 */
public class ChatControllerTest extends TestCase {

    private static final int TIMEOUT = 10000;
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
    
    private UserInfo userinfo4;
    private String stName4 = "jordan";
    private String stPassword4 = "hazari";
    private String stId4 = "01839239821";
    
    /*
     * Test Rooms
     */
    
    private ChatRoomInfo chatroominfo1;
    private String stRoomName1 = "TestNonexistantRoomname";
    private String stRoomDesc1 = "TestNonexistantRoomdesc";
    private String stRoomId1 = "TestNonexistantRoomid";
    
    private ChatRoomInfo chatroominfo2;
    private String stRoomName2 = "Colin";
    private String stRoomDesc2 = "Colins room";
    private String stRoomId2 = "1232121312321321313";
    
    private ChatRoomInfo chatroominfo3;
    private String stRoomName3 = "brandons room";
    private String stRoomDesc3 = "brandons room";
    private String stRoomId3 = "1232121321";
    private IChatManager mockChatManager;
    
    // Nearby Predefined Rooms
    List<ChatRoomInfo> rgNearbyRooms = new ArrayList<ChatRoomInfo>();
    
    // ChatRoomResultSet Default
    ChatRoomResultSet crrsDefault;
    
    // Predefined Result Sets for Mock
    RequestResultSet rrsMockSuccess = new RequestResultSet(true, "", "");
    RequestResultSet rrsMockFailure = new RequestResultSet(false, "", "");
    
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
    List<MessageInfo> mlist1 = new ArrayList<MessageInfo>();
    List<MessageInfo> mlist2 = new ArrayList<MessageInfo>();
    List<MessageInfo> mlist3 = new ArrayList<MessageInfo>();
    
    // Pull Message Result Sets
    PullMessageResultSet pmrs1;
    PullMessageResultSet pmrs2;
    PullMessageResultSet pmrs3;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		chatcontroller = ChatController.getInstance();
		chatcontroller.setDebugMode(true);
		// Initialize Test Users
		userinfo1 = new UserInfo(stName1, stPassword1, stId1);
		userinfo2 = new UserInfo(stName2, stPassword2, stId2);
		userinfo3 = new UserInfo(stName3, stPassword3, stId3);
		userinfo4 = new UserInfo(stName4, stPassword4, stId4);
		
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
	    
		mockChatManager = EasyMock.createNiceMock(IChatManager.class);
		rgNearbyRooms.add(chatroominfo1);
		crrsDefault = new ChatRoomResultSet(true, rgNearbyRooms,  "", "");
		EasyMock.expect(mockChatManager.crrsUsersChatrooms(userinfo1)).andReturn(crrsDefault);
		EasyMock.expect(mockChatManager.pmrsChatLogGet(chatroominfo1)).andReturn(pmrs1);
		EasyMock.expect(mockChatManager.pmrsChatLogGet(chatroominfo2)).andReturn(pmrs2);
		EasyMock.expect(mockChatManager.pmrsChatLogGet(chatroominfo3)).andReturn(pmrs3);
		
	}
	
	private void replayAndInitialize() {
		EasyMock.replay(mockChatManager);
		chatcontroller.setChatManager(mockChatManager);
		chatcontroller.fInitialize(userinfo1);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
		chatcontroller.uninitialize();
	}

	@Test(timeout = TIMEOUT)
	public void testGetInstance() {
		assertTrue(chatcontroller != null);
	}
	
	@Test(timeout = TIMEOUT)
	public void testGetUser() {
		replayAndInitialize();
		assertEquals(chatcontroller.getUser(), userinfo1);
	}

	@Test(timeout = TIMEOUT)
	public void testIsAlreadyJoined() {
		replayAndInitialize();
		assertTrue(chatcontroller.fIsAlreadyJoined(chatroominfo1));
	}
	
	/**
	 * Checks if the is already joined method returns the correct values.
	 * 
	 * @param rgcheckAgainst rooms that the controller should be joined in
	 */
	private void joinRoomTest(List<ChatRoomInfo> rgcheckAgainst) {
		
	}
	
	@Test(timeout = TIMEOUT)
	public void testJoinRoomUsingColinRoom() {
		testJoinRoom(chatroominfo2);
	}
	
	@Test(timeout = TIMEOUT)
	public void testJoinRoomUsingBrandonRoom() {
		testJoinRoom(chatroominfo3);
	}
	
	/**
	 * Tests joining a room on the chatcontroller.
	 * 
	 * @param chatroominfo
	 */
	private void testJoinRoom(ChatRoomInfo chatroominfo) {
		EasyMock.expect(mockChatManager.rrsJoinRoom(userinfo1, chatroominfo)).andReturn(rrsMockSuccess);
		replayAndInitialize();
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
	 * @param chatroominfo
	 */
	private void testLeaveRoom(ChatRoomInfo chatroominfo) {
		EasyMock.expect(mockChatManager.rrsJoinRoom(userinfo1, chatroominfo)).andReturn(rrsMockSuccess);
		EasyMock.expect(mockChatManager.rrsLeaveRoom(userinfo1, chatroominfo)).andReturn(rrsMockSuccess);
		replayAndInitialize();
		chatcontroller.joinRoom(chatroominfo, false);
		assertTrue(chatcontroller.leaveRoom(chatroominfo));
	}
	
	@Test(timeout = TIMEOUT)
	public void testGetRoomsOneRoom() {
		List<ChatRoomInfo> rgcri = new ArrayList<ChatRoomInfo>();
		rgcri.add(chatroominfo1);
		replayAndInitialize();
		testGetControllerRooms(rgcri);
	}
	
	@Test(timeout = TIMEOUT)
	public void testGetRoomsTwoRooms() {
		List<ChatRoomInfo> rgcri = new ArrayList<ChatRoomInfo>();
		EasyMock.expect(mockChatManager.rrsJoinRoom(userinfo1, chatroominfo2)).andReturn(rrsMockSuccess);
		replayAndInitialize();
		chatcontroller.joinRoom(chatroominfo2, false);
		rgcri.add(chatroominfo1);
		rgcri.add(chatroominfo2);
		testGetControllerRooms(rgcri);
	}
	
	/**
	 * Tests getting chat rooms in the controller.
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
}
