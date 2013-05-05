package realtalk.activities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import realtalk.util.ChatRoomInfo;
import realtalk.util.MessageInfo;
import realtalk.util.ChatManager;
import realtalk.util.PullMessageResultSet;
import realtalk.util.RequestResultSet;
import realtalk.util.User;

import com.example.realtalk.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ChatRoomActivity extends Activity {
	ChatRoomInfo room;
	User user;
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		
		room = new ChatRoomInfo("Room 001", "001", "a room", 0.0, 0.0, "hazarij", 1, new Timestamp(System.currentTimeMillis()));
		Bundle extras = getIntent().getExtras();
		String uName = extras.getString("USER_NAME");
		String pWord = extras.getString("PASSWORD");
//		String uName = "hazarij";
//		String pWord = "jordan";
		
		user = new User(uName, pWord);
		new RoomCreator(room).execute();
		new MessageLoader(this, room).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_room, menu);
		return true;
	}
	
	public void createMessage(View view) {
		EditText text = (EditText)findViewById(R.id.message);
		String value = text.getText().toString();
		
		MessageInfo message = new MessageInfo
				(value, user.getUsername(), new Timestamp(System.currentTimeMillis()));
		
		new MessageSender(message, room).execute();
		text.setText("");
	}
	
	class MessageSender extends AsyncTask<String, String, RequestResultSet> {
		private MessageInfo message;
		private ChatRoomInfo chatroominfo;
		
		public MessageSender(MessageInfo message, ChatRoomInfo chatroominfo) {
			this.message = message;
			this.chatroominfo = chatroominfo;
		}

		@Override
		protected RequestResultSet doInBackground(String... params) {
			return ChatManager.postMessage(user, chatroominfo, message);
		}
		
	}
	
	class MessageLoader extends AsyncTask<String, String, PullMessageResultSet> {
		private ChatRoomActivity chatroomactivity;
		private ChatRoomInfo chatroominfo;
		
		public MessageLoader(ChatRoomActivity chatroomactivity, ChatRoomInfo chatroominfo) {
			this.chatroomactivity = chatroomactivity;
			this.chatroominfo = chatroominfo;
		}

		@Override
		protected PullMessageResultSet doInBackground(String... params) {
			List<MessageInfo> messages = new ArrayList<MessageInfo>();
			
			PullMessageResultSet result = ChatManager.pullmessageresultsetChatRecentChat
					(chatroominfo, new Timestamp(System.currentTimeMillis()-10000000));
			
			messages = result.rgmessage;
//			MessageInfo a = new MessageInfo("hello", "hazarij", new Timestamp(System.currentTimeMillis()));
//			messages.add(a);

			String[] messageArray = new String[messages.size()];
			for (int i = 0; i < messages.size(); i++) {
				String displayedMessage = messages.get(i).getSender() + ": " + 
						messages.get(i).getBody();
				messageArray[i] = displayedMessage;
			}
			ListView listView = (ListView) findViewById(R.id.list);
			// Binding resources Array to ListAdapter
	        listView.setAdapter(new ArrayAdapter<String>
	        	(chatroomactivity, R.layout.list_item, R.id.label, messageArray));
			return result;
		}
		
	}
	
	
	class RoomCreator extends AsyncTask<String, String, RequestResultSet> {
		private ChatRoomInfo room;
		
		public RoomCreator(ChatRoomInfo room) {
			this.room = room;
		}
		
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChatRoomActivity.this);
            pDialog.setMessage("Creating room. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

		@Override
		protected RequestResultSet doInBackground(String... params) {
			RequestResultSet rrs = ChatManager.addRoom(room, user);
			if (!rrs.fSucceeded) {
				rrs = ChatManager.joinRoom(user, room);
				if (!rrs.fSucceeded) {
					throw new RuntimeException("server error");
				}
			}
			
			return rrs;
		}
		
		@Override
        protected void onPostExecute(RequestResultSet requestresultset) {
            pDialog.dismiss();
		}
	}

}
