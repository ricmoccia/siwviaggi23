package it.siw.viaggi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.siw.viaggi.model.User;
import it.siw.viaggi.model.Credentials;
import it.siw.viaggi.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected CredentialsRepository credentialsRepository;

	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}

	public User getUserDetails (String username) {
		try {
			return getCredentials(username).getUser();
		} catch (NullPointerException e) {
			System.out.println("No user found. " + e.getMessage());
			return null;
		} catch (Exception e) {
			System.out.println("Generic error: " + e.getMessage());
			return null;
		}
	}	
	
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);//viene registrato come default
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}

	@Transactional
	public Credentials saveCredentialsAdmin(Credentials credentials) {
		credentials.setRole(Credentials.ADMIN_ROLE);//viene registrato come admin
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}
	
	/*
	@Transactional
	public Credentials saveCredentials2 (Credentials credentials) {
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return credentialsRepository.save(credentials);
	}
	*/
}
