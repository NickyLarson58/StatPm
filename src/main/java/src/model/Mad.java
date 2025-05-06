package src.model;

import javax.persistence.*;

@Entity
@Table(name = "mad")
public class Mad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mad;

    @Column(name = "natinf")
    private int natinf;

    @Column(name = "libelle", length = 255)
    private String libelle;

    public Mad() {
    }

    public Mad(int natinf, String libelle) {
        this.natinf = natinf;
        this.libelle = libelle;
    }

    public Long getId_mad() {
        return id_mad;
    }

    public void setId_mad(Long id_mad) {
        this.id_mad = id_mad;
    }

    public int getNatinf() {
        return natinf;
    }

    public void setNatinf(int natinf) {
        this.natinf = natinf;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}