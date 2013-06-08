package realtalk.asynctasks;

import realtalk.activities.LoginActivity;
import realtalk.activities.SelectRoomActivity;
import realtalk.controller.ChatController;
import realtalk.util.ChatManager;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.realtalk.R;

/**
 * Updates the user to have the correct registration ID each time he/she logs in
 * 
 * @author Colin Kho
 *
 */
public class UpdateRegId extends AsyncTask<String, String, RequestResultSet> {
    /**
	 * 
	 */
	private final LoginActivity loginActivity;
	private UserInfo userinfo;
    private Activity activity;
    
    public UpdateRegId(LoginActivity loginActivity, UserInfo userinfo, Activity activity) {
        this.loginActivity = loginActivity;
		this.userinfo = userinfo;
        this.activity = activity;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.loginActivity.setProgressdialog(new ProgressDialog(this.loginActivity));
        this.loginActivity.getProgressdialog().setMessage("Updating Server. Please wait...");
        this.loginActivity.getProgressdialog().setIndeterminate(false);
        this.loginActivity.getProgressdialog().setCancelable(true);
        this.loginActivity.getProgressdialog().show();
    }
    
    /**
     * @return result set with results of change, unless disconnected from the network, then null. 
     */
    @Override
    protected RequestResultSet doInBackground(String... params) {
        UserInfo loginUserinfo = new UserInfo(userinfo.stUserName(), userinfo.stPassword(), this.loginActivity.getStRegisteredId());
        
        ConnectivityManager connectivitymanager = (ConnectivityManager) this.loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
        
		if (networkinfo != null && networkinfo.isConnected()) {
			ChatController.getInstance().fInitialize(loginUserinfo);
			return ChatManager.rrsChangeID(userinfo, this.loginActivity.getStRegisteredId());
		} else {
			return null;
		}
    }
    
    @Override
    protected void onPostExecute(RequestResultSet rrs) {
        this.loginActivity.getProgressdialog().dismiss();
        if (rrs == null) {
			Toast toast = Toast.makeText(this.loginActivity.getApplicationContext(), R.string.network_failed, Toast.LENGTH_LONG);
			toast.show();
        } else if (rrs.getfSucceeded()) {                
            Intent itRoomSelect = new Intent(activity, SelectRoomActivity.class);
            activity.startActivity(itRoomSelect);
            activity.finish();
        } else {
            Toast serverToast = Toast.makeText(activity, R.string.login_failed, Toast.LENGTH_LONG); 
            serverToast.show();
        }
    }
}
