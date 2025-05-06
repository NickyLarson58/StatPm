package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;
import src.service.LLMService;

@Controller
public class RedactionController {

    @Autowired
    private LLMService llmService;

    @GetMapping("/redaction")
    public String redaction(HttpSession session, Model model) {
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            return "redirect:/login";
        }
        return "redaction";
    }

    @GetMapping("/redaction/types")
    @ResponseBody
    public Map<String, Object> getTypesRedaction() {
        Map<String, Object> types = new HashMap<>();
        types.put("types", new String[]{
            "Rapport",
            "Procès-verbal",
            "Main courante"
        });
        return types;
    }

    @PostMapping("/redaction/create")
    @ResponseBody
    public Map<String, Object> createDocument(@RequestBody Map<String, Object> request, 
                                            HttpSession session) {
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            throw new RuntimeException("Non autorisé");
        }

        Map<String, Object> response = new HashMap<>();
        try {
            String type = (String) request.get("type");
            String titre = (String) request.get("titre");
            String contenu = (String) request.get("contenu");
            String typeInfraction = (String) request.get("infractionType");

            // Améliorer le texte avec le LLM
            
            String contenuAmeliore = llmService.enhanceText(contenu, type, titre, typeInfraction);
            
            // TODO: Sauvegarder le document avec le contenu amélioré
            
            response.put("success", true);
            response.put("message", "Document créé avec succès");
            response.put("contenuAmeliore", contenuAmeliore);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
}