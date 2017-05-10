package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.Mockito.*;

import java.sql.Time;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
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
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.dao.EventRepository;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueRepository;
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
	
	@Autowired
	private VenueService realVenueService;
	
	@Mock
	private VenueService venueService;
	
	@Mock
	private EventService mockEventService;
	
	@Mock
	private Venue venue;
	
	@Mock
	private Event event;
	
	@Before
	public void setup() {
//		MockitoAnnotations.initMocks(this);		
//		mvc = MockMvcBuilders.standaloneSetup(eventController).build();
	}

	@Test
	public void testGetAllEventsNoTwitterConn() throws Exception {
		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index"));
	}
	
	@Test
	public void testTweetingNoConn() throws Exception {
		mvc.perform(get("/events/tweet/1/aTweet").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testGetNewEventHtml() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/new").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("events/new"));
	}

	
	@Test
	public void testUpdate() throws Exception{
		mvc.perform(get("/events/34/update").accept(MediaType.TEXT_HTML)).andExpect(status().isOk()).andExpect(view().name("events/update"));
		
	}
	
	@Test
	public void testSearchAnEvent() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2018, Calendar.JANUARY, 10); //Year, month and day of month
		Date date1 = cal.getTime();
		
		cal = Calendar.getInstance();
		cal.set(2010, Calendar.JANUARY, 10); //Year, month and day of month
		Date date2 = cal.getTime();
		
		Date time1 = Time.valueOf("13:00:00");
		Date time2 = Time.valueOf("00:00:00");
		
		Venue venue1 = new Venue();
 		//venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		venue1.setAddress("Oxford Road");
 		
 		realVenueService.save(venue1);
 		
		Event eventtest1 = new Event();
		//eventtest1.setId(3);
		eventtest1.setName("teste");
		eventtest1.setVenue(venue1);
		eventtest1.setDate(date1);
		eventtest1.setTime(time1);
		
		eventService.save(eventtest1);
		
		Event eventtest2 = new Event();
		//eventtest2.setId(2);
		eventtest2.setName("testevent");
		eventtest2.setVenue(venue1);
		eventtest2.setDate(date2);
		eventtest2.setTime(time2);
		
		eventService.save(eventtest2);
		
		mvc.perform(MockMvcRequestBuilders.get("/events/search?searchEvent=TEST").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index"));
	}
	
	@Ignore
	@Test
	public void testViewEventDate() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		cal.set(2018, Calendar.JANUARY, 10); //Year, month and day of month
		
		Date time1 = Time.valueOf("13:00:00");
		Date date1 = cal.getTime();
		
		Event eventtest3 = new Event();
		eventtest3.setName("this is a test event");
		eventtest3.setDate(date1);
		eventtest3.setTime(time1);
		
		eventService.save(eventtest3);
		
		mvc.perform(MockMvcRequestBuilders.get("/events/" + eventtest3.getId()).accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("2018-01-10")))
		.andExpect(view().name("events/detail"));
	}
 	
 	@Test
 	public void testDeleteEvent() throws Exception {
 		mvc.perform(MockMvcRequestBuilders.post("/events/5/delete").accept(MediaType.TEXT_HTML))
 				.andExpect(status().isFound()).andExpect(content().string(""))
 				.andExpect(view().name("redirect:/events"));
 	}
 	
	@Test
	public void testViewEventId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/4?name=Concert Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Id")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventDescription() throws Exception {
		
		Event eventtest3 = new Event();
		eventtest3.setName("this is a test event");
		eventtest3.setDescription("This is the description of event test 3");
		eventService.save(eventtest3);
		
		mvc.perform(MockMvcRequestBuilders.get("/events/" + eventtest3.getId()).accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString(eventtest3.getDescription())))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventName() throws Exception {
		Event eventtest3 = new Event();
		eventtest3.setName("this is a test event");
		eventtest3.setDescription("This is the description of event test 3");
		eventService.save(eventtest3);
		
		mvc.perform(MockMvcRequestBuilders.get("/events/" + eventtest3.getId()).accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(eventtest3.getName())))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventVenue() throws Exception {
		
		Event eventtest3 = new Event();
		eventtest3.setName("this is a test event");
		eventService.save(eventtest3);
		
		mvc.perform(MockMvcRequestBuilders.get("/events/" + eventtest3.getId() + "?name=Concert Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString(eventtest3.getName())))
		.andExpect(view().name("events/detail"));
	}

	@Test
	public void testViewVenueName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/venues/2").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Stopford")))
		.andExpect(view().name("venues/detail"));
	}
	
	@Test
	public void testViewVenueCapacity() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/venues/2").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("500")))
		.andExpect(view().name("venues/detail"));
	}
	
	@Test
	public void testViewVenueAddress() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/venues/2").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Stopford Building")))
		.andExpect(view().name("venues/detail"));
	}
	
	@Test
	public void testUpcomingEvents() throws Exception {
		when(venue.getEvents()).thenReturn(Collections.<Event> emptyList());
		when(venueService.findById(5)).thenReturn(venue);
		mvc.perform(MockMvcRequestBuilders.get("/venues/2").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Gnother Event")))
		.andExpect(view().name("venues/detail"));
	}
	
	@Test 
	public void testGoogleMapsAPIPresent() throws Exception {
		mvc.perform(get("/events").accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(view().name("events/index"))
				.andExpect(content().string(containsString("maps.googleapis.com/maps/api")))
				.andExpect(content().string(containsString("id=\"map\"")));
	}
	
	@Test
	public void testUpdateAnEventHtml() throws Exception {
 
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(100);
 		venue1.setAddress("12 Test Address, M15 6GH");
 		realVenueService.save(venue1);

 		//update values
		String name = "testevent";
		String vname = "Kilburn";
		String description = "Updated description.";
		Date etime = Time.valueOf("13:00:00");
		Calendar cal = Calendar.getInstance();
		cal.set(2018, Calendar.JANUARY, 10); //Year, month and day of month
		Date date = cal.getTime();
		
		//initial values
		cal.set(2018, Calendar.JANUARY, 15); //Year, month and day of month
		Date date2 = cal.getTime();
		Date etime2 = Time.valueOf("15:00:00");
		String description2 = "Initial Description";
			
		Event eventtest = new Event();
		eventtest.setId(1);
		eventtest.setName("EVENT");
		eventtest.setVenue(venue1);
		eventtest.setDate(date2);
		eventtest.setTime(etime2);
		eventtest.setDescription(description2);
		
		eventService.save(eventtest);
		
		when(mockEventService.findById(1)).thenReturn(eventtest);

		mvc.perform(MockMvcRequestBuilders.post("/events/1/update").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", name)
				.param("eventvenue", vname)
				.param("date", ""+date)
				.param("description", description)
				.param("time", ""+etime)
				.accept(MediaType.TEXT_HTML))
				.andExpect(view().name("redirect:/events"));

	}
	
	@Test
	public void testAddAnEventHtml() throws Exception {
		
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(100);
 		venue1.setAddress("12 Test Address, M15 6GH");
 		realVenueService.save(venue1);
 		
		String name = "testevent";
		String vname = "Kilburn";
		String description = "A description.";
		String time = "15:00:00";
		String date = "2018/1/15";

		String URL = "/events/new";
		
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("eventname", name)
				.param("eventdate", date)
				.param("eventvenue", vname)
				.param("eventtime", time)
				.param("eventdescription", description)
				.accept(MediaType.TEXT_HTML))
				.andExpect(view().name("redirect:/events/"));
	}
    
    @Test
	public void testAddEventHtmlBadTime() throws Exception {

		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(100);
 		venue1.setAddress("12 Test Address, M15 6GH");
 		realVenueService.save(venue1);
    	
 		String name = "testevent";
		String vname = "Kilburn";
		String description = "A description.";
		String time = "error";
		String date = "2018/1/15";
		
		String URL = "/events/new";
		
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("eventname", name)
				.param("eventdate", date)
				.param("eventvenue", vname)
				.param("eventtime", time)
				.param("eventdescription", description)
				.accept(MediaType.TEXT_HTML))
				.andExpect(view().name("redirect:/events/"));
		verify(event, never()).setTime(null);

	}
    
    @Test
	public void testAddEventHtmlHasPassed() throws Exception {
		
    	Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(100);
 		venue1.setAddress("12 Test Address, M15 6GH");
 		realVenueService.save(venue1);
		
 		String name = "testevent";
		String vname = "Kilburn";
		String description = "A description.";
		String time = "15:00:00";
		String date = "2016/1/15";
		
		String URL = "/events/new";
		
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("eventname", name)
				.param("eventdate", date)
				.param("eventvenue", vname)
				.param("eventtime", time)
				.param("eventdescription", description)
				.accept(MediaType.TEXT_HTML))
				.andExpect(view().name("redirect:/events/new"));

	}
	
}