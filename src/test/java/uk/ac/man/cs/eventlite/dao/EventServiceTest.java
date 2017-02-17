package uk.ac.man.cs.eventlite.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.entities.Event;

public class EventServiceTest extends TestParent {

	@Autowired
	private EventService eventService;

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
		
		Event eventtest = new Event();
		eventtest.setId(3);
		eventtest.setName("testevent");
		eventtest.setVenue(1);
		eventtest.setDate(null);
		
		eventService.save(eventtest);
		
		List<Event> events = (List<Event>) eventService.findAll();

		assertEqual(count, (long) events.size());
	}

	private void assertEqual(long count, long size) {
		// TODO Auto-generated method stub
		
	}
}
