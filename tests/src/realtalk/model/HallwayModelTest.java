package realtalk.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import realtalk.model.ChatRoomModel;
import realtalk.model.HallwayModel;
import realtalk.util.ChatRoomInfo;
import realtalk.util.MessageInfo;
//import realtalk.util.UserInfo;
import android.test.AndroidTestCase;

//Hungarian: hm = HallwayModel

public class HallwayModelTest extends AndroidTestCase {
	
	private HallwayModel hm;

	protected void setUp() throws Exception {
		super.setUp();	
		hm = new HallwayModel();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testCrmGetFromIdReturnNullIfNoRoom() {
		assertNull(hm.crmGetFromId("something"));
	}
	
	public void testCrmGetFromIdWith2Rooms() {
		hm.addRoom("a", "id", "d", 0, 0, "C", new Timestamp(1));
		hm.addRoom("b", "b", "b", 0, 0, "C", new Timestamp(0));
		ChatRoomModel crm = hm.crmGetFromId("id");
		
		assertEquals("a", crm.stName());
		assertEquals("d", crm.stDescription());
	}

	
	public void testRgmiGetFromCrmId() {
		MessageInfo mi1 = new MessageInfo("a", "sender", 1234L);
		MessageInfo mi2 = new MessageInfo("b", "sender", 1235L);
		hm.addRoom("a", "id", "d", 0, 0, "C", new Timestamp(1));
		hm.addMiToCrm(mi1, "id");
		hm.addMiToCrm(mi2, "id");
		List<MessageInfo> rgmi = hm.rgmiGetFromCrmId("id");
		assertEquals(2, rgmi.size());
	}
	
	
	public void testRgmiGetFromCrmIdThrowException() {
		try {
			hm.rgmiGetFromCrmId("this id doesnt exist");
			fail("no exception thrown");
		} catch (IllegalArgumentException e){
			//test passes
		}
	}
	
	public void testAddRoom() {
		hm.addRoom("a", "id", "d", 0, 0, "C", new Timestamp(1));
	}

	
	public void testFRemoveRoomReturnsFalseIfNoRoom() {
		assertFalse("fRemoveRoom unexpectedly returned true", hm.fRemoveRoom("someNonexistentRoom"));
	}
	
	
	public void testFRemoveRoom() {
		hm.addRoom("a", "id", "describes", 0, 0, "C", new Timestamp(1));
		assertTrue(hm.fRemoveRoom("id"));
		assertFalse(hm.fChatRoomExists("id"));
	}

//	These test the deprecated add/remove user stuff
//	Leave them in just in case we switch back.
	
//	public void testFAddUToCrmThrowsExceptionIfNoRoom() {
//		try {
//			hm.fAddUToCrm(new UserInfo("a","b", "c"), "some bad id");
//			fail("no exception thrown");
//		} catch (IllegalArgumentException e) {
//			//test passes when exception is thrown
//		}
//	}	
	
//	public void testFAddUToCrm() {
//		hm.addRoom("a", "id", "d", 0, 0, "C", new Timestamp(1));
//		UserInfo u = new UserInfo("a","b","c");
//		assertTrue(hm.fAddUToCrm(u, "id"));
//		assertFalse("should return false if user already in room.", hm.fAddUToCrm(u, "id"));
//	}
	
//	public void testFRemoveUFromCrmThrowsExceptionIfNoRoom() {
//		try {
//			hm.fRemoveUFromCrm(new UserInfo("a","b","c"), "id");
//			fail("no exception thrown when removing U from nonexistent crm");
//		} catch (IllegalArgumentException e) {
//			//test passes when exception thrown
//		}
//	}
	
//	
//	public void testFRemoveUFromCrm() {
//		hm.addRoom("a", "id", "d", 0, 0, "C", new Timestamp(1));
//		UserInfo u = new UserInfo("a","b","c");
//		assertFalse("user should not already be added", hm.fRemoveUFromCrm(u, "id"));
//		hm.fAddUToCrm(u, "id");
//		assertTrue("user not properly removed", hm.fRemoveUFromCrm(u, "id"));
//	}

	
	public void testAddMiToCrmThrowsExceptionIfNoRoom() {
		try {
			hm.addMiToCrm(new MessageInfo("a","b", 1L), "no such room");
			fail("no exception thrown when adding messages to nonexistent room");
		} catch (IllegalArgumentException e) {
			//test passes if exception thrown
		}
	}

	
	public void testAddRgMiToCrmThrowsExceptionIfNoRoom() {
		try {
			ArrayList<MessageInfo> rgmi = new ArrayList<MessageInfo>();
			rgmi.add(new MessageInfo("a","b", 1L));
			hm.addRgMiToCrm(rgmi, "no such room");
			fail("no exception thrown when adding messages to nonexistent room");
		} catch (IllegalArgumentException e) {
			//test passes if exception thrown
		}
	}
	
	public void testAddRgMiToCrm() {
		ArrayList<MessageInfo> rgmi = new ArrayList<MessageInfo>();
		rgmi.add(new MessageInfo("a","b", 2L));
		rgmi.add(new MessageInfo("c","d", 3L));
		hm.addRoom("a", "id", "d", 0, 0, "C", new Timestamp(1));
		hm.addRgMiToCrm(rgmi, "id");
	}
	
	public void testFChatRoomExists() {
		assertFalse(hm.fChatRoomExists("no"));
		hm.addRoom("a", "id", "d", 0, 0, "C", new Timestamp(1));
		hm.fChatRoomExists("id");
	}
	
	public void testUpdateChatroomAndGetRoomInfo() {
		hm.addRoom("a", "id", "d", 0, 0, "C", new Timestamp(1));
		ChatRoomInfo criExpected = new ChatRoomInfo("a2", "id", "d2", 1.0, 1.0, "C2", 4, new Timestamp(123));
		hm.updateChatroom(criExpected);
		ChatRoomInfo criActual = hm.getRoomInfo().get(0);
		assertEquals(criExpected.stId(), criActual.stId());
		assertEquals(criExpected.stCreator(), criActual.stCreator());
		assertEquals(criExpected.stName(), criActual.stName());
		assertEquals(criExpected.timestampCreated(), criActual.timestampCreated());
	}

}
