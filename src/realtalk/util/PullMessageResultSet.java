package realtalk.util;

import java.util.List;

public class PullMessageResultSet {
	//shows if pull message request succeeded
	boolean fSucceeded;
	//stError is only populated if the request failed
	public String stError;
	//rgmessage is only populated if request succeeded
	List<MessageInfo> rgmessage;
	public PullMessageResultSet(boolean fSucceeded, List<MessageInfo> rgmessage, String stError) {
		this.fSucceeded = fSucceeded;
		this.stError = stError;
		this.rgmessage = rgmessage;
	}
}
