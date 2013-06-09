package realtalk.asynctasks;

import realtalk.activities.SelectRoomActivity;
import realtalk.controller.ChatController;
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
public class RoomLeaverFromRoomList extends AsyncTask<String, String, Boolean> {
    /**
	 * 
	 */
	private SelectRoomActivity selectRoomActivity;
	private ChatRoomInfo chatroominfo;
    private ProgressDialog progressdialog;
    public RoomLeaverFromRoomList(SelectRoomActivity selectRoomActivity, ChatRoomInfo roominfo) {
        this.selectRoomActivity = selectRoomActivity;
		chatroominfo = roominfo;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressdialog = new ProgressDialog(this.selectRoomActivity);
        progressdialog.setMessage("Leaving room. Please wait...");
        progressdialog.setIndeterminate(false);
        progressdialog.setCancelable(true);
        progressdialog.show();
    }
    
    @Override
    protected Boolean doInBackground(String... params) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) this.selectRoomActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        
		if (networkinfo != null && networkinfo.isConnected()) {
            Boolean leaveRoomSuccess = ChatController.getInstance().leaveRoom(chatroominfo);
            return leaveRoomSuccess;
		} else {
			return false;
		}
    }
    
    @Override
    protected void onPostExecute(Boolean success) {
        if (progressdialog != null) {
            progressdialog.dismiss();
        }
        
        if (!success) {
        	Toast toast = Toast.makeText(this.selectRoomActivity.getApplicationContext(), R.string.leave_room_failed, Toast.LENGTH_SHORT);
			toast.show();
        } else {
	        Intent itViewRooms = new Intent(this.selectRoomActivity, SelectRoomActivity.class);
	  		this.selectRoomActivity.startActivity(itViewRooms);
	  		this.selectRoomActivity.finish();
        }
    }    
}
