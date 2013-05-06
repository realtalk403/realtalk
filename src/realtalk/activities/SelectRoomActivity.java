package realtalk.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.realtalk.R;

/**
 * Activity for selecting a chat room to join
 * 
 * @author Jordan Hazari
 *
 */
public class SelectRoomActivity extends Activity {

	/**
	 * Sets up the activity, and diplays a list of available rooms
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_room);
		
		ListView listview = (ListView) findViewById(R.id.list);
		listview.setClickable(false);
		
		// when a room is clicked, starts a new ChatRoomActivity
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		Intent itStartChat = new Intent(SelectRoomActivity.this, ChatRoomActivity.class);
        		Bundle bundleExtras = getIntent().getExtras();
        		itStartChat.putExtras(bundleExtras);
        		SelectRoomActivity.this.startActivity(itStartChat);
            }
        });
		
		String[] rgstRoom = {"Room 001"};
		// Binding resources Array to ListAdapter
        listview.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, rgstRoom));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_room, menu);
		return true;
	}

}
