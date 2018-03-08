package uk.ac.man.cs.eventlite.controllers;


import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
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
	public String searchAnURI(@RequestParam(value="searchVenue", required=false) String name, Model model) throws ParserConfigurationException, SAXException, IOException, ClassCastException, ClassNotFoundException {
		
		Event content = new Event();
		content.setDescription(name);
		LinkedList<Event> pastEvents = new LinkedList<Event>();
		String serializedClassifier = "src/main/java/classifiers/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
		System.out.println(classifier.classifyWithInlineXML(name));
		String text = classifier.classifyWithInlineXML(name);
		String pattern1 = "<PERSON>";
		String pattern2 = "</PERSON>";
		Pattern p1 = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
		Matcher m1 = p1.matcher(text);
		while (m1.find()) {
			Event event = new Event();
		  System.out.println(m1.group(1));
		  String newname = m1.group(1).replaceAll(" ", "_");
		  event.setName(newname);
		  pastEvents.add(event);		  
		}
		String pattern3 = "<LOCATION>";
		String pattern4 = "</LOCATION>";
		Pattern p2 = Pattern.compile(Pattern.quote(pattern3) + "(.*?)" + Pattern.quote(pattern4));
		Matcher m2 = p2.matcher(text);
		while (m2.find()) {
			Event event = new Event();
		  System.out.println(m2.group(1));
		  String newname1 = m2.group(1).replaceAll(" ", "_");
		  event.setName(newname1);
		  pastEvents.add(event);		  
		}
		model.addAttribute("Events", pastEvents);
		model.addAttribute("Content",content);
		
		//This is from keyword search to result version 1
//		System.out.println("Keyword:"+name);
////		LinkedList<Event> futureEvents = new LinkedList<Event>();
//		LinkedList<Event> Events = new LinkedList<Event>();
//		
//		URL url = new URL("http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString="+name);
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		Document doc = db.parse(url.openStream());
//
//		//optional, but recommended
//		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
//		doc.getDocumentElement().normalize();
//
//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//
//		NodeList nList = doc.getElementsByTagName("Result");
//
//		System.out.println("----------------------------");
//		System.out.println(nList.getLength());
//
//		for (int temp = 0; temp < nList.getLength(); temp++) {
//
//			Node nNode = nList.item(temp);
//
//			System.out.println("\nCurrent Element :" + nNode.getNodeName());
//
//			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//
//				Element eElement = (Element) nNode;
//
//				Event event = new Event();
//				event.setName(eElement.getElementsByTagName("Label").item(0).getTextContent());
//				event.setDescription(eElement.getElementsByTagName("Description").item(0).getTextContent());
//				event.setLink(eElement.getElementsByTagName("URI").item(0).getTextContent());
//			    event.setRef(eElement.getElementsByTagName("Refcount").item(0).getTextContent());
//                System.out.println("--------------event-----------------");
//                System.out.println(event.getName());
//                System.out.println(event.getDescription());
//                Events.add(event);
//			}
//		}
				
//		model.addAttribute("Events", Events);

		return "venues/index";
	}	
	
	@RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String detailedVenue(@PathVariable("name") String name , Model model) throws ParserConfigurationException, SAXException, IOException {
		LinkedList<Event> Events = new LinkedList<Event>();
		name = name.replaceAll("\\s", "_");
 		System.out.println("================Text parameters=============");
 		System.out.println(name);

		
		URL url = new URL("http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString="+name);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(url.openStream());

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

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
			    if(temp==0) {
			    	event.setKey(event.getName());
			    }else {
			    	   event.setKey(Events.getFirst().getName());
			    }
                System.out.println("--------------event-----------------");
                System.out.println(event.getName());
                System.out.println(event.getDescription());
                Events.add(event);
			}
		}
		System.out.println("================Text content of subEvents===============");
		System.out.println(Events.get(1).getName());
		System.out.println(Events.get(2).getName());
		System.out.println(Events.get(3).getName());
		System.out.println(Events.get(4).getName());
		System.out.println(Events.get(0).getName());
		
		Event event1 = new Event();
		event1.setKey(Events.getFirst().getName());
		model.addAttribute("Key", event1);
		
		model.addAttribute("subEvents", Events);
		return "venues/detail";
	}
	
	
	
	@RequestMapping(value = "/{name}/{key}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String newdetailedVenue(@PathVariable("name") String name ,@PathVariable("key") String ref , Model model) throws ParserConfigurationException, SAXException, IOException {

		LinkedList<Event> Events1 = new LinkedList<Event>();
		ref = ref.replaceAll("\\s", "_");
 		System.out.println("================Text parameters=============");
 		System.out.println(ref);

		
		URL url = new URL("http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString="+ref);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(url.openStream());

		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();

//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

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
			    event.setKey(ref);
                System.out.println("--------------event-----------------");
                System.out.println(event.getName());
                System.out.println(event.getDescription());
                Events1.add(event);
			}
		}
		
		
		//		LinkedList<Event> futureEvents = new LinkedList<Event>();
		String newname = name.replaceAll(" ", "_");
		LinkedList<Event> Events = new LinkedList<Event>();
		String string1 = "https://www.googleapis.com/customsearch/v1?q=";
		String string2 = "&cx=012093427881739797142%3Agfemca_eksy&key=AIzaSyDOyxGFXb4fcoeG0P5h4eMwGjWnSAFFIrQ";
		String jsonString = string1+newname+string2;
		
        URL website = new URL(jsonString);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream(),"UTF8"));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        String jsonContent = response.toString();
        System.out.println("================This is content of json=================");
        System.out.println(jsonContent);
        
        JSONObject root = new JSONObject(jsonContent);
        JSONArray items = root.getJSONArray("items");
		for(int i=0;i<10;i++) {
			Event event = new Event();
			JSONObject eachitem = items.getJSONObject(i);
			String link = eachitem.getString("link");
			String title = eachitem.getString("title");
			String snippet = eachitem.getString("snippet");
//			JSONObject image = eachitem.getJSONObject("image");
//			String context = image.getString("contextLink");
			event.setName(title);
			event.setDescription(snippet);
			event.setLink(link);
			System.out.println("============This is title of image============");
			System.out.println(event.getDescription());
			System.out.println(event.getLink());
			Events.add(event);
		}
        
		model.addAttribute("subEvents", Events1);
		model.addAttribute("Events",Events);
		return "venues/detailnew";
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
