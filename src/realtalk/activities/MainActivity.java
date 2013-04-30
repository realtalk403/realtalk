package realtalk.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import realtalk.util.ChatManager;
import realtalk.util.JSONParser;
import realtalk.util.User;

import com.example.realtalk.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    
    private ProgressDialog pDialog;
    private ChatManager chatmanager;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		chatmanager = new ChatManager();
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
	    new UserAdder(new User("someID", stUsername, stPword), chatmanager).execute();
	}
	
	public void authenticateUser(View view) {
	    EditText edittextUser = (EditText) findViewById(R.id.editQuery);
	    EditText edittextPword = (EditText) findViewById(R.id.editPword);
	    String stUsername = edittextUser.getText().toString();
	    String stPword = edittextPword.getText().toString();
	    new Authenticator(new User("someID", stUsername, stPword), chatmanager).execute();
	}
	
	public void removeUser(View view) {
	    EditText edittextUser = (EditText) findViewById(R.id.editQuery);
	    EditText edittextPword = (EditText) findViewById(R.id.editPword);
	    String stUsername = edittextUser.getText().toString();
	    String stPword = edittextPword.getText().toString();
	    new UserRemover(new User("someID", stUsername, stPword), chatmanager).execute();
	}
	
	class UserAdder extends AsyncTask<String, String, Boolean> {
		private User user;
		private ChatManager chatmangager;
		public UserAdder(User user, ChatManager chatmanager) {
			this.user = user;
			this.chatmangager = chatmanager;
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
        protected Boolean doInBackground(String... params) {
        	return chatmanager.addUser(user);
        }
        
        @Override
        protected void onPostExecute(Boolean fAdded) {
            pDialog.dismiss();
            TextView addingResults = (TextView) findViewById(R.id.query_results_textView);
            String text;
            if (!fAdded) {
                text = "User Not Added";
                addingResults.setText(text);
            } else {
                text = "User added!";
                addingResults.setText(text);
            }
        }
	}

	class UserRemover extends AsyncTask<String, String, Boolean> {
		private User user;
		private ChatManager chatmangager;
		public UserRemover(User user, ChatManager chatmanager) {
			this.user = user;
			this.chatmangager = chatmanager;
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
        protected Boolean doInBackground(String... params) {
        	return chatmanager.removeUser(user);
        }
        
        @Override
        protected void onPostExecute(Boolean fRemoved) {
            pDialog.dismiss();
            TextView removalResults = (TextView) findViewById(R.id.query_results_textView);
            String text;
            if (!fRemoved) {
                text = "User Not Removed";
                removalResults.setText(text);
            } else {
                text = "User removed!";
                removalResults.setText(text);
            }
        }
	}
	
	class Authenticator extends AsyncTask<String, String, Boolean> {
		private User user;
		private ChatManager chatmangager;
		public Authenticator(User user, ChatManager chatmanager) {
			this.user = user;
			this.chatmangager = chatmanager;
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
        protected Boolean doInBackground(String... params) {
        	return chatmanager.authenticateUser(user);
        }
        
        @Override
        protected void onPostExecute(Boolean fAuthenticated) {
            pDialog.dismiss();
            TextView authenticationResults = (TextView) findViewById(R.id.query_results_textView);
            String text;
            if (!fAuthenticated) {
                text = "User Not Found";
                authenticationResults.setText(text);
            } else {
                text = "Authenticated!";
                authenticationResults.setText(text);
            }
        }
	}
}
