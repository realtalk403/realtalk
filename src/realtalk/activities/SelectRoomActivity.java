package realtalk.activities;


import java.util.ArrayList;
import java.util.List;

import realtalk.controller.ChatController;
import realtalk.util.ChatManager;
import realtalk.util.ChatRoomInfo;
import realtalk.util.ChatRoomResultSet;
import realtalk.util.UserInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
	private static final double HACKED_GPS_DISTANCE_CONSTANT_TO_BE_REMOVED = 500.0;  //TODO remove for final product
	private List<ChatRoomInfo> rgchatroominfo = new ArrayList<ChatRoomInfo>();
	private List<ChatRoomInfo> rgchatroominfoJoined = new ArrayList<ChatRoomInfo>();
	private List<ChatRoomInfo> rgchatroominfoUnjoined = new ArrayList<ChatRoomInfo>();
	private ChatRoomAdapter unJoinedAdapter;
	private ChatRoomAdapter joinedAdapter;
	private SharedPreferences sharedpreferencesLoginPrefs;
	private Editor editorLoginPrefs;
	private Location locationUser;

	/**
	 * Sets up the activity, and diplays a list of available rooms
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_select_room);
		sharedpreferencesLoginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		editorLoginPrefs = sharedpreferencesLoginPrefs.edit();
		
		ImageView buttonCreateRoom = (ImageView) findViewById(R.id.createRoomId);
		buttonCreateRoom.setEnabled(false);
		
		UserInfo userinfo = ChatController.getInstance().getUser();
		String stUser = userinfo.stUserName();
		TextView textviewRoomTitle = (TextView) findViewById(R.id.userTitle);
		textviewRoomTitle.setText(stUser);
		TextView textviewUserTitle = (TextView) findViewById(R.id.selectRoomTitle);
		textviewUserTitle.setText("RealTalk");

		ListView listviewJoined = (ListView) findViewById(R.id.joined_list);
		listviewJoined.setClickable(false);
		
		ListView listviewUnjoined = (ListView) findViewById(R.id.unjoined_list);
		listviewUnjoined.setClickable(false);

		// when a room is clicked, starts a new ChatRoomActivity
        listviewJoined.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	ChatRoomInfo criSelected = rgchatroominfoJoined.get(position);
        		Intent itStartChat = new Intent(SelectRoomActivity.this, ChatRoomActivity.class);
        		itStartChat.putExtra("ROOM", criSelected);
        		SelectRoomActivity.this.startActivity(itStartChat);
        		SelectRoomActivity.this.finish();
            }
        });
        
		// when a room is clicked, starts a new ChatRoomActivity
        listviewUnjoined.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	ChatRoomInfo criSelected = rgchatroominfoUnjoined.get(position);
        		Intent itStartChat = new Intent(SelectRoomActivity.this, ChatRoomActivity.class);
        		itStartChat.putExtra("ROOM", criSelected);
        		SelectRoomActivity.this.startActivity(itStartChat);
        		SelectRoomActivity.this.finish();
            }
        });

		// Binding resources Array to ListAdapter
		unJoinedAdapter = new ChatRoomAdapter(this, R.layout.message_item, rgchatroominfoUnjoined, false);
		listviewUnjoined.setAdapter(unJoinedAdapter);
		
		joinedAdapter = new ChatRoomAdapter(this, R.layout.message_item, rgchatroominfoJoined, true);
		listviewJoined.setAdapter(joinedAdapter);
		
		// REASON for commenting out location code: chatController will get updated indefinitely if location continously changes.
		// Should meet up to agree on a convention or protocol regarding this.
		// TODO
		

		//RIGHT NOW GPS IS HARD TO ENABLE ON THE EMULATOR
		//IF YOU DON'T WANT TO DEAL WITH IT, UNCOMMENT THIS LINE:
		//new RoomLoader(this, 0, 0, HACKED_GPS_DISTANCE_CONSTANT_TO_BE_REMOVED).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
		//location code:
		LocationManager locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
		final double radiusMeters = 500.0;
		Criteria criteria = new Criteria();
		String stBestProvider = locationmanager.getBestProvider(criteria, true);
		if (stBestProvider == null) {
			//no location providers, must ask user to enable a provider
			Toast.makeText(getApplicationContext(), R.string.turn_gps_on, Toast.LENGTH_SHORT).show();
		}
		else {
			// Define a listener that responds to location updates
			LocationListener locationListener = new LocationListener() {
				Location locationMostAccurate = null;
				private int locationCount = 0;
				public void onLocationChanged(Location location) {
					
					// Called when a new location is found by the network location provider.
					//if new location data is not usable...
					locationUser = location;
					loadRooms(locationUser, radiusMeters);
					locationCount++;
					if (locationMostAccurate == null || locationMostAccurate.getAccuracy() >= locationUser.getAccuracy())
						locationMostAccurate = locationUser;
					if (locationCount >= 5)
					{
						ImageView buttonCreateRoom = (ImageView) findViewById(R.id.createRoomId);
						buttonCreateRoom.setEnabled(true);
						buttonCreateRoom.setImageResource(R.drawable.createroom_icon);
					}
					//TODO stop always listening and updating?
				}

		        public void onStatusChanged(String provider, int status, Bundle extras) {
		            switch (status) {
		            case LocationProvider.OUT_OF_SERVICE:
		            	Toast.makeText(getApplicationContext(), R.string.gps_out_of_service, Toast.LENGTH_SHORT).show();
		                break;
		            case LocationProvider.TEMPORARILY_UNAVAILABLE:
		            	Toast.makeText(getApplicationContext(), R.string.gps_unavailable, Toast.LENGTH_SHORT).show();
		                break;
		            }
		        }
				public void onProviderEnabled(String provider) {}
				public void onProviderDisabled(String provider) {}
			};

			// Register the listener with the Location Manager to receive location updates
			locationmanager.requestLocationUpdates(stBestProvider, 0, 0, locationListener);
		}
	}
	
	private void loadRooms(Location location, double radiusMeters) {
		new RoomLoader(this, location.getLatitude(), location.getLongitude(), HACKED_GPS_DISTANCE_CONSTANT_TO_BE_REMOVED).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private void getDetails(int position, boolean fJoined) {
		final ChatRoomInfo chatroominfo;
		String stJoinView;
		if(fJoined) {
			chatroominfo = rgchatroominfoJoined.get(position);
			stJoinView = "View";
		} else {
			chatroominfo = rgchatroominfoUnjoined.get(position);
			stJoinView = "Join";
		}
		
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		//set title
		alertDialogBuilder.setTitle(chatroominfo.stName());
		
		//set dialog message
		alertDialogBuilder
			.setMessage(Html.fromHtml("<b>Description: </b> " +  chatroominfo.stDescription() + 
									"<br/><br/><b>Active Users: </b> " + chatroominfo.numUsersGet() +
									"<br/><br/><b>Creator: </b> " + chatroominfo.stCreator()))
			.setCancelable(true);
		
		alertDialogBuilder.setNegativeButton(stJoinView, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent itStartChat = new Intent(SelectRoomActivity.this, ChatRoomActivity.class);
        		itStartChat.putExtra("ROOM", chatroominfo);
        		SelectRoomActivity.this.startActivity(itStartChat);
        		SelectRoomActivity.this.finish();
			}	
		});
		
		alertDialogBuilder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});
		
		if(fJoined) {	
			alertDialogBuilder.setNeutralButton("Leave", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					new RoomLeaver(chatroominfo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					dialog.cancel();
				}
			});
		}
		
		//create alert dialog
		AlertDialog alertdialogDeleteAcc = alertDialogBuilder.create();
		
		//show alert dialog
		alertdialogDeleteAcc.show();
	}
	
//	private void LoadRooms(Location locationUser, double radiusMeters) {
//		new RoomLoader(this, locationUser.getLatitude(), locationUser.getLongitude(), radiusMeters).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//	}

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
			this.clickLogout(getCurrentFocus());
			return true;
		case R.id.settings:
			this.clickSettings(getCurrentFocus());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void clickSettings(View view) {
		startActivity(new Intent(this, AccountSettingsActivity.class));
	}
	
	public void clickLogout(View view) {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		//set title
		alertDialogBuilder.setTitle("Log Out");
		
		//set dialog message
		alertDialogBuilder
			.setMessage("Are you sure you want to log out?")
			.setCancelable(true);
		
		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				editorLoginPrefs.putBoolean("loggedIn", false);
				editorLoginPrefs.putString("loggedin_username", null);
				editorLoginPrefs.putString("loggedin_password", null);
				editorLoginPrefs.commit();
				Intent itLogin = new Intent(SelectRoomActivity.this, LoginActivity.class);
				startActivity(itLogin);
				finish();
			}	
		});
		
		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
				dialog.cancel();
			}
		});
		
		//create alert dialog
		AlertDialog alertdialogDeleteAcc = alertDialogBuilder.create();
		
		//show alert dialog
		alertdialogDeleteAcc.show();
		
	}

	public void createRoom(View view) {
		Intent itCreateRoom = new Intent(this, CreateRoomActivity.class);
		itCreateRoom.putExtra("LATITUDE", locationUser.getLatitude());
		itCreateRoom.putExtra("LONGITUDE", locationUser.getLongitude());
		this.startActivity(itCreateRoom);
	}

	/**
	 * Retrieves the user's available chatrooms and initializes the ChatController
	 * 
	 * @author Jordan Hazari
	 *
	 */
	class RoomLoader extends AsyncTask<String, String, ChatRoomResultSet> {
		private SelectRoomActivity selectroomactivity;
		private double radiusMeters;
		private double latitude;
		private double longitude;

		/**
		 * Constructs a RoomLoader object
		 * 
		 * @param selectroomroomactivity the activity context
		 */
		public RoomLoader(SelectRoomActivity selectroomactivity, 
		        double latitude, double longitude, double radiusMeters) {
			this.selectroomactivity = selectroomactivity;
			this.longitude = longitude;
			this.latitude = latitude;
			this.radiusMeters = radiusMeters;
		}

		/**
		 * Retrieves and displays the available rooms
		 * 
		 * @return null if application is disconnected from the network
		 */
		@Override
		protected ChatRoomResultSet doInBackground(String... params) {			
			ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
            
			if (networkinfo == null || !networkinfo.isConnected()) {
				Toast toast = Toast.makeText(getApplicationContext(), R.string.network_failed, Toast.LENGTH_SHORT);
				toast.show();
				return null;
			}
			
//		    ChatController.getInstance().fRefresh();
			ChatRoomResultSet crrsNear = ChatManager.crrsNearbyChatrooms
					(latitude, longitude, radiusMeters);

			rgchatroominfo = crrsNear.rgcriGet();

			selectroomactivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					unJoinedAdapter.clear();
					joinedAdapter.clear();

					for (int i = 0; i < rgchatroominfo.size(); i++) {
						if (!ChatController.getInstance().fIsAlreadyJoined(rgchatroominfo.get(i))) {
							unJoinedAdapter.add(rgchatroominfo.get(i));
						}
//						} else {
//							joinedAdapter.add(rgchatroominfo.get(i));
//						}
					}
					
					List<ChatRoomInfo> rgJoinedRooms = ChatController.getInstance().getChatRooms();
					for (int i = 0; i < rgJoinedRooms.size(); i++) {
						joinedAdapter.add(rgJoinedRooms.get(i));
					}
				}
			});

			return crrsNear;
		}
	}

	private class ChatRoomAdapter extends ArrayAdapter<ChatRoomInfo> {

		private List<ChatRoomInfo> rgchatroominfo;
		private boolean fJoined;

		public ChatRoomAdapter(Context context, int textViewResourceId, List<ChatRoomInfo> rgchatroominfo, boolean fJoined) {
			super(context, textViewResourceId, rgchatroominfo);
			this.rgchatroominfo = rgchatroominfo;
			this.fJoined = fJoined;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.room_item, null);
			}
			ChatRoomInfo chatroominfo = rgchatroominfo.get(position);
			if (chatroominfo != null) {
				TextView textviewTop = (TextView) view.findViewById(R.id.toptext);
				TextView textviewBottom = (TextView) view.findViewById(R.id.bottomtext);
				ImageView button = (ImageView) view.findViewById(R.id.detailsButton);
				
				OnClickListener listener = new OnClickListener() {
				    @Override
				    public void onClick(View view) {
				    	getDetails((Integer) view.getTag(), fJoined);
				    }
				};
				
				if (textviewTop != null) {
					textviewTop.setText(chatroominfo.stName());
				}
				if(textviewBottom != null) {
					textviewBottom.setText("\t" + chatroominfo.numUsersGet() + " users");
				}
				if(button != null) {
					button.setTag(position);
					button.setOnClickListener(listener);
					button.setFocusable(false);
				}
			}
			return view;
		}
	}
	
	/**
	 * AsyncTask that leaves the room.
	 * 
	 * @author Colin Kho
	 *
	 */
	class RoomLeaver extends AsyncTask<String, String, Boolean> {
	    private ChatRoomInfo chatroominfo;
	    ProgressDialog progressdialog;
	    public RoomLeaver(ChatRoomInfo roominfo) {
	        chatroominfo = roominfo;
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
            progressdialog = new ProgressDialog(SelectRoomActivity.this);
            progressdialog.setMessage("Leaving room. Please wait...");
            progressdialog.setIndeterminate(false);
            progressdialog.setCancelable(true);
            progressdialog.show();
	    }
	    
        @Override
        protected Boolean doInBackground(String... params) {
            ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
            
            if (success == false) {
            	Toast toast = Toast.makeText(getApplicationContext(), R.string.leave_room_failed, Toast.LENGTH_SHORT);
				toast.show();
            } else {
		        Intent itViewRooms = new Intent(SelectRoomActivity.this, SelectRoomActivity.class);
		  		SelectRoomActivity.this.startActivity(itViewRooms);
		  		SelectRoomActivity.this.finish();
            }
        }    
	}
}
