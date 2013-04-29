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
    public static final String TAG_USER = "user";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_NAME = "name";
    public static final String TAG_ID = "id";
	
	public ChatManager() {
		
	}
	
	public boolean authenticate(User user) {
        int fAuthenticated = 0;
        try {
            // Make GET params in NameValuePair List
            List<NameValuePair> rgParams = new ArrayList<NameValuePair>();
            rgParams.add(new BasicNameValuePair("user", user.username));
            
            // Make http request to obtain results
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_get_user_id, "GET", rgParams);
            
            fAuthenticated = json.getInt(TAG_SUCCESS);
            return fAuthenticated == 1;
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	public boolean addUser(User user) {
        String fAddSucceeded;
        try {
            List<NameValuePair> rgParams = new ArrayList<NameValuePair>();
            rgParams.add(new BasicNameValuePair("PARAMETER_REG_ID", user.id));
            rgParams.add(new BasicNameValuePair("PARAMETER_USER", user.username));
            rgParams.add(new BasicNameValuePair("PARAMETER_PWORD", user.password));
            
            // Make http request to obtain results
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_add_user, "POST", rgParams);
            
            fAddSucceeded = json.getString("PARAMETER_REG_ID");
            return fAddSucceeded == user.id;
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	public boolean removeUser(User user) {
		return true;
	}
	
	public boolean changePassword(User user, String stPasswordNew) {
		return true;
	}
	
	public boolean changeID(User user, String stIDNew) {
		return true;
	}
	
	public boolean sendMessage(Message message) {
		return true;
	}
	
	List<Message> rgstChatLogGet(String stRoomName) {
		return null;
	}
	
	public boolean unregisterDevice() {
		return true;
	}
	
	List<User> rgUserGet(String stRoomName) {
		return null;
	}
	
	public boolean joinRoom(String stRoomName) {
		return true;
	}
	
	public boolean leaveRoom(String stRoomName) {
		return true;
	}
	
	public boolean createRoom(String stRoomName) {
		return true;
	}
}
