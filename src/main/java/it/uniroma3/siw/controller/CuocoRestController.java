package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.service.CuocoService;

@RestController
public class CuocoRestController {

	@Autowired
	private CuocoService cuocoService;
	
	
	
	@GetMapping("/rest/cuochi")
	public Iterable<Cuoco> getCuochi(){
		return this.cuocoService.findAll();
	}
}
