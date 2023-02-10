package it.siw.viaggi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import it.siw.viaggi.service.GuidaService;
import it.siw.viaggi.service.AttivitaService;

@Controller
public class ViaggioController {

	@Autowired
	private ViaggioService viaggioService;

	@Autowired
	private AttivitaService attivitaService;

	@Autowired
	private GuidaService guidaService;

	@Autowired
	private ViaggioValidator viaggioValidator;	




	@GetMapping("/viaggio/{id}")
	public String getViaggio(@PathVariable("id") Long id, Model model) {
		Viaggio viaggio= viaggioService.findById(id);
		model.addAttribute("viaggio", viaggio);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo viaggio
		Guida guida= viaggio.getGuida();
		model.addAttribute("guida", guida);//metto nel model la guida che ha organizzato il viaggio
		List<Attivita> listaAttivita= viaggio.getAttivita();
		model.addAttribute("listaAttivita", listaAttivita); //metto nel model la lista di attivita del viaggio
		return "viaggio.html"; //la vista successiva mostra i dettagli del viaggio
	}

	@GetMapping("/viaggio")
	public String getAllViaggi(Model model) {
		List<Viaggio> viaggi= viaggioService.findAll();
		model.addAttribute("viaggi", viaggi);
		return "viaggi.html";
	}







	/*metodi di ViaggioController relativi all'admin*/


	@GetMapping("/admin/viaggio/{id}")
	public String getViaggioAdmin(@PathVariable("id") Long id, Model model) {
		Viaggio viaggio= viaggioService.findById(id);
		model.addAttribute("viaggio", viaggio);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo viaggio
		Guida guida= viaggio.getGuida();
		model.addAttribute("guida", guida);//metto nel model la guida che ha organizzato il viaggio
		List<Attivita> listaAttivita=viaggio.getAttivita();
		model.addAttribute("listaAttivita", listaAttivita); //metto nel model la lista di attivita del viaggio
		return "admin/viaggio.html"; //la vista successiva mostra i dettagli del viaggio
	}

	@GetMapping("/admin/viaggio")
	public String getAllViaggiAdmin(Model model) {
		List<Viaggio> viaggi = viaggioService.findAll();
		model.addAttribute("viaggi", viaggi);
		return "admin/viaggi.html";
	}


	/*Questo metodo che ritorna la form, prima di ritornarla, mette nel modello un ogg viaggio appena creato*/
	@GetMapping("/admin/viaggioForm")
	public String getVaiggioFormAdmin(Model model) {
		//in questo modo viaggioForm ha un ogg Viaggio a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("viaggio", new Viaggio());
		return "admin/viaggioForm.html"; 		
	}	

	@PostMapping("/admin/viaggio")
	public String addViaggioAdmin(@Valid @ModelAttribute("viaggio") Viaggio viaggio, BindingResult bindingResult, Model model) {
		viaggioValidator.validate(viaggio, bindingResult);//se il viaggio che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. buffet dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			viaggioService.save(viaggio);
			model.addAttribute("viaggio", viaggio);
			return "admin/viaggio.html";
		}
		else {
			model.addAttribute("listaAttivita", attivitaService.findAll());
			model.addAttribute("listaGuide", guidaService.findAll());
			return "admin/viaggioForm.html";  //altrimenti ritorna alla pagina della form
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
			vecchioViaggio.setGuida(viaggio.getGuida());
			vecchioViaggio.setAttivita(viaggio.getAttivita());
			this.viaggioService.save(vecchioViaggio);
			model.addAttribute("viaggio", viaggio);
			List<Attivita> listaAttivita=viaggio.getAttivita();
			model.addAttribute("listaAttivita", listaAttivita); //metto nel model la lista di attivita del viaggio
			return "admin/viaggio.html"; //pagina con viaggio appena modificato
		} 
		else {
			model.addAttribute("listaAttivita", attivitaService.findAll());
			model.addAttribute("listaGuide", guidaService.findAll());
			return "admin/modificaViaggioForm.html"; // ci sono errori, torna alla form iniziale
		}
	}	
	
	@GetMapping("/admin/modificaViaggio/{id}")
	public String getViaggioFormAdmin(@PathVariable Long id, Model model) {
		model.addAttribute("viaggio", viaggioService.findById(id));
		model.addAttribute("listaGuide", guidaService.findAll());
		model.addAttribute("listaAttivita", attivitaService.findAll());
		return "admin/modificaViaggioForm.html";
	}

	@GetMapping("/admin/toDeleteViaggio/{id}")
	public String toDeleteViaggioAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("viaggio", viaggioService.findById(id));
		return "admin/toDeleteViaggio.html";
	}

	/*quando elimino un viaggio:
	 * - devo mettere a null il viaggio nella guidas
	 * - devo mettere a null il viaggio nelle attivita del viaggio*/
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
		guidaService.deleteGuidaById(id);
		model.addAttribute("viaggi", viaggioService.findAll());
		return "admin/viaggi.html";
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
		model.addAttribute("attivita", viaggio.getAttivita());
		return "admin/viaggio.html";
	}

	@PostMapping(value="/admin/toRemoveAttivita/{idViaggio}/{idAttivita}")
	public String removeAttivita(@PathVariable("idViaggio") Long idViaggio, @PathVariable("idAttivita") Long idAttivita, Model model) {
		Viaggio viaggio= viaggioService.findById(idViaggio);
		Attivita attivita= attivitaService.findById(idAttivita);
		viaggio.getAttivita().remove(attivita);//rimuovo l'attivita dalla lista di attivita del viaggio
		this.viaggioService.save(viaggio);
		model.addAttribute("viaggio", viaggio);
		model.addAttribute("guida", viaggio.getGuida());
		model.addAttribute("attivita", viaggio.getAttivita());
		return "admin/viaggio.html";
	}


	@GetMapping("admin/viaggioAddAttivita/{id}")
	public String viaggioAddAttivitaAdmin(@PathVariable("id") Long id, Model model) {
		Viaggio viaggio= this.viaggioService.findById(id);
		List<Attivita> attivitaNelViaggio= viaggio.getAttivita();
		List<Attivita> listaAttivita= this.attivitaService.findAll();
		listaAttivita.removeAll(attivitaNelViaggio); //ottengo tutte le attivita meno quello presenti nel viaggio
		model.addAttribute("viaggio", viaggio);
		model.addAttribute("listaAttivita", listaAttivita);
		return "admin/viaggioAddAttivita.html";
	}
	
	/*quando aggiungo un'attivita al viaggio*/ 
	@PostMapping("admin/addAttivita/{idViaggio}/{idAttivita}")
	public String addAttivitaAdmin(@PathVariable("idViaggio") Long idViaggio, @PathVariable("idAttivita") Long idAttivita, Model model) {
		Viaggio viaggio= viaggioService.findById(idViaggio);
		Attivita attivita= attivitaService.findById(idAttivita);
		viaggio.getAttivita().add(attivita);
		viaggioService.save(viaggio);
		model.addAttribute("guida", viaggio.getGuida());	
		List<Attivita> listaAttivita=  viaggio.getAttivita();
		model.addAttribute("listaAttivita",listaAttivita);
		model.addAttribute("viaggio", viaggio);
		return "admin/viaggio.html";
	}











}
