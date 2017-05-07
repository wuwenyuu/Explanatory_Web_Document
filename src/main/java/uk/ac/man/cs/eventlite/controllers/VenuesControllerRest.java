package uk.ac.man.cs.eventlite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Venue;

@Controller
@RequestMapping("/venues")
public class VenuesControllerRest {

	@Autowired
	private VenueService venueService;

	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public String getAllVenues(Model model, UriComponentsBuilder a) {
		
		model.addAttribute("self", a.path("/venues").build().toUri());
		model.addAttribute("venues", venueService.findAllByOrderByNameAsc());

		return "venues/index";
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public String searchAnVenues(Model model, UriComponentsBuilder a, @RequestParam(value="searchVenue", required=false) String name) {

		model.addAttribute("self", a.path("/venues/search").build().toUri());
		model.addAttribute("venues", venueService.findAllByNameContainingIgnoreCaseOrderByNameAsc(name));

		return "venues/index";
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> deleteVenues(@PathVariable("id") long id) {

		venueService.delete(id);

		return ResponseEntity.noContent().build();
	}
}