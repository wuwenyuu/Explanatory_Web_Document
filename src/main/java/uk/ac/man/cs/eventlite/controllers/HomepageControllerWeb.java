package uk.ac.man.cs.eventlite.controllers;

import java.util.LinkedList;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

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
//		return "home/home";
//	}
	
	
//	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
//	public String getMostPopVenue(Model model) {

		
		LinkedList<Event> mostPopVenueEvents = new LinkedList<Event>();
		
		int maxvenueid = 0;
		
		for (Event event : eventService.findAllByOrderByDateAscTimeAscNameAsc()) {
			if(event.getVenue()!=null && (int)event.getVenue().getId()>maxvenueid){
				maxvenueid=(int)event.getVenue().getId();
			}
		}
		
	    int[] venueCount = new int[maxvenueid + 1];
		
		for (Event event : eventService.findAllByOrderByDateAscTimeAscNameAsc()) {
			venueCount[(int) event.getVenue().getId()]++;
		}
		
		int currentmax = 0;
		int currentid = 0;
		
		for(int i=0;i<maxvenueid;i++){
			if(venueCount[i]>currentmax){
				currentmax = venueCount[i];			
				currentid = i;
			}
			
		}
		
		for (Event event : eventService.findAllByOrderByDateAscTimeAscNameAsc()) {
			if((int)event.getVenue().getId()==currentid){
				mostPopVenueEvents.add(event);	
			}	
			
		}
		
		model.addAttribute("mostPopVenueEvents", mostPopVenueEvents);
		return "home/home";
	}
	
}
