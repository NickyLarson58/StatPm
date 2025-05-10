package src.controller;

import src.model.TypeSignalement;
import src.repository.TypeSignalementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TypeSignalementController {

    @Autowired
    private TypeSignalementRepository typeSignalementRepository;

    @GetMapping("/types-signalement")
    public List<TypeSignalement> getAllTypes() {
        return typeSignalementRepository.findAll();
    }
}