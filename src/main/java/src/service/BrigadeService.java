package src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.model.Brigade;
import src.repository.BrigadeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BrigadeService {
    @Autowired
    private BrigadeRepository brigadeRepository;

    public List<Brigade> getAllBrigades() {
        return brigadeRepository.findAll();
    }

    public Optional<Brigade> getBrigadeById(Long id) {
        return brigadeRepository.findById(id);
    }

    public Brigade saveBrigade(Brigade brigade) {
        return brigadeRepository.save(brigade);
    }

    public void deleteBrigade(Long id) {
        brigadeRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return brigadeRepository.existsById(id);
    }
}