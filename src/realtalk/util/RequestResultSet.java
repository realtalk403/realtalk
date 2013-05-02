package realtalk.util;

public class RequestResultSet {
	boolean fSucceeded;
	public String stMessage;
	public RequestResultSet(boolean fSucceeded, String stMessage) {
		this.fSucceeded = fSucceeded;
		this.stMessage = stMessage;
	}
}
