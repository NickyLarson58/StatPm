package src.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import SQL.ConnexionSql;
import src.Utils;
import java.util.Map;
import java.util.HashMap;

@Service
public class AuthenticationService {
    
    private final ConnexionSql connected;
    private final Utils utils;
    private final LoggingService loggingService;

    @Autowired
    public AuthenticationService(ConnexionSql connected, Utils utils, LoggingService loggingService) {
        this.connected = connected;
        this.utils = utils;
        this.loggingService = loggingService;
    }

    public boolean authenticate(String matricule, String password) {
        try {
            String passwordBd = connected.RecupererString("mdp", "agents", "matricule", matricule);
            String pouvoir = connected.RecupererString("pouvoir", "agents", "matricule", matricule);
            
            if (passwordBd != null && passwordBd.equals(password)) {
                loggingService.logInfo("AUTH", matricule, "Connexion réussie", utils.pathDestionation);
                return true;
            } else {
                loggingService.logError("AUTH", matricule, "Échec de connexion", utils.pathDestionation);
                return false;
            }
        } catch (Exception e) {
            loggingService.logError("AUTH", matricule, "Erreur: " + e.getMessage(), utils.pathDestionation);
            return false;
        }
    }

    public String getUserPouvoir(String matricule) {
        try {
            String pouvoir = connected.RecupererString("pouvoir", "agents", "matricule", matricule);
            loggingService.logDebug("AUTH", matricule, "Récupération du pouvoir: " + pouvoir, utils.pathDestionation);
            return pouvoir;
        } catch (Exception e) {
            loggingService.logError("AUTH", matricule, "Erreur lors de la récupération du pouvoir: " + e.getMessage(), utils.pathDestionation);
            return null;
        }
    }

    public String getAgentNom(String matricule) {
        try {
            String nom = connected.RecupererString("nom_agent", "agents", "matricule", matricule);
            loggingService.logDebug("AUTH", matricule, "Récupération du nom: " + nom, utils.pathDestionation);
            return nom;
        } catch (Exception e) {
            loggingService.logError("AUTH", matricule, "Erreur lors de la récupération du nom: " + e.getMessage(), utils.pathDestionation);
            return null;
        }
    }

    public String getAgentPrenom(String matricule) {
        try {
            String prenom = connected.RecupererString("prenom_agent", "agents", "matricule", matricule);
            loggingService.logDebug("AUTH", matricule, "Récupération du prénom: " + prenom, utils.pathDestionation);
            return prenom;
        } catch (Exception e) {
            loggingService.logError("AUTH", matricule, "Erreur lors de la récupération du prénom: " + e.getMessage(), utils.pathDestionation);
            return null;
        }
    }

    public Map<String, String> getAgentInfo(String matricule) {
        Map<String, String> agentDetails = new HashMap<>();
        try {
            loggingService.logInfo("AUTH", matricule, "Executing SQL query for agent details", utils.pathDestionation);
            String nom = connected.RecupererString("nom_agent", "agents", "matricule", matricule);
            String prenom = connected.RecupererString("prenom_agent", "agents", "matricule", matricule);
            String brigade = connected.RecupererString("brigade", "agents", "matricule", matricule);
            
            loggingService.logInfo("AUTH", matricule, String.format("SQL results - nom: %s, prenom: %s, brigade: %s", nom, prenom, brigade), utils.pathDestionation);
            
            agentDetails.put("nom", nom != null ? nom : "Inconnu");
            agentDetails.put("prenom", prenom != null ? prenom : "");
            agentDetails.put("brigade", brigade != null ? brigade : "BRIGADE 1");
            
            loggingService.logInfo("AUTH", matricule, "Agent details retrieved successfully", utils.pathDestionation);
        } catch (Exception e) {
            loggingService.logError("AUTH", matricule, "Error retrieving agent details: " + e.getMessage() + "\nStack trace: " + e.getStackTrace(), utils.pathDestionation);
            agentDetails.put("nom", "Inconnu");
            agentDetails.put("prenom", "");
            agentDetails.put("brigade", "BRIGADE 1");
        }
        return agentDetails;
    }
}
