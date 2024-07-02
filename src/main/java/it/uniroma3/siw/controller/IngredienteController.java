package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.QuantitaIngrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.QuantitaService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.validator.IngredienteValidator;
import jakarta.validation.Valid;

@Controller
public class IngredienteController {
	@Autowired IngredienteService ingredienteService;
	
	@Autowired QuantitaService quantitaService;
	
	@Autowired RicettaService ricettaService;
	
	@Autowired IngredienteValidator ingredienteVal;
	
	
	@GetMapping("/newIngrediente")
	public String newIngrediente(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "newIngrediente.html";
	}
	
	@PostMapping("/newIngrediente")
	public String newIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente,BindingResult bindingResult, Model model) {
		
		
		this.ingredienteVal.validate(ingrediente, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			this.ingredienteService.save(ingrediente);
		} else {
			return "newIngrediente.html";
		}
		
		return "redirect:/";
	}
	
	@PostMapping("/aggiungiIngrediente")
	public String addIngrediente(@ModelAttribute("qty") QuantitaIngrediente qty, @RequestParam("ricettaId") Long id,
			@RequestParam String nomeIngrediente, Model model) {
		
		Ricetta ricetta = ricettaService.findById(id);
		System.out.println("Nome Ricetta: " + ricetta.getNome());
		Ingrediente ingr= ingredienteService.findByNome(nomeIngrediente);
		qty.setIngrediente(ingr);
		
		
		this.quantitaService.save(qty);
		ricetta.getQuantitaIngrediente().add(qty);
		this.ricettaService.save(ricetta);
		
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		model.addAttribute("qty", new QuantitaIngrediente());
		model.addAttribute("ricetta", ricetta);
		return "aggiungiIngrediente.html";
	}
	
	
	@GetMapping("/admin/adminIngrediente")
    public String adminIngrediente(Model model) {
        model.addAttribute("ingrediente", new Ingrediente());
        return "/admin/adminIngrediente.html";
    }

    @PostMapping("/admin/aggiungiIngrediente")
    public String adminIngrediente(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente,BindingResult bindingResult, Model model) {
        
    	this.ingredienteVal.validate(ingrediente, bindingResult);
    	
        
        if(!bindingResult.hasErrors()) {
			this.ingredienteService.save(ingrediente);
		} else {
			return "/admin/adminIngrediente.html";
		}
        
        return "redirect:/admin/adminIngrediente";
    }
	
	

}
