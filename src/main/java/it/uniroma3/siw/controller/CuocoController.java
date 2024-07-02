package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.RicettaService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class CuocoController {

	@Autowired private CuocoService cuocoService;


	@Autowired private RicettaService ricettaService;


	@Autowired private UserService userService;


	@Autowired private CredentialsService credentialsService;


	@GetMapping("/cuochi")
	public String listaCuochi(Model model) {
		model.addAttribute("cuochi", this.cuocoService.findAll());
		return "cuochi.html";
	}

	@GetMapping("/cuochi/{id}")
	public String cuoco(@PathVariable("id") Long id, Model model){

		Cuoco c= this.cuocoService.findById(id);

		model.addAttribute("cuoco", c);
		model.addAttribute("ricette", this.ricettaService.findByCuoco(c));

		return "cuoco.html";
	}

	@GetMapping("/admin/adminCuochi")
	public String adminCuochi(Model model) {

		model.addAttribute("cuochi", this.cuocoService.findAll());

		return "admin/adminCuochi.html";
	}


	@GetMapping("/admin/newCuoco")
	public String newCuoco(Model model) {

		model.addAttribute("cuoco", new Cuoco());

		return "admin/newCuoco.html";
	}

	@PostMapping("/admin/newCuoco")
	public String newCuoco(@Valid @ModelAttribute("cuoco") Cuoco c,@RequestParam("imageFile") MultipartFile imageFile, Model model) throws IOException {

		this.cuocoService.save(c,imageFile);	

		model.addAttribute("cuochi", this.cuocoService.findAll());

		return "admin/adminCuochi.html";
	}

	@GetMapping("/admin/modifica/{id}")
	public String modificaCuoco(@PathVariable("id") Long id ,Model model) {

		Cuoco c= this.cuocoService.findById(id);

		model.addAttribute("cuoco", c);
		model.addAttribute("newCuoco", new Cuoco());

		return "admin/modificaCuoco.html";
	}


	@PostMapping("/admin/modificaCuoco")
	public String modificaCuoco(@Valid @ModelAttribute("cuoco") Cuoco c,@RequestParam("cuocoId") Long idc,@RequestParam("imageFile") MultipartFile imageFile, Model model) throws IOException {

		Cuoco oldC= this.cuocoService.findById(idc);


		oldC.setNome(c.getNome());
		oldC.setCognome(c.getCognome());
		oldC.setNascita(c.getNascita());


		if(imageFile != null && !imageFile.isEmpty()) {
			this.cuocoService.save(oldC,imageFile);
		} else {
			this.cuocoService.save(oldC);	
		}
		model.addAttribute("cuochi", this.cuocoService.findAll());

		return "admin/adminCuochi.html";
	}

	@GetMapping("/admin/elimina/{id}")
	public String eliminaCuoco(@PathVariable("id") Long id ,Model model) {

		Cuoco c= this.cuocoService.findById(id);

		model.addAttribute("cuoco", c);

		return "admin/eliminaCuoco.html";
	}

	@PostMapping("/admin/annullaCancellazioneCuoco")
	public String annullaCancellazioneCuoco( Model model) {
		model.addAttribute("cuochi", this.cuocoService.findAll());
		return "redirect:/admin/adminCuochi";
	}


	@PostMapping("/admin/eliminaCuoco")
	public String confermaEliminaCuoco(@RequestParam("cuocoId") Long idC, Model model) {

		Cuoco c= this.cuocoService.findById(idC);
		User u= this.userService.findByCuoco(c);
		Credentials cr= this.credentialsService.findByUser(u);
		if(cr!=null) {
			this.credentialsService.delete(cr.getId());
		} else {
			this.cuocoService.delete(c.getId());
		}
		
		return "redirect:/admin/adminCuochi";
	}
	
	
	




}
