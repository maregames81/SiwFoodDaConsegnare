package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.service.IngredienteService;

@Component
public class IngredienteValidator implements Validator {
	@Autowired
	private IngredienteService ingredienteService;

	@Override
	public void validate(Object o, Errors errors) {
		Ingrediente ingr = (Ingrediente)o;
		if (ingr.getNome()!=null && this.ingredienteService.existByNome(ingr.getNome()) ) {
			errors.reject("ingrediente.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Ingrediente.class.equals(aClass);
	}
}
