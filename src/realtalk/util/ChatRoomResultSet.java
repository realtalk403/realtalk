package realtalk.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a container containing a succession flag and error codes/messages.
 * Used for indicating the result of a post request.
 * 
 * @author Taylor Williams
 *
 */
public class ChatRoomResultSet {
	//shows if pull message request succeeded
	boolean fSucceeded;
	//stError is only populated if the request failed
	public String stErrorMessage;
	public String stErrorCode;
	//rgmessage is only populated if request succeeded
	public List<ChatRoomInfo> rgcri;
	public ChatRoomResultSet(boolean fSucceeded, List<ChatRoomInfo> rgcri, String stErrorCode, String stErrorMessage) {
		this.fSucceeded = fSucceeded;
		this.stErrorMessage = stErrorMessage;
		this.stErrorCode = stErrorCode;
		this.rgcri = new ArrayList<ChatRoomInfo>(rgcri);
	}
	
   /**
	 * Constructor that does not take a list of message info.
	 * 
	 * @param fSucceeded
	 * @param stErrorMessage
	 * @param stErrorCode
	 */
	public ChatRoomResultSet(boolean fSucceeded, String stErrorCode, String stErrorMessage) {
		this.rgcri = new ArrayList<ChatRoomInfo>();
		this.fSucceeded = fSucceeded;
		this.stErrorMessage = stErrorMessage;
		this.stErrorCode = stErrorCode;
	}
	
	
}
