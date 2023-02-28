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
	private AttivitaService AttivitaService;
	@Autowired
	private ViaggioService viaggioService;
	@Autowired
	private MaterialeService materialeService;
	@Autowired
	private AttivitaValidator AttivitaValidator;	

	/*__________________________________________METODI PUBBLICI_____________________________________________________*/

	@GetMapping("/attivita/{id}")
	public String getAttivita(@PathVariable("id") Long id, Model model) {
		Attivita Attivita = AttivitaService.findById(id);
		model.addAttribute("attivita", Attivita);
		model.addAttribute("listaViaggi", Attivita.getViaggi());
		model.addAttribute("listaMateriale", Attivita.getMateriali());
		return "attivita.html"; //la vista successiva mostra i dettagli dell'Attivita
	}

	@GetMapping("/attivita")
	public String getAllAttivita(Model model) {
		List<Attivita> listaAttivita= AttivitaService.findAll();
		model.addAttribute("listaAttivita", listaAttivita);
		model.addAttribute("numAttivita", AttivitaService.count());
		return "activities.html";
	}


	/*___________________________________________METODI PROTECTED________________________________________________________*/

	@GetMapping("/protected/attivita/{id}")
	public String getAttivitaProtedted(@PathVariable("id") Long id, Model model) {
		model.addAttribute("attivita", this.AttivitaService.findById(id));
		return "attivita.html"; //la vista successiva mostra i dettagli dell'attivita
	}


	/*___________________________________________METODI ADMIN_________________________________________________________*/

	@GetMapping("/admin/attivita/{id}")
	public String getAttivitaAdmin(@PathVariable("id") Long id, Model model) {
		Attivita Attivita = AttivitaService.findById(id);
		model.addAttribute("Attivita", Attivita);
		model.addAttribute("listaViaggi", Attivita.getViaggi());
		model.addAttribute("listaMateriali", Attivita.getMateriali());
		return "attivita.html"; //la vista successiva mostra i dettagli dell'Attivita
	}

	@GetMapping("/admin/attivita")
	public String getAllActivitiesAdmin(Model model) {
		List<Attivita> listaAttivita= AttivitaService.findAll();
		model.addAttribute("listaAttivita", listaAttivita);
		return "activities.html";
	}

	/*Questo metodo ritorna la form, prima di ritornarla, mette nel modello un ogg Attivita appena creato*/
	@GetMapping("/admin/attivitaForm")
	public String getAttivitaFormAdmin(Model model) {
		//in questo modo AttivitaForm ha un ogg Attivita a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("attivita", new Attivita());
		model.addAttribute("materiali", materialeService.findAll());
		return "attivitaForm.html"; 		
	}	


	@PostMapping("/admin/attivita")
	public String addAttivitaAdmin(@Valid @ModelAttribute("Attivita") Attivita Attivita, BindingResult bindingResult, Model model) {
		AttivitaValidator.validate(Attivita, bindingResult);//se l'Attivita che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. attivita dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			AttivitaService.save(Attivita);
			model.addAttribute("attivita", Attivita);
			return "attivita.html";
		}
		else {
			List<Materiale> listaMateriali= materialeService.findAll();
			model.addAttribute("listaMateriali", listaMateriali);
			return "attivitaForm.html";  //altrimenti ritorna alla pagina della form
		}
	}


	@Transactional
	@PostMapping("/admin/attivitaEdited/{id}")
	public String modificaAttivitaAdmin(@PathVariable Long id, @Valid @ModelAttribute("Attivita") Attivita Attivita, BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {
			Attivita vecchiaAttivita = AttivitaService.findById(id);
			vecchiaAttivita.setNome(Attivita.getNome());
			vecchiaAttivita.setDescrizione(Attivita.getDescrizione());
			vecchiaAttivita.setDurata(Attivita.getDurata());
			vecchiaAttivita.setMateriali(Attivita.getMateriali());
			this.AttivitaService.save(vecchiaAttivita);
			model.addAttribute("attivita", Attivita);
			return "attivita.html";
		} 
		else {
			model.addAttribute("listaMateriali", materialeService.findAll());
			return "modificaAttivitaForm.html";
		}
	}	
	@GetMapping("/admin/modificaAttivita/{id}")
	public String getAttivitaFormAdmin(@PathVariable Long id, Model model) {
		model.addAttribute("attivita", AttivitaService.findById(id));
		model.addAttribute("materiali", materialeService.findAll());
		return "modificaAttivitaForm.html";
	}

	@GetMapping("/admin/toDeleteAttivita/{id}")
	public String toDeleteAttivitaAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("attivita", AttivitaService.findById(id));
		model.addAttribute("attivita", AttivitaService.findById(id));
		return "toDeleteAttivita.html";
	}

	/*quando elimino un'Attivita, la devo eliminare anche dalla lista di Attivita nel viaggio*/
	@Transactional
	@GetMapping(value="/admin/deleteAttivita/{id}")
	public String deleteViaggio(@PathVariable("id") Long id, Model model) {
		Attivita Attivita= this.AttivitaService.findById(id);
		List<Viaggio> elencoViaggi= Attivita.getViaggi();
		for(Viaggio viaggio: elencoViaggi) {
			if(viaggio.getAttivita().contains(Attivita)) {
				viaggio.getAttivita().remove(Attivita);
				this.viaggioService.save(viaggio);
			}
		}
		AttivitaService.deleteAttivitaById(id);
		List<Attivita> listaAttivita= this.AttivitaService.findAll();
		model.addAttribute("listaAttivita", listaAttivita);
		return "activities.html";
	}



	@GetMapping("admin/attivitaAddMateriale/{id}")
	public String attivitaAddMaterialeAdmin(@PathVariable("id") Long id, Model model) {
		Attivita Attivita= this.AttivitaService.findById(id);
		List<Materiale> materialiNellAttivita= Attivita.getMateriali();
		List<Materiale> listaMateriali= this.materialeService.findAll();
		listaMateriali.removeAll(materialiNellAttivita); //ottengo tutti i materiali meno quelli presenti nell'Attivita
		model.addAttribute("attivita", Attivita);
		model.addAttribute("listaMateriale", listaMateriali);
		return "attivitaAddMateriale.html";
	}
	
	/*aggiungo un materiale all' Attivita*/ 
	@PostMapping("admin/addMateriale/{idAttivita}/{idMateriale}")
	public String addMaterialeAdmin(@PathVariable("idAttivita") Long idAttivita, @PathVariable("idMateriale") Long idIMateriale, Model model) {
		Attivita Attivita = this.AttivitaService.findById(idAttivita);
		model.addAttribute("attivita", Attivita);
		Materiale materiale= this.materialeService.findById(idIMateriale);
		if(!Attivita.getMateriali().contains(materiale)) {
			Attivita.getMateriali().add(materiale);
		}
		AttivitaService.save(Attivita);
		List <Materiale> listaMateriali= Attivita.getMateriali();
		model.addAttribute("listaMateriali", listaMateriali);
		return "attivita.html";
	}







}
