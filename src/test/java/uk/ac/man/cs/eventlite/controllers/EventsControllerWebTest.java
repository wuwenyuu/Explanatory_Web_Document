package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.dao.EventService;

@AutoConfigureMockMvc
public class EventsControllerWebTest extends TestParent {

	@Autowired
	private MockMvc mvc;
	
   	@Autowired
	private EventService eventService;

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

}
