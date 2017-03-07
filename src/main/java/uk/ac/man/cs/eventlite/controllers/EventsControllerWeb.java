package uk.ac.man.cs.eventlite.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestMapping(value = "/new", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String newGreetingHtml(Model model) {
		return "events/new";
	}


	@RequestMapping(value = "/{eventname}/{eventdate}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
			MediaType.TEXT_HTML_VALUE })
	public String createEventFromForm(@PathVariable("eventname") String eventname, @PathVariable("eventdate") String date,
			@RequestParam(value = "eventname", defaultValue = "Empty") String name, 
			@RequestParam(value = "eventdate", defaultValue = "Empty") Date edate,
			Model model) {

		Event event = new Event();
		event.setName(name);
		event.setDate(edate);
		
		eventService.save(event);

		return "redirect:/events/";
	}
}
