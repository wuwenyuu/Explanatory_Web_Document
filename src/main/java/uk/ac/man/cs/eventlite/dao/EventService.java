package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Event;

public interface EventService {

	public long count();

	public Iterable<Event> findAll();
	
	public void save(Event event);
	
	public Iterable<Event> findAllByOrderByDateAsc();
	
	public Event findByName(String name);

	public Event findOne(long id);
}
