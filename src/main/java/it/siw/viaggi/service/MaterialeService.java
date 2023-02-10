package it.siw.viaggi.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.siw.viaggi.model.Materiale;
import it.siw.viaggi.repository.MaterialeRepository;

@Service
public class MaterialeService {

	@Autowired
	private MaterialeRepository materialeRepository;

	@Transactional
	public void save(Materiale materiale) {
		materialeRepository.save(materiale);
	}
	
	@Transactional
	public Materiale inserisci(Materiale materiale) {
		return materialeRepository.save(materiale);
	}
	
	public Materiale findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return materialeRepository.findById(id).get();
	}
	
	public List<Materiale> findByNomeOrDescrizione(String nome, String descrizione) {
		return materialeRepository.findByNomeOrDescrizione(nome, descrizione);
	}
	
	public List<Materiale> findAll(){
		List<Materiale> materiali= new ArrayList<>();
		for(Materiale m: materialeRepository.findAll()) {
			materiali.add(m);
		}
		return materiali;
	}
	
	/*bisogna verificare se un materiale e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Materiale materiale) {
		return materialeRepository.existsByNomeAndDescrizione(materiale.getNome(), materiale.getDescrizione());		 
	}

	@Transactional
	public void delete(Materiale materiale) {
		materialeRepository.delete(materiale);
	}
	
	public void deleteMaterialeById(Long id) {
		materialeRepository.deleteById(id);
	}

	public void deleteById(Long id) {
		this.materialeRepository.deleteById(id);		
	}	

}
