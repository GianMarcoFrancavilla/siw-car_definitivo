package it.uniroma3.siw.controller;

import java.util.*;   
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validator.CarValidator;
import it.uniroma3.siw.model.Brand;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.Car;
import it.uniroma3.siw.model.CarImage;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.*;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import jakarta.validation.Valid;

@Controller
public class CarController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private ModelRepository modelRepository;

	@Autowired
	private CarImageRepository carImageRepository;

	@Autowired
	CredentialsRepository credentialsRepository;

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private CarValidator carValidator;



	@GetMapping(value = "/admin/formNewCar")
	public String formNewCar(Model model) {
		model.addAttribute("car", new Car());
		List<Brand> brands = brandRepository.findAll();
		model.addAttribute("brands", brands);
		List<it.uniroma3.siw.model.Model> models = modelRepository.findAll();
		model.addAttribute("models", models);
		return "admin/formNewCar.html";
	}

	@GetMapping(value = "/admin/formUpdateCar/{id}")
	public String formUpdatecar(@PathVariable("id") Long id, Model model) {
		model.addAttribute("car", carRepository.findById(id).get());
		model.addAttribute("users",userRepository.findAll());
		return "admin/formUpdateCar.html";
	}

	@GetMapping(value = "/admin/indexCar")
	public String indexcar() {
		return "admin/indexCar.html";
	}

	@GetMapping(value = "/admin/manageCars")
	public String managecars(Model model) {
		model.addAttribute("cars", this.carRepository.findAll());
		return "admin/manageCars.html";
	}

	@PostMapping("/car")
	public String newCar(@Valid @ModelAttribute("car") Car car,
			@RequestPart("uploadedImages") MultipartFile[] uploadedImages,
			Model model, BindingResult bindingResult) {
		
		
		carValidator.validate(car, bindingResult);

		if (bindingResult.hasErrors()) {
			// La validazione ha fallito, torna al form con gli errori
			List<Brand> brands = brandRepository.findAll();
			List<it.uniroma3.siw.model.Model> models = modelRepository.findAll();
			model.addAttribute("brands", brands);
			model.addAttribute("models", models);
			return "admin/formNewCar.html";
		}
		
		try {
			List<CarImage> carImages = new ArrayList<>();

			car.setSold(false);
			// Salva prima l'istanza di Car
			carRepository.save(car);

			for (MultipartFile uploadedImage : uploadedImages) {
				CarImage carImage = new CarImage();
				byte[] imageBytes = uploadedImage.getBytes();
				carImage.setImageData(imageBytes);
				carImage.setCar(car);  // Imposta l'istanza di Car
				carImages.add(carImage);
			}

			// Ora puoi salvare l'istanza di CarImage
			carImageRepository.saveAll(carImages);

			// Aggiorna la lista delle immagini nella tua istanza di Car
			car.setCarImages(carImages);


			List<String> base64Strings = car.getCarImages().stream()
					.filter(Objects::nonNull)
					.map(carImage -> Base64Utils.encodeToString(carImage.getImageData()))
					.collect(Collectors.toList());

			model.addAttribute("car", car);
			model.addAttribute("base64Strings", base64Strings);



			return "car.html";

		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/error";
		}
	}


	@SuppressWarnings("unused")
	private String saveImage(MultipartFile uploadedImage) throws IOException {
		String desktopPath = System.getProperty("user.home") + "/Desktop";
		String imageDirectory = desktopPath + "/foto_siw";

		File directory = new File(imageDirectory);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String imagePath = imageDirectory + "/" + uploadedImage.getOriginalFilename();
		uploadedImage.transferTo(new File(imagePath));

		return imagePath;
	}


	@GetMapping("/filterPage")
	public String filterPage(Model model) {
		model.addAttribute("cars", this.carRepository.findAll());
		model.addAttribute("brands", this.brandRepository.findAll());
		model.addAttribute("models", this.modelRepository.findAll());

		return "filterPage.html";
	}

	@GetMapping(value = "/admin/formUpdateCars")
	public String formUpdateCars(Model model) {
		List<Car> cars = this.carRepository.findUnsolds();
		model.addAttribute("cars", cars);
		List<User> users = userRepository.findAll();
		model.addAttribute("users", users);
		return "admin/formUpdateCars.html";
	}

	@GetMapping("/car/{id}")
	public String getCar(@PathVariable("id") Long id, Model model,Principal principal) {
		Car car = this.carRepository.findById(id).orElse(null);
		String username = principal.getName();

		if (car != null) {
			List<String> base64Strings = car.getCarImages().stream()
					.filter(Objects::nonNull)
					.map(carImage -> Base64Utils.encodeToString(carImage.getImageData()))
					.collect(Collectors.toList());

			model.addAttribute("car", car);
			model.addAttribute("base64Strings", base64Strings); // Aggiungi le stringhe base64 delle immagini al modello
			model.addAttribute("username", username);

			return "car.html";
		} else {
			return "redirect:/error"; // Gestisci il caso in cui l'auto non sia presente
		}
	}




	@GetMapping("/carsForSale")
	public String getCarsForSale(Model model) {
		model.addAttribute("cars", this.carRepository.findUnsolds());
		model.addAttribute("brands", this.brandRepository.findAll());
		return "carsForSale.html";
	}

	@GetMapping("/carsNotForSale")
	public String getCarsNotForSale(Model model, Principal principal) {
		String username = principal.getName();
		Optional<Credentials> currentUser = credentialsRepository.findByUsername(username);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("cars", this.carRepository.findSolds());
		model.addAttribute("brands", this.brandRepository.findAll());
		return "carsNotForSale.html";
	}

	@GetMapping("/admin/carsNotForSaleAdmin")
	public String getCarsNotForSaleAdmin(Model model, Principal principal) {
		model.addAttribute("cars", this.carRepository.findSolds());
		model.addAttribute("brands", this.brandRepository.findAll());
		return "admin/carsNotForSaleAdmin.html";
	}

	@GetMapping("/formSearchCars")
	public String formSearchcars() {
		return "formSearchCars.html";
	}

	@GetMapping("/filteredCars")
	public String filteredCars(
			@RequestParam(name = "licensePlate", required = false) String licensePlate,
			@RequestParam(name = "model", required = false) it.uniroma3.siw.model.Model carModel,
			@RequestParam(name = "brand", required = false) Brand brand,
			@RequestParam(name = "minYear", required = false) Integer minYear,
			@RequestParam(name = "maxYear", required = false) Integer maxYear,
			@RequestParam(name = "minMileage", required = false) Integer minMileage,
			@RequestParam(name = "maxMileage", required = false) Integer maxMileage,
			@RequestParam(name = "color", required = false) String color,
			@RequestParam(name = "carType", required = false) String carType,
			@RequestParam(name = "minPrice", required = false) Integer minPrice,
			@RequestParam(name = "maxPrice", required = false) Integer maxPrice,
			Model model) {

		// Esempio di servizio fittizio:
		List<Car> filteredCars = carRepository.findByAttributes(
				licensePlate, carModel, brand, minYear, maxYear,
				minMileage, maxMileage,minPrice, maxPrice, color);



		// Inserisci i risultati nel model
		model.addAttribute("filteredCars", filteredCars);

		// Ritorna il nome della pagina risultante
		return "filteredCarsPage";
	}

	@PostMapping("/admin/vendi-auto/{id}")
	public String vendiAuto(@PathVariable Long id, @RequestParam Long buyer, Model model) {
		// Find the car to sell
		Car carToSell = carRepository.findById(id).orElse(null);
		User buyerUser = userRepository.findById(buyer).orElse(null);

		if (carToSell != null && buyerUser != null) {
			// Set the 'sold' attribute to true
			carToSell.setSold(true);

			// Set the buyer as the owner of the car
			carToSell.setOwner(buyerUser);

			// Save the updated car
			carRepository.save(carToSell);
		}

		// Aggiorna la lista degli utenti nel model
		List<User> users = userRepository.findAll();
		model.addAttribute("users", users);

		// Update the list of cars in the model
		List<Car> cars = carRepository.findUnsolds();
		model.addAttribute("cars", cars);

		return "admin/formUpdateCars.html";
	}



	@PostMapping("/admin/elimina-auto/{id}")
	public String eliminaAuto(@PathVariable Long id, Model model) {
		// Trova l'auto da eliminare
		Car autoDaEliminare = carRepository.findById(id).orElse(null);

		if (autoDaEliminare != null) {

			// Elimina l'auto dalla repository
			carRepository.deleteById(id);

		}

		// Aggiorna la lista delle auto nel model
		List<Car> cars = carRepository.findAll();
		model.addAttribute("cars", cars);

		return "admin/formUpdateCars.html";
	}

	@GetMapping("/writeReview")
	public String showReviewForm(@RequestParam Long carId, Model model) {
		Car car = carRepository.findById(carId)
				.orElseThrow();

		model.addAttribute("car", car);
		return "formNewReview";
	}

	@GetMapping("/deleteReview/{id}")
	public String eliminaReview(@PathVariable Long id) {
		// Trova la review da eliminare
		Optional<Review> reviewDaEliminareOptional = reviewRepository.findById(id);

		if (reviewDaEliminareOptional.isPresent()) {
			Review reviewDaEliminare = reviewDaEliminareOptional.get();

			// Trova la car associata alla review
			Car carAssociata = reviewDaEliminare.getCar();

			// Rimuovi la review dalla lista di recensioni della car
			carAssociata.setReview(null);

			// Salva la car aggiornata
			carRepository.save(carAssociata);

			// Elimina la review dalla repository
			reviewRepository.deleteById(id);
		}

		// Redirect alla tua pagina principale delle auto o a dove preferisci
		return "redirect:/";
	}

	@PostMapping("/writeReview")
	public String createReview(@RequestParam Long carId, @RequestParam String title, 
			@RequestParam String text, @RequestParam int rating, Principal principal) {
		// Ottieni l'utente corrente da Spring Security
		String username = principal.getName();
		User currentUser = userRepository.findByName(username);
		// Ottieni l'auto dall'ID
		Car car = carRepository.findById(carId)
				.orElseThrow();

		Review review = new Review();
		review.setTitle(title);
		review.setText(text);
		review.setRating(rating);
		review.setCar(car);
		review.setUser(currentUser);
		reviewRepository.save(review);

		return "redirect:/"; // Redirect alla tua pagina principale delle auto o a dove preferisci
	}
	
	@GetMapping("/editReview/{id}")
	public String showEditReviewForm(@PathVariable Long id, Model model) {
	    // Trova la review da modificare
	    Optional<Review> reviewDaModificareOptional = reviewRepository.findById(id);
	    if (reviewDaModificareOptional.isPresent()) {
	        Review reviewDaModificare = reviewDaModificareOptional.get();
	        Car carAssociata = reviewDaModificare.getCar();

	        model.addAttribute("review", reviewDaModificare);
	        model.addAttribute("car", carAssociata);

	        return "formEditReview"; // Assicurati di avere un template separato per la modifica della recensione
	    } else {
	        return "redirect:/error"; // Gestisci il caso in cui la recensione non sia presente
	    }
	}

	@PostMapping("/editReview")
	public String editReview(
	        @RequestParam Long reviewId,
	        @RequestParam String title,
	        @RequestParam String text,
	        @RequestParam int rating,
	        Model model) {

	    // Trova la review da modificare
	    Optional<Review> reviewToEditOptional = reviewRepository.findById(reviewId);

	    if (reviewToEditOptional.isPresent()) {
	        Review reviewToEdit = reviewToEditOptional.get();

	        // Aggiorna la recensione esistente
	        reviewToEdit.setTitle(title);
	        reviewToEdit.setText(text);
	        reviewToEdit.setRating(rating);
	        reviewRepository.save(reviewToEdit);

	        // Reindirizza alla pagina desiderata (ad esempio, la pagina di visualizzazione della recensione)
	        return "redirect:/"; // Assicurati di specificare la tua URL corretta
	    } else {
	        return "redirect:/error"; // Gestisci il caso in cui la recensione non sia presente
	    }
	}

}


