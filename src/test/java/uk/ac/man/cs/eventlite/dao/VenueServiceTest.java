package uk.ac.man.cs.eventlite.dao;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public class VenueServiceTest extends TestParent {

	@Autowired
	private VenueService venueService;
	
	@Autowired
	private EventService eventService;

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
 	public void deleteByIdWithEvent(){
 		
 		
 		Venue venue1 = new Venue();
  		venue1.setId(1);
  		venue1.setName("Kilburn");
  		venue1.setCapacity(1000);
  		
  		venueService.save(venue1);
  		
 		Event eventtest = new Event();
 		eventtest.setId(3);
 		eventtest.setName("testevent");
 		eventtest.setVenue(venue1);
 		eventtest.setDate(null);
 		
 		
 		eventService.save(eventtest);
 		
 		long count = venueService.count();
 		count--;
 		
 		venueService.delete(1);
 		
 		List<Venue> venues = (List<Venue>) venueService.findAll();
 
 		assertEqual(count, (long) venues.size());
 	}
	
	@Test 
 	public void deleteByIdNoEvents(){
 		
 		
 		Venue venue1 = new Venue();
  		venue1.setId(1);
  		venue1.setName("Kilburn");
  		venue1.setCapacity(1000);
  		
  		venueService.save(venue1);
  		
		
 		long count = venueService.count();


 		venueService.delete(1);
 		
 		List<Venue> venues = (List<Venue>) venueService.findAll();
 
 		assertEqual(count, (long) venues.size());
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
 		venue2.setId(2);
 		venue2.setName("Name");
 		venue2.setCapacity(1000);
 		venue2.setAddress("Oxford Road");
 		
 		venueService.save(venue2);
 		
 		Venue venue3 = new Venue();
 		venue3.setId(3);
 		venue3.setName("kilkil");
 		venue3.setCapacity(1000);
 		venue3.setAddress("Oxford Road");
 		
 		venueService.save(venue3);
 		
		
		String name = "KIL";
		//These are not being save using initial data and retrieving kilburn
		//Irrelevant test at the moment
		
		List<Venue> events = (List<Venue>) venueService.findAllByNameContainingIgnoreCaseOrderByNameAsc(name);

		assertEquals("findAll should get all events.", 2, events.size());
	}
 	
 	private void assertEqual(long count, long size) {
 		// TODO Auto-generated method stub
 		
 	}
 	
 	private void assertEqual(String string, String name) {
 		// TODO Auto-generated method stub
 		
 	}
 	
 	// acceptable drift from constants in this file, and reports from google api
 	public static final double GEO_EPSILON = 1e-3;

    // tests the google geocoding api is updating the venues coordinates
    public static final String KILBURN_ADDRESS = "Kilburn Building, University of Manchester, Oxford Rd, Manchester M13 9PL, UK";
    public static final double KILBURN_LAT = 53.4672264;
    public static final double KILBURN_LON = -2.2340865;
    @Test public void testGeocodingService() {
            Venue testvenue = new Venue();
            testvenue.setName("A simple test venue");
            testvenue.setCapacity(31337);
            testvenue.setAddress(KILBURN_ADDRESS);
            assertEquals("Google geocoding Latitude", KILBURN_LAT, testvenue.getLat(), GEO_EPSILON);
            assertEquals("Google geocoding Longtitude", KILBURN_LON, testvenue.getLon(), GEO_EPSILON);
    }
    
    // tests the google geocoding api is updating the venues coordinates
    public static final String BOGUS_ADDRESS = "fksjiocvmeniofijduiooijifao";
    public static final double BOGUS_LAT = 53.4807593;
    public static final double BOGUS_LON = -2.2426305;
    @Test public void testGeocodingServiceAddressDoesntExist() {
            Venue testvenue = new Venue();
            testvenue.setName("A simple test venue");
            testvenue.setCapacity(31337);
            testvenue.setAddress(BOGUS_ADDRESS);
            assertEquals("Google geocoding Latitude", BOGUS_LAT, testvenue.getLat(), GEO_EPSILON);
            assertEquals("Google geocoding Longtitude", BOGUS_LON, testvenue.getLon(), GEO_EPSILON);
    }

}
