package realtalk.asynctasks;

import realtalk.activities.ChatRoomActivity;
import realtalk.util.ChatRoomInfo;
import realtalk.util.MessageInfo;
import realtalk.util.RequestResultSet;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.realtalk.R;

/**
 * Posts a message to the room's database
 * 
 * @author Jordan Hazari
 *
 */
public class MessageSender extends AsyncTask<String, String, RequestResultSet> {
	/**
	 * 
	 */
	private ChatRoomActivity chatRoomActivity;
	private MessageInfo messageinfo;
	private ChatRoomInfo chatroominfo;
	
	/**
	 * Constructs a MessageSender object
	 * 
	 * @param message the message to be sent
	 * @param chatroominfo the room to post the message to
	 * @param chatRoomActivity TODO
	 */
	public MessageSender(ChatRoomActivity chatRoomActivity, MessageInfo message, ChatRoomInfo chatroominfo) {
		this.chatRoomActivity = chatRoomActivity;
		this.messageinfo = message;
		this.chatroominfo = chatroominfo;
	}

	/**
	 * Posts the message to the room
	 * 
	 * @return null if disconnected from network
	 */
	@Override
	protected RequestResultSet doInBackground(String... params) {
		ConnectivityManager connectivitymanager = (ConnectivityManager) this.chatRoomActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
		if (networkinfo != null && networkinfo.isConnected()) {
			Log.d("connectivity", "Connected and attempting to post message");
			RequestResultSet rrs = this.chatRoomActivity.getChatController().rrsPostMessage(this.chatRoomActivity.getUserinfo(), chatroominfo, messageinfo);
			Log.d("connectivity", "Message posted");
			return rrs;
		} else {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(RequestResultSet rrs) {
		if (rrs == null) {
			Toast toast = Toast.makeText(this.chatRoomActivity.getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG);
			toast.show();
		}
	}
}
