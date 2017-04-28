package uk.ac.man.cs.eventlite.controllers;


import java.util.ArrayList;
import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;
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
@RequestMapping("/venues")
public class VenuesControllerWeb {

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	// page to list every event
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String getVenueList(Model model) {
		model.addAttribute("venues", venueService.findAllByOrderByNameAsc());
		return "venues/index";
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String searchAVenue(@RequestParam(value="searchVenue", required=false) String name, Model model) {
		
		model.addAttribute("venues", venueService.findAllByNameContainingIgnoreCaseOrderByNameAsc(name));

		return "venues/index";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
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
			model.addAttribute("venue", venueService.findById(id));
//			model.addAttribute("venue",venueService.findOne(id));
			
		} catch(Exception ex){
			System.out.println("Exception came........");
		}
		return "venues/detail";
	}
	
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST, produces = { MediaType.TEXT_HTML_VALUE })
	public String deleteVenue(@PathVariable("id") long id) {

		boolean response = venueService.delete(id);
		
		if(response)
			return "redirect:/venues";
		else
			return "venues/deleteVenueFail";
	}
	
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String updateVenue(@PathVariable("id") long id, Model model) {
		model.addAttribute("venues", venueService.findById(id));
		return "venues/update";
	}
	
	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
			MediaType.TEXT_HTML_VALUE })
	public String createVenueFromForm(@PathVariable("id") long id,
			@RequestParam(value = "name", defaultValue = "0") String name,
			@RequestParam(value = "address", defaultValue = "empty") String address, 
			@RequestParam(value = "capacity", defaultValue = "0") int capacity, 
			Model model) {
		
		Venue venue = venueService.findById(id);
		venue.setName(name);
		venue.setCapacity(capacity);

		
		if (address.matches(".{1,299}[A-Z]{1,2}[0-9][A-Z0-9]? [0-9][ABD-HJLNP-UW-Z]{2}"))
		{	
			venue.setAddress(address);
		}else{
			return "redirect:/{id}/update";
		}
		
		venueService.save(venue);

		return "redirect:/venues/";
	}
	
}