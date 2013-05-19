package realtalk.util;

import java.util.List;

import org.junit.Test;

import android.test.AndroidTestCase;
/**
 * ChatManager Test
 * 
 * Black Box Test Case.
 * 
 * @author Taylor Williams
 *
 */
public class ChatManagerTest extends AndroidTestCase {
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
    public void testRemoveUserFailure() {
    	RequestResultSet rrs = ChatManager.rrsRemoveUser(userinfo);
    	assertFalse(rrs.getfSucceeded());
    }
    
    @Test(timeout = TIMEOUT)
    public void testAuthenticateFailure() {
    	RequestResultSet rrs = ChatManager.rrsAuthenticateUser(userinfo);
    	assertFalse(rrs.getfSucceeded());
    }
    
    @Test(timeout = TIMEOUT)
    public void testNearbyRooms() {
    	ChatRoomResultSet crrs = ChatManager.crrsNearbyChatrooms(0, 0, 500.0);
    	assertTrue(crrs.fSucceeded());
    	assertTrue(crrs.rgcriGet() != null);
    }
    
    @Test(timeout = TIMEOUT)
    public void testChatRecentTest() {
    	ChatRoomResultSet crrs = ChatManager.crrsNearbyChatrooms(0, 0, 500.0);
    	List<ChatRoomInfo> rgcri = crrs.rgcriGet();
    	if (rgcri != null && rgcri.size() > 0)
    	{
    		PullMessageResultSet pmrs = ChatManager.pmrsChatLogGet(rgcri.get(0));
    		assertTrue(pmrs.fIsSucceeded());
    	}
    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
