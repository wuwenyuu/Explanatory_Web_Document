package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public interface VenueService {

	public Iterable<Venue> findAll();
	
	public long count();
	
	public void save(Venue venue);

	public Venue findById(long id);
}
