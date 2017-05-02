package uk.ac.man.cs.eventlite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.MediaType;

@Controller
@RequestMapping("/")
public class HomePageControllerRest {
	@RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public String getRoot(Model model, UriComponentsBuilder a, UriComponentsBuilder b) { 
		
		UriComponents link= b.path("/").build();
		UriComponents link0= a.path("/").build();
		UriComponents link1= b.path("/events").build();
		UriComponents link2= a.path("/venues").build();
		model.addAttribute("self_link", link.toUri());
		model.addAttribute("events", link1.toUri());
		model.addAttribute("venues", link2.toUri());
		
		return "home/index";
		
		}
}