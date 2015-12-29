package de.lmu.ifi.bouncingbash.server;

import java.util.Date;

public class Location {

	private Date timestamp;
	private double latitude;
	private double longitude;
	private String mac;

	public Location(double lat, double lng, String mac) {
		latitude = lat;
		longitude = lng;
		this.mac = mac;
		timestamp = new Date();
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	public String getMac() {
		return mac;
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
