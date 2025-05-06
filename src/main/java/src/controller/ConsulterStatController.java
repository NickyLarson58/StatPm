package src.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsulterStatController {

    @GetMapping("/consulterStat")
    public String consulterStat() {
        return "consulterStat";
    }
}