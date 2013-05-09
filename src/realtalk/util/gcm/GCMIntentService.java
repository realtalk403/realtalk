package realtalk.util.gcm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * IntentService responsible for handling GCM messages
 * 
 * @author Colin Kho
 *
 */
public class GCMIntentService extends GCMBaseIntentService {
    
    public GCMIntentService() {
        super();
    }
    
    @Override
    protected void onError(Context context, String stRegId) {
        Log.v("GCMIntentService", "Registration Error");
        GCMUtilities.sendRegistrationResult(context, stRegId, GCMUtilities.ERROR);
        
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onRegistered(Context context, String stRegId) {
        Log.v("GCMIntentService", "Registration Successful");
        GCMUtilities.sendRegistrationResult(context, stRegId, GCMUtilities.SUCCESS);       
    }

    @Override
    protected void onUnregistered(Context context, String stRegId) {
        // TODO Auto-generated method stub
        
    }

}
