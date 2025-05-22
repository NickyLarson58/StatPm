package src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.model.Commercant;
import src.model.Telephone;
import src.repository.CommercantRepository;
import src.repository.TelephoneRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommercantService {
    @Autowired
    private CommercantRepository commercantRepository;
    @Autowired
    private TelephoneRepository telephoneRepository;

    public Commercant saveCommercant(Commercant commercant) {
        if (commercant.getTelephones() != null) {
            for (Telephone tel : commercant.getTelephones()) {
                tel.setCommerce(commercant);
                if (tel.getStatut() == null) {
                    tel.setStatut("inconnu"); // Valeur par défaut si besoin
                }
            }
        }
        commercant.setNumeroAdresse(commercant.getNumeroAdresse());
        return commercantRepository.save(commercant);
    }

    public List<Commercant> getAllCommercants() {
        return commercantRepository.findAll();
    }

    public Optional<Commercant> getCommercantById(Long id) {
        return commercantRepository.findById(id);
    }

    public List<Commercant> searchByNom(String nom) {
        return commercantRepository.findByNomCommerceContainingIgnoreCase(nom);
    }

    public List<Commercant> searchByActivite(String nomActivite) {
        return commercantRepository.findByActivite_NomContainingIgnoreCase(nomActivite);
    }

    public List<Commercant> searchByAdresse(String adresse) {
        return commercantRepository.findByAdresseCommerceContainingIgnoreCase(adresse);
    }

    public List<Commercant> searchByTelephone(String numero) {
        return commercantRepository.findByTelephones_NumeroContaining(numero);
    }

    public List<Commercant> searchByNomProprietaire(String nom) {
        return commercantRepository.findByNomProprietaireContainingIgnoreCase(nom);
    }

    public List<Commercant> searchByTelephoneProprietaire(String numero) {
        return commercantRepository.findByTelephoneProprietaireContaining(numero);
    }

    public void deleteCommercant(Long id) {
        commercantRepository.deleteById(id);
    }

    public Telephone saveTelephone(Telephone telephone) {
        return telephoneRepository.save(telephone);
    }

    public List<Telephone> getTelephonesByCommerce(Commercant commercant) {
        return telephoneRepository.findAll().stream().filter(t -> t.getCommerce().equals(commercant)).collect(Collectors.toList());
    }
}