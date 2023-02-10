package it.siw.viaggi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.siw.viaggi.model.Guida;
import it.siw.viaggi.model.Viaggio;

public interface ViaggioRepository  extends CrudRepository<Viaggio, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndDescrizione(String nome, String descrizione);
	
	public Viaggio findByNome(String nome);
	
	/*trova i viaggi di una determinata guida*/
	public List<Viaggio> findByGuida(Guida guida);
}
