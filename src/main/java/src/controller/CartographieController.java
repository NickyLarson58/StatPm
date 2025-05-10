package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import src.model.Signalement;
import src.repository.SignalementRepository;
import org.springframework.ui.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class CartographieController {

    @Autowired
    private SignalementRepository signalementRepository;

    private final String UPLOAD_DIR = "uploads/signalements";

    @GetMapping("/cartographie")
    public String afficherCarte(Model model) {
        List<Signalement> signalements = signalementRepository.findAll();
        model.addAttribute("signalements", signalements);
        return "cartographie";
    }

    @PostMapping("/api/signalements")
    @ResponseBody
    public ResponseEntity<?> creerSignalement(@RequestBody Signalement signalement) {
        signalement.setDateCreation(LocalDateTime.now());
        signalement.setStatut("en_attente");
        Signalement nouveauSignalement = signalementRepository.save(signalement);
        return ResponseEntity.ok(nouveauSignalement);
    }

    @GetMapping("/api/signalements")
    @ResponseBody
    public List<Signalement> getSignalements(@RequestParam(required = false) String statut) {
        if (statut != null) {
            return signalementRepository.findByStatut(statut);
        }
        return signalementRepository.findAll();
    }

    @PostMapping("/api/signalements/{id}/photos")
    @ResponseBody
    public ResponseEntity<?> uploadPhotos(@PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        try {
            Signalement signalement = signalementRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));

            StringBuilder photoPaths = new StringBuilder();
            for (MultipartFile file : files) {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
                photoPaths.append(fileName).append(";");
            }

            signalement.setPhotos(photoPaths.toString());
            signalementRepository.save(signalement);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'upload des photos");
        }
    }

    @GetMapping("/api/signalements/export")
    @ResponseBody
    public ResponseEntity<String> exporterSignalements() {
        try {
            List<Signalement> signalements = signalementRepository.findAll();
            StringBuilder csv = new StringBuilder();
            csv.append("ID,Type,Description,Latitude,Longitude,Date Création,ID Utilisateur,Statut\n");

            for (Signalement s : signalements) {
                csv.append(String.format("%d,\"%s\",\"%s\",%f,%f,%s,%d,\"%s\"\n",
                        s.getId(),
                        s.getType(),
                        s.getDescription().replace("\"", "\"\""),
                        s.getLatitude(),
                        s.getLongitude(),
                        s.getDateCreation(),
                        s.getIdUtilisateur(),
                        s.getStatut()));
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "text/csv")
                    .header("Content-Disposition", "attachment; filename=signalements.csv")
                    .body(csv.toString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'export");
        }
    }

    @PutMapping("/api/signalements/{id}/moderation")
    @ResponseBody
    public ResponseEntity<?> modererSignalement(@PathVariable Long id, @RequestParam String statut) {
        try {
            Signalement signalement = signalementRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
            signalement.setStatut(statut);
            signalementRepository.save(signalement);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la modération");
        }
    }
}