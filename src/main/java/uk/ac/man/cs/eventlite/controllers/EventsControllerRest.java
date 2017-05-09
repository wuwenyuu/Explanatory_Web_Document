package uk.ac.man.cs.eventlite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.entities.Event;

@Controller
@RequestMapping("/events")
public class EventsControllerRest {

	@Autowired
	private EventService eventService;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public String getAllEvents(Model model, UriComponentsBuilder b, UriComponentsBuilder c) {

		UriComponents eventslink = b.path("/events").build();		
		model.addAttribute("self_link", eventslink.toUri());
		
		UriComponents venueslink = b.path("/venues").build();
		model.addAttribute("venue_self_link", venueslink.toUri());
		
		model.addAttribute("events", eventService.findAll());
		
		return "events/index";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
 	public String getAnEvent(@PathVariable("id") long id, Model model, UriComponentsBuilder b, UriComponentsBuilder c) {
	
		Event event = eventService.findById(id);
		UriComponents link = b.path("/events/").build();		
		model.addAttribute("self_link", link.toUri());
		UriComponents venueLink = c.path("/venues/").build();
		model.addAttribute("venue_link", venueLink.toUri());
		model.addAttribute("event", event);		
		return "events/detail";
		
 		}
	
	@RequestMapping(value="/search", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public HttpEntity<Iterable<Event>> searchAnEvents(@RequestParam(value="searchEvent", required=false) String name) {

		return new ResponseEntity<Iterable<Event>>(eventService.findAllByNameContainingIgnoreCaseOrderByDateAscTimeAscNameAsc(name), HttpStatus.OK);
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> deleteEvent(@PathVariable("id") long id) {

		eventService.delete(id);

		return ResponseEntity.noContent().build();
	}
}