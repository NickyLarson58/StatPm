package src.model.dto;

import src.model.Agents;
import src.model.Adresses;
import java.time.LocalDate;
import java.util.List;

public class StatistiqueDTO {
    private List<Agents> agents;
    private String brigade;
    private String nomIntervention;
    private Integer nombre;
    private Adresses adresse;
    private LocalDate date;

    // Getters et setters
    public List<Agents> getAgents() {
        return agents;
    }

    public void setAgents(List<Agents> agents) {
        this.agents = agents;
    }

    public String getBrigade() {
        return brigade;
    }

    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }

    public String getNomIntervention() {
        return nomIntervention;
    }

    public void setNomIntervention(String nomIntervention) {
        this.nomIntervention = nomIntervention;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public Adresses getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresses adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}