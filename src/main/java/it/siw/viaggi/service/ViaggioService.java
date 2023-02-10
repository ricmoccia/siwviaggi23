package it.siw.viaggi.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.siw.viaggi.model.Viaggio;
import it.siw.viaggi.model.Guida;
import it.siw.viaggi.repository.ViaggioRepository;

@Service
public class ViaggioService {

	@Autowired
	private ViaggioRepository viaggioRepository;

	@Transactional
	public void save(Viaggio viaggio) {
		viaggioRepository.save(viaggio);
	}
	
	@Transactional
	public Viaggio inserisci (Viaggio viaggio) {
		return viaggioRepository.save(viaggio);
	}
	
	public Viaggio findById(Long id) {
		//quando uso un metodo optional, devo usare get() per farmi ritornare l'oggetto
		return viaggioRepository.findById(id).get();
	}
	
	public Viaggio findByNome(String nome) {
		return viaggioRepository.findByNome(nome);
	}
	
	public List<Viaggio> findByGuida(Guida guida) {
		return viaggioRepository.findByGuida(guida);
	}
	
	public List<Viaggio> findAll(){
		List<Viaggio> viaggi= new ArrayList<>();
		for(Viaggio v: viaggioRepository.findAll()) {
			viaggi.add(v);
		}
		return viaggi;
	}
	
	/*bisogna verificare se un viaggio e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Viaggio viaggio) {
		return viaggioRepository.existsByNomeAndDescrizione(viaggio.getNome(), viaggio.getDescrizione());		 
	}

	@Transactional
	public void delete(Viaggio viaggio) {
		viaggioRepository.delete(viaggio);
	}
	
	@Transactional
	public void deleteViaggioById (Long id) {
		viaggioRepository.deleteById(id);
	}
	
	public void deleteGuidaById(Long id) {
		viaggioRepository.deleteById(id);
	}	
}
