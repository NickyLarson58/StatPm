package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.model.Brigade;
import src.service.BrigadeService;

import java.util.List;

@RestController
@RequestMapping("/api/brigades")
public class BrigadeController {
    @Autowired
    private BrigadeService brigadeService;

    @GetMapping
    public List<Brigade> getAllBrigades() {
        return brigadeService.getAllBrigades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brigade> getBrigadeById(@PathVariable Long id) {
        return brigadeService.getBrigadeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Brigade createBrigade(@RequestBody Brigade brigade) {
        return brigadeService.saveBrigade(brigade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brigade> updateBrigade(@PathVariable Long id, @RequestBody Brigade brigade) {
        if (!brigadeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        brigade.setIdBrigade(id);
        return ResponseEntity.ok(brigadeService.saveBrigade(brigade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrigade(@PathVariable Long id) {
        if (!brigadeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        brigadeService.deleteBrigade(id);
        return ResponseEntity.ok().build();
    }
}