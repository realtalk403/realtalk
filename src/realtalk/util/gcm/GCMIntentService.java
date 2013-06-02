package realtalk.util.gcm;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.realtalk.R;

import realtalk.activities.LoginActivity;
import realtalk.controller.ChatController;
import realtalk.util.MessageInfo;
import realtalk.util.RequestParameters;

/**
 * IntentService responsible for handling GCM messages
 * 
 * @author Colin Kho
 *
 */
public class GCMIntentService extends GCMBaseIntentService {
    
    public GCMIntentService() {
        super(GCMUtilities.SENDER_ID);
    }
    
    @Override
    protected void onError(Context context, String stRegId) {
        Log.v("GCMIntentService", "Registration Error");
        GCMUtilities.sendRegistrationResult(context, stRegId, GCMUtilities.ERROR);
        
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.v("GCMIntentService", "Received Message");
        handleMessage(context, intent);
    }

    @Override
    protected void onRegistered(Context context, String stRegId) {
        Log.v("GCMIntentService", "Registration Successful");
        GCMUtilities.sendRegistrationResult(context, stRegId, GCMUtilities.SUCCESS);       
    }

    @Override
    protected void onUnregistered(Context context, String stRegId) {        
    }
    
    /**
     * Helper method that handles the message received by GCM.
     * 
     * @param context Context it is received in.
     * @param intent  Intent containing GCM message.
     */
    private void handleMessage(Context context, Intent intent) {
        Log.v("GCMIntentService", "Handling Message");
        String stSender = intent.getStringExtra(RequestParameters.PARAMETER_MESSAGE_SENDER);
        String stTimestamp = intent.getStringExtra(RequestParameters.PARAMETER_MESSAGE_TIMESTAMP);
        String stBody = intent.getStringExtra(RequestParameters.PARAMETER_MESSAGE_BODY);
        String stRoomName = intent.getStringExtra(RequestParameters.PARAMETER_ROOM_NAME);
        String stRoomId = intent.getStringExtra(RequestParameters.PARAMETER_ROOM_ID);
        long timestamp = Long.parseLong(stTimestamp);
        
        // Display the message in the room, and send a notification
        // if the user is not currently using the app (RealTalk is not on the screen).
        if (!fAppIsInForeground(context)) {
        	sendNotification(stRoomId, stRoomName, timestamp);
        }
        MessageInfo msginfo = new MessageInfo(stBody, stSender, timestamp);
        ChatController.getInstance().addMessageToRoom(msginfo, stRoomId, context);
    }

	private boolean fAppIsInForeground(Context context) {
		ActivityManager activityManager = 
				(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> services = activityManager
	            .getRunningTasks(Integer.MAX_VALUE);

	    return services.get(0).topActivity.getPackageName().toString()
	            .equalsIgnoreCase(context.getPackageName().toString());
	}

	/**
	 * Send a notification to the Android status bar, indicating a new message
	 * has arrived in a chat room the user has joined.
	 * 
	 * @param stRoomId
	 * @param stRoomName
	 * @param timestamp
	 */
	@SuppressLint("NewApi") private void sendNotification(
			String stRoomId, String stRoomName, long timestamp) {
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.statusbar_icon)
		        .setContentTitle("New message")
		        .setContentText(stRoomName)
		        .setWhen(timestamp)
		        .setAutoCancel(true);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, LoginActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen. 
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		//stackBuilder.addParentStack(ChatRoomActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Use room id as the notification id: only one notification for each room
		mNotificationManager.notify(0, mBuilder.build());
		//mNotificationManager.notify(Integer.valueOf(stRoomId), mBuilder.build());
	}
}
