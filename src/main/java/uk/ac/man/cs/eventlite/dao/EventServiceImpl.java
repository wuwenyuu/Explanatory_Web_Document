package uk.ac.man.cs.eventlite.dao;

import java.util.Iterator;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.man.cs.eventlite.entities.Event;

@Service
public class EventServiceImpl implements EventService {

	
	@Autowired
	private EventRepository eventRepository;
	
	
	private final static Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

	private final static String DATA = "data/events.json";

	@Override
	public long count() {
		return eventRepository.count();
	}

	@Override
	public Iterable<Event> findAll() {
		return eventRepository.findAll();
	}
	
	@Override
	public Iterable<Event> findAllByOrderByDateAsc() {
		return eventRepository.findAllByOrderByDateAsc();
	}
	
	@Override
	public void save(Event event) {
		eventRepository.save(event);
		//<S extends T> S save(S entity);
	}
	
	@Override
	public void delete(Event event) {
		eventRepository.delete(event);
	}
	
	@Override
	public void delete(long id) {
		eventRepository.delete(id);
	}
}
