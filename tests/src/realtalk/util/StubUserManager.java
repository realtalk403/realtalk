package realtalk.util;

public class StubUserManager extends UserManager {
	public StubUserManager() { }
	
	public boolean fLogin(String name, String password) {
		return true;
	}
	
	public boolean fLogout() {
		return true;
	}
	
	public boolean fChangeUser(String name, String password) {
		return true;
	}
	
	public String stGetUserName() {
		return "USERNAME";
	}
	
	public String stGetPassword() {
		return "PASSWORD";
	}
	
	public String stGetRegId() {
		return "ID";
	}
}
