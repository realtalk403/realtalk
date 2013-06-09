/**
 * 
 */
package realtalk.util;

/**
 * UserInfo is an immutable class that contains meta information about the user.
 * 
 * @author Colin Kho
 *
 */
public class UserInfo {
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
}
