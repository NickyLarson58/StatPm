package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import src.model.StatIntervention;
import src.model.dto.CreatedStatInterventionDTO;
import src.service.StatInterventionService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Controller
public class StatInterventionController {

    @Autowired
    private StatInterventionService statInterventionService;

    /**
     * Endpoint pour valider une intervention
     * Cet endpoint est appelé lorsque l'utilisateur clique sur "Valider Intervention"
     * Il enregistre l'intervention en base de données et l'associe aux agents participants
     */
    @PostMapping("/api/valider-intervention")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validerIntervention(
            @RequestBody CreatedStatInterventionDTO interventionDTO,
            HttpSession session) {
        return processIntervention(interventionDTO, session);
    }

    @PostMapping("/api/valider-interventions")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> validerInterventions(
            @RequestBody List<CreatedStatInterventionDTO> interventionsDTO,
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
            List<Long> savedInterventionIds = new ArrayList<>();
            for (CreatedStatInterventionDTO interventionDTO : interventionsDTO) {
                StatIntervention savedIntervention = statInterventionService.createStatIntervention(interventionDTO);
                savedInterventionIds.add(savedIntervention.getIdStatIntervention());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Toutes les interventions ont été enregistrées avec succès");
            response.put("interventionIds", savedInterventionIds);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur lors de l'enregistrement des interventions: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    private ResponseEntity<Map<String, Object>> processIntervention(
            CreatedStatInterventionDTO interventionDTO,
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
            // Créer l'intervention en base de données
            StatIntervention savedIntervention = statInterventionService.createStatIntervention(interventionDTO);
            
            // Préparer la réponse
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Intervention enregistrée avec succès");
            response.put("idIntervention", savedIntervention.getIdStatIntervention());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // En cas d'erreur
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erreur lors de l'enregistrement de l'intervention: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}