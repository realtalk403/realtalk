package realtalk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * JSONParser is a helper class that makes a generic POST/GET request to a given url,
 * with embedded params.
 * 
 * @author Taylor Williams / Colin Kho
 *
 */
public final class JSONParser {
    private static final int BUFFER_SIZE = 8;
    
    /*
     * Private constructor to disable default public one
     */

    private JSONParser() {
        throw new UnsupportedOperationException();
    }
        
    /**
     * This method takes in an InputStream that contains JSON data and returns
     * a JSON object representing that data.
     * 
     * @param inputstream Stream containing JSON data
     * @return json data as a JSONObject
     * @throws JSONException If stream does not contain valid JSON
     * @throws IOException   If an error was encountered parsing the JSON.
     */
    public static JSONObject parseStream(InputStream inputstream) 
    		throws JSONException, IOException {
    	if (inputstream == null) {
    		return null;
    	}
    	BufferedReader reader = null;
    	String stJson = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    inputstream, "iso-8859-1"), BUFFER_SIZE);
            StringBuilder stringbuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringbuilder.append(line + "\n");
            }
            if (inputstream != null) {
                inputstream.close();
            }
            stJson = stringbuilder.toString();
            reader.close();
        } catch (IOException e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        } finally {
        	try {
        		if (reader != null) {
        			reader.close();
        		}
        	} catch (Exception e) {
        		Log.e("Reader Closing Error", e.toString());
        		e.printStackTrace();
        	}
        }
 
        // try parse the string to a JSON object
        JSONObject jsonobject = new JSONObject(stJson);
        // return JSON String
        return jsonobject;
    }
}
