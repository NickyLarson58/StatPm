package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import src.model.Commercant;
import src.model.Telephone;
import src.repository.AdressesRepository;
import src.repository.ActiviteRepository;
import src.service.CommercantService;
import src.service.TelephoneService;
import java.util.List;

@Controller
public class CommerceController {
    @Autowired
    private CommercantService commercantService;
    @Autowired
    private TelephoneService telephoneService;
    @Autowired
    private AdressesRepository adressesRepository;
    @Autowired
    private ActiviteRepository activiteRepository;

    @GetMapping("/declarer-commerce")
    public String afficherFormulaireCommerce(Model model) {
        model.addAttribute("commercant", new Commercant());
        model.addAttribute("activites", activiteRepository.findAll());
        return "declarer-commerce";
    }

    @PostMapping("/declarer-commerce")
    public String saveCommerce(@ModelAttribute Commercant commercant, @RequestParam(value = "telephones", required = false) List<Telephone> telephones) {
        if (telephones != null) {
            for (Telephone tel : telephones) {
                tel.setCommerce(commercant);
            }
            commercant.setTelephones(telephones);
        }
        commercant.setNumeroAdresse(commercant.getNumeroAdresse());
        commercantService.saveCommercant(commercant);
        return "redirect:/liste-commercants";
    }

    @GetMapping("/liste-commercants")
    public String listCommercants(Model model) {
        List<Commercant> commercants = commercantService.getAllCommercants();
        model.addAttribute("commercants", commercants);
        return "liste-commercants";
    }

    @GetMapping("/filtrer-commercants")
    public String filterCommercants(@RequestParam(required = false) String nom,
                                    @RequestParam(required = false) String adresse,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) String nomProprietaire,
                                    Model model) {
        List<Commercant> commercants;
        if (nom != null && !nom.isEmpty()) {
            commercants = commercantService.searchByNom(nom);
        } else if (adresse != null && !adresse.isEmpty()) {
            commercants = commercantService.searchByAdresse(adresse);
        } else if (type != null && !type.isEmpty()) {
            commercants = commercantService.searchByActivite(type);
        } else if (nomProprietaire != null && !nomProprietaire.isEmpty()) {
            commercants = commercantService.searchByNomProprietaire(nomProprietaire);
        } else {
            commercants = commercantService.getAllCommercants();
        }
        model.addAttribute("commercants", commercants);
        return "liste-commercants";
    }
}