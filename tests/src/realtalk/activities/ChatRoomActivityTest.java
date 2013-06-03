package realtalk.activities;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import realtalk.controller.ChatControllerStub;
import realtalk.util.ChatRoomInfo;
import realtalk.util.UserInfo;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.AutoCompleteTextView;

import com.jayway.android.robotium.solo.Solo;
import com.realtalk.R;

/**
 * Black Box Tests that test functionality of the Chatroom page
 * Integration Testing for V1.0 using a Stub class.
 * 
 * This is to allow ChatRoom Activity to be tested in isolation from
 * the server as Chat Controller makes calls to the server. As a Mock
 * Object would be insufficient as we need a way to have a real time
 * way of storing Chat Logs and displaying them. Hence a stub was the
 * best way of doing this.
 * 
 * @author Jordan Hazari
 *
 */
@SuppressLint("NewApi") public class ChatRoomActivityTest extends ActivityInstrumentationTestCase2<ChatRoomActivity> {
	private Solo solo;
	
	public ChatRoomActivityTest() {
		super(ChatRoomActivity.class);	
	}
	
	/**
	 * Sets up the test by initializing the "stub" controller
	 * and providing all necessary information for a chatroom.
	 */
	@Before
	public void setUp() throws Exception {
	    ChatRoomInfo chatroominfo = new ChatRoomInfo("Test Room", "testroom", "a test room", 10.0, 10.0, "hazarij", 1, new Timestamp(System.currentTimeMillis()));
        ChatControllerStub.getInstance().fInitialize(new UserInfo("hazarij", "password", "aa"));
        ChatControllerStub.getInstance().joinRoom(chatroominfo, false);
        Intent it = new Intent();
        it.putExtra("ROOM", chatroominfo);
        it.putExtra("DEBUG", true);
        setActivityIntent(it);
        solo = new Solo(getInstrumentation(), getActivity());
	}
	
	/**
	 * Tests the buttons and text exists on the activity
	 */
	@Test
	public void testButtonsAndTextDisplay() {
	    solo.sleep(10000);
		assertTrue(solo.searchButton("Send"));
		assertTrue(solo.searchButton("Leave Room"));
		assertFalse(solo.searchButton("WRONG_BUTTON"));
	}
	
	/**
	 * This tests if the send button populates the controller and displays it on the screen
	 */
	@Test
	public void testSendingMessages() {
		int cMessageInfo = ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom").size();
		sendMessage("test message");
		solo.sleep(30000);
		assertTrue("Message did not send", ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom").size() == cMessageInfo+1);
		
		sendMessage("another message");
		sendMessage("hello there");
		sendMessage("hi!");
		sendMessage("how are you?");
		sendMessage("I'm pretty good, how have you been?");
		sendMessage("great! see you later!");
		sendMessage("bye!");
	}
	
	/**
	 * A helper method to send a message.
	 * 
	 * @param stMessage the message to send
	 */
	private void sendMessage(String stMessage) {
		AutoCompleteTextView edittextMessage = (AutoCompleteTextView) solo.getView(R.id.message);
		solo.enterText(edittextMessage, stMessage);
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
	}
	
	/**
	 * Tests the leave room button in debug mode.
	 */
	@Test
	public void testLeaveRoom() {
	    solo.clickOnButton("Leave Room");
	    // This is simulated not to leave the activity but merely close it in debug mode.
	}
}
