package realtalk.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class ChatManager {
	
	public static final String url_qualifier = "http://realtalkserver.herokuapp.com/";
	
	//User servlets
    public static final String url_add_user = url_qualifier+"register";
    public static final String url_remove_user = url_qualifier+"unregister";
    public static final String url_authenticate = url_qualifier+"authenticate";
    public static final String url_change_password = url_qualifier+"changePwd";
    public static final String url_change_id = url_qualifier+"changeRegId";
    //Chat room servlets
    public static final String url_add_room = url_qualifier+"addRoom";
    public static final String url_join_room = url_qualifier+"joinRoom";
    public static final String url_leave_room = url_qualifier+"leaveRoom";
    public static final String url_post_message = url_qualifier+"post";
    
    private static List<NameValuePair> rgparamsUserBasicInfo(User user) {
        List<NameValuePair> rgparams = new ArrayList<NameValuePair>();
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_REG_ID, user.getId()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_USER, user.getUsername()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_PWORD, user.getPassword()));
        return rgparams;
    }
    
    private static List<NameValuePair> rgparamsChatRoomBasicInfo(ChatRoomInfo chatroominfo) {
        List<NameValuePair> rgparams = new ArrayList<NameValuePair>();
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_ROOM_NAME, chatroominfo.getName()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_ROOM_ID, chatroominfo.getId()));
        return rgparams;
    }
    
    private static RequestResultSet makeUserPostRequest(List<NameValuePair> rgParams, String url, String stSuccess,
    		String stFailure)
    {
    	JSONObject json = null;
    	JSONParser jsonParser = new JSONParser();
		json = jsonParser.makeHttpRequest(url, "POST", rgParams);
        try {
        	boolean fSucceeded = json.getString(RequestParameters.PARAMETER_SUCCESS).equals("true");
            String stMessage = fSucceeded ? stSuccess : stFailure;
            return new RequestResultSet(fSucceeded, stMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return null;
    }
	
	public static RequestResultSet authenticateUser(User user) {
        List<NameValuePair> rgParams = rgparamsUserBasicInfo(user);
        return makeUserPostRequest(rgParams, url_authenticate, "Authenticated!", "Incorrect username/password.");
	}
	
	public static RequestResultSet addUser(User user) {
        List<NameValuePair> rgParams = rgparamsUserBasicInfo(user);
        return makeUserPostRequest(rgParams, url_add_user, "User added!", "User already exists.");
	}
	
	public static RequestResultSet removeUser(User user) {
        List<NameValuePair> rgParams = rgparamsUserBasicInfo(user);
        return makeUserPostRequest(rgParams, url_remove_user, "User removed!", "User does not exist.");
	}
	
	public static RequestResultSet changePassword(User user, String stPasswordNew) {
        List<NameValuePair> rgParams = rgparamsUserBasicInfo(user);
        rgParams.add(new BasicNameValuePair(RequestParameters.PARAMETER_NEW_PWORD, stPasswordNew));
        return makeUserPostRequest(rgParams, url_change_password, "Password changed.", "Could not change password.");
	}
	
	public static RequestResultSet changeID(User user, String stIdNew) {
        List<NameValuePair> rgParams = rgparamsUserBasicInfo(user);
        rgParams.add(new BasicNameValuePair(RequestParameters.PARAMETER_NEW_REG_ID, stIdNew));
        return makeUserPostRequest(rgParams, url_change_id, "ID changed.", "Could not change ID.");
	}
	
	public static RequestResultSet createRoom(ChatRoomInfo chatroominfo) {
		return null;
	}
	
	public static RequestResultSet joinRoom(User user, ChatRoomInfo chatroominfo) {
        List<NameValuePair> rgParams = rgparamsUserBasicInfo(user);
		return null;
	}
	
	public static RequestResultSet leaveRoom(User user, ChatRoomInfo chatroominfo) {
		return null;
	}
	
	public static RequestResultSet sendMessage(Message message) {
		return null;
	}
	
	public static RequestResultSet unregisterDevice() {
		return null;
	}
	
	public static List<Message> rgstChatLogGet(ChatRoomInfo chatroominfo) {
		return null;
	}
	
	public static List<User> rgUserGet(ChatRoomInfo chatroominfo) {
		return null;
	}
	
}
