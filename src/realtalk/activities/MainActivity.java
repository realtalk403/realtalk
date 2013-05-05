package realtalk.activities;

import realtalk.util.ChatManager;
import realtalk.util.RequestResultSet;
import realtalk.util.User;
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

public class MainActivity extends Activity {
    
    private ProgressDialog pDialog;
    
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
		Intent createAcc = new Intent(this, CreateAccountActivity.class);
		this.startActivity(createAcc);
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
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			//show alert dialog
			alertDialog.show();	
	    } else {
	    	new Authenticator(new User(stUsername, stPword), this).execute();
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
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			//show alert dialog
			alertDialog.show();
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
				    new UserRemover(new User(stUsername, stPword), MainActivity.this).execute();
				}	
			});
			
			alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
					dialog.cancel();
				}
			});
			
			//create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			//show alert dialog
			alertDialog.show();	
		}
	}


	class UserRemover extends AsyncTask<String, String, RequestResultSet> {
		private User user;
		private Activity activity;
		public UserRemover(User user, Activity activity) {
			this.user = user;
			this.activity = activity;
		}
		
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading user details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
	    
        @Override
        protected RequestResultSet doInBackground(String... params) {
        	return ChatManager.removeUser(user);
        }
        
        @Override
        protected void onPostExecute(RequestResultSet requestresultset) {
            pDialog.dismiss();
            if(requestresultset.fSucceeded == false) {
            	//invalid username or password
            	
            	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
				//set title
				alertDialogBuilder.setTitle("Invalid fields");
				
				//set dialog message
				alertDialogBuilder
					.setMessage("Invalid username/password. Please try again.")
					.setCancelable(false)
					.setPositiveButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//close the dialog box if this button is clicked
							dialog.cancel();
						}	
				});
				
				//create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
				//show alert dialog
				alertDialog.show();	
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
				AlertDialog alertDialog = alertDialogBuilder.create();
				
				//show alert dialog
				alertDialog.show();	
				
				TextView username = (TextView) findViewById(R.id.editQuery);
	            username.setText("");
            }
            
            TextView password = (TextView) findViewById(R.id.editPword);
            password.setText("");
        }
	}
	
	class Authenticator extends AsyncTask<String, String, RequestResultSet> {
		private User user;
		private Activity activity;
		
		public Authenticator(User user, Activity activity) {
			this.user = user;
			this.activity = activity;
		}
		
	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading user details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
	    
        @Override
        protected RequestResultSet doInBackground(String... params) {
        	return ChatManager.authenticateUser(user);
        }
        
        @Override
        protected void onPostExecute(RequestResultSet requestresultset) {
            pDialog.dismiss();       
            //invalid username/password
            if(requestresultset.fSucceeded == false) {
            	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
				//set title
				alertDialogBuilder.setTitle("Invalid fields");
				
				//set dialog message
				alertDialogBuilder
					.setMessage("Invalid username/password. Please try again.")
					.setCancelable(false)
					.setPositiveButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//close the dialog box if this button is clicked
							dialog.cancel();
						}	
				});
				
				//create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
				//show alert dialog
				alertDialog.show();	
				
				TextView password = (TextView) findViewById(R.id.editPword);
	            password.setText("");
            } else {
            	//PUT WHATEVER CODE FOR A SUCCESSFUL LOGIN HERE
            	
            	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
				//set title
				alertDialogBuilder.setTitle("Login successful");
				
				//set dialog message
				alertDialogBuilder
					.setMessage("Login Successful!")
					.setCancelable(false)
					.setPositiveButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//close the dialog box if this button is clicked
							dialog.cancel();
						}	
				});
				
				//create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
				//show alert dialog
				alertDialog.show();	
            }
        }
	}
}
