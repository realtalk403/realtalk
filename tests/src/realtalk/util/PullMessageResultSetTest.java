package realtalk.util;

import java.util.ArrayList;

import org.junit.Test;

import android.test.AndroidTestCase;
/**
 * PullMessageResult Test
 * 
 * White Box Test Case.
 * 
 * @author Taylor Williams
 *
 */
public class PullMessageResultSetTest extends AndroidTestCase {
    private PullMessageResultSet pmrsSuccess;
    private PullMessageResultSet pmrsFailure;
    private static final int TIMEOUT = 10000;
    private static final String stErrorCode = "ERRORCODE";
    private static final String stErrorMessage = "ERRORMESSAGE";
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        pmrsFailure = new PullMessageResultSet(false, stErrorMessage, stErrorCode);
        pmrsSuccess = new PullMessageResultSet(true, new ArrayList<MessageInfo>(), "NONE", "NONE");
    }
    
    @Test(timeout = TIMEOUT)
    public void testSuccessionGetter() {
        assertTrue(!pmrsFailure.fIsSucceeded());
        assertTrue(pmrsSuccess.fIsSucceeded());
    }
    
    @Test(timeout = TIMEOUT)
    public void testMessagesGetter() {
    	//will cause a null pointer exception if constructor failed
        assertTrue(pmrsSuccess.getRgmessage().size() == 0);
    }
    
    @Test(timeout = TIMEOUT)
    public void testErrorMessageGetter() {
        assertTrue(pmrsFailure.getStErrorMessage().equals(stErrorMessage));
    }
    
    @Test(timeout = TIMEOUT)
    public void testErrorCodeGetter() {
    	assertTrue(pmrsFailure.getStErrorCode().equals(stErrorCode));
    }
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
