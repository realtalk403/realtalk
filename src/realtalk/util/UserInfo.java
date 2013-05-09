/**
 * 
 */
package realtalk.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * UserInfo is an immutable class that contains meta information about the user.
 * 
 * @author Colin Kho
 *
 */
public class UserInfo implements Parcelable{
    private String stName;
    private String stPassword;
    private String stRegistrationId;
    
    /**
     * @param stName           User's Name
     * @param stPassword       User's Password
     * @param stRegistrationId User's Google Cloud Messaging Registration ID
     */
    public UserInfo(String stName, String stPassword, String stRegistrationId) {
        this.stName = stName;
        this.stPassword = stPassword;
        this.stRegistrationId = stRegistrationId;
    }

    /**
     * @return the userName
     */
    public String stUserName() {
        return stName;
    }

    /**
     * @return the password
     */
    public String stPassword() {
        return stPassword;
    }

    /**
     * @return the registrationId
     */
    public String stRegistrationId() {
        return stRegistrationId;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(stName);
        out.writeString(stPassword);
        out.writeString(stRegistrationId);
    }
    
    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
		public UserInfo createFromParcel(Parcel in) {
		    return new UserInfo(in);
		}
		
		public UserInfo[] newArray(int size) {
		    return new UserInfo[size];
		}
    };
    
    private UserInfo(Parcel in) {
        stName = in.readString();
        stPassword = in.readString();
        stRegistrationId = in.readString();
    }
}
