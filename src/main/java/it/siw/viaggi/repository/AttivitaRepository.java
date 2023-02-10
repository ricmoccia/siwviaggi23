package it.siw.viaggi.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.siw.viaggi.model.Materiale;
import it.siw.viaggi.model.Attivita;

public interface AttivitaRepository  extends CrudRepository<Attivita, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndDescrizione(String nome, String descrizione);
	
	public Attivita findByNome(String nome);

	public List<Attivita> findByMaterialiContaining(Materiale i);
	
}
