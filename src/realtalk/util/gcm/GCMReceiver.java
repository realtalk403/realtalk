/**
 * 
 */
package realtalk.util.gcm;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/**
 * @author Colin Kho
 *
 */
public class GCMReceiver extends GCMBroadcastReceiver {
    @Override
    protected String getGCMIntentServiceClassName(Context context) {
        return "realtalk.util.gcm.GCMIntentService";
    }
}
