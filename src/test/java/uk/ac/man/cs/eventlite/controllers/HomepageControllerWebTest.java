package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.internal.matchers.LessThan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.man.cs.eventlite.TestParent;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
//import org.apache.commons.io.FileUtils;
import org.apache.commons.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;

@AutoConfigureMockMvc
public class HomepageControllerWebTest extends TestParent{


		@Autowired
		private MockMvc mvc;

		@Autowired
		private EventService eventService;

		@Autowired
		private VenueService venueService;
		
		public final String URL = "http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString=paris";
		@Test
		public void testJackson() throws IOException, SAXException, ParserConfigurationException {
			String data = getDataByJavaIo(URL);
//			System.out.println(data.length());
//			System.out.println(data);
			LinkedList<String> labels = new LinkedList<String>();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource src = new InputSource();
			src.setCharacterStream(new StringReader(data));

			Document doc = builder.parse(src);
			for(int i=0;i<5;i++) {
			   labels.add(doc.getElementsByTagName("Result").item(i).getTextContent());
			}
			System.out.println(labels.getLast());
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
		@Test
		public void testViewFirstEvent() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home")).andExpect(content().string(containsString("Gnother Event")));

		}
		
		@Test
		public void testViewSecondEvent() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home"))
			.andExpect(content().string(containsString("Concert Event")));
		}
		
		@Test
		public void testViewFirstVenue() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home")).andExpect(content().string(containsString("Alan Gilbert")));

		}
		
		@Test
		public void testViewSecondVenue() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home"))
			.andExpect(content().string(containsString("Stopford")));
		}
		
		@Test
		public void testViewThirdVenue() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("").accept(MediaType.TEXT_HTML))
			.andExpect(status().isOk()).andExpect(view().name("home/home")).andExpect(content().string(containsString("Kilburn")));

		}
		
		
		@Test
		public void testGetAllVenues() throws Exception {
			mvc.perform(get("").accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(view().name("home/home"))
				.andExpect(model().attribute("topvenue", Matchers.iterableWithSize((3))));
			
		}
		
		@Test
		public void testGetAllEvents() throws Exception {
			mvc.perform(get("").accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk())
				.andExpect(view().name("home/home"))
				.andExpect(model().attribute("futureEvents", Matchers.iterableWithSize((2))));
			
		}
		  public static void main(String argv[]) {

			    try {
				
				URL url = new URL("http://lookup.dbpedia.org/api/search.asmx/KeywordSearch?QueryClass=place&QueryString=London");
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

						System.out.println("Label : " + eElement.getElementsByTagName("Label").item(0).getTextContent());
						System.out.println("URI : " + eElement.getElementsByTagName("URI").item(0).getTextContent());
						System.out.println("Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
						System.out.println("Refcount : " + eElement.getElementsByTagName("Refcount").item(0).getTextContent());

						//						NodeList nodes = eElement.getChildNodes();
//						System.out.println("-----------------------------------");
//						System.out.println("Classes : ");
//						for(int i =0; i < nodes.getLength(); i++){
//							Node subNode = nodes.item(i);
//						     if(subNode.getNodeType() == Element.ELEMENT_NODE){
//						    	         Element child = (Element) subNode;
//						             String tagName = child.getTagName();
//						             if(tagName.equals("Classes")){
//						                   NodeList ClassesChilds = child.getChildNodes();
//						                   for(int j = 0; j < ClassesChilds.getLength(); j++){
//						                      //and like above
//						                	   Node fin = ClassesChilds.item(j);
//						                	   if(fin.getNodeName() == "Class") {
//						                	   System.out.println("\nSub Element :" + fin.getNodeName());
//						                	   if(fin.getNodeType() == Node.ELEMENT_NODE) {
//						                		   Element finn = (Element) fin;
//							                	   System.out.println("Label : " + finn.getElementsByTagName("Label").item(0).getTextContent());
//											   System.out.println("URI : " + finn.getElementsByTagName("URI").item(0).getTextContent());
//						                	   }
//						                	   }
//						                   }
//						             }
//						     }
//						}
//						System.out.println("---------------------------------");
//						System.out.println("Categories : ");
//						for(int i =0; i < nodes.getLength(); i++){
//							Node subNode = nodes.item(i);
//						     if(subNode.getNodeType() == Element.ELEMENT_NODE){
//						    	         Element child = (Element) subNode;
//						             String tagName = child.getTagName();
//						             if(tagName.equals("Categories")){
//						                   NodeList ClassesChilds = child.getChildNodes();
//						                   for(int j = 0; j < ClassesChilds.getLength(); j++){
//						                      //and like above
//						                	   Node fin = ClassesChilds.item(j);
//						                	   if(fin.getNodeName() == "Category") {
//						                	   System.out.println("\nSub Element :" + fin.getNodeName());
//						                	   if(fin.getNodeType() == Node.ELEMENT_NODE) {
//						                		   Element finn = (Element) fin;
//							                	   System.out.println("Label : " + finn.getElementsByTagName("Label").item(0).getTextContent());
//											   System.out.println("URI : " + finn.getElementsByTagName("URI").item(0).getTextContent());
//						                	   }
//						                	   }
//						                   }
//						             }
//						     }
//						}
//						System.out.println("-----------------------------------------------------------------");

					}
				}
			    } catch (Exception e) {
				e.printStackTrace();
			    }
			  }

}
