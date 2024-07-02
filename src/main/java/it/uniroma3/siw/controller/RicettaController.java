package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.QuantitaIngrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.IngredienteService;
import it.uniroma3.siw.service.QuantitaService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.validator.RicettaValidator;
import jakarta.validation.Valid;

@Controller 
public class RicettaController {

	@Autowired RicettaService ricettaService;

	@Autowired IngredienteService ingredienteService;

	@Autowired CredentialsService credentialsService;

	@Autowired CuocoService cuocoService;

	@Autowired QuantitaService quantitaService;
	
	@Autowired RicettaValidator ricettaValidator;


	@GetMapping("/ricette")
	public String showRicette (Model model) {
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "ricette.html";
	}

	@GetMapping("/newRicetta")
	public String newRicetta(Model model) {
		model.addAttribute("ricetta", new Ricetta());

		return "newRicetta.html";
	}



	@PostMapping(value = { "/newRicetta" })
	public String newRicetta(@Valid @ModelAttribute("ricetta") Ricetta ricetta,BindingResult bindingResult,
			@ModelAttribute("userDetails") UserDetails userD,@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam("descrizione") String desc, Model model) throws IOException {

		String username = userD.getUsername();
		Cuoco c= credentialsService.getCredentials(username).getUser().getCuoco();

		//imposto i valori nella ricetta
		ricetta.setCuoco(c);
		ricetta.setDescrizione(desc);
		
		//verifica se la ricetta non è duplicata
		this.ricettaValidator.validate(ricetta, bindingResult);
		
		if(!bindingResult.hasErrors()) {
			ricettaService.save(ricetta, imageFile);
			c.getRicette().add(ricetta);
			cuocoService.save(c);
			
			model.addAttribute("ingredienti", this.ingredienteService.findAll());
			model.addAttribute("qty", new QuantitaIngrediente());
			model.addAttribute("ricetta", ricetta);

			System.out.println("Nome Ricetta: " + ricetta.getNome());

			return "aggiungiIngrediente.html";
			
		}
		
		return "newRicetta.html";
		
	} 

	@GetMapping("/ricette/{id}")
	public String ricetta(@PathVariable("id") Long id, Model model) {
		Ricetta r= ricettaService.findById(id);
		model.addAttribute("ricetta", r);
		model.addAttribute("ingredienti", r.getQuantitaIngrediente());
		return "ricetta.html";
	}
	
	
	
	@GetMapping("/modificaRicetteCuoco")
	public String modificaMyRicette(@ModelAttribute("userDetails") UserDetails userD, Model model){
		
		String username= userD.getUsername();
		Cuoco c= this.credentialsService.getCredentials(username).getUser().getCuoco();
		
		model.addAttribute("ricette", this.ricettaService.findByCuoco(c));
		
		return "modificaRicetteCuoco.html";
	}
	
	
	@GetMapping("/eliminaRicetta/{id}")
	public String eliminaRicettaCuoco(@PathVariable("id") Long id, Model model) {
		Ricetta r= ricettaService.findById(id);
		model.addAttribute("ricetta", r);
		return "eliminaRicettaCuoco.html";
	}
	
	
	@PostMapping("/annullaCancellazioneRicettaCuoco")
	public String annullaCancellazioneRicettaCuoco( Model model) {
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "redirect:/modificaRicetteCuoco";
	}
	
	
	@PostMapping("/eliminaRicettaCuoco")
	public String confermaEliminaRicettaCuoco(@RequestParam("ricettaId") Long idR, Model model) {

		this.ricettaService.delete(idR);
		return "redirect:/modificaRicetteCuoco";
	}
	
	
	
	@GetMapping("/modificaRicetta/{id}")
	public String modificaRicettaCuoco(@PathVariable("id") Long id, Model model) {
		Ricetta r= ricettaService.findById(id);
		model.addAttribute("ricetta", r);
		model.addAttribute("ingredienti", r.getQuantitaIngrediente());
		model.addAttribute("newRicetta", new Ricetta());
		return "modificaRicettaCuoco.html";
	}
	
	
	
	@GetMapping("/eliminaQtyRicetta/{idR}/{idQ}")
	public String eliminaQtyRicettaCuoco(@PathVariable("idR") Long idR,@PathVariable("idQ") Long idQ, Model model) {
		QuantitaIngrediente qty= this.quantitaService.findById(idQ);
		Ricetta r= ricettaService.findById(idR);
		r.getQuantitaIngrediente().remove(qty);
		this.quantitaService.delete(qty.getId());

		model.addAttribute("ricetta", r);
		model.addAttribute("ingredienti", r.getQuantitaIngrediente());
		model.addAttribute("newRicetta", new Ricetta());
		return "redirect:/modificaRicetta/"+r.getId();
	}

	@GetMapping("/modificaQtyRicetta/{idR}/{idQ}")
	public String modificaQtyRicettaCuoco(@PathVariable("idR") Long idR,@PathVariable("idQ") Long idQ, Model model) {
		QuantitaIngrediente qty= this.quantitaService.findById(idQ);
		Ricetta r= ricettaService.findById(idR);


		model.addAttribute("ricetta", r);
		model.addAttribute("qty", qty);
		return "modificaQtyRicettaCuoco.html";
	}
	
	
	@PostMapping("/modificaQtyRicetta")
	public String modificaQtyRicettaCuoco(@RequestParam("qtyId") Long qtyId,@RequestParam("ricettaId") Long idR,@RequestParam("nuovaQuantita") int q, Model model) {

		QuantitaIngrediente qty= this.quantitaService.findById(qtyId);
		
		qty.setQuantita(q);
		this.quantitaService.save(qty);
		return "redirect:/modificaRicetta/"+ idR;
	}
	
	
	
	@GetMapping("/aggiungiIngrediente/{id}")
	public String aggiungiIngrediente(@PathVariable("id") Long idR,Model model) {
		
		
		Ricetta ricetta= this.ricettaService.findById(idR);
		
		model.addAttribute("ingredienti", this.ingredienteService.findAll());
		model.addAttribute("qty", new QuantitaIngrediente());
		model.addAttribute("ricetta", ricetta);
		
		
		return "aggiungiIngrediente.html";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@GetMapping("/admin/adminRicette")
	public String adminRicette (Model model) {
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "admin/adminRicette.html";
	}



	@GetMapping("/admin/eliminaRicetta/{id}")
	public String eliminaRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta r= ricettaService.findById(id);
		model.addAttribute("ricetta", r);
		return "admin/eliminaRicetta.html";
	}


	@PostMapping("/admin/annullaCancellazioneRicetta")
	public String annullaCancellazioneRicetta( Model model) {
		model.addAttribute("ricette", this.ricettaService.findAll());
		return "redirect:/admin/adminRicette";
	}


	@PostMapping("/admin/eliminaRicetta")
	public String confermaEliminaRicetta(@RequestParam("ricettaId") Long idR, Model model) {

		this.ricettaService.delete(idR);
		return "redirect:/admin/adminRicette";
	}


	@GetMapping("/admin/modificaRicetta/{id}")
	public String modificaRicetta(@PathVariable("id") Long id, Model model) {
		Ricetta r= ricettaService.findById(id);
		model.addAttribute("ricetta", r);
		model.addAttribute("ingredienti", r.getQuantitaIngrediente());
		model.addAttribute("newRicetta", new Ricetta());
		return "admin/modificaRicetta.html";
	}


	@GetMapping("/admin/eliminaQtyRicetta/{idR}/{idQ}")
	public String eliminaQtyRicetta(@PathVariable("idR") Long idR,@PathVariable("idQ") Long idQ, Model model) {
		QuantitaIngrediente qty= this.quantitaService.findById(idQ);
		Ricetta r= ricettaService.findById(idR);
		r.getQuantitaIngrediente().remove(qty);
		this.quantitaService.delete(qty.getId());

		model.addAttribute("ricetta", r);
		model.addAttribute("ingredienti", r.getQuantitaIngrediente());
		model.addAttribute("newRicetta", new Ricetta());
		return "redirect:/admin/modificaRicetta/"+r.getId();
	}

	@GetMapping("/admin/modificaQtyRicetta/{idR}/{idQ}")
	public String modificaQtyRicetta(@PathVariable("idR") Long idR,@PathVariable("idQ") Long idQ, Model model) {
		QuantitaIngrediente qty= this.quantitaService.findById(idQ);
		Ricetta r= ricettaService.findById(idR);


		model.addAttribute("ricetta", r);
		model.addAttribute("qty", qty);
		return "admin/modificaQtyRicetta.html";
	}


	@PostMapping("/admin/modificaQtyRicetta")
	public String modificaQtyRicetta(@RequestParam("qtyId") Long qtyId,@RequestParam("ricettaId") Long idR,@RequestParam("nuovaQuantita") int q, Model model) {

		QuantitaIngrediente qty= this.quantitaService.findById(qtyId);
		
		qty.setQuantita(q);
		this.quantitaService.save(qty);
		return "redirect:/admin/modificaRicetta/"+ idR;
	}

	@GetMapping("/admin/newRicetta")
	public String adminNewRicetta(Model model) {

		model.addAttribute("ricetta", new Ricetta());
		model.addAttribute("cuochi", this.cuocoService.findAll());
		return "admin/newAdminRicetta.html";
	}

	@PostMapping("/admin/newAdminRicetta")
	public String adminNewRicetta(@ModelAttribute("ricetta") Ricetta r,@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam("IdCuoco") Long idC,@RequestParam("descrizione") String desc, Model model) throws IOException {


		Cuoco c= this.cuocoService.findById(idC);
		r.setDescrizione(desc);
		r.setCuoco(c);
		this.ricettaService.save(r, imageFile);
		c.getRicette().add(r);
		cuocoService.save(c);
		return "redirect:/admin/adminRicette";
	}
}
