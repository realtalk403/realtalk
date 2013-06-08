package realtalk.asynctasks;

import java.util.List;

import realtalk.activities.SelectRoomActivity;
import realtalk.controller.ChatController;
import realtalk.util.ChatManager;
import realtalk.util.ChatRoomInfo;
import realtalk.util.ChatRoomResultSet;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.realtalk.R;

/**
	 * Retrieves the user's available chatrooms and initializes the ChatController
	 * 
	 * @author Jordan Hazari
	 *
	 */
public class RoomLoader extends AsyncTask<String, String, ChatRoomResultSet> {
		/**
		 * 
		 */
		private SelectRoomActivity selectRoomActivity2;
		private SelectRoomActivity selectroomactivity;
		private double radiusMeters;
		private double latitude;
		private double longitude;

		/**
		 * Constructs a RoomLoader object
		 * @param selectRoomActivity2 TODO
		 * 
		 * @param selectroomroomactivity the activity context
		 */
		public RoomLoader(SelectRoomActivity selectRoomActivity2, SelectRoomActivity selectroomactivity, 
		        double latitude, double longitude, double radiusMeters) {
			this.selectRoomActivity2 = selectRoomActivity2;
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
			ConnectivityManager connectivitymanager = (ConnectivityManager) this.selectRoomActivity2.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
            
			if (networkinfo == null || !networkinfo.isConnected()) {
				Toast toast = Toast.makeText(this.selectRoomActivity2.getApplicationContext(), R.string.network_failed, Toast.LENGTH_SHORT);
				toast.show();
				return null;
			}
			
//		    ChatController.getInstance().fRefresh();
			ChatRoomResultSet crrsNear = ChatManager.crrsNearbyChatrooms
					(latitude, longitude, radiusMeters);

			this.selectRoomActivity2.rgchatroominfo = crrsNear.rgcriGet();

			selectroomactivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					RoomLoader.this.selectRoomActivity2.unJoinedAdapter.clear();
					RoomLoader.this.selectRoomActivity2.joinedAdapter.clear();

					for (int i = 0; i < RoomLoader.this.selectRoomActivity2.rgchatroominfo.size(); i++) {
						if (!ChatController.getInstance().fIsAlreadyJoined(RoomLoader.this.selectRoomActivity2.rgchatroominfo.get(i))) {
							RoomLoader.this.selectRoomActivity2.unJoinedAdapter.add(RoomLoader.this.selectRoomActivity2.rgchatroominfo.get(i));
						}
//						} else {
//							joinedAdapter.add(rgchatroominfo.get(i));
//						}
					}
					
					List<ChatRoomInfo> rgJoinedRooms = ChatController.getInstance().getChatRooms();
					for (int i = 0; i < rgJoinedRooms.size(); i++) {
						RoomLoader.this.selectRoomActivity2.joinedAdapter.add(rgJoinedRooms.get(i));
					}
				}
			});

			return crrsNear;
		}
	}