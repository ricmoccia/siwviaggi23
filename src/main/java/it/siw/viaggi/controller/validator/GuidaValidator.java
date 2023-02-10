package it.siw.viaggi.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.siw.viaggi.model.Guida;
import it.siw.viaggi.service.GuidaService;

@Component
public class GuidaValidator implements Validator{

	@Autowired
	private GuidaService guidaService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Guida.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.guidaService.alreadyExists((Guida) target)) { //se la guida gia esiste
			/*specifica che c'è stato un errore nella validazione e registra un codice di errore(stringa guida.duplicato)
			 * il codice di errore guida.duplicato è associato ad un messaggio nel file messages_IT.properties*/
			errors.reject("guida.duplicato");
		}
	}

}
