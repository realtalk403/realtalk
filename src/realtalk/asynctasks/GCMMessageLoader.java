package realtalk.asynctasks;

import realtalk.activities.ChatRoomActivity;
import realtalk.util.ChatRoomInfo;
import android.app.Activity;
import android.os.AsyncTask;

/**
 * Loads messages from chat controller which is prepared for GCM.
 * 
 * @author Colin Kho
 *
 */
public class GCMMessageLoader extends AsyncTask<String, String, Boolean> {
    /**
	 * 
	 */
	private ChatRoomActivity chatRoomActivity;
	private Activity activity;
    private ChatRoomInfo chatroominfo;
    
    public GCMMessageLoader(ChatRoomActivity chatRoomActivity, Activity activity, ChatRoomInfo chatroominfo) {
        this.chatRoomActivity = chatRoomActivity;
		this.activity = activity;
        this.chatroominfo = chatroominfo;
    }
    @Override
    protected Boolean doInBackground(String... params) {
        this.chatRoomActivity.rgmi = this.chatRoomActivity.chatController.getMessagesFromChatRoom(chatroominfo.stId());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GCMMessageLoader.this.chatRoomActivity.adapter.clear();
                
                for (int i = 0; i < GCMMessageLoader.this.chatRoomActivity.rgmi.size(); i++) {
                    GCMMessageLoader.this.chatRoomActivity.adapter.add(GCMMessageLoader.this.chatRoomActivity.rgmi.get(i));
                }
            }
        });
        return true;
    }	    
}