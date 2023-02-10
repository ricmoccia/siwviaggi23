package it.siw.viaggi.controller;

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

import it.siw.viaggi.controller.validator.AttivitaValidator;
import it.siw.viaggi.model.Viaggio;
import it.siw.viaggi.model.Guida;
import it.siw.viaggi.model.Materiale;
import it.siw.viaggi.model.Attivita;
import it.siw.viaggi.service.ViaggioService;
import it.siw.viaggi.service.MaterialeService;
import it.siw.viaggi.service.AttivitaService;

@Controller
public class AttivitaController {

	@Autowired
	private AttivitaService attivitaService;

	@Autowired
	private ViaggioService viaggioService;

	@Autowired
	private MaterialeService materialeService;

	@Autowired
	private AttivitaValidator attivitaValidator;	




	@GetMapping("/attivita/{id}")
	public String getAttivita(@PathVariable("id") Long id, Model model) {
		Attivita attivita = attivitaService.findById(id);
		model.addAttribute("attivita", attivita);
		model.addAttribute("listaViaggi", attivita.getViaggi());
		model.addAttribute("listaMateriale", attivita.getMateriali());
		return "attivita.html"; //la vista successiva mostra i dettagli dell'attivita
	}

	@GetMapping("/attivita")
	public String getAllAttivita(Model model) {
		List<Attivita> listaAttivita= attivitaService.findAll();
		model.addAttribute("listaAttivita", listaAttivita);
		return "activities.html";
	}




	/*metodi di AttivitaController relativi all'admin*/



	@GetMapping("/admin/attivita/{id}")
	public String getAttivitaAdmin(@PathVariable("id") Long id, Model model) {
		Attivita attivita = attivitaService.findById(id);
		model.addAttribute("attivita", attivita);
		model.addAttribute("listaViaggi", attivita.getViaggi());
		model.addAttribute("listaMateriali", attivita.getMateriali());
		return "admin/attivita.html"; //la vista successiva mostra i dettagli dell'attivita
	}

	@GetMapping("/admin/attivita")
	public String getAllActivitiesAdmin(Model model) {
		List<Attivita> listaAttivita= attivitaService.findAll();
		model.addAttribute("listaAttivita", listaAttivita);
		return "admin/activities.html";
	}

	/*Questo metodo ritorna la form, prima di ritornarla, mette nel modello un ogg attivita appena creato*/
	@GetMapping("/admin/attivitaForm")
	public String getAttivitaFormAdmin(Model model) {
		//in questo modo attivitaForm ha un ogg Attivita a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("attivita", new Attivita());
		return "admin/attivitaForm.html"; 		
	}	


	@PostMapping("/admin/attivita")
	public String addAttivitaAdmin(@Valid @ModelAttribute("attivita") Attivita attivita, BindingResult bindingResult, Model model) {
		attivitaValidator.validate(attivita, bindingResult);//se l'attivita che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. persona dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			attivitaService.save(attivita);
			model.addAttribute("attivita", attivita);
			return "admin/attivita.html";
		}
		else {
			List<Materiale> listaMateriali= materialeService.findAll();
			model.addAttribute("listaMateriali", listaMateriali);
			return "admin/attivitaForm.html";  //altrimenti ritorna alla pagina della form
		}
	}


	@Transactional
	@PostMapping("/admin/attivitaEdited/{id}")
	public String modificaAttivitaAdmin(@PathVariable Long id, @Valid @ModelAttribute("attivita") Attivita attivita, BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {
			Attivita vecchiaAttivita = attivitaService.findById(id);
			vecchiaAttivita.setNome(attivita.getNome());
			vecchiaAttivita.setDescrizione(attivita.getDescrizione());
			vecchiaAttivita.setDurata(attivita.getDurata());
			this.attivitaService.save(vecchiaAttivita);
			model.addAttribute("attivita", attivita);
			return "admin/attivita.html";
		} 
		else {
			model.addAttribute("listaMateriali", materialeService.findAll());
			return "admin/modificaAttivitaForm.html";
		}
	}	
	@GetMapping("/admin/modificaAttivita/{id}")
	public String getAttivitaFormAdmin(@PathVariable Long id, Model model) {
		model.addAttribute("attivita", attivitaService.findById(id));
		return "admin/modificaAttivitaForm.html";
	}

	@GetMapping("/admin/toDeleteAttivita/{id}")
	public String toDeleteAttivitaAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("attivita", attivitaService.findById(id));
		return "admin/toDeleteAttivita.html";
	}

	/*quando elimino un'attivita, la devo eliminare anche dalla lista di attivita nel viaggio*/
	@Transactional
	@GetMapping(value="/admin/deleteAttivita/{id}")
	public String deleteViaggio(@PathVariable("id") Long id, Model model) {
		Attivita attivita= this.attivitaService.findById(id);
		List<Viaggio> elencoViaggi= attivita.getViaggi();
		for(Viaggio viaggio: elencoViaggi) {
			if(viaggio.getAttivita().contains(attivita)) {
				viaggio.getAttivita().remove(attivita);
				this.viaggioService.save(viaggio);
			}
		}
		attivitaService.deleteAttivitaById(id);
		List<Attivita> listaAttivita= this.attivitaService.findAll();
		model.addAttribute("listaAttivita", listaAttivita);
		return "admin/activities.html";
	}






	@GetMapping("admin/attivitaAddMateriale/{id}")
	public String attivitaAddIngredienteAdmin(@PathVariable("id") Long id, Model model) {
		Attivita attivita= this.attivitaService.findById(id);
		List<Materiale> materialiNellAttivita= attivita.getMateriali();
		List<Materiale> listaMateriali= this.materialeService.findAll();
		listaMateriali.removeAll(materialiNellAttivita); //ottengo tutti i materiali meno quelli presenti nell'attivita
		model.addAttribute("attivita", attivita);
		model.addAttribute("listaMateriale", listaMateriali);
		return "admin/attivitaAddMateriale.html";
	}
	
	/*aggiungo un materiale al piatto*/ 
	@PostMapping("admin/addMateriale/{idAttivita}/{idMateriale}")
	public String addMaterialeAdmin(@PathVariable("idAttivita") Long idAttivita, @PathVariable("idMateriale") Long idIMateriale, Model model) {
		Attivita attivita = this.attivitaService.findById(idAttivita);
		model.addAttribute("attivita", attivita);
		Materiale materiale= this.materialeService.findById(idIMateriale);
		if(!attivita.getMateriali().contains(materiale)) {
			attivita.getMateriali().add(materiale);
		}
		attivitaService.save(attivita);
		List <Materiale> listaMateriali= attivita.getMateriali();
		model.addAttribute("listaMateriali", listaMateriali);
		return "admin/attivita.html";
	}







}
