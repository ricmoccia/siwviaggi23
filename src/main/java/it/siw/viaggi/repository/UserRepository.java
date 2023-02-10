package it.siw.viaggi.repository;

import org.springframework.data.repository.CrudRepository;

import it.siw.viaggi.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

}