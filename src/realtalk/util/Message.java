package realtalk.util;

import java.util.Date;

public class Message {
	String stMessage;
	User user;
	Date dateTimestamp;
	
	public Message(String stMessage, User user, Date dateTimestamp) {
		this.stMessage = stMessage;
		this.user = user;
		this.dateTimestamp = dateTimestamp;
	}
	
	public String toString() {
		return stMessage + "\n" +
			   "sent by " + user + " at " + dateTimestamp;
	}
}
