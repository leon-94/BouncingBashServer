package de.lmu.ifi.bouncingbash.server;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

@Path("/service")
public class Service {

	@GET
	@Path("/ping")
	@Produces(MediaType.TEXT_PLAIN)
	public String ping() {
		return "ping";
	}

	/*@POST
	@Path("/signup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String signup(String raw) {
		
		DataManager dm = DataManager.getDataManager();		
		JsonObject response = new JsonObject();
		response.add("type", "signup");		
		JsonObject message = (JsonObject) Json.parse(raw);
		
		String userId = message.getString("userId", null);
		String password = message.getString("password",  null);
		String mac = message.getString("mac",  null);
		
		boolean success = dm.createNewUser(userId, password, mac);

		response.add("success",  success);
		
		return response.toString();
	}*/
	
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
					response.add("success", "true");
				}
				else {
					response.add("success", "false");
					response.add("message", "Location update failed.");
				}
			}
			else {
				response.add("success", "false");
				response.add("message", "One or more invalid arguments.");
			}
		}
		else {
			response.add("success", false);
			response.add("message", "Authentication failed.");
		}

		return response.toString();
	}
	
	@PUT
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
	}
	
	private boolean notNull(Object... values) {
		for(int i = 0; i < values.length; i++) {
			if(values[i] == null) return false;
		}
		return true;
	}
}
