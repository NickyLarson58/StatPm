package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TravauxController {

    @GetMapping("/travaux")
    public String travaux() {
        return "travaux";
    }
}