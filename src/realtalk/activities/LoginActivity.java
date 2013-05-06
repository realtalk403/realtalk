
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
import android.widget.TextView;

import com.example.realtalk.R;
/**
 * 
 * @author blee92
 *
 */
public class LoginActivity extends Activity {
    
	private static final String DEFAULT_ID = "someID";
    private ProgressDialog progressdialog;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
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
	    EditText edittextUser = (EditText) findViewById(R.id.editQuery);
	    EditText edittextPword = (EditText) findViewById(R.id.editPword);
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
	
	class Authenticator extends AsyncTask<String, String, RequestResultSet> {
		private UserInfo userinfo;
		private Activity activity;
		
		public Authenticator(UserInfo userinfo, Activity activity) {
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
        	return ChatManager.rrsAuthenticateUser(userinfo);
        }
        
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
                EditText uNameText = (EditText)findViewById(R.id.editQuery);
        		String uName = uNameText.getText().toString();
        		
        		EditText pWordText = (EditText)findViewById(R.id.editPword);
        		String pWord = pWordText.getText().toString();
        		
                Intent viewRs = new Intent(activity, SelectRoomActivity.class);
                UserInfo userinfo = new UserInfo(uName, pWord, DEFAULT_ID);
                viewRs.putExtra("USER", userinfo);
        		activity.startActivity(viewRs);
            }
        }
	}
}
