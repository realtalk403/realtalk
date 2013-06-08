package realtalk.asynctasks;

import realtalk.activities.CreateAccountActivity;
import realtalk.activities.LoginActivity;
import realtalk.util.ChatManager;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.realtalk.R;

/**
 * Adds a user to the database
 * 
 * @author Brandon Lee
 *
 */
public class UserAdder extends AsyncTask<String, String, RequestResultSet> {
	/**
	 * 
	 */
	private CreateAccountActivity createAccountActivity;
	private UserInfo userinfo;
	private Activity activity;
	
	/**
	 * Constructs a UserAdder object
	 * 
	 * @param user	the user to add
	 * @param activity	the activity context
	 * @param createAccountActivity TODO
	 */
	public UserAdder(CreateAccountActivity createAccountActivity, UserInfo user, Activity activity) {
		this.createAccountActivity = createAccountActivity;
		this.userinfo = user;
		this.activity = activity;
	}
	
	/**
	 * Displays a popup dialogue while adding the user
	 */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.createAccountActivity.setProgressdialog(new ProgressDialog(this.createAccountActivity));
        this.createAccountActivity.getProgressdialog().setMessage("Loading user details. Please wait...");
        this.createAccountActivity.getProgressdialog().setIndeterminate(false);
        this.createAccountActivity.getProgressdialog().setCancelable(true);
        this.createAccountActivity.getProgressdialog().show();
    }
    
    /**
     * Adds a user to the database
     */
    @Override
    protected RequestResultSet doInBackground(String... params) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) this.createAccountActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        
		if (networkinfo != null && networkinfo.isConnected()) {
			return ChatManager.rrsAddUser(userinfo);
		} else {
			return null;
		}
    }
    
    /**
     * Closes the dialogue, and lets the user know if they have input
     * invalid fields, or if their desired username already exists
     */
    @Override
    protected void onPostExecute(RequestResultSet requestresultset) {
        this.createAccountActivity.getProgressdialog().dismiss();
        if (requestresultset == null) {
        	Toast toast = Toast.makeText(this.createAccountActivity.getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG);
			toast.show();
        } else if(!requestresultset.getfSucceeded()) {
        	//user already exists pop up
        	AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(activity);
			//set title
			alertdialogbuilder.setTitle("Invalid fields");
			
			//set dialog message
			alertdialogbuilder
				.setMessage("Username already exists.  Please choose another username.")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//close the dialog box if this button is clicked
						dialog.cancel();
					}	
			});
			
			//create alert dialog
			AlertDialog alertdialogBadUname = alertdialogbuilder.create();
			
			//show alert dialog
			alertdialogBadUname.show();	
        } else {
        	//redirect back to LoginActivity if successful
        	Intent createAcc = new Intent(activity, LoginActivity.class);
			activity.startActivity(createAcc);
			activity.finish();
        } 
    }
}
