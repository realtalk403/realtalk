package realtalk.asynctasks;

import realtalk.activities.AccountSettingsActivity;
import realtalk.activities.LoginActivity;
import realtalk.controller.ChatController;
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
 * Removes a user from the database 
 * 
 * @author Brandon
 *
 */
public class UserRemover extends AsyncTask<String, String, RequestResultSet> {
	/**
	 * 
	 */
	private AccountSettingsActivity accountSettingsActivity;
	private UserInfo userinfo;
	private Activity activity;
	
	/**
	 * Constructs a UserRemover object
	 * 
	 * @param userinfo the user to remove
	 * @param activity the activity context
	 * @param accountSettingsActivity TODO
	 */
	public UserRemover(AccountSettingsActivity accountSettingsActivity, UserInfo userinfo, Activity activity) {
		this.accountSettingsActivity = accountSettingsActivity;
		this.userinfo = userinfo;
		this.activity = activity;
	}
	
	/**
	 * Displays a popup dialog while account is being removed
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
     * Removes account from the database
     * 
     * @return rrs with results or null if diconnected
     */
    @Override
    protected RequestResultSet doInBackground(String... params) {
    	 ConnectivityManager connectivitymanager = (ConnectivityManager) this.accountSettingsActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
         
		if (networkinfo != null && networkinfo.isConnected()) {
			return ChatManager.rrsRemoveUser(userinfo);
		} else {
			return null;
		}
    }
    
    /**
     * Prompts the user that their account has been deleted and redirects them to login page
     */
    @Override
    protected void onPostExecute(RequestResultSet rrs) {
    	this.accountSettingsActivity.getProgressdialog().dismiss();
    	if (rrs == null) {
    		Toast toast = Toast.makeText(this.accountSettingsActivity.getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG);
			toast.show();
    	} else if(rrs.getfSucceeded()) {
        	this.accountSettingsActivity.getEditorLoginPrefs().clear();
        	this.accountSettingsActivity.getEditorLoginPrefs().commit();
        	
            this.accountSettingsActivity.getProgressdialog().dismiss();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
			//set title
			alertDialogBuilder.setTitle("Account deleted");
			
			//set dialog message
			alertDialogBuilder
				.setMessage("Your account has been deleted.")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//close the dialog box if this button is clicked
						dialog.cancel();
						
						Intent itLogin = new Intent(activity, LoginActivity.class);
						itLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
						itLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						ChatController.getInstance().uninitialize();
						activity.startActivity(itLogin);
						activity.finish();
					}	
			});
				
			//create alert dialog
			AlertDialog alertdialogAccDeleted = alertDialogBuilder.create();
				
			//show alert dialog
			alertdialogAccDeleted.show();	
    	} else {
    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
			//set title
			alertDialogBuilder.setTitle("Account not deleted");
			
			//set dialog message
			alertDialogBuilder
				.setMessage("Error.  Your account has not been deleted.")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//close the dialog box if this button is clicked
						dialog.cancel();
					}	
			});
				
			//create alert dialog
			AlertDialog alertdialogAccNotDeleted = alertDialogBuilder.create();
				
			//show alert dialog
			alertdialogAccNotDeleted.show();
    	}
		
    }
}
