package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import uk.ac.man.cs.eventlite.TestParent;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventsControllerWebIntegrationTest extends TestParent {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate template;

	private HttpEntity<String> httpEntity;
	
	private HttpEntity<String> postEntity;

	@Before
	public void setup() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.TEXT_HTML));
		httpEntity = new HttpEntity<String>(headers);
	}

	@Test
	public void testGetAllEvents() {
		get("/events");
	}
	
	@Test
	public void testGetTwitter() {
		get("/events/twitter");
	}
	
	@Ignore
	@Test
	public void testGetSearchEvent() {
		get("/events/search?searchEvent=ev");
	}
	
	@Test
	public void testGetTeet() {
		get("/events/tweet/1/aTweet");
	}
	
	@Test
	public void testAddNewEventGet() {
		get("/events/new");
	}
	
	@Test
	public void testUpdateEventGet() {
		get("/events/1/update");
	}
	
	@Test
	public void testDetailedEvent() {
		get("/events/1");
	}
	
	@Test
	public void testAddNewEventPost() {
		HttpHeaders postHeaders = new HttpHeaders();
		
		//Accept only MediaType.APPLICATION_FORM_URLENCODED_VALUE.
		//postHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED));
		postHeaders.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		postEntity = new HttpEntity<String>("eventname=asdfasdf&eventdate=2020%2F10%2F10&eventvenue=Stopford&eventtime=20%3A00%3A00&eventdescription=sdasdfasdf",
				postHeaders);
		ResponseEntity<String> response = template.exchange("/events/new", HttpMethod.POST, postEntity, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getHeaders().getContentType().toString(), containsString(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testUpdateEventPost() {
		HttpHeaders postHeaders = new HttpHeaders();
		
		//Accept only MediaType.APPLICATION_FORM_URLENCODED_VALUE.
		postHeaders.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		postEntity = new HttpEntity<String>("name=Thirddddd+Event&venuename=Alan+Gilbert&date=2013-01-09&time=15%3A00%3A00&description=This+is+third+event",
				postHeaders);
		ResponseEntity<String> response = template.exchange("/events/6/update", HttpMethod.POST, postEntity, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getHeaders().getContentType().toString(), containsString(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}
	
	@Test
	public void testDeleteEventPost() {
		HttpHeaders postHeaders = new HttpHeaders();
		
		postHeaders.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		postEntity = new HttpEntity<String>("name=Concert%20Event",
				postHeaders);
		
		ResponseEntity<String> response = template.exchange("/events/4/delete", HttpMethod.POST, postEntity, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getHeaders().getContentType().toString(), containsString(MediaType.APPLICATION_JSON_UTF8_VALUE));
	}

	private void get(String url) {
		ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, httpEntity, String.class);
		assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
		assertThat(response.getHeaders().getContentType().toString(), containsString(MediaType.TEXT_HTML_VALUE));
	}
}