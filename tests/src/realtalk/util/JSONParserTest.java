package realtalk.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

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
	private List<NameValuePair> rgparams;
	InputStream inputstreamResponse;
    protected void setUp() throws Exception {
        super.setUp();
        rgparams = new ArrayList<NameValuePair>();
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_REG_ID, "badID"));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_USER, "badName"));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_PWORD, "badPword"));
        try {
            inputstreamResponse = HttpUtility.sendPostRequest(ChatManager.URL_AUTHENTICATE, rgparams);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test(timeout = TIMEOUT)
    public void testPostRequest() {
        JSONObject jsonobjectResponse = null;
        
        try {
            jsonobjectResponse = JSONParser.parseStream(inputstreamResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonobjectResponse = new JSONObject();
        } catch (IOException e) {
            e.printStackTrace();
            jsonobjectResponse = new JSONObject();
        }
    	//if fSucceeded is changed to false, and no exception is thrown,
    	//then JSONParser did it's job and sent a request
    	boolean fSucceeded = true; 
    	try {
    		fSucceeded = jsonobjectResponse.getString(RequestParameters.PARAMETER_SUCCESS).equals("true");
    	} catch (JSONException e) {
            e.printStackTrace();
        }
    	assertFalse(fSucceeded);
    }
    
    @Test(timeout = TIMEOUT)
    public void testGetRequest() {
        JSONObject jsonobjectResponse = null;
        
        try {
            jsonobjectResponse = JSONParser.parseStream(inputstreamResponse);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonobjectResponse = new JSONObject();
        } catch (IOException e) {
            e.printStackTrace();
            jsonobjectResponse = new JSONObject();
        }
    	boolean fSucceeded = true;
    	try {
    		fSucceeded = jsonobjectResponse.getString(RequestParameters.PARAMETER_SUCCESS).equals("true");
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
