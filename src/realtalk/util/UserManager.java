package realtalk.util;

/**
 * UserManager is a singleton which keeps track of RealTalk's current user and provides
 * methods that allow logging out, logging in and managing the registration id of the user.
 * It is discouraged to call it directly and should be accessed through the ChatController.
 * 
 * @author Colin Kho
 *
 */
public class UserManager {
	private static UserManager userControllerSingleton = 
			null;
	
	protected UserManager() { }
	
	/**
	 * Gets a singleton instance of the UserManager
	 * 
	 * @return current instance of UserManager
	 */
	public static UserManager getInstance() {
		return userControllerSingleton;
	}
	
	public static void setInstance(UserManager usermanager) {
		userControllerSingleton = usermanager;
	}
	
	public boolean fLogin(String name, String password) {
		throw new UnsupportedOperationException();
	}
	
	public boolean fLogout() {
		throw new UnsupportedOperationException();
	}
	
	public boolean fChangeUser(String name, String password) {
		throw new UnsupportedOperationException();
	}
	
	public String stGetUserName() {
		throw new UnsupportedOperationException();
	}
	
	public String stGetPassword() {
		throw new UnsupportedOperationException();
	}
	
	public String stGetRegId() {
		throw new UnsupportedOperationException();
	}
}
