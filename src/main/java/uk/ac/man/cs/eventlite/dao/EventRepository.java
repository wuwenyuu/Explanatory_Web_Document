package uk.ac.man.cs.eventlite.dao;

import org.springframework.data.repository.CrudRepository;

import uk.ac.man.cs.eventlite.entities.Event;

public interface EventRepository extends CrudRepository<Event, Long> {
	
	public Event findByName(String name);
	public Iterable<Event> findAllByOrderByDateAsc();
	public Iterable<Event> findAllByOrderByDateAscNameAsc();
	public Iterable<Event> findAllByOrderByDateAscTimeAscNameAsc();
	public Iterable<Event> findAllByNameContainingIgnoreCaseOrderByDateAscTimeAscNameAsc(String name);
	public Event findById(long id);

}