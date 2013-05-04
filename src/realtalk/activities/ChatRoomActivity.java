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
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ChatRoomActivity extends Activity {
	String uName;
	String pWord;
	List<MessageInfo> messages;
	
	ChatRoomInfo room = new ChatRoomInfo("Room001", "001", "everywhere", 
			"hazarij", 1, new Timestamp(System.currentTimeMillis()));
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		
//		Bundle extras = getIntent().getExtras();
//		String uName = extras.getString("USER_NAME");
//		String pWord = extras.getString("PASSWORD");
		
		uName = "hazarij";
		pWord = "jordan";
		
		//new MessageLoader().execute();
		List<MessageInfo> messages = new ArrayList<MessageInfo>();
		MessageInfo a = new MessageInfo("hello", "hazarij", new Timestamp(System.currentTimeMillis()));
		messages.add(a);
		
		String[] messageArray = new String[messages.size()];
		for (int i = 0; i < messages.size(); i++) {
			String displayedMessage = messages.get(i).getSender() + ": " + 
					messages.get(i).getBody();
			messageArray[i] = displayedMessage;
		}
		ListView listView = (ListView) findViewById(R.id.list);
		// Binding resources Array to ListAdapter
        listView.setAdapter(new ArrayAdapter<String>
        	(this, R.layout.list_item, R.id.label, messageArray));
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
				(value, uName, new Timestamp(System.currentTimeMillis()));
		
		new MessageSender(message).execute();
		text.setText("");
	}
	
	class MessageSender extends AsyncTask<String, String, RequestResultSet> {
		private User user;
		private MessageInfo message;
		
		public MessageSender(MessageInfo message) {
			this.user = new User(uName, pWord);
			this.message = message;
		}
		
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

		@Override
		protected RequestResultSet doInBackground(String... params) {
			return ChatManager.postMessage(user, room, message);
		}
		
	}
	
	class MessageLoader extends AsyncTask<String, String, PullMessageResultSet> {
		//private ChatRoomInfo thisRoom;
		
		public MessageLoader() {
			//thisRoom = room;
		}
		
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

		@Override
		protected PullMessageResultSet doInBackground(String... params) {
			PullMessageResultSet result = ChatManager.rgstChatLogGet(room);
			if(result.rgmessage != null)
				messages = result.rgmessage;
			else
				messages = new ArrayList<MessageInfo>();
			return result;
		}
		
	}

}
