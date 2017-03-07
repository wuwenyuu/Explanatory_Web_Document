package uk.ac.man.cs.eventlite.config.data;

import java.util.Calendar;
import java.util.Date;

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
        
		Calendar cal = Calendar.getInstance();
		cal.set(2013, Calendar.JANUARY, 11); //Year, month and day of month
		Date date1 = cal.getTime();
		
		cal.set(2013, Calendar.JANUARY, 10); //Year, month and day of month
		Date date2 = cal.getTime();
		
		cal.set(2013, Calendar.JANUARY, 9); //Year, month and day of month
		Date date3 = cal.getTime();
		
		Venue venue1 = new Venue();
 		venue1.setId(4);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		venueService.save(venue1);
 		
 		Venue venue2 = new Venue();
 		venue2.setId(5);
 		venue2.setName("Stopford");
 		venue2.setCapacity(500);
 		venueService.save(venue2);

 		Venue venue3 = new Venue();
 		venue3.setId(6);
 		venue3.setName("Alan Gilbert");
 		venue3.setCapacity(750);
 		venueService.save(venue3);
 		
		// Build and save initial models here.
		Event event1 = new Event();
		event1.setId(4);
		event1.setDate(date1);
		event1.setName("Concert");
		event1.setVenue(venue1);
		event1.setDescription("this is first event");
		eventService.save(event1);
		
		Event event2 = new Event();
		event2.setId(5);
		event2.setDate(date2);
		event2.setName("Another Event");
		event2.setVenue(venue2);
		event2.setDescription("this is another event");
		eventService.save(event2);
		
		Event event3 = new Event();
		event3.setId(6);
		event3.setDate(date3);
		event3.setName("Third Event");
		event3.setVenue(venue3);
		event3.setDescription("this is third event");
		eventService.save(event3);
				

	}
	
}
