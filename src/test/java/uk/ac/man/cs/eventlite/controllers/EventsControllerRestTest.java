package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@AutoConfigureMockMvc
public class EventsControllerRestTest extends TestParent {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;

	@Test
	public void testGetAllEvents() throws Exception {
		mvc.perform(get("/events").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void testSearchAnEvent() throws Exception {
		
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
		
		mvc.perform(MockMvcRequestBuilders.get("/events/search?searchEvent=TEST").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
    public void getEventPageTest() throws Exception{
	Venue location = new Venue();
	
	location.setName("Rock'a'Fella");
	location.setAddress("21 Jump Street");
	location.setCapacity(1500);
	location.setId(911);
	
	Event event = new Event();
	event.setName("The After Party");
	event.setVenue(location);
	event.setDate(event.getDate());
	event.setTime(event.getTime());
	
	
	mvc.perform(get("/events/" + location.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	.andExpect(jsonPath("$.title", equalTo("The After Party")))
	.andExpect(jsonPath("$._self", equalTo("http://localhost/events/" + event.getId())))
	.andExpect(jsonPath("$.id", equalTo((int) location.getId())))
	.andExpect(jsonPath("$.date", equalTo(event.getDate().toString())))
	.andExpect(jsonPath("$.name", equalTo("The After Party")))
	.andExpect(jsonPath("venue.id", equalTo("Rock'a'Fella")))
	.andExpect(jsonPath("venue.name", equalTo(1500)))
	.andExpect(jsonPath("$.venue._self", equalTo("http://localhost/venues/" + location.getId())));
}
	
 	@Test
 	public void testDeleteEvent() throws Exception {
 		mvc.perform(MockMvcRequestBuilders.delete("/events/1")).andExpect(status().isNoContent());
 	}
}