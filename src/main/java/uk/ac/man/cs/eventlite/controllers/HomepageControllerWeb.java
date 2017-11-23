package uk.ac.man.cs.eventlite.controllers;

import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//import extractKeyword.db;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class HomepageControllerWeb {

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value="/{plaintext}", method=RequestMethod.GET)
	public String getPlainText(@RequestParam(value="plaintext") String text, Model model) throws Exception {
//		String URL = "http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString="+text;
//		String data = getDataByJavaIo(URL);
		System.out.println("Input text: "+text);
		
        db c = new db ();
        c.configiration(0.25,0);
        //, 0, "non", "AtLeastOneNounSelector", "Default", "yes");
        c.evaluate(text);
        System.out.println("resource : "+c.getResu());

//		LinkedList<String> labels = new LinkedList<String>();
//
//		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//		InputSource src = new InputSource();
//		src.setCharacterStream(new StringReader(data));
//
//		Document doc = builder.parse(src);
//		   labels.add(doc.getElementsByTagName("Categories").item(0).getTextContent());
//		System.out.println(labels.getFirst());
//		   model.addAttribute("categories",labels);
		   
		
		return "home/home";
		
	}
	
	public String getDataByJavaIo(String url) throws IOException {
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = new URL(url).openStream();
             bufferedReader = new BufferedReader(new InputStreamReader(inputStream));			
		     return readData(bufferedReader);
		}catch(IOException e) {
			throw e;
		}finally {
			closeResource(inputStream);
			closeResource(bufferedReader);
		}
	}
	
	public String readData(Reader reader) throws IOException{
		StringBuilder stringBuilder = new StringBuilder();
		int cp;
		while((cp = reader.read())!=-1) {
			stringBuilder.append((char)cp);
		}
		return stringBuilder.toString();
	}
	
	public void closeResource(AutoCloseable closeable) {
		try {
			if(closeable!=null) {
				closeable.close();
				System.out.println("\n"+closeable.getClass().getName()+"CLOSED...");
			}
		}catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
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
		
		
		
		int maxvenueid = 0;
		
		//finds the possible max number of venues in db
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


		model.addAttribute("topvenue", topvenue);
		
		return "home/home";
	}
	
    @RequestMapping(value = "/spotlight/{tweet}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String sendTweet(@PathVariable("tweet") String tweet, Model model) {
		try{
//			twitter.timelineOperations().updateStatus(tweet);
//			model.addAttribute("event", eventService.findById(id));
//			model.addAttribute("tweets", tweet);
			
		}catch(Exception ex){
			System.out.println("Unsuccessful tweet!!!");
		}

		return "home/home";
	}
	
}
