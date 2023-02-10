package it.siw.viaggi.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.siw.viaggi.model.Attivita;
import it.siw.viaggi.repository.AttivitaRepository;

@Service
public class AttivitaService {

	@Autowired
	private AttivitaRepository attivitaRepository;

	@Transactional
	public void save(Attivita attivita) {
		attivitaRepository.save(attivita);
	}
	
	@Transactional
	public Attivita inserisci(Attivita attivita) {
		return attivitaRepository.save(attivita);
	}
	
	public Attivita findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return attivitaRepository.findById(id).get();
	}
	
	public Attivita findByNome(String nome) {
		return attivitaRepository.findByNome(nome);
	}
	
	public List<Attivita> findByIds(List<Long> ids) {
		var i = attivitaRepository.findAllById(ids);
		List<Attivita> listaAttivita = new ArrayList<>(); 
		for(Attivita attivita : i)
			listaAttivita.add(attivita);
		return listaAttivita;
	}
	
	public List<Attivita> findAll(){
		List<Attivita> attivita= new ArrayList<>();
		for(Attivita a: attivitaRepository.findAll()) {
			attivita.add(a);
		}
		return attivita;
	}
	
	/*bisogna verificare se un'attivita e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Attivita attivita) {
		return attivitaRepository.existsByNomeAndDescrizione(attivita.getNome(), attivita.getDescrizione());		 
	}

	@Transactional
	public void delete(Attivita attivita) {
		attivitaRepository.delete(attivita);
	}
	
	public void deleteAttivitaById(Long id) {
		attivitaRepository.deleteById(id);
	}	
}
