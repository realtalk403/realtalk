package realtalk.asynctasks;

import realtalk.activities.LoginActivity;
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
 * Authenticates a user in the database 
 * 
 * @author Brandon
 *
 */
public class Authenticator extends AsyncTask<String, String, RequestResultSet> {
	/**
	 * 
	 */
	private final LoginActivity loginActivity;
	private UserInfo userinfo;
	private Activity activity;
	
	/**
	 * Constructs an Authenticator object
	 * 
	 * @param userinfo user to be authenticated
	 * @param activity the activity context
	 * @param loginActivity TODO
	 */
	public Authenticator(LoginActivity loginActivity, UserInfo userinfo, Activity activity) {
		this.loginActivity = loginActivity;
		this.userinfo = userinfo;
		this.activity = activity;
	}
	
	/**
	 * Pop up dialog while user is being authenticated 
	 * 
	 */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.loginActivity.progressdialog = new ProgressDialog(this.loginActivity);
        this.loginActivity.progressdialog.setMessage(loginActivity.getResources().getString(R.string.load_user_details));
        this.loginActivity.progressdialog.setIndeterminate(false);
        this.loginActivity.progressdialog.setCancelable(true);
        this.loginActivity.progressdialog.show();
    }
    
    /**
     * Authenticates user in the database
     * 
     * @return rrs with results, or null if disconnected
     */
    @Override
    protected RequestResultSet doInBackground(String... params) {
        ConnectivityManager connectivitymanager = (ConnectivityManager) this.loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        
		if (networkinfo != null && networkinfo.isConnected()) {
			return ChatManager.rrsAuthenticateUser(userinfo);
		} else {
			return null;
		}
    }
    
    /**
     * Prompts user if their username/password were incorrect.  Otherwise, redirects
     * to the Select Rooms activity.
     */
    @Override
    protected void onPostExecute(RequestResultSet requestresultset) {
        this.loginActivity.progressdialog.dismiss();
        if (requestresultset == null) {
        	Toast toast = Toast.makeText(this.loginActivity.getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG);
			toast.show();
        } else if(!requestresultset.getfSucceeded()) {
        	AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(activity);
			//set title
			alertdialogbuilder.setTitle(R.string.invalid_fields);
			
			//set dialog message
			alertdialogbuilder
				.setMessage(R.string.invalid_uname_pword)
				.setCancelable(false)
				.setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//close the dialog box if this button is clicked
						dialog.cancel();
					}	
			});
			
			//create alert dialog
			AlertDialog alertdialogBadPword = alertdialogbuilder.create();
			
			//show alert dialog
			alertdialogBadPword.show();	
			
			TextView textviewPword = (TextView) this.loginActivity.findViewById(R.id.editPword);
            textviewPword.setText("");
        } else {           	
            String username = userinfo.stUserName();
            String password = userinfo.stPassword();
    		
    		//for when we have a logout button, so that pressing back on the rooms page 
    		//doesn't take you back to the login screen, but rather exits the app.
    		this.loginActivity.editorLoginPrefs.putBoolean("loggedIn", true);
    		this.loginActivity.editorLoginPrefs.putString("loggedin_username", username);
    		this.loginActivity.editorLoginPrefs.putString("loggedin_password", password);
    		this.loginActivity.editorLoginPrefs.commit();
    		
    		this.loginActivity.updateRegId(new UserInfo(username, password, this.loginActivity.stRegisteredId));  
        }
    }
}