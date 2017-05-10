package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import org.junit.Test;
import org.mockito.Mock;
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
		mvc.perform(get("/events").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
						.andExpect(content().contentType(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("$.title", equalTo("EventLite Events")))
						.andExpect(jsonPath("$._self", equalTo("http://localhost/events")))
						.andExpect(model().attributeExists("events"));
	}
	
	@Test
	public void testEventsList() throws Exception {
		List<Event> events = (List<Event>) eventService.findAll();
		for (int i = 0; i < events.size(); i++)
		{
			int eventId = (int) events.get(i).getId();
			String eventName = events.get(i).getName();
			Date eventDate = events.get(i).getDate();
			Venue venue = events.get(i).getVenue();
			mvc.perform(get("/events").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
					.andExpect(jsonPath("$.events[*].id", hasItem(eventId)))
					.andExpect(jsonPath("$.events[*].name", hasItem(eventName)))
					.andExpect(jsonPath("$.events[*].date", hasItem(eventDate.toString())))
					.andExpect(jsonPath("$.events[*].venue.id", hasItem((int) venue.getId())))
					.andExpect(jsonPath("$.events[*].venue.name", hasItem(venue.getName())))
					.andExpect(jsonPath("$.events[*].venue.capacity", hasItem(venue.getCapacity())))
					.andExpect(jsonPath("$.events[*].venue._self", hasItem("http://localhost/venues/" + (int) venue.getId())));
		}
		
	}
	
	@Test
	public void testGetAnEvent() throws Exception {
		List<Event> events = (List<Event>) eventService.findAll();
		for (int i = 0; i < events.size(); i++) {
			int eventId = (int) events.get(i).getId();
			System.out.println("----------- " + eventId);
			mvc.perform(get("/events/" + eventId).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.title", equalTo("EventLite Event " + eventId)))
		 	.andExpect(jsonPath("$._self", equalTo("http://localhost/events/" + eventId)))
		 	.andExpect(jsonPath("$.id", equalTo(eventId)))
		 	.andExpect(jsonPath("$.date", equalTo(events.get(i).getDate().toString())))
		 	.andExpect(jsonPath("$.name", equalTo(events.get(i).getName())))
		 	.andExpect(jsonPath("$.venue.id", equalTo((int) events.get(i).getVenue().getId())))
		 	.andExpect(jsonPath("$.venue.name", equalTo(events.get(i).getVenue().getName())))
		 	.andExpect(jsonPath("$.venue.capacity", equalTo(events.get(i).getVenue().getCapacity())))
		 	.andExpect(jsonPath("$.venue._self", equalTo("http://localhost/venues/" + events.get(i).getVenue().getId())));

		}
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
 	public void testDeleteEvent() throws Exception {
 		
		Event eventtest3 = new Event();
		eventtest3.setName("this is a test event");
		eventService.save(eventtest3);
 		
 		mvc.perform(MockMvcRequestBuilders.delete("/events/" + eventtest3.getId())).andExpect(status().isNoContent());
 	}
}