package src.model;

import javax.persistence.*;

@Entity
@Table(name = "missions")
public class Missions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mission")
    private Long idMission;

    @Column(name = "nom_mission")
    private String nomMission;

    public Long getIdMission() {
        return idMission;
    }

    public void setIdMission(Long idMission) {
        this.idMission = idMission;
    }

    public String getNomMission() {
        return nomMission;
    }

    public void setNomMission(String nomMission) {
        this.nomMission = nomMission;
    }
}
