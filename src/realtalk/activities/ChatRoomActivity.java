package realtalk.activities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import realtalk.util.ChatRoomInfo;
import realtalk.util.MessageInfo;
import realtalk.util.ChatManager;
import realtalk.util.PullMessageResultSet;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;

import com.example.realtalk.R;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Activity for a chat room, where the user can send/recieve messages.
 * 
 * @author Jordan Hazari
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChatRoomActivity extends Activity {
	private static final String DEFAULT_ID = "someID";
	ChatRoomInfo room;
	UserInfo user;
	private ProgressDialog pDialog;
	List<MessageInfo> messages = new ArrayList<MessageInfo>();
	List<String> messageArray;
	ArrayAdapter<String> adapter;
	
	/**
	 * Sets up the chat room activity and loads the previous
	 * messages from the chat room
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		
		room = new ChatRoomInfo("Room 001", "001", "a room", 0.0, 0.0, "hazarij", 1, new Timestamp(System.currentTimeMillis()));
		Bundle extras = getIntent().getExtras();
		String uName = extras.getString("USER_NAME");
		String pWord = extras.getString("PASSWORD");
		
		user = new UserInfo(uName, pWord, DEFAULT_ID);
		new RoomCreator(room).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
		messageArray = new ArrayList<String>();

		ListView listView = (ListView) findViewById(R.id.list);
		// Binding resources Array to ListAdapter
		adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, messageArray);
		listView.setAdapter(adapter);
		
		new MessageLoader(this, room).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_room, menu);
		return true;
	}
	
	/**
	 * Creates and sends a message typed by the user.
	 * 
	 * @param view
	 */
	public void createMessage(View view) {
		EditText text = (EditText)findViewById(R.id.message);
		String value = text.getText().toString();
		
		MessageInfo message = new MessageInfo
				(value, user.stUserName(), new Timestamp(System.currentTimeMillis()));
		
		new MessageSender(message, room).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		text.setText("");
	}
	
	/**
	 * Posts a message to the room's database
	 * 
	 * @author Jordan Hazari
	 *
	 */
	class MessageSender extends AsyncTask<String, String, RequestResultSet> {
		private MessageInfo message;
		private ChatRoomInfo chatroominfo;
		
		/**
		 * Constructs a MessageSender object
		 * 
		 * @param message the message to be sent
		 * @param chatroominfo the room to post the message to
		 */
		public MessageSender(MessageInfo message, ChatRoomInfo chatroominfo) {
			this.message = message;
			this.chatroominfo = chatroominfo;
		}

		/**
		 * Posts the message to the room
		 */
		@Override
		protected RequestResultSet doInBackground(String... params) {
			return ChatManager.rrsPostMessage(user, chatroominfo, message);
		}
		
	}
	
	/**
	 * Retrieves the message log of a chat room
	 * 
	 * @author Jordan Hazari
	 *
	 */
	class MessageLoader extends AsyncTask<String, String, PullMessageResultSet> {
		private ChatRoomActivity chatroomactivity;
		private ChatRoomInfo chatroominfo;
		
		/**
		 * Constructs a MessageLoader object
		 * 
		 * @param chatroomactivity the activity context
		 * @param chatroominfo the chat room to retrieve the chat log from
		 */
		public MessageLoader(ChatRoomActivity chatroomactivity, ChatRoomInfo chatroominfo) {
			this.chatroomactivity = chatroomactivity;
			this.chatroominfo = chatroominfo;
		}

		/**
		 * Retrieves and displays the chat log, constantly updating
		 */
		@Override
		protected PullMessageResultSet doInBackground(String... params) {
			while (true) {
				PullMessageResultSet result = ChatManager.pmrsChatRecentChat
						(chatroominfo, new Timestamp(System.currentTimeMillis()-10000000));
				
				messages = result.rgmessage;
				
				chatroomactivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.clear();
						
						for (int i = 0; i < messages.size(); i++) {
							String displayedMessage = messages.get(i).stSender() + ": " + 
									messages.get(i).stBody();
							adapter.add(displayedMessage);
						}
					}
				});
			}
		}
		
	}
	
	/**
	 * Creates and joins a chat room
	 * 
	 * @author Jordan Hazari
	 *
	 */
	class RoomCreator extends AsyncTask<String, String, RequestResultSet> {
		private ChatRoomInfo room;
		
		/**
		 * Constructs a RoomCreator object
		 * 
		 * @param room the room to create/join
		 */
		public RoomCreator(ChatRoomInfo room) {
			this.room = room;
		}
		
		/**
		 * Displays a popup dialogue while joining the room
		 */
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChatRoomActivity.this);
            pDialog.setMessage("Joining room. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

	    /**
	     * Adds the room, or joins it if it already exists
	     */
		@Override
		protected RequestResultSet doInBackground(String... params) {
			RequestResultSet rrs = ChatManager.rrsAddRoom(room, user);
			if (!rrs.fSucceeded) {
				rrs = ChatManager.rrsJoinRoom(user, room);
				if (!rrs.fSucceeded) {
					throw new RuntimeException("server error");
				}
			}
			return rrs;
		}
		
		/**
		 * Closes the popup dialogue
		 */
		@Override
        protected void onPostExecute(RequestResultSet requestresultset) {
            pDialog.dismiss();
		}
	}
}