package de.lmu.ifi.bouncingbash.server;

import java.util.Collection;
import java.util.HashMap;

import com.eclipsesource.json.JsonObject;

public class DataManager {
	
	private static DataManager dm;
	
	private HashMap<String, User> users;
	private HashMap<String, Session> sessions;
	
	public static synchronized DataManager getDataManager() {
		if(dm == null) dm = new DataManager();
		return dm;
	}
	
	private DataManager() {
		users = new HashMap<String, User>();
		sessions = new HashMap<String, Session>();
		sessions.put("crazyMotherfucker", new Session("crazyMotherfucker", "a0:45:f2:7b:10:34", 48.5, 13.7));
	}

	public synchronized boolean createNewUser(String userId, String password, String mac) {
		
		if(users.get(userId) != null) return false;
		
		User user = new User(userId, password);
		users.put(userId, user);
		
		return true;
	}

	public synchronized boolean authenticate(JsonObject jsonObj) {
		JsonObject credentials = (JsonObject) jsonObj.get("credentials");
		if(credentials == null) return false;
		String userId = credentials.getString("userId", "");
		String password = credentials.getString("password", "");
		return authenticate(userId, password);
	}
	
	public synchronized boolean authenticate(String userId, String password) {
		
		User user = users.get(userId);
		if(user == null) return false;
		if(!user.getPassword().equals(password)) return false;
		return true;
	}
	
	public synchronized boolean locationUpdate(String userId, double lat, double lng, String mac) {
		User user = users.get(userId);
		if(user == null) return false;
		final Location location = new Location(lat, lng, mac);
		user.setLocation(location);
		return true;
	}
	
	public synchronized boolean newSession(String userId, String mac, double lat, double lng) {
		
		if(sessions.get(userId) != null) return false;
		sessions.put(userId, new Session(userId, mac, lat, lng));
		return true;
	}
	
	public synchronized Collection<Session> getSessions() {
		return sessions.values();
	}
	
	public synchronized Session joinSession(String hostId, String otherId, String otherMac) {

		Session session = sessions.get(hostId);
		if(session == null) return null;
		if(session.getOtherId() != null) return null;
		
		session.setOtherId(otherId);
		session.setOtherMac(otherMac);
		
		return session;
	}

}
