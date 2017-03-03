package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import uk.ac.man.cs.eventlite.TestParent;

@AutoConfigureMockMvc
public class EventsControllerWebTest extends TestParent {

	@Autowired
	private MockMvc mvc;

	@Test
	public void testGetAllEvents() throws Exception {
		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index"));
	}
	
	@Test
	public void getEventId() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(content().string(containsString("Id"))).andExpect(view().name("events/detail"));
	}
	
	@Test
	public void getEventDescription() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(content().string(containsString("Description"))).andExpect(view().name("events/detail"));
	}
	
	@Test
	public void getEventName() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(content().string(containsString("Third Event"))).andExpect(view().name("events/detail"));
	}
	
	@Test
	public void getEventVenue() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(content().string(containsString("Alan Gilbert"))).andExpect(view().name("events/detail"));
	}
	
	@Test
	public void getEventDate() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/events/3?name=Third Event").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(content().string(containsString("2013-01-09"))).andExpect(view().name("events/detail"));
	}
}
