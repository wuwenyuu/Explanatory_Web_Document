package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.matchers.LessThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;

@AutoConfigureMockMvc
public class HomepageControllerWebTest extends TestParent{


		@Autowired
		private MockMvc mvc;

		@Autowired
		private EventService eventService;

		@Autowired
		private VenueService venueService;
		
		@Test
		public void testViewFirstEvent() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home")).andExpect(content().string(containsString("Gnother Event")));

		}
		
		@Test
		public void testViewSecondEvent() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home"))
			.andExpect(content().string(containsString("Concert Event")));
		}
		
		@Test
		public void testViewFirstVenue() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home")).andExpect(content().string(containsString("Alan Gilbert")));

		}
		
		@Test
		public void testViewSecondVenue() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home"))
			.andExpect(content().string(containsString("Stopford")));
		}
		
		@Test
		public void testViewThirdVenue() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home")).andExpect(content().string(containsString("Kilburn")));

		}
		
		
		@Test
		public void testGetAllVenues() throws Exception {
			mvc.perform(get("").accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(view().name("home/home"))
				.andExpect(model().attribute("topvenue", Matchers.iterableWithSize((3))));
			
		}
		
		@Test
		public void testGetAllEvents() throws Exception {
			mvc.perform(get("").accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(view().name("home/home"))
				.andExpect(model().attribute("futureEvents", Matchers.iterableWithSize((2))));
			
		}
}
