package uk.ac.man.cs.eventlite.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.entities.Venue;

public class VenueServiceTest extends TestParent {

	@Autowired
	private VenueService venueService;

	@Test
	public void findAll() {
		List<Venue> venues = (List<Venue>) venueService.findAll();
		long count = venueService.count();

		assertThat("findAll should get all venues.", count, equalTo((long) venues.size()));
	}
	
 	@Test 
	public void save(){
 		
 		long count = venueService.count();
 		count++;
 		
 	    Venue venuetest = new Venue();
 		venuetest.setId(1);
 		venuetest.setName("venuetest");
 		venuetest.setCapacity(100);
 		
 		venueService.save(venuetest);
 		
 		List<Venue> venues = (List<Venue>) venueService.findAll();
 
 		assertEqual(count, (long) venues.size());
 	}
 	
	
	@Test
	public void findOneByName() {
		String name = "venueTest";
		Venue venuetest = new Venue();
	    venuetest.setId(1);
	 	venuetest.setName(name);
	 	venuetest.setCapacity(100);
	 	venueService.save(venuetest);
	 	
		Venue findvenue = venueService.findOneByName(name);

		assertEqual(findvenue.getName(), name);
	}
 	
 	private void assertEqual(long count, long size) {
 		// TODO Auto-generated method stub
 		
 	}
 	
 	private void assertEqual(String string, String name) {
 		// TODO Auto-generated method stub
 		
 	}

}
