package it.siw.viaggi.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.siw.viaggi.model.Attivita;
import it.siw.viaggi.service.AttivitaService;

@Component
public class AttivitaValidator implements Validator{

	@Autowired
	private AttivitaService AttivitaService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Attivita.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.AttivitaService.alreadyExists((Attivita) target)) { //se l'Attivita gia esiste
			/*specifica che c'è stato un errore nella validazione e registra un codice di errore(stringa Attivita.duplicato)
			 * il codice di errore Attivita.duplicato è associato ad un messaggio nel file messages_IT.properties*/
			errors.reject("Attivita.duplicato");
		}
	}

}
