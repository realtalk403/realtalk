package realtalk.util;

public class RequestResultSet {
	public boolean fSucceeded;
	public String stErrorMessage;
	public String stErrorCode;
	public RequestResultSet(boolean fSucceeded, String stErrorCode, String stErrorMessage) {
		this.fSucceeded = fSucceeded;
		this.stErrorMessage = stErrorMessage;
		this.stErrorCode = stErrorCode;
	}
}
