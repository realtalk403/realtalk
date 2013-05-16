package realtalk.activities;


import java.util.ArrayList;
import java.util.List;

import realtalk.controller.ChatController;
import realtalk.util.ChatManager;
import realtalk.util.ChatRoomInfo;
import realtalk.util.ChatRoomResultSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.realtalk.R;

/**
 * Activity for selecting a chat room to join
 * 
 * @author Jordan Hazari and Taylor Williams
 *
 */
@SuppressLint("NewApi")
public class SelectRoomActivity extends Activity {
	List<String> rgstDisplayRoom;
	List<ChatRoomInfo> rgchatroominfo = new ArrayList<ChatRoomInfo>();
	ChatRoomAdapter adapter;
	Bundle bundleExtras;
	private SharedPreferences sharedpreferencesLoginPrefs;
	private Editor editorLoginPrefs;

	/**
	 * Sets up the activity, and diplays a list of available rooms
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_room);
		bundleExtras = getIntent().getExtras();
		sharedpreferencesLoginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		editorLoginPrefs = sharedpreferencesLoginPrefs.edit();

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
		
		// REASON for commenting out location code: chatController will get updated indefinitely if location continously changes.
		// Should meet up to agree on a convention or protocol regarding this.
		// TODO
		

		//RIGHT NOW GPS IS HARD TO ENABLE ON THE EMULATOR
		//IF YOU DON'T WANT TO DEAL WITH IT, UNCOMMENT THIS LINE:
		new RoomLoader(this, 0, 0, 500.0).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
		//todo put a message on screen that is NON BLOCKING!!! that says "loading rooms..."
		//allowing the user to back out if gps is never found.
		
		//location code:
//		LocationManager locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
//		final double radiusMeters = 500.0;
//		Criteria criteria = new Criteria();
//		String stBestProvider = locationmanager.getBestProvider(criteria, true);
//		if (stBestProvider == null) {
//			//no location providers, must ask user to enable a provider
//			//TODO set loading text equal to "fail...enable a location provider (wifi/network/gps)"
//			throw new RuntimeException("NO LOCATION PROVIDER");
//		}
//		else {
//			// Define a listener that responds to location updates
//			LocationListener locationListener = new LocationListener() {
//				Location locationUser;
//				public void onLocationChanged(Location location) {
//					// Called when a new location is found by the network location provider.
//					//if new location data is not usable...
//					if (locationUser == null)
//					{
//						locationUser = location;
//						LoadRooms(locationUser, radiusMeters);
//					}
//					else if (location.getAccuracy() <= locationUser.getAccuracy()) {
//						locationUser = location;
//						LoadRooms(locationUser, radiusMeters);
//					}
//					//TODO stop always listening and updating?
//				}
//
//				public void onStatusChanged(String provider, int status, Bundle extras) {}
//				public void onProviderEnabled(String provider) {}
//				public void onProviderDisabled(String provider) {}
//			};
//
//			// Register the listener with the Location Manager to receive location updates
//			locationmanager.requestLocationUpdates(stBestProvider, 0, 0, locationListener);
//		}
	}

	private void LoadRooms(Location locationUser, double radiusMeters) {
		new RoomLoader(this, locationUser.getLatitude(), locationUser.getLongitude(), radiusMeters).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_room, menu);
		return true;
	}

	/**
	 * Menu options
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//respond to menu item selection
		switch (item.getItemId()){
		case R.id.logout:
			editorLoginPrefs.putBoolean("loggedIn", false);
			editorLoginPrefs.putString("loggedin_username", null);
			editorLoginPrefs.putString("loggedin_password", null);
			editorLoginPrefs.commit();
			Intent itLogin = new Intent(this, LoginActivity.class);
			startActivity(itLogin);
			finish();
			return true;
		case R.id.settings:
			startActivity(new Intent(this, AccountSettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
		double radiusMeters;
		double latitude;
		double longitude;

		/**
		 * Constructs a RoomLoader object
		 * 
		 * @param selectroomroomactivity the activity context
		 */
		public RoomLoader(SelectRoomActivity selectroomactivity, double latitude, double longitude, double radiusMeters) {
			this.selectroomactivity = selectroomactivity;
			this.longitude = longitude;
			this.latitude = latitude;
			this.radiusMeters = radiusMeters;
		}

		/**
		 * Retrieves and displays the available rooms
		 */
		@Override
		protected ChatRoomResultSet doInBackground(String... params) {
			ChatRoomResultSet crrsNear = ChatManager.crrsNearbyChatrooms
					(latitude, longitude, radiusMeters);

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
