package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.service.RicettaService;

@Component
public class RicettaValidator implements Validator {

	@Autowired private RicettaService ricettaService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Ricetta.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Ricetta ricetta = (Ricetta)target;
		if (ricetta.getNome()!=null && ricetta.getCuoco()!=null && this.ricettaService.existByNomeAndCuoco(ricetta.getNome(),ricetta.getCuoco())) {
			errors.rejectValue("nome", "ricetta.duplicate");
		}
		
	}
}
