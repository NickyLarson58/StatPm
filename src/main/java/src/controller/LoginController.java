package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import src.service.AuthenticationService;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {
        try {
            if (authenticationService.authenticate(username, password)) {
                // Get user's information
                Map<String, String> agentInfo = authenticationService.getAgentInfo(username);
                String pouvoir = authenticationService.getUserPouvoir(username);
                
                // Store user information in session
                session.setAttribute("matricule", username);
                session.setAttribute("brigade", agentInfo.get("brigade"));
                session.setAttribute("nom_agent", agentInfo.get("nom"));
                session.setAttribute("prenom_agent", agentInfo.get("prenom"));
                session.setAttribute("pouvoir", pouvoir);
                session.setAttribute("secteur", agentInfo.get("secteur"));
                
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Matricule ou mot de passe invalide");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur de connexion: " + e.getMessage());
            return "login";
        }
    }
}