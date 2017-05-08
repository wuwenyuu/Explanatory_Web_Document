package uk.ac.man.cs.eventlite.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues")
public class Venue {
	
	@Id
	@GeneratedValue
	private long id;

	private String name;
	
	private boolean coordsSet;
	private double lat, lon;

	private String address;

	private int capacity;
	
	@JsonIgnore
	@OneToMany(mappedBy = "venue")
	private List<Event> events;
	  
	public Venue() { 
		coordsSet = false;
		events = new ArrayList<Event>();
	}
	
	@JsonIgnore
	public List<Event> getEvents() {
	    return events;
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
		findCoords();
	}
	

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setLatLon(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		if (!coordsSet) findCoords();
		return lat;
	}

	public double getLon() {
		if (!coordsSet) findCoords();
		return lon;
	}

    public static final double BOGUS_LAT = 53.4807593;
    public static final double BOGUS_LON = -2.2426305;
	public boolean findCoords()  {
		if (address == null)
			return false;
		try {
			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAZrPWp7kC6ARg5hqFw1ROfyZ1n-z4Ig3o");
			GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
			if (results.length == 0) {
				lat = BOGUS_LAT;
				lon = BOGUS_LON;
				coordsSet = true;
				return true;
			}
			lat = results[0].geometry.location.lat;
			lon = results[0].geometry.location.lng;
			coordsSet = true;
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean hasCoords() {
		return coordsSet;
	}
}
