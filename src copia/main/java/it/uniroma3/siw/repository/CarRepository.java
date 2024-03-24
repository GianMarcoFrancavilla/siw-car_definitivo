package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Brand;
import it.uniroma3.siw.model.Car;
import it.uniroma3.siw.model.Model;

public interface CarRepository extends CrudRepository<Car, Long> {
	
	
	public Optional<Car> findById(Long id);

	public Car findByLicensePlate(String licensePlate);

	public List<Car> findByBrand(Brand brand);
	
	public List<Car> findAll();
	
	@Query("SELECT c FROM Car c " +
	        "WHERE (:licensePlate IS NULL OR c.licensePlate = :licensePlate) " +
	        "AND (:carModel IS NULL OR c.model = :carModel) " +
	        "AND (:brand IS NULL OR c.brand = :brand) " +
	        "AND (:minYear IS NULL OR c.yearOfProduction BETWEEN :minYear AND :maxYear) " +
	        "AND (:maxYear IS NULL OR c.yearOfProduction BETWEEN :minYear AND :maxYear) " +
	        "AND (:minMileage IS NULL OR c.mileage >= :minMileage) " +
	        "AND (:maxMileage IS NULL OR c.mileage <= :maxMileage) " +
	        "AND (:minPrice IS NULL OR c.price >= :minPrice) " +
	        "AND (:maxPrice IS NULL OR c.price <= :maxPrice) " +
	        "AND (:color IS NULL OR c.color = :color OR :color = '') " )
	List<Car> findByAttributes(@Param("licensePlate") String licensePlate,
	                           @Param("carModel") Model carModel,
	                           @Param("brand") Brand brand,
	                           @Param("minYear") Integer minYear,
	                           @Param("maxYear") Integer maxYear,
	                           @Param("minMileage") Integer minMileage,
	                           @Param("maxMileage") Integer maxMileage,
	                           @Param("minPrice") Integer minPrice,
	                           @Param("maxPrice") Integer maxPrice,
	                           @Param("color") String color);



	public boolean existsByLicensePlate(String licensePlate);

	@Query("SELECT c FROM Car c WHERE c.sold = false")
	public List<Car> findUnsolds();
	
	@Query("SELECT c FROM Car c WHERE c.sold = true")
	public List<Car> findSolds();

}