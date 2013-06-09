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
	private ProgressDialog progressdialog;
	private UserInfo u;
	private double latitude;
	private double longitude;
	private boolean fDebugMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_room);
		
		
		Bundle extras = getIntent().getExtras();
		setLatitude(extras.getDouble("LATITUDE"));
		setLongitude(extras.getDouble("LONGITUDE"));
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
			alertDialogBuilder.setTitle("Invalid Field");
			
			//set dialog message
			alertDialogBuilder
				.setMessage("Room Name must not exceed 40 characters.")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
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
			alertDialogBuilder.setTitle("Invalid Field");
			
			//set dialog message
			alertDialogBuilder
				.setMessage("Room Description must not exceed 100 characters.")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
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
	
	/*
	 * This method gets the debug mode status of the activity.
	 * 
	 */
	public boolean fDebugMode() {
		return isfDebugMode();
	}
	
	/*
	 * Debug Mode methods. This sets the activity in debug mode. 
	 */
	public void setDebugMode() {
		setfDebugMode(true);
	}

    /**
     * @return the progressdialog
     */
    public ProgressDialog getProgressdialog() {
        return progressdialog;
    }

    /**
     * @param progressdialog the progressdialog to set
     */
    public void setProgressdialog(ProgressDialog progressdialog) {
        this.progressdialog = progressdialog;
    }

    /**
     * @return the fDebugMode
     */
    public boolean isfDebugMode() {
        return fDebugMode;
    }

    /**
     * @param fDebugMode the fDebugMode to set
     */
    public void setfDebugMode(boolean fDebugMode) {
        this.fDebugMode = fDebugMode;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
	
}
