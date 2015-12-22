package de.lmu.ifi.bouncingbash.server;

public class User {

	private String userId;
	private String password;
	private String mac;
	private Location location;
	
	public User(String userId, String password, String mac) {
		super();
		this.userId = userId;
		this.password = password;
		this.mac = mac;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location loc) {
		location = loc;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	
}
