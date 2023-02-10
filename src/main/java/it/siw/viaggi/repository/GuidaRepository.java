package it.siw.viaggi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.siw.viaggi.model.Guida;

public interface GuidaRepository extends CrudRepository<Guida, Long> {
	
	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndCognomeAndNazionalita(String nome, String cognome, String nazionalita);
	
	/*permette di fare la cancellazione prendendo come parametro un id*/
	public void deleteById(Long id);
	
	/*prende tutte le guide in ordine crescente di nome(lessicografico)*/
	public List<Guida> findAllByOrderByNomeAsc();
	
	/*ordina le guide per nome crescente e prende le prime 2*/
	 public List<Guida> findFirst2ByOrderByNomeAsc();
	

}
