package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import src.model.Infraction;
import src.model.Interventions;
import src.model.Mad;
import src.model.Missions;
import src.repository.InterventionsRepository;
import src.service.StatService;
import src.repository.MadRepository;
import src.repository.MissionsRepository;
import src.repository.InfractionRepository;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/configServer")
public class ConfigController {
    
    @Value("${server.ip}")
    private String serverIp;
    
    @Value("${server.port}")
    private String serverPort;
    
    @GetMapping("/config")
    public Map<String, String> getServerConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("serverIp", serverIp);
        config.put("serverPort", serverPort);
        return config;
    }
        
    @Autowired
    private StatService statService;

    @Autowired
    private MadRepository madRepository;

    @Autowired
    private InfractionRepository InfractionRepository;

    @Autowired
    private InterventionsRepository interventionsRepository;

    @Autowired
    private MissionsRepository missionsRepository;

    @GetMapping("/accueilStat")
    public String accueilStat() {
        return "AccueilStat"; // Cela doit correspondre au nom de votre fichier HTML sans l'extension .html
    }
    
    @GetMapping("/api/infractions")
    @ResponseBody
    public List<Infraction> getSous_infractionsNoms() {
        List<Infraction> infractions = InfractionRepository.findAll();
        return infractions;
    }


    @GetMapping("/api/mad")
    @ResponseBody
    public List<Mad> getMadNoms() {
        List<Mad> mads = madRepository.findAll();
        return mads;
    }
    
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/natinf")
    public String natinfPage(HttpSession session, Model model) {
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            return "redirect:/login";
        }
        return "natinf";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }

    // Redaction endpoint moved to RedactionController

    @GetMapping("/creer-stat-form")
    public String creerStatPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            return "redirect:/login";
        }
        
        try {
            // Récupérer la liste des brigades
            List<String> brigades = statService.getBrigades();
            model.addAttribute("brigades", brigades);

            // Récupérer la liste des interventions
            List<String> interventions = statService.getInterventions();
            model.addAttribute("interventions", interventions);

            // Date du jour par défaut
            model.addAttribute("dateCreation", LocalDate.now());

            // Liste des équipages (à implémenter)
            List<Map<String, Object>> equipages = new ArrayList<>();
            model.addAttribute("equipages", equipages);

            // Liste des interventions (à implémenter)
            List<Map<String, Object>> recapInterventions = new ArrayList<>();
            model.addAttribute("recapInterventions", recapInterventions);

            return "creer-stat";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "La base de données n'est pas accessible. Veuillez vérifier la configuration de la base de données.");
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/creer-stat")
    public String creerStat(HttpSession session, Model model) {
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            return "redirect:/login";
        }
        
        String brigade = (String) session.getAttribute("brigade");
        model.addAttribute("userBrigade", brigade);
        return "creer-stat";
    }

    @PostMapping("/creer-stat")
    public String creerStat(@RequestParam Map<String, String> params, 
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            return "redirect:/login";
        }

        try {
            String brigade = params.get("brigade");
            String intervention = params.get("typeIntervention");
            String date = params.get("dateCreation");
            String adresse = "";
            
            statService.creerStat(brigade, intervention, adresse, date);

            String[] membres = params.get("membres[]").split(",");
            
            for (String membre : membres) {
                if (membre != null && !membre.trim().isEmpty()) {
                    String[] membreInfo = membre.split(" ", 2);
                    if (membreInfo.length == 2) {
                        statService.creerStatAgent(membreInfo[0], membreInfo[1], intervention, adresse, date);
                    }
                }
            }
            
            redirectAttributes.addFlashAttribute("success", "Stat créée avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "La base de données n'est pas accessible. Veuillez vérifier la configuration de la base de données.");
        }
        
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/api/intervention")
    @ResponseBody
    public Map<String, Object> creerIntervention(@RequestBody Map<String, Object> request, HttpSession session) {
        String matricule = (String) session.getAttribute("matricule");
        if (matricule == null) {
            throw new RuntimeException("Non autorisé");
        }
        
        try {
            String nomBrigade = (String) request.get("brigade");
            String typeIntervention = (String) request.get("type");
            String lieu = (String) request.get("lieu");
            int nombre = Integer.parseInt(request.get("nombre").toString());
            
            return statService.creerIntervention(nomBrigade, typeIntervention, lieu, nombre);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    @PutMapping("/api/intervention/{id}")
    @ResponseBody
    public Map<String, Object> modifierIntervention(@PathVariable String id, @RequestBody Map<String, Object> request) {
        try {
            String typeIntervention = (String) request.get("type");
            String lieu = (String) request.get("lieu");
            int nombre = Integer.parseInt(request.get("nombre").toString());
            
            return statService.modifierIntervention(id, typeIntervention, lieu, nombre);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    @DeleteMapping("/api/intervention/{id}")
    @ResponseBody
    public Map<String, Object> supprimerIntervention(@PathVariable String id) {
        try {
            statService.supprimerIntervention(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }



    @GetMapping("/api/agents")
    @ResponseBody
    public List<String> getAgents() {
        try {
            return statService.getAgents();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GetMapping("/api/types-interventions")
    @ResponseBody
    public List<Interventions> getTypesInterventions() {
        try {
            return interventionsRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GetMapping("/api/types-missions")
    @ResponseBody
    public List<Missions> getTypesMissions() {
        try {
            return missionsRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @GetMapping("/api/user-info")
    @ResponseBody
    public Map<String, String> getUserInfo(HttpSession session) {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("matricule", (String) session.getAttribute("matricule"));
        userInfo.put("brigade", (String) session.getAttribute("brigade"));
        userInfo.put("pouvoir", (String) session.getAttribute("pouvoir"));
        return userInfo;
    }

    @GetMapping("/api/agent-brigade")
    @ResponseBody
    public Map<String, String> getAgentBrigade(HttpSession session) {
        Map<String, String> response = new HashMap<>();
        String brigade = (String) session.getAttribute("brigade");
        if (brigade != null) {
            response.put("brigade", brigade);
        }
        return response;
    }
}
