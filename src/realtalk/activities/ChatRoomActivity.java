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
	ChatRoomInfo chatroominfo;
	UserInfo userinfo;
	private ProgressDialog progressdialog;
	List<MessageInfo> rgmessageinfo = new ArrayList<MessageInfo>();
	List<String> rgstDisplayMessage;
	ArrayAdapter<String> adapter;
	
	/**
	 * Sets up the chat room activity and loads the previous
	 * messages from the chat room
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		
		chatroominfo = new ChatRoomInfo("Room 001", "001", "a room", 0.0, 0.0, "hazarij", 1, new Timestamp(System.currentTimeMillis()));
		Bundle extras = getIntent().getExtras();
		String stUsername = extras.getString("USER_NAME");
		String stPword = extras.getString("PASSWORD");
		
		userinfo = new UserInfo(stUsername, stPword, DEFAULT_ID);
		new RoomCreator(chatroominfo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
		rgstDisplayMessage = new ArrayList<String>();

		ListView listview = (ListView) findViewById(R.id.list);
		// Binding resources Array to ListAdapter
		adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, rgstDisplayMessage);
		listview.setAdapter(adapter);
		
		new MessageLoader(this, chatroominfo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
		EditText edittext = (EditText)findViewById(R.id.message);
		String stValue = edittext.getText().toString();
		
		MessageInfo message = new MessageInfo
				(stValue, userinfo.stUserName(), new Timestamp(System.currentTimeMillis()));
		
		new MessageSender(message, chatroominfo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		edittext.setText("");
	}
	
	/**
	 * Posts a message to the room's database
	 * 
	 * @author Jordan Hazari
	 *
	 */
	class MessageSender extends AsyncTask<String, String, RequestResultSet> {
		private MessageInfo messageinfo;
		private ChatRoomInfo chatroominfo;
		
		/**
		 * Constructs a MessageSender object
		 * 
		 * @param message the message to be sent
		 * @param chatroominfo the room to post the message to
		 */
		public MessageSender(MessageInfo message, ChatRoomInfo chatroominfo) {
			this.messageinfo = message;
			this.chatroominfo = chatroominfo;
		}

		/**
		 * Posts the message to the room
		 */
		@Override
		protected RequestResultSet doInBackground(String... params) {
			return ChatManager.rrsPostMessage(userinfo, chatroominfo, messageinfo);
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
				PullMessageResultSet pmrsRecent = ChatManager.pmrsChatRecentChat
						(chatroominfo, new Timestamp(System.currentTimeMillis()-10000000));
				
				rgmessageinfo = pmrsRecent.rgmessage;
				
				chatroomactivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						adapter.clear();
						
						for (int i = 0; i < rgmessageinfo.size(); i++) {
							String stMessage = rgmessageinfo.get(i).stSender() + ": " + 
									rgmessageinfo.get(i).stBody();
							adapter.add(stMessage);
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
		private ChatRoomInfo chatroominfo;
		
		/**
		 * Constructs a RoomCreator object
		 * 
		 * @param chatroominfo the room to create/join
		 */
		public RoomCreator(ChatRoomInfo chatroominfo) {
			this.chatroominfo = chatroominfo;
		}
		
		/**
		 * Displays a popup dialogue while joining the room
		 */
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog = new ProgressDialog(ChatRoomActivity.this);
            progressdialog.setMessage("Joining room. Please wait...");
            progressdialog.setIndeterminate(false);
            progressdialog.setCancelable(true);
            progressdialog.show();
        }

	    /**
	     * Adds the room, or joins it if it already exists
	     */
		@Override
		protected RequestResultSet doInBackground(String... params) {
			RequestResultSet rrs = ChatManager.rrsAddRoom(chatroominfo, userinfo);
			if (!rrs.fSucceeded) {
				rrs = ChatManager.rrsJoinRoom(userinfo, chatroominfo);
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
            progressdialog.dismiss();
		}
	}
}