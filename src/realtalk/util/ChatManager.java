package realtalk.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ChatManager is a helper class that allows the Android side of RealTalk to cleanly
 * communicate with the server while keeping it abstracted.
 * 
 * @author Taylor Williams
 *
 */
public class ChatManager {
	
	//HUNGARIAN TAGS:
	//	rrs		RequestResultSet
	//	pmrs	PullMessageResultSet
	
	public static final String url_qualifier = "http://realtalkserveralpha.herokuapp.com/";
	
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
    public static final String url_get_recent_messages = url_qualifier+"pullRecentChat";
    public static final String url_get_all_messages = url_qualifier + "pullChat";
    
    
    /**
     * @param messageinfo         Message info object
     * @return the list of parameters as basic name value pairs
     */
    private static List<NameValuePair> rgparamsMessageInfo(MessageInfo messageinfo) {
        List<NameValuePair> rgparams = new ArrayList<NameValuePair>();
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_MESSAGE_TIMESTAMP, Long.valueOf(messageinfo.timestampGet().getTime()).toString()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_MESSAGE_BODY, messageinfo.stBody()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_MESSAGE_SENDER, messageinfo.stSender()));
        return rgparams;
    }
    
    /**
     * @param userinfo         User info object
     * @return the list of parameters as basic name value pairs
     */
    private static List<NameValuePair> rgparamsUserBasicInfo(UserInfo userinfo) {
        List<NameValuePair> rgparams = new ArrayList<NameValuePair>();
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_REG_ID, userinfo.stRegistrationId()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_USER, userinfo.stUserName()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_PWORD, userinfo.stPassword()));
        return rgparams;
    }
    
    /**
     * @param chatroominfo         Chat room info object
     * @return the list of parameters as basic name value pairs
     */
    private static List<NameValuePair> rgparamsChatRoomBasicInfo(ChatRoomInfo chatroominfo) {
        List<NameValuePair> rgparams = new ArrayList<NameValuePair>();
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_ROOM_NAME, chatroominfo.stName()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_ROOM_ID, chatroominfo.stId()));
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_ROOM_DESCRIPTION, chatroominfo.stDescription()));
        return rgparams;
    }
    
    /**
     * @param rgparam         List of parameters to embed in the request
     * @param stUrl			The url to send the request to
     * @return A resultset containing the result of the request
     */
    private static RequestResultSet rrsMakePostRequest(List<NameValuePair> rgparam, String stUrl) {
    	JSONObject json = null;
    	JSONParser jsonParser = new JSONParser();
		json = jsonParser.makeHttpRequest(stUrl, "POST", rgparam);
        try {
        	boolean fSucceeded = json.getString(RequestParameters.PARAMETER_SUCCESS).equals("true");
        	String stErrorCode = fSucceeded ? "NO ERROR MESSAGE" : json.getString(ResponseParameters.PARAMETER_ERROR_CODE);
        	String stErrorMessage = fSucceeded ? "NO ERROR MESSAGE" : json.getString(ResponseParameters.PARAMETER_ERROR_MSG);
            return new RequestResultSet(fSucceeded, stErrorCode, stErrorMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return null;
    }
    
    /** Sends a message/chatroom specific request.
     * @param rgparam         List of parameters to embed in the request
     * @param stUrl			The url to send the request to
     * @return A resultset containing the result of the request
     */
    private static PullMessageResultSet pmrsPostRequest(List<NameValuePair> rgparam, String stUrl) {
    	JSONObject json = null;
    	JSONParser jsonParser = new JSONParser();
		json = jsonParser.makeHttpRequest(stUrl, "POST", rgparam);
        try {
        	boolean fSucceeded = json.getString(RequestParameters.PARAMETER_SUCCESS).equals("true");
        	if (fSucceeded) {
        		List<MessageInfo> rgmessageinfo = new ArrayList<MessageInfo>();
        		JSONArray rgmessage = json.getJSONArray(RequestParameters.PARAMETER_MESSAGE_MESSAGES);
        		for (int i = 0; i < rgmessage.length(); i++) {
        			JSONObject jsonobject = rgmessage.getJSONObject(i);
        			String stBody = jsonobject.getString(RequestParameters.PARAMETER_MESSAGE_BODY);
        			long ticks = jsonobject.getLong(RequestParameters.PARAMETER_MESSAGE_TIMESTAMP);
        			String stSender = jsonobject.getString(RequestParameters.PARAMETER_MESSAGE_SENDER);
        			rgmessageinfo.add(new MessageInfo(stBody, stSender, ticks));
        		}
        		return new PullMessageResultSet(true, rgmessageinfo, "NO ERROR CODE", "NO ERROR MESSAGE");
        	}
            return new PullMessageResultSet(false, new ArrayList<MessageInfo>(), 
            		json.getString(ResponseParameters.PARAMETER_ERROR_CODE), 
            		json.getString(ResponseParameters.PARAMETER_ERROR_MSG));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    	return null;
    }
	
    /** Authenticates a user
     * @param userinfo		The user to authenticate
     * @return A resultset containing the result of the authentication
     */
	public static RequestResultSet rrsAuthenticateUser(UserInfo userinfo) {
        List<NameValuePair> rgparams = rgparamsUserBasicInfo(userinfo);
        return rrsMakePostRequest(rgparams, url_authenticate);
	}
	
    /** Adds a user
     * @param userinfo		The user to add
     * @return A resultset containing the result of the addition
     */
	public static RequestResultSet rrsAddUser(UserInfo userinfo) {
        List<NameValuePair> rgparams = rgparamsUserBasicInfo(userinfo);
        return rrsMakePostRequest(rgparams, url_add_user);
	}
	
    /** Remove a user
     * @param userinfo		The user to remove
     * @return A resultset containing the result of the removal
     */
	public static RequestResultSet rrsRemoveUser(UserInfo userinfo) {
        List<NameValuePair> rgparams = rgparamsUserBasicInfo(userinfo);
        return rrsMakePostRequest(rgparams, url_remove_user);
	}
	
    /** Changes a user's password
     * @param userinfo		The user to change
     * @param stPasswordNew		The new password
     * @return A resultset containing the result of the change
     */
	public static RequestResultSet rrsChangePassword(UserInfo userinfo, String stPasswordNew) {
        List<NameValuePair> rgparams = rgparamsUserBasicInfo(userinfo);
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_NEW_PWORD, stPasswordNew));
        return rrsMakePostRequest(rgparams, url_change_password);
	}
	
    /** Changes a user's ID
     * @param userinfo		The user to change
     * @param stIdNew		The new ID
     * @return A resultset containing the result of the change
     */
	public static RequestResultSet rrsChangeID(UserInfo userinfo, String stIdNew) {
        List<NameValuePair> rgparams = rgparamsUserBasicInfo(userinfo);
        rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_NEW_REG_ID, stIdNew));
        return rrsMakePostRequest(rgparams, url_change_id);
	}
	
    /** Adds a new chatroom
     * @param chatroominfo		The chatroom to add
     * @param userinfo		The user to associate with the new room
     * @return A resultset containing the result of the addition
     */
	public static RequestResultSet rrsAddRoom(ChatRoomInfo chatroominfo, UserInfo userinfo) {
        List<NameValuePair> rgparams = rgparamsChatRoomBasicInfo(chatroominfo);
        rgparams.addAll(rgparamsUserBasicInfo(userinfo));
		return rrsMakePostRequest(rgparams, url_add_room);
	}
	
    /** Joins a user to a chatroom
     * @param chatroominfo		The chatroom to join
     * @param userinfo		The user to join into the room
     * @return A resultset containing the result of the join
     */
	public static RequestResultSet rrsJoinRoom(UserInfo userinfo, ChatRoomInfo chatroominfo) {
        List<NameValuePair> rgparams = rgparamsUserBasicInfo(userinfo);
        rgparams.addAll(rgparamsChatRoomBasicInfo(chatroominfo));
		return rrsMakePostRequest(rgparams, url_join_room);
	}
	
    /** Leaves a chatroom
     * @param chatroominfo		The chatroom to leave
     * @param userinfo		The user leaving the room
     * @return A resultset containing the result of the leave
     */
	public static RequestResultSet rrsLeaveRoom(UserInfo userinfo, ChatRoomInfo chatroominfo) {
        List<NameValuePair> rgparams = rgparamsUserBasicInfo(userinfo);
        rgparams.addAll(rgparamsChatRoomBasicInfo(chatroominfo));
		return rrsMakePostRequest(rgparams, url_leave_room);
	}
	
    /** Posts a message to a chatroom
     * @param chatroominfo		The chatroom to post a message to
     * @param userinfo		The user posting the message
     * @return A resultset containing the result of the post
     */
	public static RequestResultSet rrsPostMessage(UserInfo userinfo, ChatRoomInfo chatroominfo, MessageInfo message) {
        List<NameValuePair> rgparams = rgparamsUserBasicInfo(userinfo);
        rgparams.addAll(rgparamsChatRoomBasicInfo(chatroominfo));
        rgparams.addAll(rgparamsMessageInfo(message));
		return rrsMakePostRequest(rgparams, url_post_message);
	}
	
    /** Returns the chatlog for a certain chatroom
     * @param chatroominfo		The chatroom to pull the log from
     * @return A resultset containing the result of the pull
     */
	public static PullMessageResultSet pmrsChatLogGet(ChatRoomInfo chatroominfo) {
        List<NameValuePair> rgparams = rgparamsChatRoomBasicInfo(chatroominfo);
		return pmrsPostRequest(rgparams, url_get_all_messages);
	}
	
	/**
	 * This method pulls all recent messages to a specific given chatroom after a given time as indicated
	 * in timestamp
	 * 
	 * @param chatroominfo Information about chatroom to pull messages from.
	 * @param timestamp    Time 
	 * @return             Result set that contains a boolean that indicates success or failure and 
	 *                     returns an error code and message if failure was occurred. If success,
	 *                     it returns a list of MessageInfo that have a timestamp later than the given
	 *                     timestamp
	 */
	public static PullMessageResultSet pmrsChatRecentChat(ChatRoomInfo chatroominfo, Timestamp timestamp) {
		long rawtimestamp = timestamp.getTime();
		String stTimestamp = "";
		try {
			stTimestamp = String.valueOf(rawtimestamp);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return new PullMessageResultSet(false, "ERROR_INVALID_TIMESTAMP", "ERROR_MESSAGE_PARSING_ERROR");
		}
		List<NameValuePair> rgparams = rgparamsChatRoomBasicInfo(chatroominfo);
		rgparams.add(new BasicNameValuePair(RequestParameters.PARAMETER_TIMESTAMP, stTimestamp));
		return pmrsPostRequest(rgparams, url_get_recent_messages);
	}
	
}
