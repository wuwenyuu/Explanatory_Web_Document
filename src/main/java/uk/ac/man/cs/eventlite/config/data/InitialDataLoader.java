package uk.ac.man.cs.eventlite.config.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Component
@Profile({ "default", "test" })
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger log = LoggerFactory.getLogger(InitialDataLoader.class);

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (eventService.count() > 0) {
			log.info("Database already populated. Skipping data initialization.");
			return;
		}

		// Build and save initial models here.
		Event event1 = new Event();
		event1.setId(1);
		event1.setDate(null);
		event1.setName("Concert");
		event1.setVenue(1);
		eventService.save(event1);
		
		Event event2 = new Event();
		event2.setId(2);
		event2.setDate(null);
		event2.setName("Another Event");
		event2.setVenue(2);
		eventService.save(event2);
		
		Event event3 = new Event();
		event3.setId(3);
		event3.setDate(null);
		event3.setName("Third Event");
		event3.setVenue(3);
		eventService.save(event3);
				
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		venueService.save(venue1);
 		
 		Venue venue2 = new Venue();
 		venue2.setId(2);
 		venue2.setName("Sackville");
 		venue2.setCapacity(500);
 		venueService.save(venue2);
 		

 		Venue venue3 = new Venue();
 		venue2.setId(3);
 		venue2.setName("Stopford");
 		venue2.setCapacity(750);
 		venueService.save(venue3);

	}
	
}
