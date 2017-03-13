package uk.ac.man.cs.eventlite.controllers;

//import hello.models.Greeting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import uk.ac.man.cs.eventlite.entities.Venue;

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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String detailedEvent(@PathVariable("id") long id , Model model) {
		try{
			model.addAttribute("event",eventService.findOne(id));
		} catch(Exception ex){}
		return "events/detail";
	}
	
	@RequestMapping(value = "/venue/{id}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String detailedVenue(@PathVariable("id") long id , Model model) {
		try{
			List<Event> allEvents = (List<Event>) eventService.findAll();
			List<Event> upcomingEvents = new ArrayList<Event>();
			List<Event> pastEvents = new ArrayList<Event>();
			Date current = new Date();
			
			for(Event e : allEvents){
				if(e.getVenue().getId() == id){
					if(e.getDate().before(current)){
						pastEvents.add(e);
					}
				else{
					upcomingEvents.add(e);
				}
			}
			}
			model.addAttribute("upcoming", upcomingEvents);
			model.addAttribute("pastEvent", pastEvents);
			model.addAttribute("venue", venueService.findOne(id));
//			model.addAttribute("venue",venueService.findOne(id));
		} catch(Exception ex){
			System.out.println("Exception came........");
		}
		return "events/venues";
	}

}
