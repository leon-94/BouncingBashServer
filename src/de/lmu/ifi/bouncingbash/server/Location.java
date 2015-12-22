package de.lmu.ifi.bouncingbash.server;

import java.util.Date;

public class Location {

	private Date timestamp;
	private String mac;
	private double latitude;
	private double longitude;
	
	public Location(double lat, double lng, String mac) {
		latitude = lat;
		longitude = lng;
		this.mac = mac;
		timestamp = new Date();
	}
}
