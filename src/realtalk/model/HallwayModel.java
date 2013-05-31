package realtalk.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import realtalk.util.ChatRoomInfo;
import realtalk.util.MessageInfo;


/**
 * This is the model class that essentially holds all the Chat Rooms.
 * Operations to update them should be run through here. 
 * 
 * @author Cory Shiraishi
 *
 */
public class HallwayModel {


    /*
     * A map of from the chat room id to the corresponding object
     * 
     * representation invariant:
     * key = value.stId()
     */
	private Map<String, ChatRoomModel> mpcricrm;
	
	public HallwayModel() {
		mpcricrm = new HashMap<String, ChatRoomModel>();
	}
	
	/**
	 * Gets the ChatRoomModel associated with the id 
	 * 
	 * @return crm associated with stId, or null if none exists.
	 */
	public ChatRoomModel crmGetFromId(String stcrmId) {
		return mpcricrm.get(stcrmId);
	}
	
	/**
	 * Gets an unmodifiable list of the messages from a chat room. If no chat room
	 * exists matching the cri, then this throws an IllegalArgumentException
	 * 
	 * @param cri
	 * @return returns an unmodifiable List<MessageInfo> of the messages in the chat room
	 * indicated by cri.
	 */
	public List<MessageInfo> rgmiGetFromCrmId(String stcrmId) {
		ChatRoomModel crm = crmGetFromId(stcrmId);
		if (crm == null) {
			throw new IllegalArgumentException("No room matching id: " +stcrmId+" exists.");
		}
		//Currently, this is an immutable list being returned.
		return crm.rgmiGetMessages();
	}
	
	/**
	 * Adds a new room to match the given cri
	 * 
	 * @param cri info for the room to be added
	 */
	public void addRoom(String stName, String stId, String stDescription, double latitude, 
						double longitude, String stCreator, Timestamp timestampCreated) {
		
		ChatRoomModel crm = new ChatRoomModel(stName, 
											  stId, 
											  stDescription, 
											  latitude, 
											  longitude, 
											  stCreator,
											  0,
											  timestampCreated);
		mpcricrm.put(stId, crm);
	}
	
	/**
	 * Adds a new room to match the given cri
	 * 
	 * @param cri info for the room to be added
	 */
	public void addRoom(String stName, String stId, String stDescription, double latitude, 
						double longitude, String stCreator, int numUsers, Timestamp timestampCreated) {
		
		ChatRoomModel crm = new ChatRoomModel(stName, 
											  stId, 
											  stDescription, 
											  latitude, 
											  longitude, 
											  stCreator,
											  numUsers,
											  timestampCreated);
		mpcricrm.put(stId, crm);
	}
	
	/**
	 * Removes an existing room from the model
	 * 
	 * @param stRoomId Chat Rooms Id
	 * 
	 * @return true if left room, false if room does not exist or an error has occurred leaving the room.
	 */
	public boolean fRemoveRoom(String stRoomId) {
	    if (mpcricrm.containsKey(stRoomId)) {
	        mpcricrm.remove(stRoomId);
	        return true;
	    } else {
	        return false;
	    }
	}
	
//	Unused, but keep this around in case we switch to explicit users rather than # of users	
//	/**
//	 * Adds a user to a room. Throws an IllegalArgumentException if no such room exists.
//	 * 
//	 * @param u user to add to the room
//	 * @param cri the room to be added to
//	 * @return if the user was added to the room. 
//	 */
//	public boolean fAddUToCrm(UserInfo u, String stcrmId) {
//		ChatRoomModel crm = crmGetFromId(stcrmId);
//		if (crm == null) {
//			throw new IllegalArgumentException("No room matching id: \"" + stcrmId + "\" exists.");
//		} else {
//			return crm.fAddU(u);
//		}
//	}
	
//	Unused, but keep this around in case we switch to explicit users rather than # of users
//	/**
//	 * Removes a user from a room. Throws an IllegalArgumentException if no such room exists.
//	 * 
//	 * @param u user to remove from the room
//	 * @param cri the room to be removed from
//	 * @return true if the user was removed from the room, false if the user was 
//	 * not in the room to start.
//	 */
//	public boolean fRemoveUFromCrm(UserInfo u, String stcrmId) {
//		ChatRoomModel crm = crmGetFromId(stcrmId);
//		if (crm == null) {
//			throw new IllegalArgumentException("No room matching id: \"" + stcrmId + "\" exists.");
//		} else {
//			return crm.fRemoveU(u);
//		}
//	}
	
	/**
	 * Adds a message to a chat room.
	 * 
	 * @param mi MessageInfo representing the message
	 * @param cri ChatRoomInfo corresponding to the target chat room.
	 */
	public void addMiToCrm(MessageInfo mi, String stcrmId) {
		ChatRoomModel crm = crmGetFromId(stcrmId);
		if (crm == null) {
			throw new IllegalArgumentException("No room matching id: \"" + stcrmId + "\" exists.");
		} else {
			crm.addMi(mi);
		}
	}
	
	/**
	 * Adds messages to a chat room. 
	 * 
	 * This method has less lookups than repeated calls to addMiToCrm().
	 * 
	 * @param rgmi a collection of MessageInfos representing the messages to be added
	 * @param cri ChatRoomInfo corresponding to the target chat room.
	 */
	public void addRgMiToCrm(Collection<MessageInfo> rgmi, String stcrmId) {
		ChatRoomModel crm = crmGetFromId(stcrmId);
		if (crm == null) {
			throw new IllegalArgumentException("No room matching id: \"" + stcrmId + " exists.");
		} else {
			for (MessageInfo mi : rgmi) {
				crm.addMi(mi);
			}
		}
	}
	
	/**
	 * Checks to see if a chatroom exists in the model.
	 * 
	 * @param stRoomId Chatrooms Id
	 * @return         true if it exists, otherwise false.
	 */
	public boolean fChatRoomExists(String stRoomId) {
	    return mpcricrm.containsKey(stRoomId);
	}
	
	/**
	 * Updates a chatroom in the model using the given chatroominfo. This updates all fields except messages.
	 * If chatroom does not exist, it does nothing.
	 * 
	 * @param chatroominfo Information about chatroom to update.
	 */
	public void updateChatroom(ChatRoomInfo chatroominfo) {
	    if (fChatRoomExists(chatroominfo.stId())) {
	        ChatRoomModel room = crmGetFromId(chatroominfo.stId());
	        room.setName(chatroominfo.stName());
	        room.setCreator(chatroominfo.stCreator());
	        room.setDescription(chatroominfo.stDescription());
	        room.setLongitude(chatroominfo.getLongitude());
	        room.setLatitude(chatroominfo.getLatitude());
	        room.setNumUsers(chatroominfo.numUsersGet());
	        room.setTimestampCreated(chatroominfo.timestampCreated());
	    }
	}
	
	/**
	 * Gets a list of the rooms currently held in the ChatModel
	 * 
	 * @return List of ChatRoomInfo
	 */
	public List<ChatRoomInfo> getRoomInfo() {
	    List<ChatRoomInfo> rgcri = new ArrayList<ChatRoomInfo>();
	    for (Entry<String, ChatRoomModel> entry : mpcricrm.entrySet()) {
	        ChatRoomModel room = entry.getValue();
	        ChatRoomInfo roominfo = new ChatRoomInfo(room.stName(), 
	                room.stId(), room.stDescription(), room.getLatitude(), 
	                room.getLongitude(), room.stCreator(), room.getNumUsers(), 
	                room.timestampCreated());
	        rgcri.add(roominfo);
	    }
	    return rgcri;
	}
}
