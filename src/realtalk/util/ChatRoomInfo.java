/**
 * 
 */
package realtalk.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ChatRoomInfo is an immutable container class used to represent a chat room.
 * 
 * @author Colin Kho
 *
 */
public class ChatRoomInfo {
    private String stName;
    private String stId;
    private String stDescription;
    private double latitude;
    private double longitude;
    private String stCreator;
    private int numUsers;
    private Timestamp timestampCreated;
    
    /**
     * @param stName         Chat Room Name
     * @param stId           Chat Room ID
     * @param stDescription  Chat Room description
     * @param latitude     Chat Room latitude
     * @param longitude    Chat Room longitude
     * @param stCreator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param timestampCreated    Timestamp of when room was created.
     */
    public ChatRoomInfo(String stName, String stId, String stDescription, double latitude,
            double longitude, String stCreator, int numUsers, Timestamp timestampCreated) {
        this.stName = stName;
        this.stId = stId;
        this.stDescription = stDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stCreator = stCreator;
        this.numUsers = numUsers;
        if (timestampCreated == null) {
        	// Stamp it here
        	Date date = new Date();
        	timestampCreated = new Timestamp(date.getTime());
        }
        this.timestampCreated = timestampCreated;
    }
    
    /**
     * @param stName         Chat Room Name
     * @param stId           Chat Room Id
     * @param stDescription  Chat Room description
     * @param latitude     Chat Room latitude
     * @param longitude    Chat Room longitude
     * @param stCreator      Chat Room Creator
     * @param numUsers     Number of users in chat room
     * @param timestampCreated    Timestamp of when room was created in the
     *                     form of a long where it is the milliseconds 
     *                     since January 1, 1970, 00:00:00 GMT
     */
    public ChatRoomInfo(String stName, String stId, String stDescription, double latitude,
            double longitude, String stCreator, int numUsers, long timestampCreated) {
        this.stName = stName;
        this.stId = stId;
        this.stDescription = stDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stCreator = stCreator;
        this.numUsers = numUsers;
        this.timestampCreated = new Timestamp(timestampCreated);
    }

    /**
     * @return the name
     */
    public String stName() {
        return stName;
    }

    /**
     * @return the id
     */
    public String stId() {
        return stId;
    }

    /**
     * @return the description
     */
    public String stDescription() {
    	return stDescription;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
    	return latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
    	return longitude;
    }

    /**
     * @return the creator
     */
    public String stCreator() {
        return stCreator;
    }

    /**
     * @return the numUsers
     */
    public int numUsersGet() {
        return numUsers;
    }

	/**
	 * @return the timeStampCreated
	 */
	public Timestamp timestampCreated() {
		return new Timestamp(timestampCreated.getTime());
	}

}
