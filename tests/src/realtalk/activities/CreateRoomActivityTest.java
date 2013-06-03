package realtalk.activities;

import org.junit.Before;
import org.junit.Test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.jayway.android.robotium.solo.Solo;
import com.realtalk.R;

/**
 * @author Cory Shiraishi
 * 
 * Tests for CreateRoomActivity
 */
public class CreateRoomActivityTest extends ActivityInstrumentationTestCase2<CreateRoomActivity> {

	private Solo solo;
	private double latitude = 100.0;
	private double longitude = 10.0;
	
	public CreateRoomActivityTest() throws Exception {
		super(CreateRoomActivity.class);
	}
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.putExtra("LATITUDE", latitude);
		intent.putExtra("LONGITUDE", longitude);
		intent.putExtra("DEBUG_MODE", true);
		setActivityIntent(intent);
		CreateRoomActivity activity = getActivity();
		activity.setDebugMode();
		solo = new Solo(getInstrumentation(), activity);
	}
	
	/**
	 * Tests room creation without a description
	 */
	@Test
	public void testCreateRoomNoDescription() {
		solo.enterText((EditText) solo.getView(R.id.roomName), "Another Test Room");
		solo.clickOnButton("Create Room");
	}
	
	/**
	 * Tests room creation without a name
	 */
	@Test
	public void testCreateRoomNoName() {
		solo.enterText((EditText) solo.getView(R.id.description), "some description");
		solo.clickOnButton("Create Room");
	}
}
