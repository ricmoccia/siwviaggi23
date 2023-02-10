package it.siw.viaggi.controller;

import java.util.ArrayList;
import java.util.Collections;
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

import it.siw.viaggi.controller.validator.GuidaValidator;
import it.siw.viaggi.model.Viaggio;
import it.siw.viaggi.model.Guida;
import it.siw.viaggi.service.ViaggioService;
import it.siw.viaggi.service.GuidaService;

@Controller
public class GuidaController {

	@Autowired
	private GuidaService guidaService;

	@Autowired
	private ViaggioService viaggioService;

	@Autowired
	private GuidaValidator guidaValidator;	


	@GetMapping("/guida/{id}")
	public String getGuida(@PathVariable("id") Long id, Model model) {
		Guida guida = guidaService.findById(id);
		model.addAttribute("guida", guida);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo guida
		List<Viaggio> listaViaggi= guida.getViaggi(); //ottengo tutti i viaggi per una guida
		model.addAttribute("listaViaggi", listaViaggi);
		return "guida.html"; //la vista successiva mostra i dettagli della guida
	}


	@GetMapping(value="/guide")
	public String getAllGuide(Model model) {
		List<Guida> guide= guidaService.findAll();
		model.addAttribute("guide", guide);
		return "guide.html";
	}

	@GetMapping("/orderguide")
	public String getAllOrderedGuide(Model model) {
		List<Guida> guide= guidaService.findAll();
		//Collections.sort(guide);
		model.addAttribute("guide", guide);
		model.addAttribute("numGuide", guide.size());
		return "guide.html";
	}







	/*metodi di GuidaController che riguardano l'admin*/



	@GetMapping("/admin/guida/{id}")
	public String getGuidaAdmin(@PathVariable("id") Long id, Model model) {
		Guida guida= guidaService.findById(id);
		model.addAttribute("guida", guida);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo guida
		List<Viaggio> listaViaggi= guida.getViaggi(); //ottengo tutti i viaggi per una guida
		model.addAttribute("listaViaggi", listaViaggi);
		return "admin/guida.html"; //la vista successiva mostra i dettagli della guida
	}

	@GetMapping("/admin/guide")
	public String getAllGuidesAdmin(Model model) {
		List<Guida> guide= guidaService.findAll();
		model.addAttribute("guide", guide);
		model.addAttribute("numGiude", guide.size());
		return "admin/guide.html";
	}



	@PostMapping("/admin/guida")
	public String addGuidaAdmin(@Valid @ModelAttribute("guida") Guida guida, BindingResult bindingResult, Model model) {
		guidaValidator.validate(guida, bindingResult);//se la guida che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. guida dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			guidaService.save(guida);
			model.addAttribute("guida", guida);
			List<Viaggio> listaViaggi= guida.getViaggi();
			model.addAttribute("listaViaggi", listaViaggi);
			return "/admin/guida.html"; //pagina con guida aggiunta
		}
		return "/admin/guidaForm.html";  //altrimenti ritorna alla pagina della form(ci sono stati degli errori)
	}	

	/*Questo metodo che ritorna la form, prima di ritornarla, mette nel modello un ogg guida appena creato*/
	@GetMapping("/admin/guidasForm")
	public String getGuidasForm(Model model) {
		//in questo modo guidaForm ha un ogg Guida a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("guida", new Guida());
		return "admin/guidaForm.html"; 		
	}



	@GetMapping("/admin/modificaGuida/{id}")
	public String getGuidaFormAdmin(@PathVariable Long id, Model model) { 
		Guida guida= guidaService.findById(id);
		model.addAttribute("guida", guida);
		return "admin/modificaGuidaForm.html";
	}

//	@Transactional
//	@PostMapping("/admin/modificaChef/{id}")
//	public String modificaChefAdmin(@PathVariable Long id, @Valid @ModelAttribute("chef") Chef chef, BindingResult bindingResults, Model model) {
//		if(!bindingResults.hasErrors()) {//se non ci sono stati err di validazione
//			Chef vecchioChef = chefService.findById(id);
//			vecchioChef.setId(chef.getId());
//			vecchioChef.setNome(chef.getNome());
//			vecchioChef.setCognome(chef.getCognome());
//			vecchioChef.setNazionalita(chef.getNazionalita());
//			this.chefService.save(vecchioChef);
//			model.addAttribute("chef", chef);
//			return "admin/chef.html";//pagina con lo chef modificato
//		} 
//		return "admin/modificaChefForm.html";//se ci sono stati degli errori ritorna alla pagina per la modifica dello chef
//	}
	
	@Transactional
	@PostMapping("/admin/guidaEdited/{id}")
	public String editedGuida(@PathVariable Long id, @Valid @ModelAttribute("guida") Guida guida, BindingResult bindingResults, Model model) {

		Guida vecchiaGuida = guidaService.findById(id);

		if(!guida.getNome().equals(vecchiaGuida.getNome()))
			guidaValidator.validate(guida, bindingResults);

		if(!bindingResults.hasErrors()) {

			vecchiaGuida.setId(guida.getId());
			vecchiaGuida.setNome(guida.getNome());
			vecchiaGuida.setCognome(guida.getCognome());
			vecchiaGuida.setNazionalita(guida.getNazionalita());
			vecchiaGuida.setViaggi(guida.getViaggi());
			this.guidaService.save(vecchiaGuida);
			model.addAttribute("guidas", vecchiaGuida);
			return "admin/guida.html";
		}
			return "admin/modificaGuidaForm.html";
	}
		


	@GetMapping("/admin/toDeleteGuida/{id}")
	public String toDeleteGuidaAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("guida", guidaService.findById(id));
		return "admin/toDeleteGuida.html";
	}

	/*QUANDO ELIMINO UNA GUIDA:
	 * -devo mettere a null la guida nei viaggi della guida
	 */
	//	@Transactional
	//	@GetMapping("/admin/deleteChef/{id}")
	//	public String deleteChefAdmin(@PathVariable("id")Long id, Chef chef, BindingResult bindingResult,Model model) {
	//		List<Buffet> listaBuffet= (List<Buffet>) buffetService.findAll();
	//		for(Buffet buffet: listaBuffet) {
	//			if(buffet.getChef().equals(chef)) {
	//				buffet.setChef(null);
	//				buffetService.save(buffet);
	//			}
	//		}
	//		chefService.deleteChefById(id);
	//		model.addAttribute("chefs", chefService.findAll());
	//		return "admin/chefs.html";
	//	}
	@GetMapping(value="/admin/deleteGuida/{id}")
	public String deleteGuida(@PathVariable("id") Long id, Model model) {
		Guida guida= this.guidaService.findById(id);
		List<Viaggio> elencoViaggi= guida.getViaggi();
		for(Viaggio viaggio: elencoViaggi) {
			viaggio.setGuida(null);
			this.viaggioService.save(viaggio);
		}
		guidaService.deleteGuidaById(id);
		//model.addAttribute("numGuide", guidaService.count());
		List<Guida> guide= this.guidaService.findAll();
		model.addAttribute("guide", guide);
		return "admin/guide.html";
	}




	@PostMapping("/admin/deleteViaggio/{id}")
	public String deleteViaggio(@PathVariable("id") Long id, Model model) {
		Viaggio viaggio= this.viaggioService.findById(id);
		Guida guida= viaggio.getGuida();
		viaggio.setGuida(null);
		guida.getViaggi().remove(viaggio);
		model.addAttribute("guida", guida);
		List<Viaggio> listaViaggi= guida.getViaggi();
		model.addAttribute("listaViaggi", listaViaggi);
		this.viaggioService.save(viaggio);
		this.guidaService.save(guida);
		return "admin/guida.html";
	}


	/*quando aggiungo alla guida un viaggio, devo modificare il viaggio(adesso ha una guida) e passo la lista dei viaggi della guida*/
	@PostMapping("admin/addViaggio/{idGuida}/{idViaggio}")
	public String addViaggio(@PathVariable("idGuida") Long idGuida, @PathVariable("idViaggio") Long idViaggio, Model model) {
		Guida guida= this.guidaService.findById(idGuida);
		Viaggio viaggio= this.viaggioService.findById(idViaggio);
		viaggio.setGuida(guida);
		this.viaggioService.save(viaggio);
		model.addAttribute("guida", guida);
		List<Viaggio> listaViaggi= guida.getViaggi();
		model.addAttribute("listaViaggi", listaViaggi);
		return "admin/guida.html";
	}

	@GetMapping("admin/guidaAddViaggio/{id}")
	public String guidaAddViaggioAdmin(@PathVariable("id") Long id, Model model) {
		Guida guida= guidaService.findById(id);
		model.addAttribute("guida", guida);
		/*bisogna passare nel model i viaggi che non hanno una guida assegnata(elimino dalla lista di viaggi quelli con guida!=null) */
		List<Viaggio> listaViaggi= new ArrayList<>();
		for(Viaggio viaggio:viaggioService.findAll()) {
			if(viaggio.getGuida()==null)
				listaViaggi.add(viaggio);
		}
		model.addAttribute("listaViaggi", listaViaggi);
		return "admin/guidaAddViaggio.html";
	}











}
