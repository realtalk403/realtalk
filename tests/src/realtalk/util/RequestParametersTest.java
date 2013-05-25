package realtalk.util;

import org.junit.Test;
import android.test.AndroidTestCase;

public class RequestParametersTest extends AndroidTestCase {

	@Test
	public void test() {
		assertTrue(RequestParameters.PARAMETER_REG_ID.equals("PARAMETER_REG_ID"));
		assertTrue(RequestParameters.PARAMETER_PWORD.equals("PARAMETER_PWORD"));
		assertTrue(RequestParameters.PARAMETER_USER.equals("PARAMETER_USER"));
		assertTrue(RequestParameters.PARAMETER_ROOM_NAME.equals("PARAMETER_ROOM_NAME"));
		assertTrue(RequestParameters.PARAMETER_ROOM_ID.equals("PARAMETER_ROOM_ID"));
		assertTrue(RequestParameters.PARAMETER_ROOM_DESCRIPTION.equals("PARAMETER_ROOM_DESCRIPTION"));
		assertTrue(RequestParameters.PARAMETER_ROOM_LATITUDE.equals("PARAMETER_ROOM_LATITUDE"));
		assertTrue(RequestParameters.PARAMETER_ROOM_LONGITUDE.equals("PARAMETER_ROOM_LONGITUDE"));
		assertTrue(RequestParameters.PARAMETER_ROOM_ROOMS.equals("PARAMETER_ROOM_ROOMS"));
		assertTrue(RequestParameters.PARAMETER_MESSAGE_BODY.equals("PARAMETER_MESSAGE_BODY"));
		assertTrue(RequestParameters.PARAMETER_MESSAGE_SENDER.equals("PARAMETER_MESSAGE_SENDER"));
		assertTrue(RequestParameters.PARAMETER_MESSAGE_MESSAGES.equals("PARAMETER_MESSAGE_MESSAGES"));
	}

}
