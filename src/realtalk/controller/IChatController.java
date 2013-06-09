package realtalk.controller;

import java.util.List;

import android.content.Context;
import android.location.Location;
import realtalk.util.ChatRoomInfo;
import realtalk.util.MessageInfo;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;

public interface IChatController {
    /**
     * This initializes the ChatController by loading all the rooms that the user is currently in
     * from the last session. It takes a userinfo and is set to that user.
     * 
     * NOTE: Must be called from an async task
     * 
     * @param userinfo
     * @return true if initialization succeeded, false if it failed.
     */
    boolean fInitialize(UserInfo userinfo);
    
    /**
     * Returns a location if the Controller has stored a recent location (less than five minutes old)
     * 
     * @return a location if there exists a stored recent location, null otherwise.
     */
    Location getRecentLocation();
    
    /**
     * Adds a recent location to the controller.
     */
    void setRecentLocation(Location location);
    
    /**
     * Gets the current user logged in.
     * 
     * @return UserInfo describing the current user. null if logged out.
     */
    UserInfo getUser();
    
    /**
     * This method refreshes the chatrooms that the current instance user has joined.
     * 
     * NOTE: Must be called from an async task.
     * 
     * @return True if refresh succeeded, false if otherwise.
     */
    boolean fRefresh();
    
    /**
     * Adds a new message to the room. It also broadcasts to the chatroom activity if visible about the 
     * new message, prompting it to refresh itself.
     * 
     * @param msginfo  Message Info
     * @param roomId   Rooms Id
     * @param context  Context used to send broadcast.
     */
    void addMessageToRoom(MessageInfo msginfo, String roomId, Context context);
    
    /**
     * Returns an immutable list of messages that correspond to the given chatroom
     * 
     * @param roomId The rooms Id to retrieve messages from.
     * @return List of messages in an immutable read only list.
     */
    List<MessageInfo> getMessagesFromChatRoom(String roomId);
    
    /**
     * Gets a list of chatroominfo that represent the current state of the rooms available.
     * This method should be called after a successful initialize.
     * 
     * @return
     */
    List<ChatRoomInfo> getChatRooms();
    
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
    boolean joinRoom(ChatRoomInfo chatroom, boolean fAnon);
    
    /**
     * Leaves the specified room from the chatcontroller. This is the same as saying the user leaves the room.
     * 
     * @param chatroom
     * @return
     */
    boolean leaveRoom(ChatRoomInfo chatroom);
    
    /**
     * This method is used to uninitialize the chat controller. This should be used when a user is logged out.
     */
    void uninitialize();
    
    /**
     * Checks to see if the chatroom is already present in the controller. This is
     * same as saying that the user has already joined the room.
     * 
     * @param cri ChatRoomInfo
     * @return true if user is has already joined the chatroom and false if not.
     */
    boolean fIsAlreadyJoined(ChatRoomInfo cri);
    
    /**
     * Posts a message to a chatroom.
     * 
     * @param userinfo the user posting the message
     * @param chatroominfo the chatroom the message is being posted to
     * @param messageinfo the message being posted
     */
    RequestResultSet rrsPostMessage(UserInfo userinfo, ChatRoomInfo chatroominfo, MessageInfo messageinfo);
}
