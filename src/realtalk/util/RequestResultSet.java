package realtalk.util;

/**
 * This class is used as a container containing a succession flag and error codes/messages.
 * Used for indicating the result of a post request.
 * 
 * @author Taylor Williams
 *
 */
public class RequestResultSet {
	public boolean fSucceeded;
	public String stErrorMessage;
	public String stErrorCode;
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
}