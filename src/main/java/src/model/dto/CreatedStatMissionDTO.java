package src.model.dto;

import src.model.Agents;
import java.time.LocalDate;
import java.util.List;

public class CreatedStatMissionDTO {
    private Long idMission;
    private LocalDate dateMission;
    private String nomMission;
    private int duree;
    private String commerce;
    private Long idAdresse;
    private List<Agents> agents;
    private String brigade;

    // Getters and Setters
    public String getBrigade() {
        return brigade;
    }

    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }
    
    public Long getIdMission() {
        return idMission;
    }

    public void setIdMission(Long idMission) {
        this.idMission = idMission;
    }

    public LocalDate getDateMission() {
        return dateMission;
    }

    public void setDateMission(LocalDate dateMission) {
        this.dateMission = dateMission;
    }

    public String getNomMission() {
        return nomMission;
    }

    public void setNomMission(String nomMission) {
        this.nomMission = nomMission;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getCommerce() {
        return commerce;
    }

    public void setCommerce(String commerce) {
        this.commerce = commerce;
    }

    public Long getIdAdresse() {
        return idAdresse;
    }

    public void setIdAdresse(Long idAdresse) {
        this.idAdresse = idAdresse;
    }

    public List<Agents> getAgents() {
        return agents;
    }

    public void setAgents(List<Agents> agents) {
        this.agents = agents;
    }
}