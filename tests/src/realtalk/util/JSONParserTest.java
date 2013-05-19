package realtalk.util;

import org.junit.Test;
import realtalk.util.JSONParser;
import realtalk.util.RequestParameters;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import realtalk.util.RequestParameters;

import org.json.JSONException;
import org.json.JSONObject;

import realtalk.util.ChatManager;

import android.os.UserHandle;
import android.test.AndroidTestCase;
/**
 * JSONParser Test
 * 
 * White Box Test Case.
 * 
 * @author Taylor Williams
 *
 */
public class JSONParserTest extends AndroidTestCase {
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
	private final int TIMEOUT = 10000;
	List<NameValuePair> rgparams;
	JSONParser jsonparser;
    protected void setUp() throws Exception {
        super.setUp();
        rgparams = new ArrayList<NameValuePair>();
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_REG_ID, "badID"));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_USER, "badName"));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_PWORD, "badPword"));
        jsonparser = new JSONParser();
    }
    
    @Test(timeout = TIMEOUT)
    public void testPostRequest() {
    	JSONObject json = jsonparser.makeHttpRequest(ChatManager.URL_AUTHENTICATE, "POST", rgparams);
    	//if fSucceeded is changed to false, and no exception is thrown,
    	//then JSONParser did it's job and sent a request
    	boolean fSucceeded = true; 
    	try {
    		fSucceeded = json.getString(RequestParameters.PARAMETER_SUCCESS).equals("true");
    	} catch (JSONException e) {
            e.printStackTrace();
        }
    	assertFalse(fSucceeded);
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetRequest() {
    	JSONObject json = jsonparser.makeHttpRequest(ChatManager.URL_AUTHENTICATE, "GET", rgparams);
    	boolean fSucceeded = true;
    	try {
    		fSucceeded = json.getString(RequestParameters.PARAMETER_SUCCESS).equals("true");
    	} catch (JSONException e) {
            e.printStackTrace();
        }
    	assertFalse(fSucceeded);
    }
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
