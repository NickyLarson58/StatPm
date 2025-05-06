package src.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.model.Interventions;
import src.model.dto.CreatedStatInterventionDTO;
import src.repository.InterventionsRepository;
import src.service.InterventionService;

@RestController
@RequestMapping("/api")
public class InterventionsController {

    @Autowired
    InterventionsRepository interventionsRepository;

    @Autowired
    private InterventionService interventionService;

    @GetMapping("/interventions")
    public ResponseEntity<List<Interventions>> getAllInterventions() {
        try {
            List<Interventions> interventions =  interventionsRepository.findAll();
            if (interventions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(interventions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/interventions/{id}")
    public ResponseEntity<Interventions> getInterventionsById(@PathVariable("id") int id) {
        Optional<Interventions> interventionsData = interventionsRepository.findById(id);
        if (interventionsData.isPresent()) {
            return new ResponseEntity<>(interventionsData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/interventions")
    public ResponseEntity<Long> createIntervention(@RequestBody CreatedStatInterventionDTO interventionDTO) {
        Long createdId = interventionService.createIntervention(interventionDTO);
        return ResponseEntity.ok(createdId);
    }

    @PutMapping("/interventions/{id}")
    public ResponseEntity<Interventions> updateInterventions(@PathVariable("id") int id, @RequestBody Interventions interventions) {
        Optional<Interventions> interventionsData = interventionsRepository.findById(id);
        if (interventionsData.isPresent()) {
            Interventions _interventions = interventionsData.get();
            _interventions.setNomInterventions(interventions.getNomInterventions());
            return new ResponseEntity<>(interventionsRepository.save(_interventions), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/interventions/{id}")
    public ResponseEntity<HttpStatus> deleteInterventions(@PathVariable("id") int id) {
        try {
            interventionsRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/interventions")
    public ResponseEntity<HttpStatus> deleteAllInterventions() {
        try {
            interventionsRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}