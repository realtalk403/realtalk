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
 * @author Jordan Hazari
 *
 */
@SuppressLint("NewApi") public class ChatRoomActivityTest extends ActivityInstrumentationTestCase2<ChatRoomActivity> {
	private Solo solo;
	 
	public ChatRoomActivityTest() {
		super(ChatRoomActivity.class);
		
		ChatRoomInfo chatroominfo = new ChatRoomInfo("Test Room", "testroom", "a test room", 10.0, 10.0, "hazarij", 1, new Timestamp(System.currentTimeMillis()));
		ChatControllerStub.getInstance().fInitialize(new UserInfo("hazarij", "password", "aa"));
		ChatControllerStub.getInstance().joinRoom(chatroominfo, false);
		Intent it = new Intent();
		it.putExtra("ROOM", chatroominfo);
		it.putExtra("DEBUG", true);
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
	
	@Test
	public void testSendingMessages() {
		int cMessageInfo = ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom").size();
		AutoCompleteTextView edittextMessage = (AutoCompleteTextView) solo.getView(R.id.message);
		solo.enterText(edittextMessage, "test message");
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
		
		assertTrue("Message did not send", ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom").size() == cMessageInfo+1);
		solo.enterText(edittextMessage, "another message");
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
		
		solo.enterText(edittextMessage, "hello there");
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
		
		solo.enterText(edittextMessage, "hi!");
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
		
		solo.enterText(edittextMessage, "how are you?");
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
		
		solo.enterText(edittextMessage, "I'm pretty good, how have you been?");
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
		
		solo.enterText(edittextMessage, "great! see you later!");
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
		
		solo.enterText(edittextMessage, "bye!");
		solo.clickOnButton("Send");
		getActivity().populateAdapter(ChatControllerStub.getInstance().getMessagesFromChatRoom("testroom"));
	}
}
