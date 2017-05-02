package uk.ac.man.cs.eventlite.controllers;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Collections;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.mock.MockCreationSettings;
import org.hamcrest.Matchers;
import org.hamcrest.beans.HasProperty;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueRepository;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.entities.Event;
import static org.junit.Assert.*;

@AutoConfigureMockMvc
public class VenuesControllerWebTest extends TestParent {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService realVenueService;
	
	@Mock
	private VenueService venueService;
	
	@Mock
	private VenueRepository venueRepository;
	
	@Mock
	private Venue venue;
	
	@InjectMocks
	private VenuesControllerWeb venueController;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(venueController).build();
	}

	@Test
	public void testGetAllVenues() throws Exception {
		when(venueService.findAllByOrderByNameAsc()).thenReturn(realVenueService.findAllByOrderByNameAsc());
		mvc.perform(get("/venues").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			.andExpect(view().name("venues/index"))
			.andExpect(model().attribute("venues", Matchers.iterableWithSize((int)realVenueService.count())));
	}
	
	@Test
	public void testSearchAVenue() throws Exception {
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		venue1.setAddress("Oxford Road");
 		
 		venueService.save(venue1);
 		
 		Venue venue2 = new Venue();
 		venue2.setId(1);
 		venue2.setName("Kilners");
 		venue2.setCapacity(1000);
 		venue2.setAddress("Oxford Road");
 		
 		venueService.save(venue2);
 		
 		Venue venue3 = new Venue();
 		venue3.setId(1);
 		venue3.setName("kilkil");
 		venue3.setCapacity(1000);
 		venue3.setAddress("Oxford Road");
 		
 		venueService.save(venue3);
		
		mvc.perform(MockMvcRequestBuilders.get("/venues/search?searchVenue=kil").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("venues/index"));
	}
	
	@Test
	@Ignore
	public void deleteVenueWithEvent() throws Exception {		
		mvc.perform(MockMvcRequestBuilders.post("/venues/4/delete").accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk()).andExpect(content().string(""))
		.andExpect(view().name("venues/deleteVenueFail"));		
	public void deleteVenueWithoutEvent() throws Exception {
		when(venue.getEvents()).thenReturn(Collections.<Event> emptyList());
		when(venueService.delete(1)).thenReturn(true);
		mvc.perform(MockMvcRequestBuilders.post("/venues/1/delete").accept(MediaType.TEXT_HTML))
		.andExpect(status().isFound()).andExpect(content().string(""))
		.andExpect(view().name("redirect:/venues"));
		//mockPost("/venues/1/delete", MediaType.TEXT_HTML, "redirect:/venues", 302);
		//verify(venueService).delete(1L);
	}
	
	@Test
	public void deleteVenueWithEvent() throws Exception {
		when(venue.getEvents()).thenReturn(Collections.<Event> singletonList(new Event()));
		when(venueService.findById(1L)).thenReturn(venue);
		
		//mockPost("/venues/1/delete", MediaType.TEXT_HTML, "redirect:/venues/1", 302);
		mvc.perform(MockMvcRequestBuilders.post("/venues/1/delete").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(content().string(""))
			.andExpect(view().name("venues/deleteVenueFail"));
		//verify(venueService, never()).delete(1L);
	}
	
	@Test
	public void testUpdateVenue() throws Exception{
		mvc.perform(get("/venues/34/update").accept(MediaType.TEXT_HTML)).andExpect(status().isOk()).andExpect(view().name("venues/update"));
		
	}
	
    @Test
	public void updateVenueHtml() throws Exception {
 
    	long countBefore = eventService.count();
		String name = "testvenue";
		String address = "Richmond Road, KT2 5PL";
		int capacity = 200;
		mvc.perform(MockMvcRequestBuilders.post(name, address, capacity).accept(MediaType.TEXT_HTML));
		long countAfter = eventService.count();
		assertEquals(countAfter, countBefore);
		mvc.perform(MockMvcRequestBuilders.get("/venues/search?searchVenue=testvenue").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("venues/index"));
		
	}
	@Test
	public void testGetNewVenueHtml() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/venues/new").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("venues/new"));
	}
	
    @Test
	public void testAddVenueHtml() throws Exception {
 
    	long countBefore = venueService.count();
		String name = "testaddvenue";
		String address = "12 Test Address, M15 6GH";
		int capacity = 100;
		String URL = "/venues/new";
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("venuename", name)
				.param("venueaddress", address)
				.param("venuecapacity", ""+capacity)
				.accept(MediaType.TEXT_HTML));
		long countAfter = venueService.count();
		assertEquals(countBefore+1, countAfter);
		mvc.perform(MockMvcRequestBuilders.get("/venues/search?searchVenue=testaddvenue").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("venues/index"));
	}
	
}
