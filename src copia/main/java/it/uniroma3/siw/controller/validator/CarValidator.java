package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Car;
import it.uniroma3.siw.repository.CarRepository;

@Component
public class CarValidator implements Validator {
    @Autowired
    private CarRepository carRepository;

    @Override
    public void validate(Object o, Errors errors) {
        Car car = (Car) o;
        if (car.getId() == null || !carRepository.existsById(car.getId())) {
            // L'auto con l'ID specificato non esiste, quindi possiamo verificare la targa
            if (car.getLicensePlate() != null && carRepository.existsByLicensePlate(car.getLicensePlate())) {
                errors.rejectValue("licensePlate", "car.duplicate", "Auto con la stessa targa già presente.");
            }
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Car.class.equals(aClass);
    }
}
