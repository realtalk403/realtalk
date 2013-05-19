package realtalk.model;

import java.sql.Timestamp;
import java.util.List;

import realtalk.model.ChatRoomModel;
import realtalk.util.MessageInfo;
//import realtalk.util.UserInfo;
import android.test.AndroidTestCase;

public class ChatRoomModelTest extends AndroidTestCase {

	
	private ChatRoomModel crm;
	private String stName = "crmName";
	private String stId = "id123";
	private String stDescription = "description goes here";
	private double latitude = 123.456;
	private double longitude = 78.90;
	private String stCreator = "Cory";
	private Timestamp timestampCreated = new Timestamp(123456);
	
	protected void setUp() throws Exception {
		super.setUp();
		crm = new ChatRoomModel(stName, stId, stDescription, latitude, longitude, stCreator, 0, timestampCreated);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}


	private List<MessageInfo> rgmiGetMessagesAndAssertExpectedNumber(ChatRoomModel crm, int imiExpected) {
		List<MessageInfo> rgmi = crm.rgmiGetMessages();
		assertEquals("Did not find expected amount of messages in rgmi", imiExpected, rgmi.size());
		return rgmi;
	}
	
	private void assertMessagesInOrderFromOldestToNewest(List<MessageInfo> rgmi) {
		for (int i = 1; i < rgmi.size(); i++) {
			assertTrue("Messages out of order", rgmi.get(i-1).timestampGet().before(rgmi.get(i).timestampGet()));
		}
	}
	
	private void assertMiEquals(MessageInfo miExpected, MessageInfo miActual) {
		assertEquals("the mi retrieved from the crm is different from the one added.", 
				miExpected.stBody(), miActual.stBody());
		assertEquals("the mi retrieved from the crm is different from the one added.", 
				miExpected.stSender(), miActual.stSender());
		assertEquals("the mi retrieved from the crm is different from the one added.", 
				miExpected.timestampGet(), miActual.timestampGet());
	}
	
	public void testAdd1Mi() {
		MessageInfo miAdded = new MessageInfo("hello world", "Cory", new Timestamp(123467));
		crm.addMi(miAdded);
		
		List<MessageInfo> rgmi = rgmiGetMessagesAndAssertExpectedNumber(crm, 1);
		assertMiEquals(miAdded, rgmi.get(0));
	}
	
	public void testAdd2MiInOrder() {
		MessageInfo mi1 = new MessageInfo("1", "Cory", new Timestamp(1234));
		MessageInfo mi2 = new MessageInfo("2", "Cory", new Timestamp(1235));
		crm.addMi(mi1);
		crm.addMi(mi2);
		
		List<MessageInfo> rgmi = rgmiGetMessagesAndAssertExpectedNumber(crm, 2);
		assertMessagesInOrderFromOldestToNewest(rgmi);
		assertMiEquals(mi1, rgmi.get(0));
		assertMiEquals(mi2, rgmi.get(1));
	}
	
	public void testAdd2MiOutOfOrder() {
		MessageInfo mi1 = new MessageInfo("1", "Cory", new Timestamp(1234));
		MessageInfo mi2 = new MessageInfo("2", "Cory", new Timestamp(1235));
		crm.addMi(mi2);
		crm.addMi(mi1);
		
		List<MessageInfo> rgmi = rgmiGetMessagesAndAssertExpectedNumber(crm, 2);
		assertMessagesInOrderFromOldestToNewest(rgmi);
		assertMiEquals(mi1, rgmi.get(0));
		assertMiEquals(mi2, rgmi.get(1));
	}

//	
//	public void testAdd1U() {
//		UserInfo u1 = new UserInfo("a", "a", "a");
//		assertTrue(crm.fAddU(u));
//		assertEquals("Did not find 1 user added", 1, crm.cu());
//		
//	}
	
//	
//	public void testAdd2U() {
//		UserInfo u1 = new UserInfo("a", "a", "a");
//		UserInfo u2 = new UserInfo("b", "b", "b");
//		assertTrue(crm.fAddU(u1));
//		assertTrue(crm.fAddU(u2));
//		assertEquals("Did not find 2 users added", 2, crm.cu());
//	}
//	
//	
//	public void testAddDuplicateU() {
//		UserInfo u = new UserInfo("a","a","a");
//		assertTrue(crm.fAddU(u));
//		assertFalse(crm.fAddU(u));
//		assertEquals("Did not find 1 user added",  1, crm.cu());
//	}
//
//	
//	public void testFRemoveUReturnsFalseIfUserNotPresent() {
//		UserInfo uAdded = new UserInfo("a","a","a");
//		UserInfo uNotAdded = new UserInfo("b", "b", "b");
//		assertFalse(crm.fRemoveU(uNotAdded));
//		crm.fAddU(uAdded);
//		assertFalse(crm.fRemoveU(uNotAdded));
//	}
//
//	
//	public void testFRemoveUWithOneU() {
//		UserInfo u = new UserInfo("a", "a", "a");
//		crm.fAddU(u);
//		assertEquals("User added incorrectly, test invalid", 1, crm.cu());
//		
//		assertTrue("User was not removed correctly", crm.fRemoveU(u));
//		assertEquals("User found when none should have been", crm.cu());
//	}
//	
//	
//	public void testFRemoveUWith2U() {
//		UserInfo u1 = new UserInfo("a", "a", "a");
//		UserInfo u2 = new UserInfo("b", "b", "b");
//		crm.fAddU(u1);
//		crm.fAddU(u2);
//		assertEquals("Users added incorrectly, test invalid", 2, crm.cu());
//		
//		assertTrue("User was not removed correctly", crm.fRemoveU(u1));
//		assertEquals("Did not find exactly 1 remaining user", crm.cu());
//	}
//	
//	public void testNumUsersGetWith1User() {
//		crm.fAddU(new UserInfo("a", "a", "a"));
//		assertEquals(1, crm.cu());
//	}
//	
//	
//	public void testNumUsersGetWith2Users() {
//		crm.fAddU(new UserInfo("a", "a", "a"));
//		crm.fAddU(new UserInfo("b", "b", "b"));
//		assertEquals(2, crm.cu());
//	}

	public void testNumUsersGetWhenEmpty() {
		assertEquals(0, crm.getNumUsers());
	}
	
	public void testRgmiGetMessagesWhenEmpty() {
		assertTrue(crm.rgmiGetMessages().isEmpty());
	}
	
	public void testRgmiGetMessagesWith2() {
		crm.addMi(new MessageInfo("a", "a", 1));
		crm.addMi(new MessageInfo("b", "b", 2));
		assertEquals(2, crm.rgmiGetMessages().size());
	}
	
	public void testStName() {
		assertEquals("wrong name returned", stName, crm.stName());
	}
	

	public void testSetName() {
		String stNewName = "nn";
		assertNotSame(stNewName, crm.stName());
		crm.setName(stNewName);
		assertEquals(stNewName, crm.stName());
	}

	public void testStId() {
		assertEquals("Wrong id returned", stId, crm.stId());
	}

	public void testStDescription() {
		assertEquals("Wrong description returned", stDescription, crm.stDescription());
	}
	
	public void testSetDescription() {
		String stNewDesc = "nd";
		assertNotSame(stNewDesc, crm.stDescription());
		crm.setDescription(stNewDesc);
		assertEquals(stNewDesc, crm.stDescription());
	}
	
	public void testGetLatitude() {
		assertEquals("Wrong latitude returned", latitude, crm.getLatitude());
	}
	
	public void testSetLatitude() {
		double stNew = 3.14159;
		assertNotSame(stNew, crm.getLatitude());
		crm.setLatitude(3.14159);
		assertEquals(stNew, crm.getLatitude());
	}

	public void testGetLongitude() {
		assertEquals("Wrong longitude returned", longitude, crm.getLongitude());
	}
	
	public void testSetLongitude() {
		double stNew = 3.14159;
		assertNotSame(stNew, crm.getLongitude());
		crm.setLongitude(stNew);
		assertEquals(stNew, crm.getLongitude());
	}

	public void testStCreator() {
		assertEquals("Wrong creator returned", stCreator, crm.stCreator());
	}
	
	public void testSetCreator() {
		String stNew = "nd";
		assertNotSame(stNew, crm.stCreator());
		crm.setCreator(stNew);
		assertEquals(stNew, crm.stCreator());
	}
	
	public void testTimestampCreated() {
		assertEquals("Wrong timestamp returned", timestampCreated, crm.timestampCreated());
	}


}
