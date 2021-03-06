package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public interface VenueService {

	public Iterable<Venue> findAll();
	
	public long count();
	
	public void save(Venue venue);
	
	public boolean delete(long id);

	public Venue findById(long id);
	
	public Venue findOneByName(String name);

	public Iterable<Venue> findAllByOrderByNameAsc();
	
	public Iterable<Venue> findAllByNameContainingIgnoreCaseOrderByNameAsc(String name);

}
