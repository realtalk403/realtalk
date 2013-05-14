
package realtalk.activities;

import realtalk.util.ChatManager;
import realtalk.util.RequestResultSet;
import realtalk.util.UserInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.realtalk.R;
/**
 * Activity for login page
 * 
 * @author Brandon
 *
 */
public class LoginActivity extends Activity {
    
	private static final String DEFAULT_ID = "someID";
    private ProgressDialog progressdialog;
    private CheckBox checkboxRememberMe;
    private SharedPreferences sharedpreferencesLoginPrefs;
    private Editor editorLoginPrefs;
    private Boolean fRememberMe;
    private EditText edittextUser;
    private EditText edittextPword;
    private boolean fLoggedIn;
    
    
    /**
     * Sets up activity
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// For remembering username/password and if user is already logged in
		edittextUser = (EditText) findViewById(R.id.editQuery);
		edittextPword = (EditText) findViewById(R.id.editPword);
		checkboxRememberMe = (CheckBox)findViewById(R.id.rememberme);
		sharedpreferencesLoginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		editorLoginPrefs = sharedpreferencesLoginPrefs.edit();
		
//		editorLoginPrefs.clear();
//		editorLoginPrefs.commit();
		
		fLoggedIn = sharedpreferencesLoginPrefs.getBoolean("loggedIn", false);
		
		String stUsername = sharedpreferencesLoginPrefs.getString("loggedin_username", null);
		String stPassword = sharedpreferencesLoginPrefs.getString("loggedin_password", null);
		
		//if user is already logged in, redirect to select room page
		if(fLoggedIn) {
			Intent itRoomSelect = new Intent(this, SelectRoomActivity.class);
			UserInfo userinfo = new UserInfo(stUsername, stPassword, DEFAULT_ID);
            itRoomSelect.putExtra("USER", userinfo);
			this.startActivity(itRoomSelect);
			finish();
		} else {			
			fRememberMe = sharedpreferencesLoginPrefs.getBoolean("saveLogin", false);
			
			if(fRememberMe) {	
				//loading saved username/password if setting is on
				edittextUser.setText(sharedpreferencesLoginPrefs.getString("username", null));
				edittextPword.setText(sharedpreferencesLoginPrefs.getString("password", null));
				checkboxRememberMe.setChecked(true);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/**
	 * Called when user clicks "Create Account".  Redirects to Create Account page.
	 * 
	 * @param view
	 */
	public void addUser(View view) {
		Intent itCreateAcc = new Intent(this, CreateAccountActivity.class);
		this.startActivity(itCreateAcc);
	}
	
	/** 
	 * Called when user attempts to login.
	 * 
	 * @param view
	 */
	public void authenticateUser(View view) {
	    String stUsername = edittextUser.getText().toString();
	    String stPword = edittextPword.getText().toString();
	    
	    if(stUsername.equals("") || stPword.equals("")) {	    	
	    	AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
			//set title
			alertdialogbuilder.setTitle("Invalid input");
			
			//set dialog message
			alertdialogbuilder
				.setMessage("Please enter a username & password.")
				.setCancelable(false);
			
			alertdialogbuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
					dialog.cancel();
				}
			});
			
			//create alert dialog
			AlertDialog alertdialogEmptyFields = alertdialogbuilder.create();
			
			//show alert dialog
			alertdialogEmptyFields.show();	
	    } else {
	    	if(checkboxRememberMe.isChecked()) {
	    		//stores login info if "Remember Me" checkbox is checked
	    		editorLoginPrefs.putBoolean("saveLogin", true);
	    		editorLoginPrefs.putString("username", stUsername);
	    		editorLoginPrefs.putString("password", stPword);
	    		editorLoginPrefs.commit();
	    	} else {
	    		//clears existing login info if "Remember Me" checkbox is not checked
	    		editorLoginPrefs.clear();
	    		editorLoginPrefs.commit();
	    	}
	    	new Authenticator(new UserInfo(stUsername, stPword, DEFAULT_ID), this).execute();
	    }
	    
	}
	
	/**
	 * Called when user attempts to delete their account.
	 * 
	 * @param view
	 */
	public void removeUser(View view) {
		EditText edittextUser = (EditText) findViewById(R.id.editQuery);
	    EditText edittextPword = (EditText) findViewById(R.id.editPword);
	    String stUsername = edittextUser.getText().toString();
	    String stPword = edittextPword.getText().toString();
		if(stUsername.equals("") || stPword.equals("")) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			//set title
			alertDialogBuilder.setTitle("Invalid input");
			
			//set dialog message
			alertDialogBuilder
				.setMessage("Please enter a username & password.")
				.setCancelable(false);
			
			
			alertDialogBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
					dialog.cancel();
				}
			});
			
			//create alert dialog
			AlertDialog alertdialogEmptyFields = alertDialogBuilder.create();
			
			//show alert dialog
			alertdialogEmptyFields.show();
		} else {
			//confirmation pop up
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			//set title
			alertDialogBuilder.setTitle("Delete account");
			
			//set dialog message
			alertDialogBuilder
				.setMessage("Are you sure you want to delete this account?")
				.setCancelable(false);
			
			alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//close the dialog box if this button is clicked
					EditText edittextUser = (EditText) findViewById(R.id.editQuery);
				    EditText edittextPword = (EditText) findViewById(R.id.editPword);
				    String stUsername = edittextUser.getText().toString();
				    String stPword = edittextPword.getText().toString();
				    new UserRemover(new UserInfo(stUsername, stPword, DEFAULT_ID), LoginActivity.this).execute();
				}	
			});
			
			alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
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
	}

	
	class UserRemover extends AsyncTask<String, String, RequestResultSet> {
		private UserInfo userinfo;
		private Activity activity;
		public UserRemover(UserInfo userinfo, Activity activity) {
			this.userinfo = userinfo;
			this.activity = activity;
		}
		
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog = new ProgressDialog(LoginActivity.this);
            progressdialog.setMessage("Loading user details. Please wait...");
            progressdialog.setIndeterminate(false);
            progressdialog.setCancelable(true);
            progressdialog.show();
        }
	    
        @Override
        protected RequestResultSet doInBackground(String... params) {
        	return ChatManager.rrsRemoveUser(userinfo);
        }
        
        @Override
        protected void onPostExecute(RequestResultSet requestresultset) {
            progressdialog.dismiss();
            if(requestresultset.fSucceeded == false) {
            	//invalid username or password
            	
            	AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(activity);
				//set title
				alertdialogbuilder.setTitle("Invalid fields");
				
				//set dialog message
				alertdialogbuilder
					.setMessage("Invalid username/password. Please try again.")
					.setCancelable(false)
					.setPositiveButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//close the dialog box if this button is clicked
							dialog.cancel();
						}	
				});
				
				//create alert dialog
				AlertDialog alertdialogBadPword = alertdialogbuilder.create();
				
				//show alert dialog
				alertdialogBadPword.show();	
            } else {
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
						}	
				});
				
				//create alert dialog
				AlertDialog alertdialogAccDeleted = alertDialogBuilder.create();
				
				//show alert dialog
				alertdialogAccDeleted.show();	
				
				TextView textviewUname = (TextView) findViewById(R.id.editQuery);
	            textviewUname.setText("");
            }
            
            TextView textviewPword = (TextView) findViewById(R.id.editPword);
            textviewPword.setText("");
        }
	}
	
	/**
	 * Authenticates a user in the database 
	 * 
	 * @author Brandon
	 *
	 */
	class Authenticator extends AsyncTask<String, String, RequestResultSet> {
		private UserInfo userinfo;
		private Activity activity;
		
		/**
		 * Constructs an Authenticator object
		 * 
		 * @param userinfo user to be authenticated
		 * @param activity the activity context
		 */
		public Authenticator(UserInfo userinfo, Activity activity) {
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
            progressdialog = new ProgressDialog(LoginActivity.this);
            progressdialog.setMessage("Loading user details. Please wait...");
            progressdialog.setIndeterminate(false);
            progressdialog.setCancelable(true);
            progressdialog.show();
        }
	    
	    /**
	     * Authenticates user in the database
	     */
        @Override
        protected RequestResultSet doInBackground(String... params) {
        	return ChatManager.rrsAuthenticateUser(userinfo);
        }
        
        /**
         * Prompts user if their username/password were incorrect.  Otherwise, redirects
         * to the Select Rooms activity.
         */
        @Override
        protected void onPostExecute(RequestResultSet requestresultset) {

            progressdialog.dismiss();
            //invalid username/password
            if(requestresultset.fSucceeded == false) {
            	AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(activity);
				//set title
				alertdialogbuilder.setTitle("Invalid fields");
				
				//set dialog message
				alertdialogbuilder
					.setMessage("Invalid username/password. Please try again.")
					.setCancelable(false)
					.setPositiveButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//close the dialog box if this button is clicked
							dialog.cancel();
						}	
				});
				
				//create alert dialog
				AlertDialog alertdialogBadPword = alertdialogbuilder.create();
				
				//show alert dialog
				alertdialogBadPword.show();	
				
				TextView textviewPword = (TextView) findViewById(R.id.editPword);
	            textviewPword.setText("");
            } else {           	
                String username = userinfo.stUserName();
                String password = userinfo.stPassword();
        		
        		editorLoginPrefs.putBoolean("loggedIn", true);
	    		editorLoginPrefs.putString("loggedin_username", username);
	    		editorLoginPrefs.putString("loggedin_password", password);
	    		editorLoginPrefs.commit();
        		
                Intent itRoomSelect = new Intent(activity, SelectRoomActivity.class);
                UserInfo userinfo = new UserInfo(username, password, DEFAULT_ID);
                itRoomSelect.putExtra("USER", userinfo);
        		activity.startActivity(itRoomSelect);
        		activity.finish();  
            }
        }
	}
}
