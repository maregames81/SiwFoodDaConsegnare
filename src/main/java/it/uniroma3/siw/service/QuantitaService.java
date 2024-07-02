package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.QuantitaIngrediente;
import it.uniroma3.siw.repository.QuantitaRepository;

@Service
public class QuantitaService {

	@Autowired
	private QuantitaRepository quantitaRepository;
	
	@Transactional
	public void save(QuantitaIngrediente qty) {
		this.quantitaRepository.save(qty);
	}
	
	@Transactional
	public QuantitaIngrediente findById(Long id) {
		return this.quantitaRepository.findById(id).get();
	}
	
	@Transactional
	public void delete(Long id) {
		 this.quantitaRepository.deleteById(id);
	}
	
	
}
