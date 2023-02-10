package it.siw.viaggi.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.siw.viaggi.model.Viaggio;
import it.siw.viaggi.service.ViaggioService;

@Component
public class ViaggioValidator implements Validator{

	@Autowired
	private ViaggioService viaggioService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Viaggio.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.viaggioService.alreadyExists((Viaggio) target)) { //se io viaggio gia esiste
			/*specifica che c'è stato un errore nella validazione e registra un codice di errore(stringa viaggio.duplicato)
			 * il codice di errore viaggio.duplicato è associato ad un messaggio nel file messages_IT.properties*/
			errors.reject("viaggio.duplicato");
		}
	}

}
