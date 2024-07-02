package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.service.RicettaService;

@RestController
public class RicettaRestController {
	
	@Autowired
	private RicettaService ricettaService;
	
	@GetMapping("/rest/ricette")
	public Iterable<Ricetta> getRicette(){
		return this.ricettaService.findAll();
	}

}
