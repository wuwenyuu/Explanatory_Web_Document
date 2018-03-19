package uk.ac.man.cs.eventlite.controllers;


import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.jsoup.Jsoup;
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
		String pattern5 = "<ORGANIZATION>";
		String pattern6 = "</ORGANIZATION>";
		Pattern p3 = Pattern.compile(Pattern.quote(pattern5) + "(.*?)" + Pattern.quote(pattern6));
		Matcher m3 = p3.matcher(text);
		while (m3.find()) {
			Event event = new Event();
		  System.out.println(m3.group(1));
		  String newname1 = m3.group(1).replaceAll(" ", "_");
		  event.setName(newname1);
		  pastEvents.add(event);		  
		}
		model.addAttribute("Events", pastEvents);
		model.addAttribute("Content",content);
		


		return "venues/index";
	}	
	
	@RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String detailedVenue(@PathVariable("name") String name , Model model) throws ParserConfigurationException, SAXException, IOException {
		LinkedList<Event> Events = new LinkedList<Event>();
		name = name.replaceAll("\\s", "_");
		String url1 = "http://dbpedia.org/sparql/?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+%3Fcategory+where+%7B+%0D%0A++%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F";
        String url2 = "%3E+dct%3Asubject+%3Fcategory%0D%0A%7D&format=text%2Fhtml&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000&debug=on&run=+Run+Query+";
		String urlstring = url1+name+url2;
		URL url = new URL(urlstring);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();  // ** WRONG: should use "con.getContentType()" instead but it returns something like "text/html; charset=UTF-8" so this value must be parsed to extract the actual encoding
		encoding = encoding == null ? "UTF-8" : encoding;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
		    baos.write(buf, 0, len);
		}
		String body = new String(baos.toByteArray(), encoding);
		System.out.println("=============body==============");
		System.out.println(body);
		String pattern1 = "<td><a href=\"http://dbpedia.org/resource/Category:";
		String pattern2 = "\">http://dbpedia.org/resource/Category:";
		Pattern p1 = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
		Matcher m1 = p1.matcher(body);
		int count=0;
		while (m1.find()) {
			Event event = new Event();
			System.out.println("=========test categories===========");
		  System.out.println(m1.group(1));
		  count = count+1;
		  event.setName(m1.group(1));
		  event.setKey(name);
		  Events.add(event);
			}
		
		model.addAttribute("subEvents", Events);
		return "venues/detail";
	}
	
	
	
	@RequestMapping(value = "/{name}/{key}", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String newdetailedVenue(@PathVariable("name") String name ,@PathVariable("key") String ref , Model model) throws ParserConfigurationException, SAXException, IOException {

		LinkedList<Event> Events = new LinkedList<Event>();
		ref = ref.replaceAll("\\s", "_");
		String url1 = "http://dbpedia.org/sparql/?default-graph-uri=http%3A%2F%2Fdbpedia.org&query=select+%3Fcategory+where+%7B+%0D%0A++%3Chttp%3A%2F%2Fdbpedia.org%2Fresource%2F";
        String url2 = "%3E+dct%3Asubject+%3Fcategory%0D%0A%7D&format=text%2Fhtml&CXML_redir_for_subjs=121&CXML_redir_for_hrefs=&timeout=30000&debug=on&run=+Run+Query+";
		String urlstring = url1+ref+url2;
		URL url = new URL(urlstring);
		URLConnection con = url.openConnection();
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();  // ** WRONG: should use "con.getContentType()" instead but it returns something like "text/html; charset=UTF-8" so this value must be parsed to extract the actual encoding
		encoding = encoding == null ? "UTF-8" : encoding;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];
		int len = 0;
		while ((len = in.read(buf)) != -1) {
		    baos.write(buf, 0, len);
		}
		String body = new String(baos.toByteArray(), encoding);
		System.out.println("=============body==============");
		System.out.println(body);
		String pattern1 = "<td><a href=\"http://dbpedia.org/resource/Category:";
		String pattern2 = "\">http://dbpedia.org/resource/Category:";
		Pattern p1 = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
		Matcher m1 = p1.matcher(body);
		String pattern3 = "<td><a href=\"http://dbpedia.org/resource/lat:";
		String pattern4 = "\">http://dbpedia.org/resource/lat:";
		Pattern p2 = Pattern.compile(Pattern.quote(pattern3) + "(.*?)" + Pattern.quote(pattern4));
		Matcher m2 = p2.matcher(body);
		double lat = Double.parseDouble(m2.group(1));
		String pattern5 = "<td><a href=\"http://dbpedia.org/resource/long:";
		String pattern6 = "\">http://dbpedia.org/resource/long:";
		Pattern p3 = Pattern.compile(Pattern.quote(pattern5) + "(.*?)" + Pattern.quote(pattern6));
		Matcher m3 = p3.matcher(body);
		double lon = Double.parseDouble(m3.group(1));
		
		int count=0;
		while (m1.find()) {
			Event event = new Event();
			System.out.println("=========test categories===========");
		  System.out.println(m1.group(1));
		  count = count+1;
		  event.setName(m1.group(1));
		  event.setKey(ref);
		  event.setLatLon(lat, lon);
		  Events.add(event);
			}
		
		
		//		LinkedList<Event> futureEvents = new LinkedList<Event>();
		String newname = name.replaceAll(" ", "_");
		LinkedList<Event> subEvents = new LinkedList<Event>();
		String string1 = "https://www.googleapis.com/customsearch/v1?q=";
		String string2 = "&cx=012093427881739797142%3Agfemca_eksy&key=AIzaSyDOyxGFXb4fcoeG0P5h4eMwGjWnSAFFIrQ";
		String jsonString = string1+ref+"/"+newname+string2;
		
        URL website = new URL(jsonString);
        URLConnection connection = website.openConnection();
        BufferedReader in1 = new BufferedReader( new InputStreamReader(connection.getInputStream(),"UTF8"));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in1.readLine()) != null) 
            response.append(inputLine);

        in1.close();

        String jsonContent = response.toString();
        System.out.println("================This is content of json=================");
        System.out.println(jsonContent);
        
        JSONObject root = new JSONObject(jsonContent);
        JSONArray items = root.getJSONArray("items");
		for(int i=0;i<items.length();i++) {
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
			subEvents.add(event);
		}
        
		model.addAttribute("subEvents", Events);
		model.addAttribute("Events",subEvents);
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
