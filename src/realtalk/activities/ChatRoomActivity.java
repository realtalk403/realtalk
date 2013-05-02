package realtalk.activities;

import java.util.Date;

import realtalk.util.Message;
import realtalk.util.RequestParameters;
import realtalk.util.User;

import com.example.realtalk.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChatRoomActivity extends Activity {
	User user = new User(RequestParameters.PARAMETER_USER, RequestParameters.PARAMETER_PWORD);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_room, menu);
		return true;
	}
	
	public void createMessage(View view) {
		EditText text = (EditText)findViewById(R.id.message);
		String value = text.getText().toString();
		
		Message message = new Message(value, user, new Date());
		
        String item = message.toString();
        			  
        
        Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
	}

}
