package uk.ac.man.cs.eventlite.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.entities.Event;
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
	public void findAllByOrderByNameAsc() {
		List<Venue> venues = (List<Venue>) venueService.findAllByOrderByNameAsc();
		long count = venueService.count();

		assertThat("findAll should get all venues.", count, equalTo((long) venues.size()));
		
		Venue v1, v2;
		
		v1 = venues.get(1);
		v2 = venues.get(2);
		
		assertTrue("v1 shoule be less than v2 in lexycographic order", v1.getName().compareTo(v2.getName()) < 0);
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
	
	@Test
	public void findAllByNameContainingIgnoreCaseOrderByNameAsc()
	{
		Venue venue1 = new Venue();
 		venue1.setId(1);
 		venue1.setName("Kilburn");
 		venue1.setCapacity(1000);
 		venue1.setAddress("Oxford Road");
 		
 		venueService.save(venue1);
 		
 		Venue venue2 = new Venue();
 		venue2.setId(1);
 		venue2.setName("Name");
 		venue2.setCapacity(1000);
 		venue2.setAddress("Oxford Road");
 		
 		venueService.save(venue2);
 		
 		Venue venue3 = new Venue();
 		venue3.setId(1);
 		venue3.setName("kilkil");
 		venue3.setCapacity(1000);
 		venue3.setAddress("Oxford Road");
 		
 		venueService.save(venue1);
 		
		
		String name = "KIL";
		
		List<Venue> events = (List<Venue>) venueService.findAllByNameContainingIgnoreCaseOrderByNameAsc(name);

		assertEquals("findAll should get all events.", 2, events.size());
	}
 	
 	private void assertEqual(long count, long size) {
 		// TODO Auto-generated method stub
 		
 	}
 	
 	private void assertEqual(String string, String name) {
 		// TODO Auto-generated method stub
 		
 	}

}
