/**
 * 
 */
package realtalk.util;

/**
 * ChatManager interface used for Mock Testing.
 * 
 * @author Colin Kho
 *
 */
public interface IChatManager {
	RequestResultSet rrsJoinRoom(UserInfo userinfo, ChatRoomInfo chatroominfo, boolean fAnon);
	ChatRoomResultSet crrsUsersChatrooms(UserInfo userinfo);
	RequestResultSet rrsLeaveRoom(UserInfo userinfo, ChatRoomInfo chatroominfo);
	PullMessageResultSet pmrsChatLogGet(ChatRoomInfo chatroominfo);
}
