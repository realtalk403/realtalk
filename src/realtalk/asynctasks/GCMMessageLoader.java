package realtalk.asynctasks;

import java.util.ArrayList;
import java.util.List;

import realtalk.activities.ChatRoomActivity;
import realtalk.controller.IChatController;
import realtalk.util.ChatRoomInfo;
import realtalk.util.MessageInfo;
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
    private IChatController chatController;
    private List<MessageInfo> rgmi;
    
    public GCMMessageLoader(ChatRoomActivity chatRoomActivity, Activity activity, 
            ChatRoomInfo chatroominfo, IChatController chatController) {
        this.chatRoomActivity = chatRoomActivity;
		this.activity = activity;
        this.chatroominfo = chatroominfo;
        this.chatController = chatController;
        this.rgmi = new ArrayList<MessageInfo>();
    }
    @Override
    protected Boolean doInBackground(String... params) {
        rgmi = chatController.getMessagesFromChatRoom(chatroominfo.stId());
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GCMMessageLoader.this.chatRoomActivity.getAdapter().clear();
                
                for (int i = 0; i < GCMMessageLoader.this.getRgmi().size(); i++) {
                    GCMMessageLoader.this.chatRoomActivity.getAdapter().add(GCMMessageLoader.this.getRgmi().get(i));
                }
            }
        });
        return true;
    }
    
    /**
     * @return the rgmi
     */
    public List<MessageInfo> getRgmi() {
        return rgmi;
    }
}
