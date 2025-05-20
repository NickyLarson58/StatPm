package src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.model.Telephone;
import src.repository.TelephoneRepository;

import java.util.List;

@Service
public class TelephoneService {
    @Autowired
    private TelephoneRepository telephoneRepository;

    public Telephone saveTelephone(Telephone telephone) {
        return telephoneRepository.save(telephone);
    }

    public List<Telephone> getAllTelephones() {
        return telephoneRepository.findAll();
    }

    public void deleteTelephone(Long id) {
        telephoneRepository.deleteById(id);
    }

    public Telephone getTelephoneById(Long id) {
        return telephoneRepository.findById(id).orElse(null);
    }
}