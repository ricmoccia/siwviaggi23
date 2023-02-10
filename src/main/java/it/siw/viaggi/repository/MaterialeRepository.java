package it.siw.viaggi.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.siw.viaggi.model.Materiale;

public interface MaterialeRepository  extends CrudRepository<Materiale, Long> {

	/*il metodo non va implementato, viene creato automaticamente dal nome del metodo*/
	public boolean existsByNomeAndDescrizione(String nome, String descrizione);
	
	public List<Materiale> findByNomeOrDescrizione (String nome, String descrizione);

	public List<Materiale> findByNome (String nome);

	public List<Materiale> findByDescrizione (String descrizione);
}
