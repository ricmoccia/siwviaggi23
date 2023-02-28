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
	private AttivitaRepository AttivitaRepository;

	@Transactional
	public void save(Attivita Attivita) {
		AttivitaRepository.save(Attivita);
	}
	
	@Transactional
	public Attivita inserisci(Attivita Attivita) {
		return AttivitaRepository.save(Attivita);
	}
	
	public Attivita findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return AttivitaRepository.findById(id).get();
	}
	
	public Attivita findByNome(String nome) {
		return AttivitaRepository.findByNome(nome);
	}
	
	public List<Attivita> findByIds(List<Long> ids) {
		var i = AttivitaRepository.findAllById(ids);
		List<Attivita> listaAttivita = new ArrayList<>(); 
		for(Attivita Attivita : i)
			listaAttivita.add(Attivita);
		return listaAttivita;
	}
	
	public List<Attivita> findAll(){
		List<Attivita> Attivita= new ArrayList<>();
		for(Attivita a: AttivitaRepository.findAll()) {
			Attivita.add(a);
		}
		return Attivita;
	}
	
	/*bisogna verificare se un'Attivita e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Attivita Attivita) {
		return AttivitaRepository.existsByNomeAndDescrizione(Attivita.getNome(), Attivita.getDescrizione());		 
	}

	@Transactional
	public void delete(Attivita Attivita) {
		AttivitaRepository.delete(Attivita);
	}
	
	public void deleteAttivitaById(Long id) {
		AttivitaRepository.deleteById(id);
	}

	public long count() {
		return this.AttivitaRepository.count();
	}	
}
