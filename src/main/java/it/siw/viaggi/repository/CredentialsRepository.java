package it.siw.viaggi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.siw.viaggi.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {
	
	public Optional<Credentials> findByUsername(String username);

}