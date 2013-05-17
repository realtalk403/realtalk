/**
 * 
 */
package realtalk.util;

import java.sql.Timestamp;

/**
 * Immutable class that holds a information about a chat message.
 * 
 * @author Colin Kho
 *
 */
public class MessageInfo implements Comparable<MessageInfo> {
    private String body;
    private String sender;
    private Timestamp timeStamp;  

    /**
     * Constructor
     * 
     * @param body
     * @param sender
     * @param date
     * @param timeStamp TimeStamp object of when room was created.
     */
    public MessageInfo(String body, String sender,Timestamp timeStamp) {
        this.body = body;
        this.sender = sender;
        this.timeStamp = new Timestamp(timeStamp.getTime());
    }
    
    /**
     * Constructor
     * 
     * @param body
     * @param sender
     * @param date
     * @param timeStamp Timestamp of when room was created in the
     *                  form of a long where it is the milliseconds 
     *                  since January 1, 1970, 00:00:00 GMT 
     */
    public MessageInfo(String body, String sender, long timeStamp) {
        this.body = body;
        this.sender = sender;
        this.timeStamp = new Timestamp(timeStamp);
    }

    /**
     * @return the body
     */
    public String stBody() {
        return body;
    }

    /**
     * @return the sender
     */
    public String stSender() {
        return sender;
    }
    
    /**
     * @return the timestamp
     */
	public Timestamp timestampGet() {
		return new Timestamp(timeStamp.getTime());
	}
	
	/**
	 * Defines on how MessageInfo objects are compared. They are compared according to
	 * their timestamps.
	 * 
	 * @param arg MessageInfo to be compared
	 * @return 0 if MessageInfo are equal, -1 if this has an earlier time stamp than arg
	 *         and 1 if this has a later time stamp than arg
	 */
	public int compareTo(MessageInfo arg) {
		return this.timeStamp.compareTo(arg.timeStamp);
	}
	
	/**
	 * Overriding equals method.
	 */
	@Override
	public boolean equals(Object obj) {
	    if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof MessageInfo))
            return false;
        MessageInfo miRhs = (MessageInfo) obj;
        return this.timeStamp.compareTo(miRhs.timeStamp) == 0;
	}
	
	/**
	 * Implements hashcode for message info.
	 */
	@Override
	public int hashCode() {
	    return body.hashCode();
	}
}
