package realtalk.model;

import java.sql.Timestamp;
import java.util.List;

import realtalk.model.ChatRoomModel;
import realtalk.util.MessageInfo;
import android.test.AndroidTestCase;

public class TestChatRoomModel extends AndroidTestCase {

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

	public void testAddOneMi() {
//		MessageInfo miAdded = new MessageInfo("hello world", "Cory", new Timestamp(123467));
//		crm.addMi(miAdded);
//		
//		List<MessageInfo> rgmi = crm.rgmiGetMessages();
//		assertEquals("More than one message appeared in the rgmi", rgmi.size(), 1);
//		
//		MessageInfo miRetrieved = rgmi.get(0);
//		assertEquals("the mi retrieved from the crm is different from the one added.", 
//				miAdded.stBody(), miRetrieved.stBody());
//		assertEquals("the mi retrieved from the crm is different from the one added.", 
//				miAdded.stSender(), miRetrieved.stSender());
//		assertEquals("the mi retrieved from the crm is different from the one added.", 
//				miAdded.timestampGet(), miRetrieved.timestampGet());
	}

	/*public void testFAddU() {
		fail("Not yet implemented");
	}

	public void testFRemoveU() {
		fail("Not yet implemented");
	}

	public void testRgmiGetMessages() {
		fail("Not yet implemented");
	}

	public void testStName() {
		fail("Not yet implemented");
	}

	public void testStId() {
		fail("Not yet implemented");
	}

	public void testStDescription() {
		fail("Not yet implemented");
	}

	public void testGetLatitude() {
		fail("Not yet implemented");
	}

	public void testGetLongitude() {
		fail("Not yet implemented");
	}

	public void testStCreator() {
		fail("Not yet implemented");
	}

	public void testNumUsersGet() {
		fail("Not yet implemented");
	}

	public void testTimestampCreated() {
		fail("Not yet implemented");
	}

	public void testFCheckRep() {
		fail("Not yet implemented");
	}*/

}
