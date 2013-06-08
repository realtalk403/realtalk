package realtalk.asynctasks;

import java.sql.Timestamp;

import realtalk.activities.CreateRoomActivity;
import realtalk.activities.SelectRoomActivity;
import realtalk.util.ChatManager;
import realtalk.util.ChatRoomInfo;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.realtalk.R;

/**
 * Creates and joins a chat room
 * 
 * @author Jordan Hazari
 *
 */
public class RoomCreator extends AsyncTask<String, String, RequestResultSet> {
	/**
	 * 
	 */
	private CreateRoomActivity createRoomActivity;
	private UserInfo userinfo;
	private CreateRoomActivity activity;
	
	/**
	 * Constructs a RoomCreator object
	 * @param createRoomActivity TODO
	 * 
	 * @param chatroominfo the room to create/join
	 */
	public RoomCreator(CreateRoomActivity createRoomActivity, UserInfo userinfo, CreateRoomActivity activity) {
		this.createRoomActivity = createRoomActivity;
		this.userinfo = userinfo;
		this.activity = activity;
	}
	
	/**
	 * Displays a popup dialogue while joining the room
	 */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.createRoomActivity.progressdialog = new ProgressDialog(this.createRoomActivity);
        this.createRoomActivity.progressdialog.setMessage("Creating room. Please wait...");
        this.createRoomActivity.progressdialog.setIndeterminate(false);
        this.createRoomActivity.progressdialog.setCancelable(true);
        this.createRoomActivity.progressdialog.show();
    }

    /**
     * Adds the room, or joins it if it already exists
     * 
     * @return rrs with results. rrs will contain false if server is down, and will be null if disconnected.
     */
	@Override
	protected RequestResultSet doInBackground(String... params) {
        EditText roomNameText = (EditText)this.createRoomActivity.findViewById(R.id.roomName);
		String stRoomName = roomNameText.getText().toString();
		
		EditText roomDescription = (EditText)this.createRoomActivity.findViewById(R.id.description);
		String stDescription = roomDescription.getText().toString();
		
		String stCreator = userinfo.stUserName();
		
		ChatRoomInfo chatroominfo = new ChatRoomInfo(stRoomName, stRoomName, stDescription, this.createRoomActivity.latitude, this.createRoomActivity.longitude, stCreator, 0, new Timestamp(System.currentTimeMillis()));
		
		ConnectivityManager connectivitymanager = (ConnectivityManager) this.createRoomActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
		
		RequestResultSet rrs = null;
		if (activity.fDebugMode) {
			rrs = new RequestResultSet(true, "NO ERROR MESSAGE", "NO ERROR MESSAGE");
		} else if (networkinfo != null && networkinfo.isConnected()) {
			 rrs = ChatManager.rrsAddRoom(chatroominfo, userinfo);
		} 
		
		return rrs;
	}
	
	/**
	 * Closes the popup dialogue
	 */
	@Override
    protected void onPostExecute(RequestResultSet rrs) {
        this.createRoomActivity.progressdialog.dismiss();
        
        if (rrs == null) {
        	Toast toast = Toast.makeText(this.createRoomActivity.getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG);
			toast.show();
        } else if (!rrs.getfSucceeded()) {
        	Toast toast = Toast.makeText(this.createRoomActivity.getApplicationContext(), R.string.server_error, Toast.LENGTH_LONG);
        	toast.show();
        } else {
        	Intent itViewRooms = new Intent(activity, SelectRoomActivity.class);
    		if (!activity.fDebugMode()) {
    			activity.startActivity(itViewRooms);
    		}
    		activity.finish();
        }
	}
}