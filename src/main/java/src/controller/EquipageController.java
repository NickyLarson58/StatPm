package src.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import src.model.Equipage;

@RestController
@RequestMapping("/api/equipages")
public class EquipageController {
    private List<Equipage> equipages = new ArrayList<>();

    @GetMapping
    public List<Equipage> getAllEquipages() {
        return equipages;
    }

    @PostMapping
    public ResponseEntity<Equipage> createEquipage(@RequestBody Equipage equipage) {
        int nextNumero = equipages.isEmpty() ? 1 : 
            equipages.stream().mapToInt(Equipage::getNumero).max().getAsInt() + 1;
        equipage.setNumero(nextNumero);
        equipages.add(equipage);
        return ResponseEntity.ok(equipage);
    }

    @DeleteMapping("/{numero}")
    public ResponseEntity<?> deleteEquipage(@PathVariable int numero) {
        equipages.removeIf(e -> e.getNumero() == numero);
        // Réajuster les numéros
        for (int i = 0; i < equipages.size(); i++) {
            equipages.get(i).setNumero(i + 1);
        }
        return ResponseEntity.ok().build();
    }
}
