package realtalk.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import realtalk.util.MessageInfo;
import realtalk.util.UserInfo;


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
		thisDoesntExist = thisCodeShouldBreakTheBuild //Used to test the Jenkins server. 
		//TODO remove ^ after Jenkins successfully fails
	}
	
//	/**
//	 * Gets the ChatRoomModel associated with the ChatRoomInfo. 
//	 * 
//	 * @return crm associated with cri, or null if none exists.
//	 */
//	private ChatRoomModel crmGetFromCri(ChatRoomInfo cri) {
//		return mpcricrm.get(cri.stId());
//	}
	
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
											  timestampCreated);
		mpcricrm.put(stId, crm);
	}
	
	/**
	 * Removes the room matching the given cri
	 * 
	 * @param cri info for the room to be removed
	 */
	public void deleteRoom(String stcrmId) {
		mpcricrm.remove(stcrmId);
	}
	
	/**
	 * Adds a user to a room. Throws an IllegalArgumentException if no such room exists.
	 * 
	 * @param u user to add to the room
	 * @param cri the room to be added to
	 * @return if the user was added to the room. 
	 */
	public boolean fAddUToCrm(UserInfo u, String stcrmId) {
		ChatRoomModel crm = crmGetFromId(stcrmId);
		if (crm == null) {
			throw new IllegalArgumentException("No room matching id: \"" + stcrmId + "\" exists.");
		} else {
			return crm.fAddU(u);
		}
	}
	
	/**
	 * Removes a user from a room. Throws an IllegalArgumentException if no such room exists.
	 * 
	 * @param u user to remove from the room
	 * @param cri the room to be removed from
	 * @return true if the user was removed from the room, false if the user was 
	 * not in the room to start.
	 */
	public boolean fRemoveUFromCrm(UserInfo u, String stcrmId) {
		ChatRoomModel crm = crmGetFromId(stcrmId);
		if (crm == null) {
			throw new IllegalArgumentException("No room matching id: \"" + stcrmId + "\" exists.");
		} else {
			return crm.fRemoveU(u);
		}
	}
	
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
}
