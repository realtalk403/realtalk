package realtalk.util;

import org.junit.Test;

import android.test.AndroidTestCase;
/**
 * UserInfo Test
 * 
 * Black Box Test Case.
 * 
 * @author Taylor Williams
 *
 */
public class UserInfoTest extends AndroidTestCase {
    private UserInfo userinfo;
    private static final int TIMEOUT = 10000;
    private static final String stName = "name";
    private static final String stPassword = "pword";
    private static final String stRegId = "someid";
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        userinfo = new UserInfo(stName, stPassword, stRegId);
    }
    
    @Test(timeout = TIMEOUT)
    public void testNameGetter() {
    	assertTrue(userinfo.stUserName().equals(stName));
    }
    
    @Test(timeout = TIMEOUT)
    public void testPasswordGetter() {
    	assertTrue(userinfo.stPassword().equals(stPassword));
    }
    
    @Test(timeout = TIMEOUT)
    public void testIdGetter() {
    	assertTrue(userinfo.stRegistrationId().equals(stRegId));
    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
