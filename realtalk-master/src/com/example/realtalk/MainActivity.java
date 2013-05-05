package com.example.realtalk;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    public static final String url_get_user_id = "http://chatrealtalk.herokuapp.com/db_get_users.php";
    public static final String TAG_USER = "user";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_NAME = "name";
    public static final String TAG_ID = "id";
    
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

	public void retrieveQuery(View view) {
	    EditText editText = (EditText) findViewById(R.id.editQuery);
	    String query = editText.getText().toString();
	    new GetUserDetails().execute(query);
	}
	
	class GetUserDetails extends AsyncTask<String, String, JSONObject> {
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
        protected JSONObject doInBackground(String... params) {
            int success = 0;
            try {
                // Make GET params in NameValuePair List
                List<NameValuePair> getParams = new ArrayList<NameValuePair>();
                getParams.add(new BasicNameValuePair("user", params[0]));
                
                // Make http request to obtain results
                JSONParser jsonParser = new JSONParser();
                JSONObject json = jsonParser.makeHttpRequest(url_get_user_id, "GET", getParams);
                
                success = json.getInt(TAG_SUCCESS);
                
                if (success == 1) {
                    JSONArray userObj = json.getJSONArray(TAG_USER);
                    
                    JSONObject user = userObj.getJSONObject(0);
                    
                    return user;                  
                    
                } else {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(JSONObject result) {
            pDialog.dismiss();
            TextView queryResults = (TextView) findViewById(R.id.query_results_textView);
            String text;
            if (result == null) {
                text = "User Not Found";
                queryResults.setText(text);
            } else {
                try {
                    text = "User Name: " + result.getString(TAG_NAME) + "\n" + "User ID  : " + result.getString(TAG_ID);
                    queryResults.setText(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
