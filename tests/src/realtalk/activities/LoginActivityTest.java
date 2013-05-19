package realtalk.activities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import realtalk.activities.LoginActivity;
import com.jayway.android.robotium.solo.Solo;
import com.realtalk.R;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	private Solo solo;
	 
	public LoginActivityTest() {
		super(LoginActivity.class);
	}
	 
	@Before
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	
	@Test
	public void testButtonsAndTextDisplay() {
		assertTrue(solo.searchButton("Login"));
		assertTrue(solo.searchButton("Create Account"));
		assertFalse(solo.searchButton("WRONG_BUTTON"));
	}
	
	
	@Test
	public void testEmptyUsername() {
		EditText edittextUsername = (EditText) solo.getView(R.id.editQuery);
		solo.enterText(edittextUsername, " ");
		EditText edittextPassword = (EditText) solo.getView(R.id.editPword);
		solo.enterText(edittextPassword, "test");
		solo.clickOnButton("Login");
		assertTrue("Could not find the dialog", solo.searchText("Invalid input"));
		solo.clickOnButton("Close");
	}  
	 
	@Test
	public void testEmptyPassword() {
		EditText edittextUsername = (EditText) solo.getView(R.id.editQuery);
		solo.enterText(edittextUsername, "test");
		EditText edittextPassword = (EditText) solo.getView(R.id.editPword);
		solo.enterText(edittextPassword, "");
		solo.clickOnButton("Login");
		assertTrue("Could not find the dialog", solo.searchText("Invalid input"));
		solo.clickOnButton("Close");
	}
	
	@Test
	public void testCreateAccountButton() {
		solo.clickOnButton("Create Account");
		solo.assertCurrentActivity("Check on current page activity", CreateAccountActivity.class);
		solo.goBack();
	}
	
	public void setUpAccount() {
		solo.clickOnButton("Create Account");
		solo.assertCurrentActivity("Check on current page activity", CreateAccountActivity.class);
		EditText edittextUsername = (EditText) solo.getView(R.id.user);
		solo.enterText(edittextUsername, "ab3r5i9");
		EditText edittextPassword = (EditText) solo.getView(R.id.pword);
		solo.enterText(edittextPassword, "test");
		EditText edittextConfPassword = (EditText) solo.getView(R.id.conf_pword);
		solo.enterText(edittextConfPassword, "test");
		solo.clickOnButton("Create Account");
	}
	
	public void deleteAccount() {
		solo.clickOnMenuItem("Account Settings");
		solo.clickOnButton("Click Here to Delete Account");
		solo.clickOnButton("Yes");
		solo.sleep(5000);
		solo.clickOnButton("Close");
	}
	
	@Test
	public void testSuccessfulLogin() {
		setUpAccount();
		EditText edittextUsername = (EditText) solo.getView(R.id.editQuery);
		solo.enterText(edittextUsername, "ab3r5i9");
		EditText edittextPassword = (EditText) solo.getView(R.id.editPword);
		solo.enterText(edittextPassword, "test");
		solo.clickOnButton("Login");
		solo.sleep(5000);
		solo.assertCurrentActivity("Check on current page activity", SelectRoomActivity.class);
		deleteAccount();
	} 

}
