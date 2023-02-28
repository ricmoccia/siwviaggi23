package it.siw.viaggi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.siw.viaggi.controller.validator.ViaggioValidator;
import it.siw.viaggi.model.Viaggio;
import it.siw.viaggi.model.Guida;
import it.siw.viaggi.model.Attivita;
import it.siw.viaggi.service.ViaggioService;
import it.siw.viaggi.service.CredentialsService;
import it.siw.viaggi.model.User;
import it.siw.viaggi.service.GuidaService;
import it.siw.viaggi.service.AttivitaService;

@Controller
public class ViaggioController {

	@Autowired
	private ViaggioService viaggioService;
	@Autowired
	private AttivitaService AttivitaService;
	@Autowired
	private GuidaService guidaService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private ViaggioValidator viaggioValidator;	
	
	
	/*__________________________________________METODI PUBBLICI_____________________________________________________*/

	@GetMapping("/viaggio/{id}")
	public String getViaggio(@PathVariable("id") Long id, Model model) {
		Viaggio viaggio= viaggioService.findById(id);
		model.addAttribute("viaggio", viaggio);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo viaggio
		Guida guida= viaggio.getGuida();
		model.addAttribute("guida", guida);//metto nel model la guida che ha organizzato il viaggio
		List<Attivita> listaAttivita= viaggio.getAttivita();
		model.addAttribute("listaAttivita", listaAttivita); //metto nel model la lista di Attivita del viaggio
		return "viaggio.html"; //la vista successiva mostra i dettagli del viaggio
	}

	@GetMapping("/viaggio")
	public String getAllViaggi(Model model) {
		List<Viaggio> viaggi= viaggioService.findAll();
		model.addAttribute("viaggi", viaggi);
		return "viaggi.html";
	}
	
	
	/*___________________________________________METODI PROTECTED________________________________________________________*/

	@GetMapping("/protected/viaggio/{id}")
	public String getViaggioProtedted(@PathVariable("id") Long id, Model model) {
		Viaggio viaggio= this.viaggioService.findById(id);
		model.addAttribute("viaggio", viaggio);
		User user = getCurrentUser();
		boolean utentePartecipa= viaggio.partecipa(user);
		model.addAttribute("utentePartecipa", utentePartecipa);
		model.addAttribute("numPartecipanti", viaggio.getViaggiatori().size());
		return "viaggio.html"; //la vista successiva mostra i dettagli del viaggio
	}
	
	@PostMapping("/protected/partecipaAViaggio/{id}")
	public String partecipaAViaggio(@PathVariable("id") Long id, Model model) {
		User user = getCurrentUser();
		Viaggio viaggio= this.viaggioService.findById(id);
		/*aggiungo il viaggiatore al viaggio solo non è gia presente*/
		if (!viaggio.getViaggiatori().contains(user)) {
			viaggio.addViaggiatore(user);
			this.viaggioService.save(viaggio);    
			viaggio = this.viaggioService.findById(id);
		}
		model.addAttribute("viaggio",viaggio);
		model.addAttribute("durata",viaggio.getDurata());
		return "redirect:/protected/viaggio/" + id;
	}
	
	@PostMapping("/protected/annullaPartecipazioneAViaggio/{id}")
	public String annulaPartecipazioneAViaggio(@PathVariable("id") Long id, Model model) {
		User user = getCurrentUser();
		Viaggio viaggio= this.viaggioService.findById(id);
		if (viaggio.rimuoviViaggiatore(user)) {
			this.viaggioService.save(viaggio);
			viaggio = this.viaggioService.findById(id);
		}
		boolean utentePartecipa= viaggio.partecipa(user);
		model.addAttribute("utentePartecipa", utentePartecipa);
		model.addAttribute("viaggio",viaggio);
		model.addAttribute("durata",viaggio.getDurata());
		return "viaggio.html";
	}
	
	private User getCurrentUser () {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		User user = credentialsService.getUserDetails(username);
		return user;
	}



	/*___________________________________________METODI ADMIN_________________________________________________________*/

	@GetMapping("/admin/viaggio/{id}")
	public String getViaggioAdmin(@PathVariable("id") Long id, Model model) {
		Viaggio viaggio= viaggioService.findById(id);
		model.addAttribute("viaggio", viaggio);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo viaggio
		Guida guida= viaggio.getGuida();
		model.addAttribute("guida", guida);//metto nel model la guida che ha organizzato il viaggio
		List<Attivita> listaAttivita=viaggio.getAttivita();
		model.addAttribute("listaAttivita", listaAttivita); //metto nel model la lista di Attivita del viaggio
		User user = getCurrentUser();
		boolean utentePartecipa= viaggio.partecipa(user);
		model.addAttribute("utentePartecipa", utentePartecipa);
		return "viaggio.html"; //la vista successiva mostra i dettagli del viaggio
	}

	@GetMapping("/admin/viaggio")
	public String getAllViaggiAdmin(Model model) {
		List<Viaggio> viaggi = viaggioService.findAll();
		model.addAttribute("viaggi", viaggi);
		return "viaggi.html";
	}


	/*Questo metodo che ritorna la form, prima di ritornarla, mette nel modello un ogg viaggio appena creato*/
	@GetMapping("/admin/viaggioForm")
	public String getViaggioFormAdmin(Model model) {
		//in questo modo viaggioForm ha un ogg Viaggio a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("viaggio", new Viaggio());
		model.addAttribute("listaAttivita", AttivitaService.findAll());
		model.addAttribute("listaGuide", guidaService.findAll());
		return "viaggioForm.html"; 		
	}	

	@PostMapping("/admin/viaggio")
	public String addViaggioAdmin(@Valid @ModelAttribute("viaggio") Viaggio viaggio, BindingResult bindingResult, Model model) {
		viaggioValidator.validate(viaggio, bindingResult);//se il viaggio che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. buffet dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			viaggioService.save(viaggio);
			model.addAttribute("viaggio", viaggio);
			User user = getCurrentUser();
			boolean utentePartecipa= viaggio.partecipa(user);
			model.addAttribute("utentePartecipa", utentePartecipa);
			return "viaggio.html";
		}
		else {
			model.addAttribute("listaAttivita", AttivitaService.findAll());
			model.addAttribute("listaGuide", guidaService.findAll());
			return "viaggioForm.html";  //altrimenti ritorna alla pagina della form
		}		
	}

	@Transactional
	@PostMapping("/admin/viaggioEdited/{id}")
	public String editViaggioAdmin(@PathVariable Long id, @Valid @ModelAttribute("viaggio") Viaggio viaggio, BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {//se non ci sono errori di validazione
			Viaggio vecchioViaggio = viaggioService.findById(id);
			vecchioViaggio.setNome(viaggio.getNome());
			vecchioViaggio.setDescrizione(viaggio.getDescrizione());
			vecchioViaggio.setPartenza(viaggio.getPartenza());
			vecchioViaggio.setDurata(viaggio.getDurata());
			vecchioViaggio.setAttivita(viaggio.getAttivita());
			vecchioViaggio.setGuida(viaggio.getGuida());
			this.viaggioService.save(vecchioViaggio);
			model.addAttribute("viaggio", viaggio);
			List<Attivita> listaAttivita=viaggio.getAttivita();
			model.addAttribute("listaAttivita", listaAttivita); //metto nel model la lista di Attivita del viaggio
			User user = getCurrentUser();
			boolean utentePartecipa= viaggio.partecipa(user);
			model.addAttribute("utentePartecipa", utentePartecipa);
			List<Viaggio> viaggi = viaggioService.findAll();
			model.addAttribute("viaggi", viaggi);
			return "viaggi.html";
			//return "viaggio.html"; //pagina con viaggio appena modificato
		} 
		else {
			model.addAttribute("listaAttivita", AttivitaService.findAll());
			model.addAttribute("listaGuide", guidaService.findAll());
			return "modificaViaggioForm.html"; // ci sono errori, torna alla form iniziale
		}
	}	
	
	@GetMapping("/admin/modificaViaggio/{id}")
	public String getViaggioFormAdmin(@PathVariable Long id, Model model) {
		model.addAttribute("viaggio", viaggioService.findById(id));
		model.addAttribute("listaGuide", guidaService.findAll());
		model.addAttribute("listaAttivita", AttivitaService.findAll());
		return "modificaViaggioForm.html";
	}

	@GetMapping("/admin/toDeleteViaggio/{id}")
	public String toDeleteViaggioAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("viaggio", viaggioService.findById(id));
		return "toDeleteViaggio.html";
	}

	/*quando elimino un viaggio:
	 * - devo mettere a null il viaggio nella guida
	 * - devo mettere a null il viaggio nelle Attivita del viaggio*/
	@Transactional
	@GetMapping("/admin/deleteViaggio/{id}")
	public String deleteViaggioAdmin(@PathVariable("id")Long id, Viaggio viaggio, BindingResult bindingResult,Model model) {
		List<Guida> listaGuide= guidaService.findAll();
		for(Guida guida: listaGuide) {
			if(guida.getViaggi().contains(viaggio)) {
				guida.getViaggi().remove(viaggio);
				this.guidaService.save(guida);
			}
		}
		viaggioService.deleteViaggioById(id);
		model.addAttribute("viaggi", viaggioService.findAll());
		return "viaggi.html";
	}


	/*aggiungi al viaggio la guida che lo ha organizzato*/
	@PostMapping(value="/admin/mettiGuida/{idViaggio}/{idGuida}") 
	public String mettiGuidaInViaggio(@PathVariable("idViaggio") Long idViaggio, @PathVariable("idGuida") Long idGuida, Model model){
		Viaggio viaggio= viaggioService.findById(idViaggio);
		model.addAttribute("viaggio", viaggio);
		Guida guida= guidaService.findById(idGuida);
		model.addAttribute("guida", guida);
		viaggio.setGuida(guida);//aggiungo la guida che ha organizzato il viaggio
		guida.getViaggi().add(viaggio);//aggiungo questo viaggio alla lista di viaggi organizzati dalla guida
		this.viaggioService.save(viaggio);
		this.guidaService.save(guida);
		model.addAttribute("Attivita", viaggio.getAttivita());
		User user = getCurrentUser();
		boolean utentePartecipa= viaggio.partecipa(user);
		model.addAttribute("utentePartecipa", utentePartecipa);
		return "viaggio.html";
	}

	@PostMapping(value="/admin/toRemoveAttivita/{idViaggio}/{idAttivita}")
	public String removeAttivita(@PathVariable("idViaggio") Long idViaggio, @PathVariable("idAttivita") Long idAttivita, Model model) {
		Viaggio viaggio= viaggioService.findById(idViaggio);
		Attivita Attivita= AttivitaService.findById(idAttivita);
		viaggio.getAttivita().remove(Attivita);//rimuovo l'Attivita dalla lista di Attivita del viaggio
		this.viaggioService.save(viaggio);
		model.addAttribute("viaggio", viaggio);
		model.addAttribute("guida", viaggio.getGuida());
		model.addAttribute("Attivita", viaggio.getAttivita());
		User user = getCurrentUser();
		boolean utentePartecipa= viaggio.partecipa(user);
		model.addAttribute("utentePartecipa", utentePartecipa);
		return "viaggio.html";
	}


	@GetMapping("admin/viaggioAddAttivita/{id}")
	public String viaggioAddAttivitaAdmin(@PathVariable("id") Long id, Model model) {
		Viaggio viaggio= this.viaggioService.findById(id);
		List<Attivita> AttivitaNelViaggio= viaggio.getAttivita();
		List<Attivita> listaAttivita= this.AttivitaService.findAll();
		listaAttivita.removeAll(AttivitaNelViaggio); //ottengo tutte le Attivita meno quello presenti nel viaggio
		model.addAttribute("viaggio", viaggio);
		model.addAttribute("listaAttivita", listaAttivita);
		return "viaggioAddAttivita.html";
	}
	
	/*quando aggiungo un'Attivita al viaggio*/ 
	@PostMapping("admin/addAttivita/{idViaggio}/{idAttivita}")
	public String addAttivitaAdmin(@PathVariable("idViaggio") Long idViaggio, @PathVariable("idAttivita") Long idAttivita, Model model) {
		Viaggio viaggio= viaggioService.findById(idViaggio);
		Attivita Attivita= AttivitaService.findById(idAttivita);
		viaggio.getAttivita().add(Attivita);
		viaggioService.save(viaggio);
		model.addAttribute("guida", viaggio.getGuida());	
		List<Attivita> listaAttivita=  viaggio.getAttivita();
		model.addAttribute("listaAttivita",listaAttivita);
		model.addAttribute("viaggio", viaggio);
		User user = getCurrentUser();
		boolean utentePartecipa= viaggio.partecipa(user);
		model.addAttribute("utentePartecipa", utentePartecipa);
		return "viaggio.html";
	}






}
