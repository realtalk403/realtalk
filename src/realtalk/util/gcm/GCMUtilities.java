package realtalk.util.gcm;

import android.content.Context;
import android.content.Intent;

/**
 * GCM Utilities is a helper class providing methods and constants common to GCM.
 * 
 * @author Colin Kho
 *
 */
public final class GCMUtilities {
    /**
     *  Google API Project ID registered to use GCM
     */
    public static final String SENDER_ID = "1026351756982";
    
    /**
     * Intent used to indicate result of registering on GCM.
     */
    public static final String GCM_REGISTER_RESULT_ACTION = "realtalk.activities.GCM_REGISTER_RESULT";
    
    /**
     * Intent extra used to identify result of gcm register 
     */
    public static final String RESULT_MESSAGE = "result_message";
    
    /**
     * Intent extra value to denote success.
     */
    public static final String SUCCESS = "success";
    
    /**
     * Intent extra value to denote failure.
     */
    public static final String ERROR = "error";
    
    /**
     * Intent extra used to store registration id.
     */
    public static final String GCM_REG_ID = "gcm_reg_id";
    
    /**
     * Method that sends a broad cast notifying the result of the registration with GCM
     * 
     * @param context   context it is used in
     * @param stRegId   Registration Id obtained from GCM Server
     * @param stRegCode Result Code
     */
    public static void sendRegistrationResult(Context context, String stRegId, String stRegCode) {
        Intent intent = new Intent(GCM_REGISTER_RESULT_ACTION);
        if (stRegCode.equals(SUCCESS)) {
            intent.putExtra(GCM_REG_ID, stRegId);
            intent.putExtra(RESULT_MESSAGE, SUCCESS);
        } else {
            intent.putExtra(RESULT_MESSAGE, ERROR);
        }
        context.sendBroadcast(intent);
    }
    
	/**
	 * Private contructor prevents this class from being instantiated.
	 */
	private GCMUtilities() {
		throw new UnsupportedOperationException("CommonUtilities is a utility class that should not be instantiated.");
	}
}
