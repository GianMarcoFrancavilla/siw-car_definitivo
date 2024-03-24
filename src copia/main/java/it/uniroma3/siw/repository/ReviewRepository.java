package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Car;
import it.uniroma3.siw.model.Model;
import it.uniroma3.siw.model.Review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface ReviewRepository extends CrudRepository<Review, Long> {
    // You can add any custom methods here if needed
	public Optional<Review> findById(Long id);
    // Example of a custom method to find reviews by car
    List<Review> findByCarId(Car carId);
}
