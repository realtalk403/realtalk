package realtalk.activities;

import realtalk.asynctasks.PwordChanger;
import realtalk.asynctasks.UserRemover;
import realtalk.util.CommonUtilities;
import realtalk.util.UserInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.realtalk.R;

/**
 * Activity for Account Settings
 * 
 * @author Brandon
 *
 */
public class AccountSettingsActivity extends Activity {
	private static final String DEFAULT_ID = "someID";
	private static final int MAX_PASSWORD_LENGTH = 20;
	private ProgressDialog progressdialog;
    private SharedPreferences sharedpreferencesLoginPrefs;
    private Editor editorLoginPrefs;

	/**
	 * Sets up activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account_settings);
		sharedpreferencesLoginPrefs = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		editorLoginPrefs = sharedpreferencesLoginPrefs.edit();
		
		String stUser = sharedpreferencesLoginPrefs.getString("loggedin_username", null);
		TextView textviewUserTitle = (TextView) findViewById(R.id.userTitle);
		textviewUserTitle.setText(stUser);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_settings, menu);
		return true;
	}
	
	/**
	 * Method called when user submits to change his/her password
	 * 
	 * @param view
	 */
	public void changePword(View view) {
		EditText edittextOldPword = (EditText) findViewById(R.id.oldpword);
		EditText edittextNewPword = (EditText) findViewById(R.id.newpword);
		EditText edittextConfPword = (EditText) findViewById(R.id.confpword);
		String stOldPword = edittextOldPword.getText().toString();
		String stNewPword = edittextNewPword.getText().toString();
		String stConfPword = edittextConfPword.getText().toString();

		//check to see if the old password is correct

		//if any fields are blank, dialog box pops up
		if(stNewPword.trim().equals("") || stOldPword.trim().equals("") || stConfPword.trim().equals("")) {
			AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
			//set title
			alertdialogbuilder.setTitle("Invalid fields");

			//set dialog message
			alertdialogbuilder
			.setMessage("Please fill in all of the fields.")
			.setCancelable(false)
			.setPositiveButton("Close", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//close the dialog box if this button is clicked
					dialog.cancel();
				}	
			});

			//create alert dialog
			AlertDialog alertdialogEmptyField = alertdialogbuilder.create();

			//show alert dialog
			alertdialogEmptyField.show();
		} else if(!stNewPword.equals(stConfPword)) {
			AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
			//set title
			alertdialogbuilder.setTitle("Invalid input");

			//set dialog message
			alertdialogbuilder
			.setMessage("New Password and Confirmation Password do not match.  Please try again.")
			.setCancelable(false)
			.setPositiveButton("Close", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//close the dialog box if this button is clicked
					dialog.cancel();
				}	
			});

			//create alert dialog
			AlertDialog alertdialogEmptyField = alertdialogbuilder.create();

			//show alert dialog
			alertdialogEmptyField.show();
		} else if(stNewPword.length() > MAX_PASSWORD_LENGTH) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			//set title
			alertDialogBuilder.setTitle("Invalid Password");

			//set dialog message
			alertDialogBuilder
			.setMessage("Password must not exceed 20 characters.  Please try again.")
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
			String stUsername = sharedpreferencesLoginPrefs.getString("loggedin_username", null);
			new PwordChanger(this, new UserInfo(stUsername, CommonUtilities.hash(stOldPword), DEFAULT_ID), 
					this, CommonUtilities.hash(stNewPword)).execute();
		}

	}
	
	
	/**
	 * Method called when user wants to delete account
	 * 
	 * @param view
	 */
	public void deleteAccount(View view) {
		//confirmation pop up
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		//set title
		alertDialogBuilder.setTitle("Delete account");
		
		//set dialog message
		alertDialogBuilder
			.setMessage("Are you sure you want to delete your account?")
			.setCancelable(false);
		
		alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				String stUsername = sharedpreferencesLoginPrefs.getString("loggedin_username", null);
				String stPassword = sharedpreferencesLoginPrefs.getString("loggedin_password", null);
			    new UserRemover(AccountSettingsActivity.this, new UserInfo(stUsername, stPassword, DEFAULT_ID), AccountSettingsActivity.this).execute();
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
     * @return the editorLoginPrefs
     */
    public Editor getEditorLoginPrefs() {
        return editorLoginPrefs;
    }

    /**
     * @param editorLoginPrefs the editorLoginPrefs to set
     */
    public void setEditorLoginPrefs(Editor editorLoginPrefs) {
        this.editorLoginPrefs = editorLoginPrefs;
    }
}


