package uk.ac.man.cs.eventlite.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.sql.Time;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import static uk.ac.man.cs.eventlite.helpers.ErrorHelpers.formErrorHelper;

@Controller
@RequestMapping("/events")
public class EventsControllerWeb {

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String getAllEvents(Model model) {

		LinkedList<Event> futureEvents = new LinkedList<Event>();
		LinkedList<Event> pastEvents = new LinkedList<Event>();
		
		for (Event event : eventService.findAllByOrderByDateAscTimeAscNameAsc()) {
			if (event.hasPassed())
				pastEvents.add(event);
			else
				futureEvents.add(event);
		}
		
		model.addAttribute("pastEvents", pastEvents);
		model.addAttribute("futureEvents", futureEvents);
		return "events/index";
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String searchAnEvent(@RequestParam(value="searchEvent", required=false) String name, Model model) {
		
		LinkedList<Event> futureEvents = new LinkedList<Event>();
		LinkedList<Event> pastEvents = new LinkedList<Event>();
		
		for (Event event : eventService.findAllByNameContainingIgnoreCaseOrderByDateAscTimeAscNameAsc(name)) {
			if (event.hasPassed())
				pastEvents.add(event);
			else
				futureEvents.add(event);
		}
		
		model.addAttribute("pastEvents", pastEvents);
		model.addAttribute("futureEvents", futureEvents);

		return "events/index";
	}
	
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String updateEvent(@PathVariable("id") long id, Model model) {
		model.addAttribute("events", eventService.findById(id));
		return "events/update";
	}
	
	
	
	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
			MediaType.TEXT_HTML_VALUE })
	public String createEventFromForm(@PathVariable("id") long id,
			@RequestParam(value = "name", defaultValue = "0") String name,
			@RequestParam(value = "venuename", defaultValue = "0") String venuename,	
			@RequestParam (value="date")@DateTimeFormat(pattern="yyyy-MM-dd") Date date, 
			@RequestParam (value="description", defaultValue="empty") String description,
			@RequestParam (value="time")@DateTimeFormat(pattern="HH:mm:SS") Date time,
			Model model) {
		
		Event event = eventService.findById(id);
		//Event event = new Event();
		event.setName(name);
		event.setVenue(venueService.findOneByName(venuename));
		event.setDate(date);
		event.setDescription(description);
		event.setTime(time);
		eventService.save(event);

		return "redirect:/events/";
	}
	
	
	

 	@ExceptionHandler(ConversionFailedException.class)
 	public String missingParameterHandler(Exception exception) {
 
 	    return "redirect:/events";
 	    // Actual exception handling
 	}
 	
 	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST, produces = { MediaType.TEXT_HTML_VALUE })
	public String deleteEvent(@PathVariable("id") long id) {

		eventService.delete(id);

		return "redirect:/events";
	}
 		@RequestMapping(value = "/new", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String newGreetingHtml(Model model) {
		return "events/new";
	}
	
	@RequestMapping(value = "/new/{eventname}/{eventdate}/{eventvenue}/{eventtime}/{eventdescription}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
			MediaType.TEXT_HTML_VALUE })
	public String createEventFromForm(@PathVariable("eventname") String eventname, 
			@PathVariable("eventdate") String eventdate,
			@PathVariable("eventvenue") String eventvenue,
			@PathVariable("eventvenue") String eventtime,
			@PathVariable("eventdescription") String eventdescription,
			@RequestParam(value = "eventname", defaultValue = "Empty") String name, 
			@RequestParam(value = "eventdate", defaultValue = "Empty") Date edate,
			@RequestParam(value = "eventvenue", defaultValue = "Empty") String vname,
			@RequestParam(value = "eventtime", defaultValue = "Empty") String etime,
			@RequestParam(value = "eventdescription", defaultValue = "Empty") String description,
			Model model) {
		
		Event event = new Event();
		event.setName(name);
		event.setDate(edate);
		event.setVenue(venueService.findOneByName(vname));
		event.setDescription(description);
		
		if (etime.matches("^(1?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$"))
		{	
			Date time = new Date();
		    time = Time.valueOf(etime); 
		    event.setTime(time);
		}
		if (event.hasPassed())
			return "redirect:/events/new";	
		eventService.save(event);

		return "redirect:/events/";
	}
	
	
 	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String detailedEvent(@PathVariable("id") long id, Model model) {
		try{
			model.addAttribute("event",eventService.findById(id));
		}catch(Exception ex){}

		return "events/detail";
	}
	
}