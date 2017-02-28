package uk.ac.man.cs.eventlite.dao;

import static org.hamcrest.Matchers.equalTo;
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

	private void assertEqual(long count, long size) {
		// TODO Auto-generated method stub
		
	}
}
