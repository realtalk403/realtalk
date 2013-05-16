package realtalk.activities;

import java.sql.Timestamp;

import realtalk.util.ChatManager;
import realtalk.util.ChatRoomInfo;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;

import com.example.realtalk.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CreateRoomActivity extends Activity {
	private ProgressDialog progressdialog;
	private UserInfo userinfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_room);
		
		Bundle extras = getIntent().getExtras();
		userinfo = extras.getParcelable("USER");
		
		String stUser = userinfo.stUserName();
		TextView textviewRoomTitle = (TextView) findViewById(R.id.userTitle);
		textviewRoomTitle.setText(stUser);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_room, menu);
		return true;
	}
	
	public void addRoom(View view) {
		new RoomCreator(userinfo, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	/**
	 * Creates and joins a chat room
	 * 
	 * @author Jordan Hazari
	 *
	 */
	class RoomCreator extends AsyncTask<String, String, RequestResultSet> {
		private UserInfo userinfo;
		private CreateRoomActivity activity;
		
		/**
		 * Constructs a RoomCreator object
		 * 
		 * @param chatroominfo the room to create/join
		 */
		public RoomCreator(UserInfo userinfo, CreateRoomActivity activity) {
			this.userinfo = userinfo;
			this.activity = activity;
		}
		
		/**
		 * Displays a popup dialogue while joining the room
		 */
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog = new ProgressDialog(CreateRoomActivity.this);
            progressdialog.setMessage("Creating room. Please wait...");
            progressdialog.setIndeterminate(false);
            progressdialog.setCancelable(true);
            progressdialog.show();
        }

	    /**
	     * Adds the room, or joins it if it already exists
	     */
		@Override
		protected RequestResultSet doInBackground(String... params) {
            EditText roomNameText = (EditText)findViewById(R.id.roomName);
    		String stRoomName = roomNameText.getText().toString();
    		
    		EditText roomDescription = (EditText)findViewById(R.id.description);
    		String stDescription = roomDescription.getText().toString();
    		
    		String stCreator = userinfo.stUserName();
			
			ChatRoomInfo chatroominfo = new ChatRoomInfo(stRoomName, stRoomName, stDescription, 0.0, 0.0, stCreator, 0, new Timestamp(System.currentTimeMillis()));
			
			RequestResultSet rrs = ChatManager.rrsAddRoom(chatroominfo, userinfo);
			if (!rrs.fSucceeded) {
				throw new RuntimeException("server error");
			}
			return rrs;
		}
		
		/**
		 * Closes the popup dialogue
		 */
		@Override
        protected void onPostExecute(RequestResultSet requestresultset) {
            progressdialog.dismiss();
            
            Intent itViewRooms = new Intent(activity, SelectRoomActivity.class);
            itViewRooms.putExtra("USER", userinfo);
    		activity.startActivity(itViewRooms);
    		activity.finish();
		}
	}

}
