package it.siw.viaggi.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.siw.viaggi.controller.validator.MaterialeValidator;
import it.siw.viaggi.model.Attivita;
import it.siw.viaggi.model.Materiale;
import it.siw.viaggi.service.AttivitaService;
import it.siw.viaggi.service.MaterialeService;

@Controller
public class MaterialeController {
	@Autowired
	private MaterialeService materialeService;
	@Autowired
	private AttivitaService AttivitaService;
	@Autowired
	private MaterialeValidator materialeValidator;	


    /*__________________________________________METODI PUBBLICI_____________________________________________________*/

	@GetMapping("/materiale/{id}")
	public String getMateriale(@PathVariable("id") Long id, Model model) {
		Materiale materiale = materialeService.findById(id);
		model.addAttribute("materiale", materiale);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo materiale
		return "materiale.html"; //la vista successiva mostra i dettagli del materiale
	}

	@GetMapping("/materiali")
	public String getAllMateriali(Model model) {
		List<Materiale> listaMateriali= materialeService.findAll();//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo materiale
		model.addAttribute("materiali", listaMateriali);
		return "materiali.html";
	}
	
	/*___________________________________________METODI PROTECTED________________________________________________________*/

	@GetMapping("/protected/materiale/{id}")
	public String getMaterialeProtedted(@PathVariable("id") Long id, Model model) {
		model.addAttribute("materiale", this.materialeService.findById(id));
		return "materiale.html"; //la vista successiva mostra i dettagli del materiale
	}




	/*___________________________________________METODI ADMIN_________________________________________________________*/

	@GetMapping("/admin/materiale/{id}")
	public String getMaterialeAdmin(@PathVariable("id") Long id, Model model) {
		Materiale materiale = materialeService.findById(id);
		model.addAttribute("materiale", materiale);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo materiale
		return "materiale.html"; //la vista successiva mostra i dettagli del materiale
	}

	@GetMapping("/admin/materiale")
	public String getAllMaterialiAdmin(Model model) {
		List<Materiale> listaMateriali= materialeService.findAll();
		model.addAttribute("materiali", listaMateriali);
		return "materiali.html";
	}




	@PostMapping("/admin/materiale")
	public String addMaterialeAdmin(@Valid @ModelAttribute("materiale") Materiale materiale, BindingResult bindingResult, Model model,
			@RequestParam("image") MultipartFile multipartFile) throws IOException{
		materialeValidator.validate(materiale, bindingResult);//se il materiale che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. materiale dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        materiale.setPhotos(fileName);
			
			Materiale savedMateriale= materialeService.save(materiale);
			
			String uploadDir = "user-photos/" + savedMateriale.getId();
			 
	        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
			model.addAttribute("materiale", materiale);
			return "materiale.html";
		}
		return "materialeForm.html";  //altrimenti ritorna alla pagina della form
	}


	@Transactional
	@PostMapping("/admin/materialeEdited/{id}")
	public String editedMateriale(@PathVariable Long id, @Valid @ModelAttribute("materiale") Materiale materiale, BindingResult bindingResult, Model model) {

		Materiale oldMateriale = materialeService.findById(id);

		if(!materiale.getNome().equals(oldMateriale.getNome()))
			materialeValidator.validate(materiale, bindingResult);

		if(!bindingResult.hasErrors()) {
			oldMateriale.setNome(materiale.getNome());			
			oldMateriale.setDescrizione(materiale.getDescrizione());
			oldMateriale.setPrezzo(materiale.getPrezzo());

			materialeService.save(oldMateriale);
			model.addAttribute("materiale", oldMateriale);
			return "materiale.html";
		}
		return "modificaMaterialeForm.html";
	}

	@GetMapping("/admin/modificaMateriale/{id}")
	public String editMateriale(@PathVariable("id") Long id, Model model) {
		Materiale materiale = materialeService.findById(id);
		model.addAttribute("materiale", materiale);
		return "modificaMaterialeForm.html";
	}


	@GetMapping("/admin/toDeleteMateriale/{id}")
	public String toDeleteMaterialeAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("materiale", materialeService.findById(id));
		return "toDeleteMateriale.html";
	}

	
	@GetMapping(value="/admin/deleteMateriale/{id}")
	public String deleteMateriale(@PathVariable("id") Long id, Model model) {
		Materiale materiale = this.materialeService.findById(id);
		List<Attivita> activities= this.AttivitaService.findAll();
		/*se le Attivita contengono il materiale, allora rimuovo il materiale da essi*/
		for(Attivita Attivita : activities) {
			if(Attivita.getMateriali().contains(materiale)) {
				Attivita.getMateriali().remove(materiale);
				this.AttivitaService.save(Attivita);
			}
		}

		materialeService.deleteById(id);
		List<Materiale> listaMateriali = this.materialeService.findAll();
		model.addAttribute("materiali", listaMateriali);
		return "materiali.html";
	}


	@GetMapping("/admin/materialeForm")
	public String getMaterialeFormAdmin(Model model) {
		model.addAttribute("materiale", new Materiale());
		return "materialeForm.html";
	}




}
