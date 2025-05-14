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
import java.util.stream.Collectors;

import src.model.Photo;
import src.repository.PhotoRepository;
import src.model.dto.SignalementDTO;

@Controller
public class CartographieController {

    @Autowired
    private SignalementRepository signalementRepository;
    @Autowired
    private PhotoRepository photoRepository;

    private final String UPLOAD_DIR = "uploads/signalements";

    @GetMapping("/cartographie")
    public String afficherCarte(Model model) {
        List<Signalement> signalements = signalementRepository.findAll();
        model.addAttribute("signalements", signalements);
        return "cartographie";
    }

    @PostMapping("/api/signalements")
    @ResponseBody
    public ResponseEntity<?> creerSignalement(
            @RequestParam("type") String type,
            @RequestParam("description") String description,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("adresse") String adresse,
            @RequestParam(value = "idUtilisateur", required = false) Long idUtilisateur,
            @RequestParam(value = "files", required = false) MultipartFile[] files) {
        try {
            Signalement signalement = new Signalement();
            signalement.setType(type);
            signalement.setDescription(description);
            signalement.setLatitude(latitude);
            signalement.setLongitude(longitude);
            signalement.setAdresse(adresse);
            signalement.setDateCreation(LocalDateTime.now());
            signalement.setStatut("visible");
            if (idUtilisateur != null) {
                signalement.setIdUtilisateur(idUtilisateur);
            }
            Signalement nouveauSignalement = signalementRepository.save(signalement);
            // Gestion des fichiers
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    Path uploadPath = Paths.get(UPLOAD_DIR);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
                    Photo photo = new Photo();
                    photo.setChemin(fileName);
                    photo.setSignalement(nouveauSignalement);
                    photoRepository.save(photo);
                }
            }
            SignalementDTO dto = toDTO(nouveauSignalement);
            return ResponseEntity.ok(dto);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la création du signalement ou de l'upload des photos");
        }
    }

    @GetMapping("/api/signalements")
    @ResponseBody
    public List<SignalementDTO> getSignalements(@RequestParam(required = false) String statut) {
        List<Signalement> signalements = (statut != null) ? signalementRepository.findByStatut(statut) : signalementRepository.findByStatut("visible");
        return signalements.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PostMapping("/api/signalements/{id}/photos")
    @ResponseBody
    public ResponseEntity<?> uploadPhotos(@PathVariable Long id, @RequestParam("files") MultipartFile[] files) {
        try {
            Signalement signalement = signalementRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
            for (MultipartFile file : files) {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
                Photo photo = new Photo();
                photo.setChemin(fileName);
                photo.setSignalement(signalement);
                photoRepository.save(photo);
            }
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'upload des photos");
        }
    }

    @PutMapping("/api/signalements/{id}")
    @ResponseBody
    public ResponseEntity<?> updateSignalement(@PathVariable Long id, @RequestParam(required = false) String description, @RequestParam(value = "files", required = false) MultipartFile[] files) {
        try {
            Signalement signalement = signalementRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Le signalement demandé n'existe pas ou a été supprimé."));
            if (description != null) {
                signalement.setDescription(description);
            }
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    Path uploadPath = Paths.get(UPLOAD_DIR);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
                    Photo photo = new Photo();
                    photo.setChemin(fileName);
                    photo.setSignalement(signalement);
                    photoRepository.save(photo);
                }
            }
            signalementRepository.save(signalement);
            SignalementDTO dto = toDTO(signalement);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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

    @GetMapping("/api/signalements/{id}/photos/{photo}")
    @ResponseBody
    public ResponseEntity<?> getPhoto(@PathVariable Long id, @PathVariable String photo) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(photo);
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }
            byte[] fileContent = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la récupération de la photo");
        }
    }

    private SignalementDTO toDTO(Signalement signalement) {
        SignalementDTO dto = new SignalementDTO();
        dto.setId(signalement.getId());
        dto.setType(signalement.getType());
        dto.setDescription(signalement.getDescription());
        dto.setLatitude(signalement.getLatitude());
        dto.setLongitude(signalement.getLongitude());
        dto.setDateCreation(signalement.getDateCreation());
        dto.setIdUtilisateur(signalement.getIdUtilisateur());
        dto.setStatut(signalement.getStatut());
        dto.setAdresse(signalement.getAdresse());
        if (signalement.getPhotos() != null) {
            dto.setPhotos(signalement.getPhotos().stream().map(Photo::getChemin).collect(Collectors.toList()));
        }
        return dto;
    }

    @PutMapping("/api/signalements/{id}/archive")
    @ResponseBody
    public ResponseEntity<?> archiverSignalement(@PathVariable Long id) {
        Signalement signalement = signalementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        signalement.setStatut("nonVisible");
        signalementRepository.save(signalement);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/signalements/{id}")
    @ResponseBody
    public ResponseEntity<?> supprimerSignalement(@PathVariable Long id) {
        // Vérification d'autorisation (à adapter selon votre logique de sécurité)
        // Exemple : if (!userHasDeleteRights()) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non autorisé");
        Signalement signalement = signalementRepository.findById(id)
                .orElse(null);
        if (signalement == null) {
            return ResponseEntity.notFound().build();
        }
        // Suppression des photos associées
        if (signalement.getPhotos() != null) {
            for (Photo photo : signalement.getPhotos()) {
                // Suppression du fichier physique
                try {
                    Path filePath = Paths.get(UPLOAD_DIR, photo.getChemin());
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    // Log erreur mais continuer la suppression
                }
                photoRepository.delete(photo);
            }
        }
        signalementRepository.delete(signalement);
        return ResponseEntity.ok().build();
    }
}