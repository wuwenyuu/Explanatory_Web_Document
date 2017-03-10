package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Event;

public interface EventService {

	public long count();

	public Iterable<Event> findAll();
	
	public void save(Event event);
	
	public void delete(Event event);
	
	public void delete(long id);
	
	public Iterable<Event> findAllByNameContainingIgnoreCaseOrderByDateAscNameAsc(String name);
	
	public Iterable<Event> findAllByOrderByDateAsc();
}
