package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public Optional<User> findById(Long id);
	public List<User> findAll();
	public User findByName(String username);
}