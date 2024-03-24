package it.uniroma3.siw.repository;

import java.util.List; 
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Brand;
import it.uniroma3.siw.model.Model;

public interface ModelRepository extends CrudRepository<Model, Long> {

	public Optional<Model> findById(Long id);
	
	public Model findByName(String name);
	
	public List<Model> findAll(); 

	public List<Model> findByBrand(Brand brand);
	
}