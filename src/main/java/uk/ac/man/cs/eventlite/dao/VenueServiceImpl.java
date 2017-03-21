package uk.ac.man.cs.eventlite.dao;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Service
public class VenueServiceImpl implements VenueService {

	
	@Autowired
	private VenueRepository venueRepository;
	
	private final static Logger log = LoggerFactory.getLogger(VenueServiceImpl.class);

	private final static String DATA = "data/venues.json";

	@Override
	public long count() {
		return venueRepository.count();
	}
	
	@Override 
	public Venue findById(long id){
		return venueRepository.findById(id);
	}
	
	@Override
	public Iterable<Venue> findAll() {
		return venueRepository.findAll();
	}
	
	@Override
	public void save(Venue venue) {
		venueRepository.save(venue);
		//<S extends T> S save(S entity);
	}
	
	@Override
	public Venue findOneByName(String name) {
		return venueRepository.findOneByName(name);
		//<S extends T> S save(S entity);
	}
	
	@Override
	public Iterable<Venue> findAllByOrderByNameAsc() {
		return venueRepository.findAllByOrderByNameAsc();
	}
	
	public Iterable<Venue> findAllByNameContainingIgnoreCaseOrderByNameAsc(String name)
	{
		return venueRepository.findAllByNameContainingIgnoreCaseOrderByNameAsc(name);
	}
}
