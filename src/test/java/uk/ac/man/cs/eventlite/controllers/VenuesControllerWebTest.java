package uk.ac.man.cs.eventlite.controllers;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.hamcrest.Matchers;
import org.hamcrest.beans.HasProperty;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Venue;

@AutoConfigureMockMvc
public class VenuesControllerWebTest extends TestParent {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@Test
	public void testGetAllVenues() throws Exception {
		mvc.perform(get("/venues").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk())
			.andExpect(view().name("venues/index"))
			.andExpect(model().attribute("venues", Matchers.iterableWithSize((int)venueService.count())));
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
	public void testGetNewVenueHtml() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/venues/new").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("venues/new"));
	}
	
    @Test
	public void testAddVenueHtml() throws Exception {
 
    	long countBefore = venueService.count();
		String name = "testaddvenue";
		String address = "12 Test Address";
		String postcode = "SK78PD";
		int capacity = 100;
		String URL = "/venues/new";
		mvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("venuename", name)
				.param("venueaddress", address)
				.param("venuepostcode", postcode)
				.param("venuecapacity", ""+capacity)
				.accept(MediaType.TEXT_HTML));
		long countAfter = venueService.count();
		assertEquals(countBefore+1, countAfter);
		mvc.perform(MockMvcRequestBuilders.get("/venues/search?searchVenue=testaddvenue").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("venues/index"));
	}
    	
}
