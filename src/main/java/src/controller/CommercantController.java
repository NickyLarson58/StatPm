package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommercantController {

    @GetMapping("/AccueilListeCommercant")
    public String travaux() {
        return "AccueilListeCommercant";
    }
}