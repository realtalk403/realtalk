package realtalk.util;

public class User {
	private String id;
	private String username;
	private String password;
	public User(String username, String password) {
		this.id = "someID";
		this.username = username;
		this.password = password;
	}
	public String getId() { return id; }
	public String getUsername() { return username; }
	public String getPassword() { return password; }
}
