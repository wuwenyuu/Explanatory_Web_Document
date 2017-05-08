package uk.ac.man.cs.eventlite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Controller
@RequestMapping("/events")
public class EventsControllerRest {

	@Autowired
	private EventService eventService;
	
	
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public HttpEntity<Iterable<Event>> getAllEvents() {

		return new ResponseEntity<Iterable<Event>>(eventService.findAllByOrderByDateAsc(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/search", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public String eventGet (@PathVariable("id") long id, Model example, UriComponentsBuilder comp) {

		UriComponents path = comp.path("/").build();
		Venue location = eventService.findById(id).getVenue();
		example.addAttribute("event", eventService.findById(id));
		example.addAttribute("path_to_event", path.toUri() + "events/" + id);
		example.addAttribute("venue", location);
		example.addAttribute("path_to_venue", path.toUri() + "venues/" + location.getId());
		
		return "event";
				
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