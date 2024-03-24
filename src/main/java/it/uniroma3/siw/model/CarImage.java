package it.uniroma3.siw.model;

import jakarta.persistence.Entity; 
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import java.util.Arrays;

@Entity
public class CarImage {
	
    // Costruttore con byte dell'immagine
    public CarImage() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aggiungi il campo per la sequenza di byte dell'immagine
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    // Metodi Getter e Setter

    public Long getId() {
        return id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(imageData);
		result = prime * result + Objects.hash(car, id);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarImage other = (CarImage) obj;
		return Objects.equals(car, other.car) && Objects.equals(id, other.id)
				&& Arrays.equals(imageData, other.imageData);
	}

    
    
}
