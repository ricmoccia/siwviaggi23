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
	private AttivitaService attivitaService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Attivita.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.attivitaService.alreadyExists((Attivita) target)) { //se l'attivita gia esiste
			/*specifica che c'è stato un errore nella validazione e registra un codice di errore(stringa attivita.duplicato)
			 * il codice di errore attivita.duplicato è associato ad un messaggio nel file messages_IT.properties*/
			errors.reject("attivita.duplicato");
		}
	}

}
