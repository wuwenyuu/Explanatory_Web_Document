package uk.ac.man.cs.eventlite.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Entity
@Table(name="events")
public class Event {

	@GeneratedValue
	@Id
	private long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIME)
	private Date time;

	private String name;
	private String ref;
	private String link;
	private String description;
	private String key;
	
	private boolean coordsSet;
	private double lat, lon;

	private String address;

	
	@ManyToOne
	@JoinColumn
	private Venue venue;

	public Event() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}
	
	public Date getTime() {
		return time;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
	
	public boolean hasPassed() {
		return date.compareTo(new Date()) < 0;
	}

	public String getName() {
		return name;
	}
	
	public String getKey() {
		return key;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
		
	}
	
	public String getRef() {
		return ref;
	}
	
	public void setRef(String ref) {
		this.ref = ref;
		
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