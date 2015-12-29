package de.lmu.ifi.bouncingbash.server;

import com.eclipsesource.json.JsonObject;

public class User {

	private String userId;
	private String password;
	private Location location;
	
	public User(String userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
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
	
	/*public JsonObject getInfoAsJson() {
		if(location == null) return null;
		
		if(System.currentTimeMillis() - location.getTimestamp().getTime() > 300000) {
			location = null;
			return null;
		}
		
		JsonObject info = new JsonObject();
		info.add("userId",  userId);
		info.add("mac", mac);
		info.add("lat", location.getLatitude());
		info.add("lng", location.getLongitude());
		return info;
	}*/
	
}
