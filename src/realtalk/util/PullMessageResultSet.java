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
public class PullMessageResultSet {
	//shows if pull message request succeeded
	boolean fSucceeded;

    //stError is only populated if the request failed
	public String stErrorMessage;
	public String stErrorCode;
	//rgmessage is only populated if request succeeded
	public List<MessageInfo> rgmessage;
	public PullMessageResultSet(boolean fSucceeded, List<MessageInfo> rgmessage, String stErrorCode, String stErrorMessage) {
		this.fSucceeded = fSucceeded;
		this.stErrorMessage = stErrorMessage;
		this.stErrorCode = stErrorCode;
		this.rgmessage = new ArrayList<MessageInfo>(rgmessage);
	}
	
   /**
	 * Constructor that does not take a list of message info.
	 * 
	 * @param fSucceeded
	 * @param stErrorMessage
	 * @param stErrorCode
	 */
	public PullMessageResultSet(boolean fSucceeded, String stErrorMessage,
			String stErrorCode) {
		this.rgmessage = new ArrayList<MessageInfo>();
		this.fSucceeded = fSucceeded;
		this.stErrorMessage = stErrorMessage;
		this.stErrorCode = stErrorCode;
	}
	
	/**
     * @return the fSucceeded
     */
    public boolean fIsSucceeded() {
        return fSucceeded;
    }

    /**
     * @return the rgmessage
     */
    public List<MessageInfo> getRgmessage() {
        return new ArrayList<MessageInfo>(rgmessage);
    }
    
}
