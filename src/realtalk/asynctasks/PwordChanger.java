package realtalk.asynctasks;

import realtalk.activities.AccountSettingsActivity;
import realtalk.controller.ChatController;
import realtalk.util.ChatManager;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.realtalk.R;

/**
 * Changes a user's password
 * 
 * @author Brandon
 *
 */
public class PwordChanger extends AsyncTask<String, String, RequestResultSet> {
	/**
	 * 
	 */
	private AccountSettingsActivity accountSettingsActivity;
	private UserInfo userinfo;
	private Activity activity;
	private String stNewPword;
	
	/**
	 * Constructs a PwordChanger object
	 * 
	 * @param user	the user to add
	 * @param activity	the activity context
	 * @param accountSettingsActivity
	 */
	public PwordChanger(AccountSettingsActivity accountSettingsActivity, UserInfo user, Activity activity, String stNewPword) {
		this.accountSettingsActivity = accountSettingsActivity;
		this.userinfo = user;
		this.activity = activity;
		this.stNewPword = stNewPword;
	}
	
	/**
	 * Displays a popup dialogue while changing the user's password
	 */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.accountSettingsActivity.setProgressdialog(new ProgressDialog(this.accountSettingsActivity));
        this.accountSettingsActivity.getProgressdialog().setMessage("Loading user details. Please wait...");
        this.accountSettingsActivity.getProgressdialog().setIndeterminate(false);
        this.accountSettingsActivity.getProgressdialog().setCancelable(true);
        this.accountSettingsActivity.getProgressdialog().show();
    }
    
    /**
     * Changes the user's password in database
     * @return rrs of the request, null if disconnected
     */
    @Override
    protected RequestResultSet doInBackground(String... params) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) this.accountSettingsActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        
		if (networkinfo != null && networkinfo.isConnected()) {
			RequestResultSet requestresultset = ChatManager.rrsChangePassword(userinfo, stNewPword);
			
            if (requestresultset.getfSucceeded()) {
                ChatController.getInstance().uninitialize();
                UserInfo updatedUserinfo = new UserInfo(userinfo.stUserName(), userinfo.stPassword(), stNewPword);
                ChatController.getInstance().fInitialize(updatedUserinfo);
            }
            
            return requestresultset;
		} else {
			return null;
		}
        
    }
    
    /**
     * Closes the dialogue, and lets the user know if they have input
     * invalid fields
     */
    @Override
    protected void onPostExecute(RequestResultSet rrs) {
        this.accountSettingsActivity.getProgressdialog().dismiss();
        if (rrs == null) {
        	Toast toast = Toast.makeText(this.accountSettingsActivity.getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG);
			toast.show();
        } else if(!rrs.getfSucceeded()) {
        	//invalid password pop up
        	AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(activity);
			//set title
			alertdialogbuilder.setTitle("Invalid fields");
			
			//set dialog message
			alertdialogbuilder
				.setMessage("Old password incorrect.  Please try again.")
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
        	this.accountSettingsActivity.getEditorLoginPrefs().putString("loggedin_password", stNewPword);
        	this.accountSettingsActivity.getEditorLoginPrefs().commit();
        	
        	//password change successful pop up
        	AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(activity);
			//set title
			alertdialogbuilder.setTitle("Password Changed");
			
			//set dialog message
			alertdialogbuilder
				.setMessage("Password changed.")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//close the dialog box if this button is clicked
						TextView textviewNewPword = (TextView) PwordChanger.this.accountSettingsActivity.findViewById(R.id.newpword);
			            textviewNewPword.setText("");
			            TextView textviewConfPword = (TextView) PwordChanger.this.accountSettingsActivity.findViewById(R.id.confpword);
			            textviewConfPword.setText("");
			            TextView textviewOldPword = (TextView) PwordChanger.this.accountSettingsActivity.findViewById(R.id.oldpword);
			            textviewOldPword.setText("");
						dialog.cancel();
					}	
			});
			
			//create alert dialog
			AlertDialog alertdialogBadUname = alertdialogbuilder.create();
			
			//show alert dialog
			alertdialogBadUname.show();	
        }
    }
}
