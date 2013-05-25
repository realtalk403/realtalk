package realtalk.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import android.test.AndroidTestCase;

/**
 * White box tests for ChatRoomResultSet
 * 
 * @author Jory Rice
 */
public class ChatRoomResultSetTest extends AndroidTestCase {

    private static final int TIMEOUT = 10000;
    private ChatRoomResultSet crrsSuccess;
    private ChatRoomResultSet crrsFailure;
    private List<ChatRoomInfo> rgcri;
    private static final String stFailureErrorCode = "FAILURECODE";
    private static final String stFailureErrorMessage = "FAILUREMESSAGE";
    private static final String stSuccessErrorCode = "SUCCESSCODE";
    private static final String stSuccessErrorMessage = "SUCCESSMESSAGE";
	
	protected void setUp() throws Exception {
		super.setUp();
		rgcri = new ArrayList<ChatRoomInfo>();
		crrsSuccess = new ChatRoomResultSet(true, rgcri, stSuccessErrorCode, stSuccessErrorMessage);
		crrsFailure = new ChatRoomResultSet(false, stFailureErrorCode, stFailureErrorMessage);
	}
	
	@Test(timeout = TIMEOUT)
	public void testConstructor() {
		assertTrue(crrsSuccess != null);
		assertTrue(crrsFailure != null);
	}
	
	@Test(timeout = TIMEOUT)
	public void testGetters() {
		assertTrue(crrsSuccess.fSucceeded());
		assertTrue(!crrsFailure.fSucceeded());
		assertTrue(crrsSuccess.stErrorCode().equals(stSuccessErrorCode));
		assertTrue(crrsFailure.stErrorCode().equals(stFailureErrorCode));
		assertTrue(crrsSuccess.stErrorMessage().equals(stSuccessErrorMessage));
		assertTrue(crrsFailure.stErrorMessage().equals(stFailureErrorMessage));
		assertTrue(crrsSuccess.rgcriGet().equals(rgcri));
		assertTrue(crrsFailure.rgcriGet().isEmpty());
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
