package uk.ac.man.cs.eventlite.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Entity
@Table(name = "venues")
public class Venue {

	@Id
	private long id;

	private String name;
	
	private String address;

	private int capacity;
	
	@JsonIgnore
	@OneToMany(mappedBy = "venue")
	private List<Event> events;
	  
	public Venue() { 
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
	
	public String getAddress() {
		return address;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}
