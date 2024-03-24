package it.uniroma3.siw.repository;
import org.springframework.data.jpa.repository.JpaRepository; 
import it.uniroma3.siw.model.CarImage;
import java.util.List;

public interface CarImageRepository extends JpaRepository<CarImage, Long> {
    List<CarImage> findByCarId(Long carId);
}
