package src.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "stat_mission")
public class StatMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stat_mission")
    private Long idStatMission;

    @Column(name = "date_mission")
    private LocalDate dateMission;

    @ManyToOne
    @JoinColumn(name = "id_adresse", referencedColumnName = "idadresse", nullable = false)
    private Adresses lieuMission;

    @ManyToOne
    @JoinColumn(name = "id_mission", referencedColumnName = "id_mission", nullable = false)
    private Missions missions;

    @Column(name = "commerce")
    private String commerce;

    @Column(name = "nomBrigade")
    private String brigade;

    @Column(name = "duree_mission")
    private Integer dureeMission;
    @ManyToMany
    @JoinTable(
        name = "stat_mission_agents", // Nom de la table de jointure
        joinColumns = @JoinColumn(name = "stat_mission_id_stat_mission", referencedColumnName = "id_stat_mission"),
        inverseJoinColumns = @JoinColumn(name = "agents_matricule", referencedColumnName = "matricule")
    )
    private List<Agents> agents;
    

    public Long getIdStatMission() {
        return idStatMission;
    }

    public void setIdStatMission(Long idStatMission) {
        this.idStatMission = idStatMission;
    }

    public LocalDate getDateMission() {
        return dateMission;
    }

    public void setDateMission(LocalDate dateMission) {
        this.dateMission = dateMission;
    }

    public Adresses getLieuMission() {
        return lieuMission;
    }

    public void setLieuMission(Adresses lieuMission) {
        this.lieuMission = lieuMission;
    }

    public Missions getMissions() {
        return missions;
    }

    public void setMissions(Missions missions) {
        this.missions = missions;
    }

    public String getCommerce() {
        return commerce;
    }

    public void setCommerce(String commerce) {
        this.commerce = commerce;
    }

    public String getBrigade() {
        return brigade;
    }

    public void setBrigade(String brigade) {
        this.brigade = brigade;
    }
    public Integer getDureeMission() {
        return dureeMission;
    }
    

    public void setDureeMission(Integer dureeMission) {
        this.dureeMission = dureeMission;
    }

    public List<Agents> getAgents() {
        return agents;
    }

    public void setAgents(List<Agents> agents) {
        this.agents = agents;
    }
}