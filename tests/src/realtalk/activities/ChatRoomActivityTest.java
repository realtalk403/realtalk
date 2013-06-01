package realtalk.activities;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import realtalk.controller.ChatController;
import realtalk.util.ChatRoomInfo;
import realtalk.util.UserInfo;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

/**
 * Black Box Tests that test functionality of the Chatroom page
 * @author Jordan Hazari
 *
 */
public class ChatRoomActivityTest extends ActivityInstrumentationTestCase2<ChatRoomActivity> {
	private Solo solo;
	 
	public ChatRoomActivityTest() {
		super(ChatRoomActivity.class);
		
		ChatController.getInstance().fInitializeTest(new UserInfo("testuser", "password", "id"));
		
		ChatRoomInfo chatroominfo = new ChatRoomInfo("Test Room", "testroom", "a test room", 10.0, 10.0, "hazarij", 1, new Timestamp(System.currentTimeMillis()));
		Intent it = new Intent();
		it.putExtra("ROOM", chatroominfo);
		it.putExtra("DEBUG", "true");
		setActivityIntent(it);
	}
	 
	@Before
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
		
	@Test
	public void testButtonsAndTextDisplay() {
		assertTrue(solo.searchButton("Send"));
		assertTrue(solo.searchButton("Leave Room"));
		assertFalse(solo.searchButton("WRONG_BUTTON"));
	}
}
