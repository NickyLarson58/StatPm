package src.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "brigades")
public class Brigade {
    @Id
    @Column(name = "idBrigade", columnDefinition = "BIGINT")
    private Long idBrigade;
    @Column(name = "nomBrigade", columnDefinition = "VARCHAR(255)")
    private String nomBrigade;

    public Brigade() {}

    public Brigade(Long idBrigade, String nomBrigade) {
        this.idBrigade = idBrigade;
        this.nomBrigade = nomBrigade;
    }

    public Long getIdBrigade() {
        return idBrigade;
    }

    public void setIdBrigade(Long idBrigade) {
        this.idBrigade = idBrigade;
    }

    public String getNomBrigade() {
        return nomBrigade;
    }

    public void setNomBrigade(String nomBrigade) {
        this.nomBrigade = nomBrigade;
    }
}