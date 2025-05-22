package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import src.model.Commercant;
import src.repository.CommercantRepository;

@Controller
public class CommercantController {

    @Autowired
    private CommercantRepository commercantRepository;

    @GetMapping("/AccueilListeCommercant")
    public String travaux() {
        return "AccueilListeCommercant";
    }

    @GetMapping("/consulter-commerce/{id}")
    public String consulterCommerce(@PathVariable Long id, Model model) {
        Commercant commercant = commercantRepository.findById(id).orElse(null);
        model.addAttribute("commercant", commercant);
        return "consulter-commerce";
    }

    @GetMapping("/modifier-commerce/{id}")
    public String afficherModifierCommerce(@PathVariable Long id, Model model) {
        Commercant commercant = commercantRepository.findById(id).orElse(null);
        model.addAttribute("commercant", commercant);
        return "declarer-commerce";
    }

    @PostMapping("/modifier-commerce/{id}")
    public String modifierCommerce(@PathVariable Long id, Commercant commercant) {
        commercant.setId(id);
        commercant.setNumeroAdresse(commercant.getNumeroAdresse());
        if (commercant.getTelephones() != null) {
            for (var tel : commercant.getTelephones()) {
                tel.setCommerce(commercant);
            }
        }
        commercantRepository.save(commercant);
        return "redirect:/liste-commercants";
    }

    @GetMapping("/supprimer-commerce/{id}")
    public String supprimerCommerce(@PathVariable Long id) {
        commercantRepository.deleteById(id);
        return "redirect:/liste-commercants";
    }
}