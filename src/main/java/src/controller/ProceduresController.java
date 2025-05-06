package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;

@Controller
public class ProceduresController {

    @GetMapping("/procedures")
    public String openProceduresFolder() {
        try {
            String path = "\\\\laciotat.local\\DFS\\Données\\pom\\CENTRE VILLE\\PROCEDURES";
            Runtime.getRuntime().exec("explorer.exe \"" + path + "\"");
            return "redirect:/dashboard";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/dashboard?error=procedures";
        }
    }

    @GetMapping("/municpol")
    public String openMunicipol() {
        try {
            String path = "\\\\ciotat28\\Logitud\\PM\\PM.exe";
            Runtime.getRuntime().exec("explorer.exe \"" + path + "\"");
            return "redirect:/dashboard";
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/dashboard?error=procedures";
        }
    }
}