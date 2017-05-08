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
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@Autowired
	private VenueService realVenueService;
	
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
	public void testGetAllEventsNoTwitterConn() throws Exception {
		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isFound())
				.andExpect(view().name("redirect:/connect/twitter"));
	}
	
	@Test
	public void testTweetingNoConn() throws Exception {
		mvc.perform(get("/events/tweet/1/aTweet").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testTwitterConnCheck() throws Exception {
		mvc.perform(get("/events/twitter").accept(MediaType.TEXT_HTML)).andExpect(status().isFound())
				.andExpect(view().name("redirect:/connect/twitter"));
	}
	
	@Test
	public void testGetNewEventHtml() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/new").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("events/new"));
	}
	
    @Test
	public void testAddEventHtml() throws Exception {
 
    	long countBefore = eventService.count();
		String name = "testaddevent";
		String date = "2020/01/01";
		long venue = 4;
		String URL = "/events/new";
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("eventname", name)
				.param("eventdate", date)
				.param("eventvenue", ""+venue)
				.accept(MediaType.TEXT_HTML));
		
		long countAfter = eventService.count();
		assertEquals(countAfter, countBefore+1);
	}
	
	@Test
	public void testUpdate() throws Exception{
		mvc.perform(get("/events/34/update").accept(MediaType.TEXT_HTML)).andExpect(status().isOk()).andExpect(view().name("events/update"));
		
	}
	
    @Test
	public void testUpdateEventHtml() throws Exception {
 
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
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		venue1.setAddress("Oxford Road");
 		
 		realVenueService.save(venue1);
 		
		Event eventtest1 = new Event();
		eventtest1.setId(3);
		eventtest1.setName("testevent");
		eventtest1.setVenue(venue1);
		eventtest1.setDate(date1);
		eventtest1.setTime(time1);
		
		eventService.save(eventtest1);
		
		Event eventtest2 = new Event();
		eventtest2.setId(2);
		eventtest2.setName("testevent");
		eventtest2.setVenue(venue1);
		eventtest2.setDate(date2);
		eventtest2.setTime(time2);
		
		eventService.save(eventtest2);
		
		mvc.perform(MockMvcRequestBuilders.get("/events/search?searchEvent=TEST").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index"));
	}
	public void testSearchAVenue() throws Exception {
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		venue1.setAddress("Oxford Road");
 		
 		venueService.save(venue1);
 		
 		Venue venue2 = new Venue();
 		venue2.setId(2);
 		venue2.setName("Kilners");
 		venue2.setCapacity(1000);
 		venue2.setAddress("Oxford Road");
 		
 		venueService.save(venue2);
 		
 		Venue venue3 = new Venue();
 		venue3.setId(3);
 		venue3.setName("kilkil");
 		venue3.setCapacity(1000);
 		venue3.setAddress("Oxford Road");
 		
 		venueService.save(venue3);
		
		mvc.perform(MockMvcRequestBuilders.get("/venues/search?searchVenue=kil").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("venues/index"));
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
		mvc.perform(MockMvcRequestBuilders.get("/events/4?name=Concert Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("This is Concert")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/4?name=Concert Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Concert Event")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventDate() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/4?name=Concert Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("2018-01-10")))
		.andExpect(view().name("events/detail"));
	}
	
	@Test
	public void testViewEventVenue() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/4?name=Concert Event").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(containsString("Concert Event")))
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
	public void testAddAnEventHtml() throws Exception {
 
		String name = "testevent";
		String vname = "testvenue";
		String description = "A description.";
		String address = "12 Test Address, M15 6GH";
		int capacity = 100;
		
		Calendar cal = Calendar.getInstance();
		cal.set(2018, Calendar.JANUARY, 10); //Year, month and day of month
		Date date = cal.getTime();
		
		cal.set(2018, Calendar.JANUARY, 15); //Year, month and day of month
		Date date2 = cal.getTime();
		
		Date etime = Time.valueOf("13:00:00");
		Date etime2 = Time.valueOf("15:00:00");
		
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(capacity);
 		venue1.setAddress(address);
 		
 		realVenueService.save(venue1);
 		
		Event eventtest1 = new Event();
		eventtest1.setId(1);
		eventtest1.setName("EVENT");
		eventtest1.setVenue(venue1);
		eventtest1.setDate(date2);
		eventtest1.setTime(etime2);
		
		eventService.save(eventtest1);
		
		String URL = "/events/new";
		
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("eventname", name)
				.param("eventdate", "" + date)
				.param("eventvenue", vname)
				.param("eventtime", "" + etime)
				.param("eventdescription", description)
				.accept(MediaType.TEXT_HTML))
				.andExpect(view().name("redirect:/events/"));
		
	}
    
    @Test
	public void testAddEventHtmlBadTime() throws Exception {

		String name = "testevent";
		String vname = "testvenue";
		String description = "A description.";
		String address = "12 Test Address, M15 6GH";
		int capacity = 100;
		
		Calendar cal = Calendar.getInstance();
		cal.set(2018, Calendar.JANUARY, 10); //Year, month and day of month
		Date date = cal.getTime();
		
		cal.set(2018, Calendar.JANUARY, 15); //Year, month and day of month
		Date date2 = cal.getTime();
		
		String etime = "13:00";
		Date etime2 = Time.valueOf("15:00:00");
		
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(capacity);
 		venue1.setAddress(address);
 		
 		realVenueService.save(venue1);
 		
		Event eventtest1 = new Event();
		eventtest1.setId(1);
		eventtest1.setName("EVENT");
		eventtest1.setVenue(venue1);
		eventtest1.setDate(date2);
		eventtest1.setTime(etime2);
		
		eventService.save(eventtest1);
		
		String URL = "/events/new";
		
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("eventname", name)
				.param("eventdate", "" + date)
				.param("eventvenue", vname)
				.param("eventtime", "" + etime)
				.param("eventdescription", description)
				.accept(MediaType.TEXT_HTML))
				.andExpect(view().name("redirect:/events/"));

	}
    
    @Test
	public void testAddEventHtmlHasPassed() throws Exception {

		String name = "testevent";
		String vname = "testvenue";
		String description = "A description.";
		String address = "12 Test Address, M15 6GH";
		int capacity = 100;
		
		Calendar cal = Calendar.getInstance();
		cal.set(2010, Calendar.JANUARY, 10); //Year, month and day of month
		Date date = cal.getTime();
		
		cal.set(2010, Calendar.JANUARY, 15); //Year, month and day of month
		Date date2 = cal.getTime();
		
		String etime = "01:00:00";
		Date etime2 = Time.valueOf("02:00:00");
		
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(capacity);
 		venue1.setAddress(address);
 		
 		realVenueService.save(venue1);
 		
		Event eventtest1 = new Event();
		eventtest1.setId(1);
		eventtest1.setName("EVENT");
		eventtest1.setVenue(venue1);
		eventtest1.setDate(date2);
		eventtest1.setTime(etime2);
		
		eventService.save(eventtest1);
		
		String URL = "/events/new";
		
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("eventname", name)
				.param("eventdate", "" + date)
				.param("eventvenue", vname)
				.param("eventtime", "" + etime)
				.param("eventdescription", description)
				.accept(MediaType.TEXT_HTML))
				.andExpect(view().name("redirect:/events/new"));

	}
}