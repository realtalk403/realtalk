package realtalk.activities;


import com.realtalk.R;

import realtalk.asynctasks.RoomCreator;
import realtalk.controller.ChatController;
import realtalk.util.UserInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CreateRoomActivity extends Activity {
    private static final int DESCLENGTH = 100;
    private static final int ROOMNAMELENGTH = 40;
	public ProgressDialog progressdialog;
	private UserInfo u;
	public double latitude;
	public double longitude;
	public boolean fDebugMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_room);
		
		
		Bundle extras = getIntent().getExtras();
		latitude = extras.getDouble("LATITUDE");
		longitude = extras.getDouble("LONGITUDE");
		if (!extras.containsKey("DEBUG_MODE")) {
			u = ChatController.getInstance().getUser();
		} else {
			u = new UserInfo("testname", "stPassword", "stRegistrationId");
			setDebugMode();
		}
		String stUser = u.stUserName();		
		
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
		EditText roomNameText = (EditText)findViewById(R.id.roomName);
		String stRoomName = roomNameText.getText().toString();
		
		EditText roomDescription = (EditText)findViewById(R.id.description);
		String stDescription = roomDescription.getText().toString();
		
		if(stRoomName.length() > ROOMNAMELENGTH) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			//set title
			alertDialogBuilder.setTitle(R.string.invalid_field);
			
			//set dialog message
			alertDialogBuilder
				.setMessage(R.string.long_room_name)
				.setCancelable(false)
				.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//close the dialog box if this button is clicked
						dialog.cancel();
					}	
			});
			
			//create alert dialog
			AlertDialog alertdialogBadPword = alertDialogBuilder.create();
			
			//show alert dialog
			alertdialogBadPword.show();	
		} else if (stDescription.length() > DESCLENGTH) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			//set title
			alertDialogBuilder.setTitle(R.string.invalid_field);
			
			//set dialog message
			alertDialogBuilder
				.setMessage(R.string.long_room_desc)
				.setCancelable(false)
				.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//close the dialog box if this button is clicked
						dialog.cancel();
					}	
			});
			
			//create alert dialog
			AlertDialog alertdialogBadPword = alertDialogBuilder.create();
			
			//show alert dialog
			alertdialogBadPword.show();	
		} else {
			new RoomCreator(this, u, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}
	
	public boolean fDebugMode() {
		return fDebugMode;
	}
	
	public void setDebugMode() {
		fDebugMode = true;
	}
	
}
