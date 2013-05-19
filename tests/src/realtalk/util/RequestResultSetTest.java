package realtalk.util;

import static org.junit.Assert.*;

import org.junit.Test;

import android.test.AndroidTestCase;

/**
 * Black box tests for RequestResultSet
 * 
 * @author Jory Rice
 */
public class RequestResultSetTest extends AndroidTestCase {

    private static final int TIMEOUT = 10000;
    private RequestResultSet rrsSuccess;
    private RequestResultSet rrsFailure;
    private static final String stFailureErrorCode = "FAILURECODE";
    private static final String stFailureErrorMessage = "FAILUREMESSAGE";
    private static final String stSuccessErrorCode = "SUCCESSCODE";
    private static final String stSuccessErrorMessage = "SUCCESSMESSAGE";
	
	protected void setUp() throws Exception {
		super.setUp();
		rrsSuccess = new RequestResultSet(true, stSuccessErrorCode, stSuccessErrorMessage);
		rrsFailure = new RequestResultSet(false, stFailureErrorCode, stFailureErrorMessage);
	}
	
	@Test(timeout = TIMEOUT)
	public void testConstructor() {
		assertTrue(rrsSuccess != null);
		assertTrue(rrsFailure != null);
	}
	
	@Test(timeout = TIMEOUT)
	public void testGetters() {
		assertTrue(rrsSuccess.getfSucceeded());
		assertTrue(!rrsFailure.getfSucceeded());
		assertTrue(rrsSuccess.getStErrorCode().equals(stSuccessErrorCode));
		assertTrue(rrsFailure.getStErrorCode().equals(stFailureErrorCode));
		assertTrue(rrsSuccess.getStErrorMessage().equals(stSuccessErrorMessage));
		assertTrue(rrsFailure.getStErrorMessage().equals(stFailureErrorMessage));
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
