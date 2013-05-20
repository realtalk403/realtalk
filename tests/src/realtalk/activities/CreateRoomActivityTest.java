package realtalk.activities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jayway.android.robotium.solo.Solo;
import com.realtalk.R;

import realtalk.activities.CreateRoomActivity;
import realtalk.util.UserInfo;
import android.app.Activity;
import android.content.Intent; 
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

public class CreateRoomActivityTest extends ActivityInstrumentationTestCase2<CreateRoomActivity> {

	private Solo solo;
	private UserInfo u;
//	private static final int TIMEOUT = 10000;
	
	public CreateRoomActivityTest() throws Exception {
		super(CreateRoomActivity.class);
	}
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		u = new UserInfo("IAmTheUser", "IAmThePassword", "IAmTheRegId");
		Intent intent = new Intent();
		intent.putExtra("USER", u);
		setActivityIntent(intent);
		CreateRoomActivity activity = getActivity();
		activity.setDebugMode();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testCreateRoom() {
		EditText edittextName = (EditText) solo.getView(R.id.roomName);
		solo.enterText(edittextName, "TRTestTestTestRoom");
		EditText edittextDesc = (EditText) solo.getView(R.id.description);
		solo.enterText(edittextDesc, "This is a test room");
		solo.clickOnButton("Create Room");
	}
	
	@Test
	public void testCreateRoomNoDescription() {
		solo.enterText((EditText) solo.getView(R.id.roomName), "Another Test Room");
		solo.clickOnButton("Create Room");
	}
	
	@Test
	public void testCreateRoomNoName() {
		solo.enterText((EditText) solo.getView(R.id.description), "some description");
		solo.clickOnButton("Create Room");
	}
}
