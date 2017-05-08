package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
public class VenuesControllerRestTest extends TestParent {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;

	@Test
	public void testGetAllVenues() throws Exception {

		mvc.perform(get("/venues").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title", equalTo("EventLite Venues")))
			.andExpect(jsonPath("$._self", equalTo("http://localhost/venues")));
		
	}
	
	@Test
	public void testSearchAVenue() throws Exception {
		
		Venue venue1 = new Venue();
 		venue1.setName("fdfsdafda");
 		venue1.setCapacity(1000);
 		venueService.save(venue1);
		
		mvc.perform(get("/venues/search?searchVenue=fdfsdafda").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title", equalTo("EventLite Venues")))
			.andExpect(jsonPath("$._self", equalTo("http://localhost/venues/search")))
			.andExpect(jsonPath("$.venues[0].id", equalTo((int)venue1.getId())))
			.andExpect(jsonPath("$.venues[0].name", equalTo(venue1.getName())))
			.andExpect(jsonPath("$.venues[0].capacity", equalTo(venue1.getCapacity())))
			.andExpect(jsonPath("$.venues[0]._self", equalTo("http://localhost/venues/" + venue1.getId())))
			.andExpect(jsonPath("$.venues[0].events", equalTo("http://localhost/venues/" + venue1.getId() + "/events")));
		
	}
}