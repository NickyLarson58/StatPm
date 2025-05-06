package src.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import SQL.ConnexionSql;
import src.model.Adresses;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdresseController {

    @Autowired
    private ConnexionSql connexionSql;

    @GetMapping("/adresses")
    public List<Adresses> getAdresses() {
        try {
            return connexionSql.RecupererAdresses();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}