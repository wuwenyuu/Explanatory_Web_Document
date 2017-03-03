package uk.ac.man.cs.eventlite.controllers;

//import hello.models.Greeting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;

@Controller
@RequestMapping("/events")
public class EventsControllerWeb {

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String getAllEvents(Model model) {

		model.addAttribute("events", eventService.findAllByOrderByDateAsc());
		//model.addAttribute("venues", venueService.findAll());

		return "events/index";
	}



}
