package uk.ac.man.cs.eventlite.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Event {

	private long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Date date;

	private String name;

	private long venue;

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

	public void setName(String name) {
		this.name = name;
	}

	public long getVenue() {
		return venue;
	}

	public void setVenue(long venue) {
		this.venue = venue;
	}
}
