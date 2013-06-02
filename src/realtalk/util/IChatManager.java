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
	public RequestResultSet rrsJoinRoom(UserInfo userinfo, ChatRoomInfo chatroominfo);
	public ChatRoomResultSet crrsUsersChatrooms(UserInfo userinfo);
	public RequestResultSet rrsLeaveRoom(UserInfo userinfo, ChatRoomInfo chatroominfo);
	public PullMessageResultSet pmrsChatLogGet(ChatRoomInfo chatroominfo);
}
