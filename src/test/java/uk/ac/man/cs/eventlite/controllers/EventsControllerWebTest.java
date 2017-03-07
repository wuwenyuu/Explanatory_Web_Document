package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
	public void testDeleteEvent() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/events/1/delete").accept(MediaType.TEXT_HTML))
				.andExpect(status().isFound()).andExpect(content().string(""))
				.andExpect(view().name("redirect:/events"));
	}

}
