/**
 * 
 */
package realtalk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import realtalk.util.ChatRoomInfo;
import realtalk.util.MessageInfo;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;
import android.content.Context;
import android.location.Location;

/**
 * Controller STUB
 * 
 * @author Taylor Williams
 *
 */
public final class ChatControllerStub implements IChatController {
    private static ChatControllerStub instance = null;
    private List<ChatRoomInfo> rgcri;
    private Map<String, List<MessageInfo>> mpstidRgmi;
    private UserInfo userinfo;
    
    private ChatControllerStub() {
        this.userinfo = null;
        mpstidRgmi = new HashMap<String, List<MessageInfo>>();
        rgcri = new ArrayList<ChatRoomInfo>();
    }
    
    /**
     * Static method that gets the current instance of the controller. It 
     * creates a new controller if the instance is not yet initialized.
     * 
     * @return ChatController's instance.
     */
    public static ChatControllerStub getInstance() {
        if (instance == null) {
            instance = new ChatControllerStub();
        }
        return instance;
    }
    
    /**
     * This initializes the ChatController by loading all the rooms that the user is currently in
     * from the last session. It takes a userinfo and is set to that user.
     * 
     * NOTE: Must be called from an async task
     * 
     * @param userinfo
     * @return true if initialization succeeded, false if it failed.
     */
    public boolean fInitialize(UserInfo userinfo) {
        this.userinfo = userinfo;
        return fRefresh();
    }
    
    /**
     * This method refreshes the chatrooms that the current instance user has joined.
     * 
     * NOTE: Must be called from an async task.
     * 
     * @return True if refresh succeeded, false if otherwise.
     */
    public boolean fRefresh() {  	
        return true;
    }
    
    /**
     * Adds a new message to the room. It also broadcasts to the chatroom activity if visible about the 
     * new message, prompting it to refresh itself.
     * 
     * @param msginfo  Message Info
     * @param roomId   Rooms Id
     * @param context  Context used to send broadcast.
     */
    public void addMessageToRoom(MessageInfo msginfo, String roomId, Context context) {
    	if (mpstidRgmi.containsKey(roomId)) {
    		mpstidRgmi.get(roomId).add(msginfo);
    	}
    }
    
    /**
     * Returns an immutable list of messages that correspond to the given chatroom
     * 
     * @param roomId The rooms Id to retrieve messages from.
     * @return List of messages in an immutable read only list.
     */
    public List<MessageInfo> getMessagesFromChatRoom(String roomId) {
        return mpstidRgmi.get(roomId);
    }
    
    /**
     * Gets a list of chatroominfo that represent the current state of the rooms available.
     * This method should be called after a successful initialize.
     * 
     * @return
     */
    public List<ChatRoomInfo> getChatRooms() {
    	return rgcri;
    }
    
    /**
     * Adds the specified room to the chatcontroller. This subsequently makes it load all the messages from the
     * server, keeping a cache of the messages. This is the same as saying the user joins the room.
     * 
     * NOTE: This method should only be called in an async task.
     * 
     * @param chatroom ChatRoomInfo describing the room to join.
     * @param fAnon whether the use is joining anonymously or not
     * @return True if succeeded, false if otherwise.
     */
    public boolean joinRoom(ChatRoomInfo chatroom, boolean fAnon) {
    	rgcri.add(chatroom);
    	mpstidRgmi.put(chatroom.stId(), new ArrayList<MessageInfo>());
        return true;
    }
    
    /**
     * Leaves the specified room from the chatcontroller. This is the same as saying the user leaves the room.
     * 
     * @param chatroom
     * @return
     */
    public boolean leaveRoom(ChatRoomInfo chatroom) {
    	return mpstidRgmi.remove(chatroom.stId()) != null;
    }
    
    /**
     * Gets the current user logged in.
     * 
     * @return UserInfo describing the current user. null if logged out.
     */
    public UserInfo getUser() {
        return userinfo;
    }
    
    /**
     * This method is used to uninitialize the chat controller. This should be used when a user is logged out.
     */
    public void uninitialize() {
        userinfo = null;
        mpstidRgmi = new HashMap<String, List<MessageInfo>>();
        rgcri = new ArrayList<ChatRoomInfo>();
    }
    
    /**
     * Checks to see if the chatroom is already present in the controller. This is
     * same as saying that the user has already joined the room.
     * 
     * @param cri ChatRoomInfo
     * @return true if user is has already joined the chatroom and false if not.
     */
    public boolean fIsAlreadyJoined(ChatRoomInfo cri) {
        for (ChatRoomInfo roominfo : rgcri) {
            if (roominfo.stId().equals(cri.stId())) {
                return true;
            }
        }
        return false;
    }

	@Override
	public Location getRecentLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRecentLocation(Location location) {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Posts a message to a chatroom.
     * 
     * @param userinfo the user posting the message
     * @param chatroominfo the chatroom the message is being posted to
     * @param messageinfo the message being posted
     */
	public RequestResultSet rrsPostMessage(UserInfo userinfo, ChatRoomInfo chatroominfo, MessageInfo messageinfo) {
		mpstidRgmi.get(chatroominfo.stId()).add(messageinfo);
		return new RequestResultSet(true, null, null);
	}
}
