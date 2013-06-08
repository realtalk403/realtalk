package realtalk.asynctasks;

import realtalk.activities.ChatRoomActivity;
import realtalk.activities.SelectRoomActivity;
import realtalk.util.ChatRoomInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.realtalk.R;

/**
 * AsyncTask that leaves the room.
 * 
 * @author Colin Kho
 *
 */
public class RoomLeaverFromRoom extends AsyncTask<String, String, Boolean> {
    /**
	 * 
	 */
	private ChatRoomActivity chatRoomActivity;
	private ChatRoomInfo chatroominfo;
    public RoomLeaverFromRoom(ChatRoomActivity chatRoomActivity, ChatRoomInfo roominfo) {
        this.chatRoomActivity = chatRoomActivity;
		chatroominfo = roominfo;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.chatRoomActivity.progressdialog = new ProgressDialog(this.chatRoomActivity);
        this.chatRoomActivity.progressdialog.setMessage(this.chatRoomActivity.getApplicationContext().getString(R.string.leaving_room));
        this.chatRoomActivity.progressdialog.setIndeterminate(false);
        this.chatRoomActivity.progressdialog.setCancelable(true);
        this.chatRoomActivity.progressdialog.show();
    }
    
    /**
     * @return Boolean with whether the room was left successfully.
     */
    @Override
    protected Boolean doInBackground(String... params) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) this.chatRoomActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        
		if (networkinfo != null && networkinfo.isConnected()) {
			Boolean fLeaveRoom = this.chatRoomActivity.chatController.leaveRoom(chatroominfo);
			return fLeaveRoom;
		} else {
			return false;
		}
    }
    
    @Override
    protected void onPostExecute(Boolean success) {
        if (this.chatRoomActivity.progressdialog != null) {
            this.chatRoomActivity.progressdialog.dismiss();
        }
        
        if (!success) {
        	Toast toast = Toast.makeText(this.chatRoomActivity.getApplicationContext(), R.string.leave_room_failed, Toast.LENGTH_SHORT);
			toast.show();
        } else {
            if (!this.chatRoomActivity.fDebug) {
                Intent itViewRooms = new Intent(this.chatRoomActivity, SelectRoomActivity.class);
	  		    this.chatRoomActivity.startActivity(itViewRooms);
	  		    this.chatRoomActivity.finish();
            }
        }
    }    
}