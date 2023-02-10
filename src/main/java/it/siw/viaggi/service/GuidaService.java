package it.siw.viaggi.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.siw.viaggi.model.Guida;
import it.siw.viaggi.repository.GuidaRepository;

@Service
public class GuidaService {

	@Autowired  //con autowired carica da solo un'istanza di guidaRepository
	private GuidaRepository guidaRepository;

	/*e un'operazione transazionale, aggiorno dati nel database*/
	@Transactional       //con l'annotazione ci pensa springboot ad aprire e chiudere la transazione
	public Guida save(Guida guida) {
		 return guidaRepository.save(guida);
	}
	
	@Transactional
	public Guida inserisci(Guida guida) {
		return guidaRepository.save(guida);
	}

	public Guida findById(Long id) {
		return guidaRepository.findById(id).get();  //quando un metodo ritorna un optional devo usare get per farmi ritornare l'oggetto
	}

	public List<Guida> findAll(){
		List<Guida> guide= new ArrayList<>();
		for(Guida g: guidaRepository.findAll()) {
			guide.add(g);
		}
		return guide;
	}

	/*bisogna verificare se una guida e gia nel database, devo chiedere al repository*/
	public boolean alreadyExists(Guida guida) {
		return guidaRepository.existsByNomeAndCognomeAndNazionalita(guida.getNome(), guida.getCognome(), guida.getNazionalita());		 
	}

	@Transactional
	public void delete(Guida guida) {
		guidaRepository.delete(guida);
	}

	public void deleteGuidaById(Long id) {
		guidaRepository.deleteById(id);
	}
	
	public long countGuide() {
		return guidaRepository.count();
	}
	
	public List<Guida> ordinaGuidePerNomeCrescente(){
		return guidaRepository.findAllByOrderByNomeAsc();
	}
	
	public List<Guida> duePersoneOrdineAlfabetico(){
		return guidaRepository.findFirst2ByOrderByNomeAsc();
	}


}
