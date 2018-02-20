package uk.ac.man.cs.eventlite.controllers;


import java.util.ArrayList;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	public String searchAnURI(@RequestParam(value="searchVenue", required=false) String name, Model model) throws ParserConfigurationException, SAXException, IOException {
		System.out.println("Keyword:"+name);
//		LinkedList<Event> futureEvents = new LinkedList<Event>();
		LinkedList<Event> Events = new LinkedList<Event>();
		
		URL url = new URL("http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString="+name);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(url.openStream());

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("Result");

		System.out.println("----------------------------");
		System.out.println(nList.getLength());

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			System.out.println("\nCurrent Element :" + nNode.getNodeName());

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				Event event = new Event();
				event.setName(eElement.getElementsByTagName("Label").item(0).getTextContent());
				event.setDescription(eElement.getElementsByTagName("Description").item(0).getTextContent());
				event.setLink(eElement.getElementsByTagName("URI").item(0).getTextContent());
			    event.setRef(eElement.getElementsByTagName("Refcount").item(0).getTextContent());
                System.out.println("--------------event-----------------");
                System.out.println(event.getName());
                System.out.println(event.getDescription());
                Events.add(event);
			}
		}
				
		model.addAttribute("Events", Events);

		return "venues/index";
	}	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String detailedVenue(@PathVariable("id") long id , Model model) {
		try{
			List<Event> allEvents = (List<Event>) eventService.findAll();
			List<Event> upcomingEvents = new ArrayList<Event>();
			List<Event> pastEvents = new ArrayList<Event>();
			Date current = new Date();
			
			for(Event e : allEvents)
              if(e.getVenue()!=null){
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
			System.out.println("Exception came........"+ ex.toString());
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
	public String updateVenueFromForm(@PathVariable("id") long id,
			@RequestParam(value = "name", defaultValue = "0") String name,
			@RequestParam(value = "address", defaultValue = "empty") String address, 
			@RequestParam(value = "capacity", defaultValue = "0") int capacity, 
			Model model) {
		
		if(venueService.findById(id)!=null)
		{	
		  Venue venue = venueService.findById(id);
		  venue.setName(name);
		  venue.setCapacity(capacity);

		
		  if (address.matches(".{1,299}[A-Z]{1,2}[0-9][A-Z0-9]? [0-9][ABD-HJLNP-UW-Z]{2}"))
		  {	
			  venue.setAddress(address);
		  }else{
			  return "redirect:/venues/{id}/update";
		  }
		
		  venueService.save(venue);
	    }

		return "redirect:/venues/";
	}
	

    @RequestMapping(value = "/new", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
    public String newVenueHtml(Model model) {
	  return "venues/new";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
			MediaType.TEXT_HTML_VALUE })
	public String createVenueFromForm(
			@RequestParam(value = "venuename", defaultValue = "Empty") String name, 
			@RequestParam(value = "venueaddress", defaultValue = "Empty") String addr,
			@RequestParam(value = "venuecapacity", defaultValue = "0") int cap,
			Model model) {
		
		Venue venue = new Venue();
		venue.setName(name);
		if (addr.matches(".{1,299}[A-Z]{1,2}[0-9][A-Z0-9]? [0-9][ABD-HJLNP-UW-Z]{2}"))
		{	
			venue.setAddress(addr);
		}else{
			return "redirect:/venues/new";
		}
		venue.setCapacity(cap);
		venueService.save(venue);

		return "redirect:/venues/";
		
	}
    
 	@ExceptionHandler(ConversionFailedException.class)
 	public String missingParameterHandler(Exception exception) {
 
 	    return "redirect:/venues";
 	    // Actual exception handling
 	}
}
