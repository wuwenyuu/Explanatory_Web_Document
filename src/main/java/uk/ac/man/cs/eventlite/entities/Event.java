package uk.ac.man.cs.eventlite.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="events")
public class Event {

	@GeneratedValue
	@Id
	private long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@Temporal(TemporalType.DATE)
	private Date date;

	private String name;
	

	@ManyToOne
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

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}
	
	public boolean findByKeyWord(String key)
	{
		String eventName = name;
		boolean closeEnough = false;

        if (eventName.toLowerCase().contains(key.toLowerCase()))
        {
        	closeEnough = true;
        }
		return closeEnough;
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public Venue getVenue() {
		return venue;
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
}
