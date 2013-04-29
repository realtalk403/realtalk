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

	public void retrieveQuery(View view) {
	    EditText edittextUser = (EditText) findViewById(R.id.editQuery);
	    String stUsername = edittextUser.getText().toString();
	    new UserAdder(new User("someID", stUsername, "somePword"), chatmanager).execute();
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
        protected void onPostExecute(Boolean fAuthenticated) {
            pDialog.dismiss();
            TextView authenticationResults = (TextView) findViewById(R.id.query_results_textView);
            String text;
            if (!fAuthenticated) {
                text = "User Not Added";
                authenticationResults.setText(text);
            } else {
                text = "User added!";
                authenticationResults.setText(text);
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
        	return chatmanager.authenticate(user);
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
