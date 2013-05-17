/**
 * 
 */
package realtalk.controller;

import java.util.List;

import android.content.Context;

import realtalk.model.HallwayModel;
import realtalk.util.ChatManager;
import realtalk.util.ChatRoomInfo;
import realtalk.util.CommonUtilities;
import realtalk.util.MessageInfo;
import realtalk.util.PullMessageResultSet;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;

/**
 * Controller that handles the chatroom model and handles all internal logic.
 * 
 * @author Colin Kho
 *
 */
public final class ChatController {
    private static ChatController instance = null;
    private HallwayModel chatModel;
    // Keeps track of the current user in the RealTalk application.
    // Set to null if user not set / logged out.
    private UserInfo userinfo;
    
    private ChatController() {
        chatModel = new HallwayModel();
        this.userinfo = null;
    }
    
    /**
     * Static method that gets the current instance of the controller. It 
     * creates a new controller if the instance is not yet initialized.
     * 
     * @return ChatController's instance.
     */
    public static ChatController getInstance() {
        if (instance == null) {
            instance = new ChatController();
        }
        return instance;
    }
    
    public void initialize(UserInfo userinfo) {
        this.userinfo = userinfo;
    }
    
    /**
     * This updates the chatrooms information if user is in chatroom
     * 
     */
    public void fUpdateChatroom(ChatRoomInfo chatroom) {
        chatModel.updateChatroom(chatroom);
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
        if (chatModel.fChatRoomExists(roomId)) {
            chatModel.addMiToCrm(msginfo, roomId);
            
            // Alert ChatRoomActivity if running to retrieve the new message by refreshing view state.
            CommonUtilities.sendNewMessageAlert(context, roomId);
        }
    }
    
    /**
     * Returns an immutable list of messages that correspond to the given chatroom
     * 
     * @param roomId The rooms Id to retrieve messages from.
     * @return List of messages in an immutable read only list.
     */
    public List<MessageInfo> getMessagesFromChatRoom(String roomId) {
        return chatModel.rgmiGetFromCrmId(roomId);
    }
    
    /**
     * Gets a list of chatroominfo that represent the current state of the rooms available.
     * This method should be called after a successful initialize.
     * 
     * @return
     */
    public List<ChatRoomInfo> getChatRooms() {
        return chatModel.getRoomInfo();
    }
    
    /**
     * Adds the specified room to the chatcontroller. This subsequently makes it load all the messages from the
     * server, keeping a cache of the messages. This is the same as saying the user joins the room.
     * 
     * NOTE: This method should only be called in an async task.
     * 
     * @param chatroom ChatRoomInfo describing the room to join.
     * @return True if succeeded, false if otherwise.
     */
    public boolean joinRoom(ChatRoomInfo chatroom) {
        RequestResultSet crrs = ChatManager.rrsJoinRoom(userinfo, chatroom);
        if (!crrs.getfSucceeded()) {
            return false;
        }
        chatModel.addRoom(chatroom.stName(), 
                chatroom.stId(), 
                chatroom.stDescription(), 
                chatroom.getLatitude(), 
                chatroom.getLongitude(), 
                chatroom.stCreator(), 
                chatroom.timestampCreated());
        // Next load the messages for that chatroom.
        PullMessageResultSet pmrsMessages = ChatManager.pmrsChatLogGet(chatroom);
        if (!pmrsMessages.fIsSucceeded()) {
            // Else remove room and return failure
            chatModel.removeRoom(chatroom.stId());
            return false;
        } else {
            // Load the messages into the correct room.
            chatModel.addRgMiToCrm(pmrsMessages.getRgmessage(), chatroom.stId());
        }
        return true;
    }
    
    /**
     * Leaves the specified room from the chatcontroller. This is the same as saying the user leaves the room.
     * 
     * @param chatroom
     * @return
     */
    public boolean leaveRoom(ChatRoomInfo chatroom) {
        RequestResultSet crrs = ChatManager.rrsLeaveRoom(userinfo, chatroom);
        if (!crrs.getfSucceeded()) {
            return false;
        }
        return chatModel.removeRoom(chatroom.stId());
    }
    
    /**
     * Gets the current user logged in.
     * 
     * @return UserInfo describing the current user. null if logged out.
     */
    public UserInfo getUser() {
        return userinfo;
    }
}
