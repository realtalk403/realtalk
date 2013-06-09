package realtalk.util;

import java.sql.Timestamp;
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
    public void testChangePwordFailure() {
    	RequestResultSet rrs = ChatManager.rrsChangePassword(userinfo, "lol");
    	assertFalse(rrs.getfSucceeded());
    }
    
    @Test(timeout = TIMEOUT)
    public void testChangeIdFailure() {
    	RequestResultSet rrs = ChatManager.rrsChangeID(userinfo, "lol");
    	assertFalse(rrs.getfSucceeded());
    }
    
    @Test(timeout = TIMEOUT)
    public void testNearbyRooms() {
    	ChatRoomResultSet crrs = ChatManager.crrsNearbyChatrooms(0, 0, 500.0);
    	assertTrue(crrs.fSucceeded());
    	assertTrue(crrs.rgcriGet() != null);
    }
    
    @Test(timeout = TIMEOUT)
    public void testChatLogGetTest() {
    	ChatRoomResultSet crrs = ChatManager.crrsNearbyChatrooms(0, 0, 500.0);
    	List<ChatRoomInfo> rgcri = crrs.rgcriGet();
    	if (rgcri != null && rgcri.size() > 0)
    	{
    		PullMessageResultSet pmrs = ChatManager.pmrsChatLogGet(rgcri.get(0));
    		assertTrue(pmrs.fIsSucceeded());
    	}
    }
    
    @SuppressWarnings("deprecation")
    @Test(timeout = TIMEOUT)
    public void testPullRecentChatTest() {
    	ChatRoomResultSet crrs = ChatManager.crrsNearbyChatrooms(0, 0, 500.0);
    	List<ChatRoomInfo> rgcri = crrs.rgcriGet();
    	if (rgcri != null && rgcri.size() > 0)
    	{
    		PullMessageResultSet pmrs = ChatManager.pmrsChatRecentChat(rgcri.get(0), new Timestamp(10000));
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
