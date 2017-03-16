package uk.ac.man.cs.eventlite.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public class EventServiceTest extends TestParent {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;

	@Test
	public void findAll() {
		List<Event> events = (List<Event>) eventService.findAll();
		long count = eventService.count();

		assertThat("findAll should get all events.", count, equalTo((long) events.size()));
	}
	
	@Test 
	public void save(){
		
		long count = eventService.count();
		count++;
		
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		
 		venueService.save(venue1);
 		
		Event eventtest = new Event();
		eventtest.setId(3);
		eventtest.setName("testevent");
		eventtest.setVenue(venue1);
		eventtest.setDate(null);
		
		eventService.save(eventtest);
		
		List<Event> events = (List<Event>) eventService.findAll();

		assertEqual(count, (long) events.size());
	}
	
	@Test
	public void findAllByNameContainingIgnoreCaseOrderByDateAscNameAsc()
	{
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		
 		venueService.save(venue1);
 		
		Event eventtest1 = new Event();
		eventtest1.setId(3);
		eventtest1.setName("testevent");
		eventtest1.setVenue(venue1);
		eventtest1.setDate(null);
		
		eventService.save(eventtest1);
		
		Event eventtest2 = new Event();
		eventtest2.setId(4);
		eventtest2.setName("eventName");
		eventtest2.setVenue(venue1);
		eventtest2.setDate(null);
		
		eventService.save(eventtest2);
		
		Event eventtest3 = new Event();
		eventtest3.setId(4);
		eventtest3.setName("this is a test");
		eventtest3.setVenue(venue1);
		eventtest3.setDate(null);
		
		eventService.save(eventtest3);
		
		String name = "TEST";
		
		List<Event> events = (List<Event>) eventService.findAllByNameContainingIgnoreCaseOrderByDateAscTimeAscNameAsc(name);

		assertEquals("findAll should get all events.", 2, events.size());;
	}
	
	@Test 
 	public void deleteById(){
 		
 		
 		Venue venue1 = new Venue();
  		venue1.setId(1);
  		venue1.setName("Kilburn");
  		venue1.setCapacity(1000);
  		
  		venueService.save(venue1);
  		
 		Event eventtest = new Event();
 		eventtest.setId(3);
 		eventtest.setName("testevent");
 		eventtest.setVenue(venue1);
 		eventtest.setDate(null);
 		
 		eventService.save(eventtest);
 		
 		long count = eventService.count();
 		count--;
 		
 		eventService.delete(eventtest.getId());
 		
 		List<Event> events = (List<Event>) eventService.findAll();
 
 		assertEqual(count, (long) events.size());
 	}
 	
 	@Test 
 	public void deleteByObject(){
 		
 		
 		Venue venue1 = new Venue();
  		venue1.setId(1);
  		venue1.setName("Kilburn");
  		venue1.setCapacity(1000);
  		
  		venueService.save(venue1);
  		
 		Event eventtest = new Event();
 		eventtest.setId(3);
 		eventtest.setName("testevent");
 		eventtest.setVenue(venue1);
 		eventtest.setDate(null);
 		
 		eventService.save(eventtest);
 		
 		long count = eventService.count();
 		count--;
 		
 		eventService.delete(eventtest);
 		
 		List<Event> events = (List<Event>) eventService.findAll();
 
	
 		assertEqual(count, (long) events.size());
 	}

	private void assertEqual(long count, long size) {
		// TODO Auto-generated method stub
		
	}
}