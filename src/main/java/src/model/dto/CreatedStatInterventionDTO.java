package src.model.dto;

import src.model.Agents;
import java.time.LocalDate;
import java.util.List;

public class CreatedStatInterventionDTO {
    private int idIntervention;
    private LocalDate dateIntervention;
    private Integer nombreIntervention;
    private Long idAdresse;
    private Long idInfraction;
    private Long idMad;
    private List<Agents> agents;
    private String brigade;

    public String getBrigade() {
        return brigade;
    }

    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }

    public Long getIdMad() {
        return idMad;
    }

    public void setIdMad(Long idMad) {
        this.idMad = idMad;
    }

    public Long getIdInfraction() {
        return idInfraction;
    }

    public void setIdInfraction(Long idInfraction) {
        this.idInfraction = idInfraction;
    }

    public Long getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(Long idAdresse) {
        this.idAdresse = idAdresse;
    }

    public Integer getNombreIntervention() {
        return nombreIntervention;
    }

    public void setNombreIntervention(Integer nombreIntervention) {
        this.nombreIntervention = nombreIntervention;
    }

    public LocalDate getDateIntervention() {
        return dateIntervention;
    }

    public void setDateIntervention(LocalDate dateIntervention) {
        this.dateIntervention = dateIntervention;
    }

    public int getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(int idIntervention) {
        this.idIntervention = idIntervention;
    }

    public List<Agents> getAgents() {
        return agents;
    }

    public void setAgents(List<Agents> agents) {
        this.agents = agents;
    }
}