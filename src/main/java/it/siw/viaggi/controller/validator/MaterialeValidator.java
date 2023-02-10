package it.siw.viaggi.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.siw.viaggi.model.Materiale;
import it.siw.viaggi.service.MaterialeService;

@Component
public class MaterialeValidator implements Validator{

	@Autowired
	private MaterialeService materialeService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Materiale.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.materialeService.alreadyExists((Materiale) target)) { //se il materiale gia esiste
			/*specifica che c'è stato un errore nella validazione e registra un codice di errore(stringa materiale.duplicato)
			 * il codice di errore materiale.duplicato è associato ad un messaggio nel file messages_IT.properties*/
			errors.reject("materiale.duplicato");
		}
	}

}
