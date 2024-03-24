package it.uniroma3.siw.model;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.repository.CredentialsRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Component
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @Min(1)
    @Max(5)
    private int rating;

    @OneToOne
    private Car car;

    @ManyToOne
    private User user;
    
    // Metodi Getter e Setter

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public Car getCar() {
        return car;
    }

    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setUser(User user) {
        this.user = user;
    }

	@Override
	public int hashCode() {
		return Objects.hash(car, id, rating, text, title, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		return Objects.equals(car, other.car) && Objects.equals(id, other.id) && rating == other.rating
				&& Objects.equals(text, other.text) && Objects.equals(title, other.title)
				&& Objects.equals(user, other.user);
	}
    
    
}
