package de.lmu.ifi.bouncingbash.server;

import java.util.Collection;
import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

@Path("/service")
public class Service {

	@GET
	@Path("/ping")
	@Produces(MediaType.TEXT_PLAIN)
	public String ping() {
		return "ping";
	}
	
	@POST
	@Path("/testauth")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String testAuthentication(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "testauth");	
		JsonObject message = (JsonObject) Json.parse(raw);
		
		Object credentialsObj = message.get("credentials");
		if(notNull(credentialsObj)) {
			JsonObject credentials = (JsonObject) credentialsObj;
			response.add("credentials",  credentials);
		}
		
		if(dm.authenticate(message)) {
			response.add("success", true);			
		}
		else {
			response.add("success", false);
			response.add("cause", "INVALID_CREDENTIALS");
		}

		return response.toString();
	}
	
	@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String signUp(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "signup");
		JsonObject message = (JsonObject) Json.parse(raw);
		
		String userId = message.getString("userId",  null);
		String password = message.getString("password",  null);
		if(notNull(userId, password)) {
			if(dm.createNewUser(userId, password, null)) {
				response.add("success",  true);
				response.add("userId",  userId);
				response.add("password",  password);
			}
			else {
				response.add("success",  false);
				response.add("cause",  "ID_NOT_UNIQUE");
			}
		}
		else {
			response.add("success",  false);
			response.add("cause",  "INVALID_ARGUMENTS");		
		}
		
		return response.toString();
	}
	
	@POST
	@Path("/postlocation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String postLocation(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "postlocation");
		JsonObject message = (JsonObject) Json.parse(raw);	

		if(dm.authenticate(message)) {
			String userId = ((JsonObject)message.get("credentials")).getString("userId", null);
			Double lat = message.getDouble("lat",  1000);
			Double lng = message.getDouble("lng",  1000);
			String mac = message.getString("mac",  null);
			if(notNull(lat, lng, mac) && lat != 1000 && lng != 1000) {
				if(dm.locationUpdate(userId, lat, lng, mac)) {
					response.add("success", true);
				}
				else {
					response.add("success", false);
					response.add("message", "Location update failed.");
				}
			}
			else {
				response.add("success", false);
				response.add("message", "One or more invalid arguments.");
			}
		}
		else {
			response.add("success", false);
			response.add("message", "Authentication failed.");
		}

		return response.toString();
	}
	
	/*@PUT
	@Path("/locationdata")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getLocationData(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "locationdata");
		JsonObject message = (JsonObject) Json.parse(raw);	

		if(dm.authenticate(message)) {

			response.add("success", true);
			
		}
		else {
			response.add("success", false);
			response.add("message", "Authentication failed.");
		}

		return response.toString();
	}*/
	

	@POST
	@Path("/postsession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String postSession(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "postsession");
		JsonObject message = (JsonObject) Json.parse(raw);
		
		if(!dm.authenticate(message)) {
			response.add("success", false);
			response.add("message", "Authentication failed.");
			return response.toString();
		}
		String userId = ((JsonObject)message.get("credentials")).getString("userId", null);
		String mac = message.getString("mac",  null);
		Double lat = message.getDouble("lat",  1000);
		Double lng = message.getDouble("lng",  1000);

		if(dm.newSession(userId, mac, lat, lng)) {
			response.add("success", true);
		}
		else {
			response.add("success", false);
			response.add("message", "Posting the session failed.");
		}
		
		return response.toString();
	}


	@POST
	@Path("/getsessions")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getSessions(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "getsessions");
		JsonObject message = (JsonObject) Json.parse(raw);
		
		if(!dm.authenticate(message)) {
			response.add("success", false);
			response.add("message", "Authentication failed.");
			return response.toString();
		}
		
		JsonArray jsonSessions = new JsonArray();
		
		Collection<Session> sessions = dm.getSessions();
		for(Session s : sessions) {
			jsonSessions.add(s.toJson());
		}
		response.add("success",  true);
		response.add("sessions", jsonSessions);
		
		return response.toString();
	}

	@POST
	@Path("/joinsession")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String joinSession(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "joinsession");
		JsonObject message = (JsonObject) Json.parse(raw);
		
		if(!dm.authenticate(message)) {
			response.add("success", false);
			response.add("message", "Authentication failed.");
			return response.toString();
		}
		
		String userId = ((JsonObject)message.get("credentials")).getString("userId", null);
		String mac = message.getString("mac",  null);
		String hostId = message.getString("hostId",  null);
		
		Session session = dm.joinSession(hostId, userId, mac);
		if(session == null) {
			response.add("success", false);
			response.add("message", "Joining the session failed.");
			return response.toString();
		}
		
//		Map map = dm.getMap(session.getLat(), session.getLng());
//		if(map == null) {
//			response.add("success", false);
//			response.add("message", "No map available.");
//			return response.toString();
//		}
//		response.add("map", map.data);

		response.add("success", true);
		response.add("session", session.toJson());
		return response.toString();
		
	}

	@POST
	@Path("/opensessionpolling")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String openSessionPolling(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "opensessionpolling");
		JsonObject message = (JsonObject) Json.parse(raw);
		
		if(!dm.authenticate(message)) {
			response.add("success", false);
			response.add("message", "Authentication failed.");
			return response.toString();
		}
		
		String userId = ((JsonObject)message.get("credentials")).getString("userId", null);
		Session session = null;
		
		int i = 0;
		while(i < 10) {

			session = dm.checkSession(userId);
			if(session == null) {
				try { Thread.sleep(1000); } catch(Exception e) { e.printStackTrace(); }
			}
			else {
				break;
			}			
			i++;
		}
		if(session != null) {
			response.add("open", false);
			response.add("session",  session.toJson());
		}
		else response.add("open", true);
		
		response.add("success", true);
		return response.toString();
		
	}


	
	private boolean notNull(Object... values) {
		for(int i = 0; i < values.length; i++) {
			if(values[i] == null) return false;
		}
		return true;
	}
}
