package it.uniroma3.siw.controller;

import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Brand;
import it.uniroma3.siw.repository.BrandRepository;
import it.uniroma3.siw.repository.ModelRepository;

import jakarta.validation.Valid;

@Controller
public class ModelController {
    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private BrandRepository brandRepository;

    @GetMapping(value = "/admin/formNewModel")
    public String formNewModel(Model model) {
        model.addAttribute("model", new it.uniroma3.siw.model.Model());
        model.addAttribute("brands", brandRepository.findAll());
        return "admin/formNewModel.html";
    }
    
    @GetMapping(value = "/admin/indexModel")
    public String indexBrand(Model model) {
        List<it.uniroma3.siw.model.Model> models = (List<it.uniroma3.siw.model.Model>) modelRepository.findAll();
        model.addAttribute("models", models);
        return "admin/indexModel.html";
    }
    
    @GetMapping(value = "/admin/formUpdateModels")
    public String formUpdateModels(Model model) {
    	List<it.uniroma3.siw.model.Model> models = this.modelRepository.findAll();
        model.addAttribute("models", models);
        return "admin/formUpdateModels.html";
    }

    @PostMapping("/model")
    public String newModel(@Valid @ModelAttribute("model") it.uniroma3.siw.model.Model model, BindingResult bindingResult, Model viewModel) {
        if (!bindingResult.hasErrors()) {
            Brand selectedBrand = model.getBrand();
            if (selectedBrand != null) {
                // Salvataggio del nuovo modello nella repository
                modelRepository.save(model);

                // Aggiungi il nuovo modello alla lista dei modelli del marchio selezionato
                List<it.uniroma3.siw.model.Model> brandModels = selectedBrand.getModels();
                brandModels.add(model);
                selectedBrand.setModels(brandModels);

                // Salvataggio del marchio con il nuovo modello nella repository
                brandRepository.save(selectedBrand);

                viewModel.addAttribute("model", model);
                return "model.html"; // Reindirizza alla pagina del modello creato con successo
            }
        }
        viewModel.addAttribute("brands", brandRepository.findAll());
        return "admin/formNewModel.html"; // Torna al form di creazione del modello con gli errori
    }
    

    @PostMapping("/admin/elimina-modello/{id}")
    public String eliminaModello(@PathVariable Long id, Model model) {
        // Trova il modello da eliminare
        it.uniroma3.siw.model.Model modelloDaEliminare = modelRepository.findById(id).orElse(null);

        if (modelloDaEliminare != null) {
            // Rimuovi il modello dal marchio associato
            Brand brandAssociato = modelloDaEliminare.getBrand();
            brandAssociato.getModels().remove(modelloDaEliminare);

            // Elimina il modello dalla repository
            modelRepository.deleteById(id);

            // Aggiorna il marchio nella repository
            brandRepository.save(brandAssociato);
        }

        // Aggiorna la lista dei modelli nel model
        List<it.uniroma3.siw.model.Model> models = modelRepository.findAll();
        model.addAttribute("models", models);

        return "admin/formUpdateModels.html";
    }

}