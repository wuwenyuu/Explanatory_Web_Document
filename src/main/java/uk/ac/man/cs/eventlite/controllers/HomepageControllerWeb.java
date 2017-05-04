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

		LinkedList<Venue> topvenue = new LinkedList<Venue>();
		LinkedList<Event> mostPopVenueEvents = new LinkedList<Event>();
		
		
		int maxvenueid = 0;
		
		//finds the possi max number of venues in db
		for (Venue venue : venueService.findAllByOrderByNameAsc()) {
			if(venue!=null && (int)venue.getId()>maxvenueid){
				maxvenueid=(int)venue.getId();
			}
		}
		
		//arrays to store the current number of event for each venue
		boolean[] isselected = new boolean[maxvenueid + 1];
	    int[] venueCount = new int[maxvenueid + 1];
	    
		for (int i=0;i<maxvenueid+1;i++) {
			venueCount[i]=0;
	
		}
		for (int i=0;i<maxvenueid+1;i++) {
			isselected[i]=false;
	
		}
		
		//update array to show number of events for each venue
		for (Event event: eventService.findAllByOrderByDateAscNameAsc()) {
			if(event.getVenue()!=null && event.getVenue().getId()<maxvenueid){
				venueCount[(int) event.getVenue().getId()]++;
			}
		}

		int currentmax = 0;
		int currentid = 0;
		
		//currently iselected always seen as false for all elements
		for(int j = 1; j<4;j++){
			for(int i=1;i<=maxvenueid;i++){
				if(!isselected[i] && venueCount[i]>=currentmax){
					currentmax = venueCount[i];			
					currentid = i;
				}
				
			}
			topvenue.add(venueService.findById(currentid));
			isselected[currentid]=true;
			currentmax=0;
			currentid=0;
		}

		for (Event event : eventService.findAllByOrderByDateAscTimeAscNameAsc()) {
			if(event.getVenue()!=null && event.getVenue().getId()<maxvenueid && (int)event.getVenue().getId()==currentid && !event.hasPassed()){
				mostPopVenueEvents.add(event);	
			}	
			
		}
		model.addAttribute("topvenue", topvenue);
		model.addAttribute("mostPopVenueEvents", mostPopVenueEvents);
		return "home/home";
	}
	
}
