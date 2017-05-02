package uk.ac.man.cs.eventlite.controllers;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;

@Controller
@RequestMapping("/")
public class HomepageControllerWeb {

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;
	
	
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String getAllEvents(Model model) {

		LinkedList<Event> futureEvents = new LinkedList<Event>();
		int count = 0;
		for (Event event : eventService.findAllByOrderByDateAscTimeAscNameAsc()) {
			if (count<3 && !event.hasPassed()){
				futureEvents.add(event);
				count++;
			}
		}
		
		model.addAttribute("futureEvents", futureEvents);
		return "home/home";
	}
}
