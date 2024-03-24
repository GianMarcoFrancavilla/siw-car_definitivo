package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "licenseplate", unique = true)
    private String licensePlate;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Min(1920)
    @Max(2023)
    @Column(name = "yearofproduction")
    private int yearOfProduction;

    @Min(0)
    private int mileage;

    @NotBlank
    private String color;

    @NotNull
    private int price;
    
	private Boolean sold;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CarImage> carImages;
    
    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Review review;  // Aggiungi questo campo
    
    @ManyToOne
    private User owner;


    // Metodi Getter e Setter per gli altri campi

    public List<CarImage> getCarImages() {
        return carImages;
    }

    public void setCarImages(List<CarImage> carImages) {
        this.carImages = carImages;
    }
    
    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Long getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Model getModel() {
        return model;
    }

    public Brand getBrand() {
        return brand;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public int getMileage() {
        return mileage;
    }

    public String getColor() {
        return color;
    }


    public int getPrice() {
        return price;
    }

    // Metodi Setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    
	public Boolean getSold() {
		return sold;
	}

	public void setSold(Boolean sold) {
		this.sold = sold;
	}
	
	

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public int hashCode() {
		return Objects.hash(brand, carImages, color, id, licensePlate, mileage, model, owner, price, sold,
				yearOfProduction);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		return Objects.equals(brand, other.brand) && Objects.equals(carImages, other.carImages)
				&& Objects.equals(color, other.color) && Objects.equals(id, other.id)
				&& Objects.equals(licensePlate, other.licensePlate) && mileage == other.mileage
				&& Objects.equals(model, other.model) && Objects.equals(owner, other.owner) && price == other.price
				&& Objects.equals(sold, other.sold) && yearOfProduction == other.yearOfProduction;
	}

	
	
    
    
    
    
}
