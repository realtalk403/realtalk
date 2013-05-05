package realtalk.activities;

import realtalk.util.ChatManager;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.example.realtalk.R;

public class CreateAccountActivity extends Activity {
	
	private static final String DEFAULT_ID = "someID";
	private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_create_account);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_account, menu);
		return true;
	}
	
	/**
	 * This method is called when user clicks "Create Account" button.  It reads
	 * the information that the user has provided and attempts to create an account.
	 * 
	 * @param v
	 */
	public void addUser(View v) {
		EditText edittextUser = (EditText) findViewById(R.id.user);
		EditText edittextPword = (EditText) findViewById(R.id.pword);
		EditText edittextConfPword = (EditText) findViewById(R.id.conf_pword);
		String stUsername = edittextUser.getText().toString();
		String stPword = edittextPword.getText().toString();
		String stConf = edittextConfPword.getText().toString();
		
		//if any fields are blank, dialog box pops up
		if(stPword.equals("") || stUsername.equals("") || stConf.equals("")) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			//set title
			alertDialogBuilder.setTitle("Invalid fields");
			
			//set dialog message
			alertDialogBuilder
				.setMessage("Please fill in all of the fields.")
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//close the dialog box if this button is clicked
						dialog.cancel();
					}	
			});
			
			//create alert dialog
			AlertDialog alertdialogEmptyField = alertDialogBuilder.create();
			
			//show alert dialog
			alertdialogEmptyField.show();
		} else if(!stPword.equals(stConf)) {	//if the password doesn't match the confirmation password
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			//set title
			alertDialogBuilder.setTitle("Invalid fields");
			
			//set dialog message
			alertDialogBuilder
				.setMessage("Passwords do not match.  Please try again.")
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
			new UserAdder(new UserInfo(stUsername, stPword, DEFAULT_ID), this).execute();
		}
	}
	
	class UserAdder extends AsyncTask<String, String, RequestResultSet> {
		//This class is called when a user is trying to be added to the database.
		//If it fails, it updates a string on the 
		private UserInfo user;
		private Activity activity;
		
		public UserAdder(UserInfo user, Activity activity) {
			this.user = user;
			this.activity = activity;
		}
		
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateAccountActivity.this);
            pDialog.setMessage("Loading user details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
	    
        @Override
        protected RequestResultSet doInBackground(String... params) {
        	return ChatManager.addUser(user);
        }
        
        @Override
        protected void onPostExecute(RequestResultSet requestresultset) {
            pDialog.dismiss();
            if(requestresultset.fSucceeded == false) {
            	//user already exists pop up
            	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
				//set title
				alertDialogBuilder.setTitle("Invalid fields");
				
				//set dialog message
				alertDialogBuilder
					.setMessage("Username already exists.  Please choose another username.")
					.setCancelable(false)
					.setPositiveButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//close the dialog box if this button is clicked
							dialog.cancel();
						}	
				});
				
				//create alert dialog
				AlertDialog alertdialogBadUname = alertDialogBuilder.create();
				
				//show alert dialog
				alertdialogBadUname.show();	
            } else {
            	//redirect back to LoginActivity if successful
            	Intent createAcc = new Intent(activity, MainActivity.class);
				activity.startActivity(createAcc);
            }
            
        }
	}
	
	

}
