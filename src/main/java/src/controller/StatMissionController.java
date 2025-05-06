package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import src.model.StatMission;
import src.model.dto.CreatedStatMissionDTO;
import src.service.StatMissionService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StatMissionController {

    @Autowired
    private StatMissionService statMissionService;

    /**
     * Endpoint pour valider une mission
     * Cet endpoint est appelé lorsque l'utilisateur clique sur "Valider Mission"
     * Il enregistre la mission en base de données et l'associe aux agents participants
     */
    @PostMapping("/api/valider-mission")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validerMission(
            @RequestBody CreatedStatMissionDTO missionDTO,
            HttpSession session) {
        
        // Vérifier que l'utilisateur est connecté
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Utilisateur non authentifié");
            return ResponseEntity.badRequest().body(response);
        }
        
        try {
            // Créer la mission en base de données
            StatMission savedMission = statMissionService.createStatMission(missionDTO);
            
            // Préparer la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Mission enregistrée avec succès");
            response.put("idMission", savedMission.getIdStatMission());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // En cas d'erreur
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur lors de l'enregistrement de la mission: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}