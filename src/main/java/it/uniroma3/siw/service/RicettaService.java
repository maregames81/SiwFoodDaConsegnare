package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.RicettaRepository;

@Service
public class RicettaService {

	@Autowired
	private RicettaRepository ricettaRepository;
	
	@Transactional
	public Ricetta findById(Long id) {
		return ricettaRepository.findById(id).get();
	}
	
	@Transactional
	public List<Ricetta> findByCuoco(Cuoco c){
		return this.ricettaRepository.findByCuoco(c);
	}

	@Transactional
	public Iterable<Ricetta> findAll() {
		return ricettaRepository.findAll();
	}

	@Transactional
	public void save(Ricetta ricetta,MultipartFile file) throws IOException {
		ricetta.setFoto(Base64.getEncoder().encodeToString(file.getBytes()));
		ricettaRepository.save(ricetta);	
		
	}
	
	
	
	@Transactional
	public void save(Ricetta ricetta) {
		ricettaRepository.save(ricetta);		
	}

	@Transactional
	public void delete(Long idC) {
		this.ricettaRepository.deleteById(idC);
		
	}

	public boolean existByNomeAndCuoco(String nome, Cuoco cuoco) {
		
		return this.ricettaRepository.existsByNomeAndCuoco(nome,cuoco);
	}

}
