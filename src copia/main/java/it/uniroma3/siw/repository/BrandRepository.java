package it.uniroma3.siw.repository;


import java.util.List; 
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Brand;

public interface BrandRepository extends CrudRepository<Brand, Long> {

	public Optional<Brand> findById(Long id);
	
	public Brand findByName(String name);

	public List<Brand> findAll();

}