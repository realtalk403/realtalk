package realtalk.activities;


import java.util.ArrayList;
import java.util.List;

import realtalk.util.ChatManager;
import realtalk.util.ChatRoomInfo;
import realtalk.util.ChatRoomResultSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.realtalk.R;

/**
 * Activity for selecting a chat room to join
 * 
 * @author Jordan Hazari
 *
 */
@SuppressLint("NewApi")
public class SelectRoomActivity extends Activity {
	List<String> rgstDisplayRoom;
	List<ChatRoomInfo> rgchatroominfo = new ArrayList<ChatRoomInfo>();
	ChatRoomAdapter adapter;
	Bundle bundleExtras;
	
	/**
	 * Sets up the activity, and diplays a list of available rooms
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_room);
		bundleExtras = getIntent().getExtras();
		
		rgstDisplayRoom = new ArrayList<String>();
		
		ListView listview = (ListView) findViewById(R.id.list);
		listview.setClickable(false);
		
		// when a room is clicked, starts a new ChatRoomActivity
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	ChatRoomInfo criSelected = rgchatroominfo.get(position);
        		Intent itStartChat = new Intent(SelectRoomActivity.this, ChatRoomActivity.class);
        		itStartChat.putExtras(bundleExtras);
        		itStartChat.putExtra("ROOM", criSelected);
        		SelectRoomActivity.this.startActivity(itStartChat);
            }
        });
		
		// Binding resources Array to ListAdapter
		adapter = new ChatRoomAdapter(this, R.layout.list_item, rgchatroominfo);
        listview.setAdapter(adapter);
        
        new RoomLoader(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_room, menu);
		return true;
	}
	
	public void createRoom(View view) {
		Intent itCreateRoom = new Intent(this, CreateRoomActivity.class);
		itCreateRoom.putExtra("USER", bundleExtras.getParcelable("USER"));
		this.startActivity(itCreateRoom);
	}
	
	
	/**
	 * Retrieves the user's available chatrooms
	 * 
	 * @author Jordan Hazari
	 *
	 */
	class RoomLoader extends AsyncTask<String, String, ChatRoomResultSet> {
		private SelectRoomActivity selectroomactivity;
		
		/**
		 * Constructs a RoomLoader object
		 * 
		 * @param selectroomroomactivity the activity context
		 */
		public RoomLoader(SelectRoomActivity selectroomactivity) {
			this.selectroomactivity = selectroomactivity;
		}

		/**
		 * Retrieves and displays the available rooms
		 */
		@Override
		protected ChatRoomResultSet doInBackground(String... params) {
			ChatRoomResultSet crrsNear = ChatManager.crrsNearbyChatrooms
					(0.0, 0.0, 500.0);
				
			rgchatroominfo = crrsNear.rgcri;
				
			selectroomactivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					adapter.clear();
						
					for (int i = 0; i < rgchatroominfo.size(); i++)
						adapter.add(rgchatroominfo.get(i));
				}
			});
			
			return crrsNear;
		}
	}
	
	private class ChatRoomAdapter extends ArrayAdapter<ChatRoomInfo> {

        private List<ChatRoomInfo> rgchatroominfo;

        public ChatRoomAdapter(Context context, int textViewResourceId, List<ChatRoomInfo> rgchatroominfo) {
            super(context, textViewResourceId, rgchatroominfo);
            this.rgchatroominfo = rgchatroominfo;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = vi.inflate(R.layout.list_item, null);
            }
            ChatRoomInfo chatroominfo = rgchatroominfo.get(position);
            if (chatroominfo != null) {
                TextView textviewTop = (TextView) view.findViewById(R.id.toptext);
                TextView textviewBottom = (TextView) view.findViewById(R.id.bottomtext);
                if (textviewTop != null) {
                    textviewTop.setText(chatroominfo.stName());
                }
                if(textviewBottom != null) {
                    textviewBottom.setText("\t" + chatroominfo.numUsersGet() + " users");
                }
            }
            return view;
        }
	}
}
