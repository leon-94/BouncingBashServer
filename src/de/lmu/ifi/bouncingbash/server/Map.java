package de.lmu.ifi.bouncingbash.server;

import com.eclipsesource.json.JsonObject;

public class Map {
	
//	public static String defaultData = "{\"id\":\"mapfromobjects\",\"walls\":[{\"x\":710,\"y\":0,\"width\":500,\"height\":30},{\"x\":710,\"y\":1050,\"width\":500,\"height\":30},{\"x\":0,\"y\":300,\"width\":500,\"height\":30},{\"x\":1420,\"y\":300,\"width\":500,\"height\":30},{\"x\":0,\"y\":750,\"width\":500,\"height\":30},{\"x\":1420,\"y\":750,\"width\":500,\"height\":30}]}";
	public static String defaultData = "{\"id\":\"mapfromobjects\",\"walls\":[{\"x\":710,\"y\":0,\"width\":500,\"height\":30},{\"x\":710,\"y\":1050,\"width\":500,\"height\":30},{\"x\":0,\"y\":300,\"width\":500,\"height\":30},{\"x\":1420,\"y\":300,\"width\":500,\"height\":30},{\"x\":0,\"y\":750,\"width\":500,\"height\":30},{\"x\":1420,\"y\":750,\"width\":500,\"height\":30}],\"switches\":[{\"x\":895,\"y\":475}],\"spawnPoints\":[{\"x\":100,\"y\":476},{\"x\":1756,\"y\":476},{\"x\":960,\"y\":296}]}";

	public String id;
	public String creator;
	public String data;
	public double lat;
	public double lng;
	
	public Map(String id, String creator, String data, double lat, double lng) {
		this.id = id;
		this.creator = creator;
		this.data = data;
		this.lat = lat;
		this.lng = lng;
	}
	
	public JsonObject toJson() {
		JsonObject jsonMap = new JsonObject();
		jsonMap.add("id", id);
		jsonMap.add("creator", creator);
		jsonMap.add("lat", lat);
		jsonMap.add("lng", lng);
		jsonMap.add("data", data);
		return jsonMap;
	}
}
