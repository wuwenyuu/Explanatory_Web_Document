package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@AutoConfigureMockMvc
public class EventsControllerWebTest extends TestParent {
//    private EventsControllerWeb controller;
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private EventService eventService;
	
	@Mock
	private VenueService venueService;
	
	@Mock
	private Venue venue;
	
//	@Before
//	public void setup(){
//		MockitoAnnotations.initMocks(this);
//		
//		mockMvc = MockMvcBuilders.standaloneSetup(controller).setSingleView(venue).build();
//	}

	@Test
	public void testGetAllEvents() throws Exception {
		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index"));
	}
	
	@Test
	public void getNewEventHtml() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/new").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("events/new"));
	}
	
    @Test
	public void addEventHtml() throws Exception {
 
    	long countBefore = eventService.count();
		String name = "testevent";
		String date = "2020/01/01";
		String venue = "Stopford";
		String URL = "/events/new/";
		mvc.perform(MockMvcRequestBuilders.post(URL,name,date,venue).accept(MediaType.TEXT_HTML));
		long countAfter = eventService.count();
		assertEqual(countAfter, countBefore+1);
	}
    
    private void assertEqual(long count, long size) {
		// TODO Auto-generated method stub
		
	}

	
	@Test
	public void testUpdate() throws Exception{
		mvc.perform(get("/events/34/update").accept(MediaType.TEXT_HTML)).andExpect(status().isOk()).andExpect(view().name("events/update"));
		
	}
	
    @Test
	public void updateVenueHtml() throws Exception {
 
    	long countBefore = eventService.count();
		String name = "testevent";
		String venuename = "Richmond Road, KT2 5PL";
		Date date = null;
		String description = "testdescription";
		Date time = null;
		mvc.perform(MockMvcRequestBuilders.post(name, venuename, date, description, time).accept(MediaType.TEXT_HTML));
		long countAfter = eventService.count();
		assertEquals(countAfter, countBefore);
		mvc.perform(MockMvcRequestBuilders.get("/events/search?searchEvent=testevent").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("events/index"));
		
	}
	
	@Ignore
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
		eventtest1.setTime(null);
		
		eventService.save(eventtest1);
		
		Event eventtest2 = new Event();
		eventtest2.setId(4);
		eventtest2.setName("eventName");
		eventtest2.setVenue(venue1);
		eventtest2.setDate(null);
		eventtest2.setTime(null);
		
		eventService.save(eventtest2);
		
		Event eventtest3 = new Event();
		eventtest3.setId(4);
		eventtest3.setName("this is a test");
		eventtest3.setVenue(venue1);
		eventtest3.setDate(null);
		eventtest3.setTime(null);
		
		
		eventService.save(eventtest3);
		
		mvc.perform(MockMvcRequestBuilders.get("/events/search?searchEvent=TEST").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index"));
	}
 	
 	@Test
 	public void testDeleteEvent() throws Exception {
 		mvc.perform(MockMvcRequestBuilders.post("/events/1/delete").accept(MediaType.TEXT_HTML))
 				.andExpect(status().isFound()).andExpect(content().string(""))
 				.andExpect(view().name("redirect:/events"));
 	}
 	
	@Test
	public void testViewEventId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Id")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventDescription() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("This is third event")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Third Event")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventDate() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("2013-01-09")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventVenue() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Alan Gilbert")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewVenueName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/venue/5").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Stopford")))
		.andExpect(view().name("events/venues"));
	}
	
	@Test
	public void testViewVenueCapacity() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/venue/5").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("500")))
		.andExpect(view().name("events/venues"));
	}
	
	@Test
	public void testViewVenueAddress() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/venue/5").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("S5 9NP")))
		.andExpect(view().name("events/venues"));
	}
	
	@Test
	public void testUpcomingEvents() throws Exception {
		when(venue.getEvents()).thenReturn(Collections.<Event> emptyList());
		when(venueService.findById(5)).thenReturn(venue);
		mvc.perform(MockMvcRequestBuilders.get("/events/venue/5").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Gnother Event")))
		.andExpect(view().name("events/venues"));
	}
}