package uk.ac.man.cs.eventlite;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;


@AutoConfigureMockMvc
public class HomePageTest extends TestParent {

	@Autowired
	private MockMvc mvc;
    
	@Test
	public void getRoot() throws Exception {
		mvc.perform(get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk());
	}

	@Test
	public void getJsonRoot() throws Exception {
		mvc.perform(get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.title", equalTo("EventLite Home")))
		.andExpect(jsonPath("$._self", equalTo("http://localhost/")))
		.andExpect(jsonPath("$.events", equalTo("http://localhost/events")))
		.andExpect(jsonPath("$.venues", equalTo("http://localhost/venues")));
	}
}
