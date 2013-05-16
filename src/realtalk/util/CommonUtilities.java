package realtalk.util;

import android.content.Context;
import android.content.Intent;

/**
 * Helper class that defines methods and constants common to other classes in the app.
 * 
 * @author Colin Kho
 *
 */
public class CommonUtilities {
    /**
     * Base URL of the RealTalk Server
     */
    public static final String SERVER_URL = "http://realtalkserver.herokuapp.com";
    
    /**
     * Intent used to indicate that new message should be retrieved
     */
    public static final String NEW_MESSAGE_ALERT = "realtalk.activities.NEW_MESSAGE_ALERT";
    
    /**
     * Intent extra value to denote the room id that new message has been added to.
     */
    public static final String ROOM_ID = "chatroom_id";
    
    /**
     * Method that sends a broadcast notifying that a new message has been added to the chatroom.
     * 
     * @param context  Context it is used in.
     * @param roomId   Room's Id
     */
    public static void sendNewMessageAlert(Context context, String roomId) {
        Intent intent = new Intent(NEW_MESSAGE_ALERT);
        intent.putExtra(ROOM_ID, roomId);
        context.sendBroadcast(intent);
    }
}
