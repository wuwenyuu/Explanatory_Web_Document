package uk.ac.man.cs.eventlite.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.sql.Time;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	
	private Twitter twitter;

    private ConnectionRepository connectionRepository;
    
    @Inject
    public EventsControllerWeb(ConnectionRepository connectionRepository){
    	this.twitter = new TwitterTemplate(
    		"GeazZxPJS9twq9HzUyaXGQmnY",
    		"tMbgOw5wFADISB0pes35mctqc8rZLwyfETphHbfll9syzL9mOU",
    		"835543865272778756-WteSkkKgICWUVAM6GkPZcFVqopzEk2z",
    		"JcujIL9O189En0OloeFRSZ4tOU4MT2PX2wrEldWGD6OUO"
    	);
    	this.connectionRepository = connectionRepository;
    }
    
    @RequestMapping(value="/twitter",method=RequestMethod.GET)
    public String connectTwitterTweet( @RequestParam(value = "eventId", required=false) Long id,
    		@RequestParam(value = "tweet", required=false) String tweet, Model model) {

        return "redirect:/events/tweet/"+id+"/"+tweet;
    }
    
    @RequestMapping(value = "/tweet/{tweet}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String sendTweet(
			@PathVariable("tweet") String tweet, Model model) {
		try{
			twitter.timelineOperations().updateStatus(tweet);
//			model.addAttribute("event", eventService.findById(id));
			model.addAttribute("tweets", tweet);
			
		}catch(Exception ex){
			System.out.println("Unsuccessful tweet!!!");
		}

		return "events/index";
	}
    
//    @RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
//    public String displayTimeline(Model model) {
//		 if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
//	            return "redirect:/connect/twitter";
//	        }
//		 List<Tweet> tweets = twitter.timelineOperations().getHomeTimeline();
//		 model.addAttribute("Tweets",tweets);
//		 
////	       try {
////	            List<Tweet> statuses = ((TimelineOperations) twitter).getHomeTimeline();
////	        } catch
//	
//		return "events/index";
//    }

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String getAllEvents(Model model) {

		LinkedList<Event> futureEvents = new LinkedList<Event>();
		LinkedList<Event> pastEvents = new LinkedList<Event>();
		
		List<Tweet> tweets = twitter.timelineOperations().getUserTimeline(3);
		model.addAttribute("AllTweets", tweets);
//		tweets.get(0).getCreatedAt()
		
		for (Event event : eventService.findAllByOrderByDateAscTimeAscNameAsc()) {
			if (event.hasPassed())
				pastEvents.add(event);
			else
				futureEvents.add(event);
		}
		
//		model.addAttribute("pastEvents", pastEvents);
//		model.addAttribute("futureEvents", futureEvents);
		return "events/index";
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String searchAnEvent(@RequestParam(value="searchEvent", required=false) String name, Model model) throws ParserConfigurationException, SAXException, IOException {
		
//		LinkedList<Event> futureEvents = new LinkedList<Event>();
		LinkedList<Event> pastEvents = new LinkedList<Event>();
		
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
                pastEvents.add(event);
			}
		}
				
		model.addAttribute("pastEvents", pastEvents);

		return "events/index";
	}	
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String updateEvent(@PathVariable("id") long id, Model model) {
		model.addAttribute("events", eventService.findById(id));
		return "events/update";
	}
	
	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
			MediaType.TEXT_HTML_VALUE })
	public String updateEventFromForm(@PathVariable("id") long id,
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
	
	@RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {
			MediaType.TEXT_HTML_VALUE })
	public String createEventFromForm(
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