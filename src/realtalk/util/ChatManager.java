package realtalk.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class ChatManager {
	
    public static final String url_get_user_id = "http://chatrealtalk.herokuapp.com/db_get_users.php";
    public static final String url_add_user = "http://realtalkserver.herokuapp.com/register";
    public static final String url_remove_user = "http://realtalkserver.herokuapp.com/unregister";
    public static final String url_authenticate = "http://realtalkserver.herokuapp.com/authenticate";
    public static final String TAG_SUCCESS = "success";
    
    private static JSONObject makePostRequest(List<NameValuePair> rgParams, String url)
    {
    	JSONObject json = null;
    	JSONParser jsonParser = new JSONParser();
		json = jsonParser.makeHttpRequest(url, "POST", rgParams);
    	return json;
    }
	
	public static boolean authenticateUser(User user) {
        boolean fAuthenticated;
        List<NameValuePair> rgParams = new ArrayList<NameValuePair>();
        rgParams.add(new BasicNameValuePair("PARAMETER_REG_ID", user.id));
        rgParams.add(new BasicNameValuePair("PARAMETER_USER", user.username));
        rgParams.add(new BasicNameValuePair("PARAMETER_PWORD", user.password));
        JSONObject json = makePostRequest(rgParams, url_authenticate);
        try {
            fAuthenticated = json.getString(TAG_SUCCESS).equals("true");
            return fAuthenticated;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	public static boolean addUser(User user) {
        boolean fAdded;
        List<NameValuePair> rgParams = new ArrayList<NameValuePair>();
        rgParams.add(new BasicNameValuePair("PARAMETER_REG_ID", user.id));
        rgParams.add(new BasicNameValuePair("PARAMETER_USER", user.username));
        rgParams.add(new BasicNameValuePair("PARAMETER_PWORD", user.password));
        JSONObject json = makePostRequest(rgParams, url_add_user);
        try {
            fAdded = json.getString(TAG_SUCCESS).equals("true");
            return fAdded;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	public static boolean removeUser(User user) {
        boolean fRemoved;
        List<NameValuePair> rgParams = new ArrayList<NameValuePair>();
        rgParams.add(new BasicNameValuePair("PARAMETER_REG_ID", user.id));
        rgParams.add(new BasicNameValuePair("PARAMETER_USER", user.username));
        rgParams.add(new BasicNameValuePair("PARAMETER_PWORD", user.password));
        JSONObject json = makePostRequest(rgParams, url_remove_user);
        try {
            fRemoved = json.getString(TAG_SUCCESS).equals("true");
            return fRemoved;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	public static boolean changePassword(User user, String stPasswordNew) {
		return true;
	}
	
	public static boolean changeID(User user, String stIDNew) {
		return true;
	}
	
	public static boolean sendMessage(Message message) {
		return true;
	}
	
	public static List<Message> rgstChatLogGet(String stRoomName) {
		return null;
	}
	
	public static boolean unregisterDevice() {
		return true;
	}
	
	public static List<User> rgUserGet(String stRoomName) {
		return null;
	}
	
	public static boolean joinRoom(String stRoomName) {
		return true;
	}
	
	public static boolean leaveRoom(String stRoomName) {
		return true;
	}
	
	public static boolean createRoom(String stRoomName) {
		return true;
	}
}
