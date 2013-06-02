/**
 * 
 */
package realtalk.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.test.AndroidTestCase;

/**
 * This test is written for the UserManager class. This is a test-driven
 * development test therefore it works on a MockUserManager object.
 * It will be run on the production UserManager class when it is planned
 * for implementation in v1. This is documented in the issue page.
 * 
 * Test-Driven Development 
 * 
 * @author Colin Kho
 *
 */
public class UserManagerTest extends AndroidTestCase {
	private static final int TIMEOUT = 10000;
	private UserManager uManager;
	
	private static final String LOGOUT_NAME = "";
	private static final String LOGOUT_PWD = "";
	private static final String DEFAULT_NAME = "Tester";
	private static final String DEFAULT_PWD = "TESTPASS";
	private static final int CYCLES = 10;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		UserManager.setInstance(new StubUserManager());
		uManager = UserManager.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	@Test(timeout = TIMEOUT)
	public void testGetInstance() {
		uManager = UserManager.getInstance();
		assertTrue("Testing get instance never returns null", uManager != null);
		assertTrue("Testing getInstance gets correct class", uManager instanceof UserManager);
	}
	
	@Test(timeout = TIMEOUT)
	public void testLoginUserColinPwdTest() {
		testLogin("Colin", "Test");
	}
	
	@Test(timeout = TIMEOUT)
	public void testLoginUserJoryPwdTest() {
		testLogin("Jory", "Test");
	}
	
	@Test(timeout = TIMEOUT)
	public void testLoginUserTaylorPwdTest() {
		testLogin("Taylor", "Test");
	}
	
	/**
	 * Method that tests logins
	 * 
	 * @param userName
	 * @param pwd
	 */
	private void testLogin(String userName, String pwd) {
		assertTrue("testLogin: test logging in successful", uManager.fLogin(userName, pwd));
		assertTrue("testLogin: test user name correct", uManager.stGetUserName().equals(userName));
		assertTrue("testLogin: test password correct", uManager.stGetPassword().equals(pwd));
	}
	
	@Test(timeout = TIMEOUT)
	public void testLogoutUserTaylorPwdTest() {
		testLogout("Taylor", "Test");
	}
	
	@Test(timeout = TIMEOUT)
	public void testLogoutUserJoryPwdTest() {
		testLogout("Jory", "Test");
	}
	
	@Test(timeout = TIMEOUT)
	public void testLoginoutColinPwdTest() {
		testLogout("Colin", "Test");
	}
	
	@Test(timeout = TIMEOUT)
	public void testDisallowMultipleLogins() {
		assertTrue("test logging in successful", uManager.fLogin(DEFAULT_NAME, DEFAULT_PWD));
		for (int i = 0; i < CYCLES; i++) {
			assertFalse("test logging in fails", uManager.fLogin(DEFAULT_NAME, DEFAULT_PWD));
		}
	}
	
	@Test(timeout = TIMEOUT)
	public void testDisallowMultipleLogouts() {
		assertTrue("test logging out successful", uManager.fLogout());
		for (int i = 0; i < CYCLES; i++) {
			assertFalse("test logging out fails", uManager.fLogout());
		}
	}
	
	/**
	 * Method that tests logouts
	 * 
	 * @param username
	 * @param pwd
	 */
	private void testLogout(String username, String pwd) {
		assertTrue("testLogout: test logging in successful", uManager.fLogin(username, pwd));
		assertTrue("testLogout: test logging out successful", uManager.fLogout());
		assertTrue("testLogin: test user name correct", uManager.stGetUserName().equals(LOGOUT_NAME));
		assertTrue("testLogin: test password correct", uManager.stGetPassword().equals(LOGOUT_PWD));
	}
	
	@Test(timeout = TIMEOUT)
	public void testFailLoginWithEmptyName() {
		assertFalse("testing that login fails with empty user name", uManager.fLogin(LOGOUT_NAME, DEFAULT_PWD));
	}
	
	@Test(timeout = TIMEOUT)
	public void testFailLoginWithEmptyPwd() {
		assertFalse("testing that login fails with empty password", uManager.fLogin(DEFAULT_NAME, LOGOUT_PWD));
	}
	
	@Test(timeout = TIMEOUT)
	public void testChangeUserColinTest() {
		testChangeUser("Colin", "Test");
	}
	
	@Test(timeout = TIMEOUT)
	public void testChangeUserJoryTest() {
		testChangeUser("Jory", "Test");
	}
	
	/**
	 * Tests method that changes user
	 * 
	 * @param username
	 * @param pwd
	 */
	private void testChangeUser(String username, String pwd) {
		assertTrue(uManager.fLogin(DEFAULT_NAME, DEFAULT_PWD));
		assertTrue("change id: test if change id is successful", uManager.fChangeUser(username, pwd));
		assertTrue("testChangeUser: test user name correct", uManager.stGetUserName().equals(username));
		assertTrue("testChangeUser: test password correct", uManager.stGetPassword().equals(pwd));
		assertTrue("testChangeUser: test logging out successful", uManager.fLogout());
	}
}
