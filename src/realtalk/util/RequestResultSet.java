package realtalk.util;

/**
 * This class is used as a container containing a succession flag and error codes/messages.
 * Used for indicating the result of a post request.
 * 
 * @author Taylor Williams
 *
 */
public class RequestResultSet {
	private boolean fSucceeded;
	private String stErrorMessage;
	private String stErrorCode;
	/** Constructor that stores flag and error indicators
	 *@param fSucceeded		flag indicating succession
	 *@param stErrorCode	the error code
	 *@param stErrorMessage	error message detailing the failure
	 */
	public RequestResultSet(boolean fSucceeded, String stErrorCode, String stErrorMessage) {
		this.fSucceeded = fSucceeded;
		this.stErrorMessage = stErrorMessage;
		this.stErrorCode = stErrorCode;
	}
    /**
     * @return the stErrorMessage
     */
    public String getStErrorMessage() {
        return stErrorMessage;
    }
    /**
     * @return the stErrorCode
     */
    public String getStErrorCode() {
        return stErrorCode;
    }
    
    /**
     * @return the fSucceeded
     */
    public boolean getfSucceeded() {
    	return fSucceeded;
    }
}
