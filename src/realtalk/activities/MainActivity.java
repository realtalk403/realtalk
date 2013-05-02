package realtalk.activities;

import realtalk.util.ChatManager;
import realtalk.util.RequestResultSet;
import realtalk.util.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.realtalk.R;

public class MainActivity extends Activity {
    
    private ProgressDialog pDialog;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void addUser(View view) {
	    EditText edittextUser = (EditText) findViewById(R.id.editQuery);
	    EditText edittextPword = (EditText) findViewById(R.id.editPword);
	    String stUsername = edittextUser.getText().toString();
	    String stPword = edittextPword.getText().toString();
	    new UserAdder(new User(stUsername, stPword)).execute();
	}
	
	public void authenticateUser(View view) {
	    EditText edittextUser = (EditText) findViewById(R.id.editQuery);
	    EditText edittextPword = (EditText) findViewById(R.id.editPword);
	    String stUsername = edittextUser.getText().toString();
	    String stPword = edittextPword.getText().toString();
	    new Authenticator(new User(stUsername, stPword)).execute();
	}
	
	public void removeUser(View view) {
	    EditText edittextUser = (EditText) findViewById(R.id.editQuery);
	    EditText edittextPword = (EditText) findViewById(R.id.editPword);
	    String stUsername = edittextUser.getText().toString();
	    String stPword = edittextPword.getText().toString();
	    new UserRemover(new User(stUsername, stPword)).execute();
	}
	
	class UserAdder extends AsyncTask<String, String, RequestResultSet> {
		private User user;
		public UserAdder(User user) {
			this.user = user;
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
        	return ChatManager.addUser(user);
        }
        
        @Override
        protected void onPostExecute(RequestResultSet requestresultset) {
            pDialog.dismiss();
            TextView addingResults = (TextView) findViewById(R.id.query_results_textView);
            addingResults.setText(requestresultset.stMessage);
        }
	}

	class UserRemover extends AsyncTask<String, String, RequestResultSet> {
		private User user;
		public UserRemover(User user) {
			this.user = user;
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
            TextView removalResults = (TextView) findViewById(R.id.query_results_textView);
            removalResults.setText(requestresultset.stMessage);
        }
	}
	
	class Authenticator extends AsyncTask<String, String, RequestResultSet> {
		private User user;
		public Authenticator(User user) {
			this.user = user;
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
            TextView authenticationResults = (TextView) findViewById(R.id.query_results_textView);
            authenticationResults.setText(requestresultset.stMessage);
        }
	}
}
